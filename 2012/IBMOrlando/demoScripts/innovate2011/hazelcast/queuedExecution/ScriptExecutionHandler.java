package demoScripts.innovate2011.hazelcast.queuedExecution;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import resources.demoScripts.innovate2011.hazelcast.queuedExecution.ScriptExecutionHandlerHelper;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.Log.LogScrubber;
import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.common.hazelcast.ScriptExecutionRequest;
import com.advancedrft.common.hazelcast.SystemMessage;
import com.advancedrft.common.lang.Reflection;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.MessageListener;
import com.ibm.rational.test.ft.sdk.RFTCore;
import com.rational.test.ft.UserStoppedScriptError;
import com.rational.test.ft.script.IScriptDefinition;
import com.rational.test.ft.script.RationalTestScript;
import com.rational.test.ft.script.impl.DatastoreDefinition;
import com.rational.test.ft.services.ILog;
import com.rational.test.ft.sys.TestContext;
import com.rational.test.ft.util.OptionManager;

/**
 * This script is a generic skeleton script capable of executing scripts supplied into the Hazelcast queue "ScriptQueue".<br>
 * It works a lot like callScript() but not exactly like it.<br>
 * For one, obviously it uses Hazelcast to receive work orders.<br>
 * Secondly, we dug out a (unsupported) way to open a new HTML log for each script this thing runs.<br>
 * You could modify this to copy the HTML log to a file share or otherwise post-process it after each script finishes.<br>
 * <br>
 * Also, since this script is designed to be run and all but forgotten about it uses a Hazelcast Topic to listen for system management commands.<br>
 * If you had several copies of this running on several boxes you could have them all exit at once with a simple command sent from a throwaway script.<br>
 * <code>HazelcastAPI.startHazelcast(); HazelcastAPI.&lt;SystemMessage&gt; getTopic("System.Messages").publish(new SystemMessage(".*", "System.exit"));</code>
 */
public class ScriptExecutionHandler extends ScriptExecutionHandlerHelper implements MessageListener<SystemMessage>
{
    
    @Override
    public void onMessage(SystemMessage arg0)
    {
        // Defensive check just to be safe
        if (arg0 != null)
        {
            // If this command is meant for our machine we want to run it, otherwise ignore it
            if (arg0.doesCommandApplyHere())
            {
                // If the command is to exit, we'll notify the main thread to stop.
                // Other commands could be added here (e.g. a command to see what's running on this box)
                if (arg0.getCommand().equals("System.exit"))
                {
                    TestContext.getRunningTestContext().setAbort("System Message From Hazelcast");
                }
            }
        }
    }
    
