package demoScripts.innovate2012;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.python.core.PyException;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import resources.demoScripts.innovate2012.JythonCmdLineLauncherHelper;

import com.advancedrft.common.lang.Reflection;
import com.advancedrft.common.lang.StringFunctions;
import com.rational.test.ft.services.IPlaybackMonitor;
import com.rational.test.util.ServiceBroker;

/**
 * Description : Functional Test Script
 * 
 * @author vmuser
 */
public class JythonCmdLineLauncher extends JythonCmdLineLauncherHelper
{
    /**
     * Script Name : <b>JythonCmdLineLauncher</b><br>
     * Generated : <b>May 20, 2012 8:55:28 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 6.1 Build 7601 (S)<br>
     * 
     * @since 2012/05/20
     * @author vmuser
     * @param args
     *            The script name and arguments
     */
    public void testMain(Object[] args)
    {
        if (args == null || args.length == 0)
        {
            logError("Must at least supply a Jython script file path.");
            return;
        }
        
        File script = new File(StringFunctions.ifNull(args[0]));
        if (!script.isFile())
        {
            logError("The Jython script \"" + args[0] + "\" does not exist.");
            return;
        }
        
        List<String> scriptArgs = new ArrayList<String>();
        for (int i = 1; i < args.length; i++)
        {
            scriptArgs.add(StringFunctions.ifNull(args[i]));
        }
        
        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
        
        File f = null;
        
        if (isDebug)
        {
            try
            {
                Object o = Reflection.executeMethod(null, "org.eclipse.core.runtime.Platform", "getBundle", "org.python.pydev.debug");
                Object o1 = Reflection.executeMethod(o, "org.eclipse.osgi.framework.internal.core.AbstractBundle", "getEntry", "/pysrc/");
                URL url = (URL) Reflection.executeMethod(null, "org.eclipse.core.runtime.FileLocator", "toFileURL", o1);
                try
                {
                    f = new File(url.toURI());
                }
                catch (URISyntaxException e)
                {
                    try
                    {
                        f = new File(url.getPath());
                    }
                    catch (Exception ex)
                    {
                        logError("Unable to locate PyDev debugger code. Error parsing the path into a File object.");
                        logException(e);
                        return;
                    }
                }
            }
            catch (Exception e)
            {
                logError("Unable to locate PyDev debugger code.");
                logException(e);
                return;
            }
        }
        
        IPlaybackMonitor ipbm = (IPlaybackMonitor) ServiceBroker.getServiceBroker().findService(IPlaybackMonitor.class.getCanonicalName());
        try
        {
            unregisterAll();
            PythonInterpreter interpreter = new PythonInterpreter();
            if (isDebug && f != null)
            {
                try
                {
                    ipbm.setDescription(IPlaybackMonitor.RUNNING, "Initializing PyDev Debug Connection. Please Wait.");
                    interpreter.getSystemState().path.append(new PyString(f.getAbsolutePath()));
                    interpreter.exec(new PyString("import pydevd; pydevd.settrace(suspend=False);"));
                }
                catch (Exception e)
                {
                    logError("Unable to initalize PyDev debugger connection.");
                    logException(e);
                    return;
                }
                ipbm.setDescription(IPlaybackMonitor.RUNNING, "");
            }
            
            // interpreter.getSystemState().setClassLoader(Reflection.getClassLoader());
            interpreter.getSystemState().argv.clear();
            interpreter.getSystemState().argv.addAll(scriptArgs);
            interpreter.execfile(script.getAbsolutePath());
        }
        catch (PyException e)
        {
            logException(e);
            logException(e.getCause());
        }
        catch (Exception e)
        {
            logException(e);
        }
    }
}