    /**
     * Script Name : <b>ScriptExecutionHandler</b><br>
     * Generated : <b>May 8, 2011 3:19:31 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 6.1 Build 7601 (S)
     * 
     * @since 2011/05/08
     * @author vmuser
     * @param args
     *            The script arguments
     */
    public void testMain(Object[] args)
    {
        // Don't show script logs when we finish a script
        OptionManager.set("rt.bring_up_logviewer", false);
        // Start Hazelcast
        HazelcastAPI.start(RationalTestScript.getCurrentProject().getLocation() + File.separator + "rftConfigs\\automation.xml");
        // Attach this script to the System.Messages topic so that it can get notified to exit.
        // Topic messages will be processed by the onMesage(SystemMessage arg0) handler in this class, but they'll be done in another thread.
        HazelcastAPI.getNode().<SystemMessage> getTopic("System.Messages").addMessageListener(this);
        //
        // Now we connect to the global queue of scripts that need to be run.
        // In theory you could also have this connect to a machine-specific queue if you wanted to (e.g. to ensure that scripts needing special requirements run on specific boxes)
        BlockingQueue<ScriptExecutionRequest> scriptQueue = HazelcastAPI.getNode().<ScriptExecutionRequest> getQueue("ScriptQueue");
        //
        // We found that when you click the stop button on the rft playback window, a variable named "aborted" inside TestContext.getRunningTestContext() gets set to true
        // We also found that you could tell if someone had hit stop by checking TestContext.getRunningTestContext().isAborted()
        // We also found that you could simulate hitting stop by calling TestContext.getRunningTestContext().setAborted(true);
        // DISCLAIMER: this is not documented and therefore not supported
        //
        // Anyway, we basically want to keep polling the Hazelcast queue once a second until someone hits stop or the script receives a command to exit.
        while (!TestContext.getRunningTestContext().isAborted())
        {
            try
            {
                // Each ScriptExecutionRequest contains a friendly name of the particular run, the name of the actual script class to run, and the arguments to feed to the script class.
                // The friendly name of the particular run is used in naming the HTML log's directory and should be different for each set of arguments you feed to a given script class
                ScriptExecutionRequest executionRequest = scriptQueue.poll(1, TimeUnit.SECONDS);
                //
                // Various negative scenarios we want to just ignore
                if (executionRequest == null)
                {
                    continue;
                }
                if (executionRequest.getName().length() == 0)
                {
                    continue;
                }
                if (executionRequest.getClassName().length() == 0)
                {
                    continue;
                }
                //
                // DISCLAIMER: below is not officially documented RFT API trickery
                // Might change in the future
                // But it's the only reasonable way to run a script without using callScript()
                // And we don't want to use that because that merges logs together into one.
                // We want to keep logs separate
                //
                // Reflectively create a new instance of the RFT HTML log.
                // For some unknown reason this class isn't on the class path when you're in Eclipse editing scripts but it is there when you're running them.
                // Therefore we can only create and manipulate it using Java reflection
                ILog originalLog = Log.rft;
                Object o = RFTCore.getScriptExecutionArgs().getScriptPlaybackLog().getClass().newInstance();
                Reflection.foreignClassLoaders.add(o.getClass().getClassLoader());
                // Set the script name
                Reflection.setVariable(o, o.getClass().getCanonicalName(), "script", executionRequest.getName());
                // Calculate the main directory that holds all logs so that we can put this new log there too
                File scriptLogFolder = new File(RFTCore.getScriptExecutionArgs().getScriptPlaybackLog().getLogDirectory()).getParentFile();
                // Tell the HTMLLog code where to create its log folder.
                Reflection.executeMethod(o, o.getClass().getSuperclass().getCanonicalName(), "setLogDirectory", new Object[] {scriptLogFolder.getAbsolutePath() + File.separator + executionRequest.getName()});
                // Tell the HTMLLog code what name to put at the top of the HTML file
                Reflection.executeMethod(o, o.getClass().getSuperclass().getCanonicalName(), "setLogName", new Object[] {executionRequest.getName()});
                // Tell the HTMLLog code to actually create the directory and the initial rational_ft_logframe.html file
                Reflection.executeMethod(o, o.getClass().getCanonicalName(), "initLog");
                //
                // We created our own logging class that has all the same basic logging methods as RationalTestScript (and more variants too)
                // We did this so that we could control where our script logging calls go.
                // You can see it by opening up com.advancedrft.common.automation.rft.Log
                // It logs to a custom ILog object. Normally that would end up being the main RFT log
                // But here we want it to be our custom new log object
                Log.rft = (ILog) o;
                
                //
                try
                {
                    Class<?> scriptClass = DatastoreDefinition.get(RationalTestScript.getCurrentProject().getLocation()).loadClass(executionRequest.getClassName());
                    Object scriptObj = scriptClass.newInstance();
                    // Tell the HTMLLog code to add a Script Start message to the log.
                    try
                    {
                        Log.rft.getClass().getMethod("scriptStart", String.class, IScriptDefinition.class);
                        Reflection.executeMethod(Log.rft, ILog.class.getCanonicalName(), "scriptStart", executionRequest.getName(), getScriptDefinition());
                    }
                    catch (NoSuchMethodException e)
                    {
                        try
                        {
                            Log.rft.getClass().getMethod("scriptStart", String.class, String.class);
                            Reflection.executeMethod(Log.rft, ILog.class.getCanonicalName(), "scriptStart", executionRequest.getName(), "java");
                        }
                        catch (Exception ex)
                        {
                            logError("Unable to start new log.");
                            logException(e);
                            Log.rft = originalLog;
                        }
                    }
                    catch (Exception e)
                    {
                        logError("Unable to start new log.");
                        logException(e);
                        Log.rft = originalLog;
                    }
                    //
                    Method m = scriptClass.getMethod("runMain", Object[].class);
                    // We need to send the runMain() an Object[] args array so it can pass it on to the testMain() in the script.
                    // Since Method.invoke takes a varargs we can't just pass in a 0-length array.
                    // That will be treated like passing no arguments and trigger an IllegalArgumentException
                    // We have to cast our arguments as a single Object so that Java passes them on
                    if (executionRequest.getArgs() == null || executionRequest.getArgs().length == 0)
                    {
                        m.invoke(scriptObj, (Object) new Object[] {});
                    }
                    else
                    {
                        m.invoke(scriptObj, (Object) executionRequest.getArgs());
                    }
                    
                }
                catch (UserStoppedScriptError e)
                {
                    TestContext.getRunningTestContext().setAbort("Requested shut down.");
                }
                catch (InvocationTargetException e)
                {
                    Log.logException(e.getTargetException());
                }
                catch (IllegalArgumentException e)
                {
                    Log.logException(e);
                    Log.logError("Invalid arguments supplied for script: " + executionRequest.getName() + ": " + Arrays.deepToString(executionRequest.getArgs()));
                }
                catch (Exception e)
                {
                    Log.logException(e);
                }
                // Finally we use our custom scrubber to remove all the "getProperty on the ___________" messages that IBM forcibly adds in RFT 8.2
                // That runs as another thread so we can move on to the next script
                LogScrubber ls = new LogScrubber(scriptLogFolder.getAbsolutePath() + File.separator + executionRequest.getName() + "/rational_ft_logframe.html");
                ls.start();
                //
                // Re-connect our log pointer to the main log so that any Hazelcast anomalies between scripts get logged to the main log.
                Log.rft.scriptEnd(executionRequest.getName(), "java");
                Log.rft.close();
                Log.rft = RFTCore.getScriptExecutionArgs().getScriptPlaybackLog();
                // In the future additional post processing could be added here for the log of the just-finished script.
                // (e.g. move log directory to shared folder somewhere)
            }
            catch (Exception e)
            {
                // Any other exceptions will cause this whole thing to shut down.
                // In the future maybe this should be changed to somehow ignore *ALL* exceptions thrown by a script execution request.
                // And only fall into here if something in the code before or after the ScriptPlayback.run() call breaks
                RationalTestScript.logException(e);
                TestContext.getRunningTestContext().setAbort("Error processing ScriptExecutionRequest message.");
                break;
            }
        }
        Hazelcast.shutdownAll();
    }
}
