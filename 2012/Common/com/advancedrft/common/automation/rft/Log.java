package com.advancedrft.common.automation.rft;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import com.advancedrft.common.lang.Regex;
import com.advancedrft.common.lang.StringFunctions;
import com.ibm.rational.test.ft.sdk.RFTCore;
import com.rational.test.ft.script.RationalTestScript;
import com.rational.test.ft.services.ILog;
import com.rational.test.ft.services.ILogMessage;
import com.rational.test.ft.services.ISimpleLog;

/**
 * A new set of logging functions that expand on the normal RFT ones. <br>
 * These have only been tested with RFT's HTML log format, but they could work with the other formats if logItem is tweaked accordingly
 */
public class Log extends AppenderSkeleton
{
    /**
     * The RFT log that all these functions log to
     */
    public static ILog rft;
    
    /**
     * A shutdown hook that scrubs out certain useless RFT log messages.
     */
    public static class LogScrubber extends Thread
    {
        
        private String file = new String();
        
        public LogScrubber(String path)
        {
            file = path;
        }
        
        @Override
        public void run()
        {
            java.io.File f = new java.io.File(file);
            
            List<String> originalLines = new ArrayList<String>();
            List<String> finalLines = new ArrayList<String>();
            
            try
            {
                // use buffering, reading one line at a time
                // FileReader always assumes default encoding is OK!
                BufferedReader input = new BufferedReader(new FileReader(f));
                try
                {
                    String line = null; // not declared within while loop
                    // readLine is a bit quirky : it returns the content of a line MINUS the newline.
                    // it returns null only for the END of the stream.
                    // it returns an empty String if two newlines appear in a row.
                    while ((line = input.readLine()) != null)
                    {
                        originalLines.add(line);
                    }
                }
                finally
                {
                    input.close();
                }
            }
            catch (Exception e)
            {
                System.err.println("================== RFT Log Cleaner Shutdown Hook - Read File As Lines  =================");
                e.printStackTrace();
                System.err.println("================== RFT Log Cleaner Shutdown Hook - Read File As Lines  =================");
            }
            
            try
            {
                boolean inGetPropCall = false;
                String propMarker = "<TD CLASS=\"note\">getProperty on";
                for (int i = 0; i < originalLines.size(); i++)
                {
                    if (originalLines.get(i).trim().length() == 0)
                    {
                        continue;
                    }
                    if (inGetPropCall)
                    {
                        if (originalLines.get(i).startsWith("</TD></TR>"))
                        {
                            inGetPropCall = false;
                        }
                        continue;
                    }
                    else
                    {
                        if (originalLines.get(i).startsWith("<TR>"))
                        {
                            if (originalLines.size() > (i + 3))
                            {
                                if (originalLines.get(i + 3).trim().startsWith(propMarker))
                                {
                                    inGetPropCall = true;
                                    continue;
                                }
                            }
                        }
                        finalLines.add(originalLines.get(i));
                    }
                }
            }
            catch (Exception e)
            {
                System.err.println("================== RFT Log Cleaner Shutdown Hook - Scrub Lines         =================");
                e.printStackTrace();
                System.err.println("================== RFT Log Cleaner Shutdown Hook - Scrub Lines         =================");
            }
            
            try
            {
                // Delete the old log to be safe
                f.delete();
                
                // use buffering, writing one line at a time
                // FileWriter always assumes default encoding is OK!
                PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(f.getAbsolutePath())));
                try
                {
                    for (String s : finalLines)
                    {
                        output.println(s);
                    }
                }
                finally
                {
                    output.close();
                }
            }
            catch (Exception e)
            {
                System.err.println("================== RFT Log Cleaner Shutdown Hook - Write Cleaned Lines =================");
                e.printStackTrace();
                System.err.println("================== RFT Log Cleaner Shutdown Hook - Write Cleaned Lines =================");
            }
        }
    }
    
    static
    {
        Log.rft = RFTCore.getScriptExecutionArgs().getScriptPlaybackLog();
        if (Log.rft.getClass().getSimpleName().startsWith("HTML"))
        {
            Runtime.getRuntime().addShutdownHook(new LogScrubber(Log.rft.getLogDirectory() + "/rational_ft_logframe.html"));
        }
    }
    
    /**
     * Logs an error message
     * 
     * @param headline
     *            A short error message
     */
    public static void logError(String headline)
    {
        Log.logItem(headline, "", null, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_FAILURE);
    }
    
    /**
     * Logs an error message
     * 
     * @param headline
     *            A short error message
     * @param screenshot
     *            A screenshot to include with the error message
     */
    public static void logError(String headline, BufferedImage screenshot)
    {
        Log.logItem(headline, "", screenshot, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_FAILURE);
    }
    
    /**
     * Logs an error message
     * 
     * @param headline
     *            A short error message
     * @param additionalInfo
     *            More details on the error
     */
    public static void logError(String headline, String additionalInfo)
    {
        Log.logItem(headline, additionalInfo, null, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_FAILURE);
    }
    
    /**
     * Logs an error message
     * 
     * @param headline
     *            A short error message
     * @param additionalInfo
     *            More details on the error
     * @param screenshot
     *            A screenshot to include with the error message
     */
    public static void logError(String headline, String additionalInfo, BufferedImage screenshot)
    {
        Log.logItem(headline, additionalInfo, screenshot, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_FAILURE);
    }
    
    /**
     * Logs a warning message
     * 
     * @param headline
     *            A short warning message
     */
    public static void logWarning(String headline)
    {
        Log.logItem(headline, "", null, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_WARNING);
    }
    
    /**
     * Logs a warning message
     * 
     * @param headline
     *            A short warning message
     * @param screenshot
     *            A screenshot to include with the warning message
     */
    public static void logWarning(String headline, BufferedImage screenshot)
    {
        Log.logItem(headline, "", screenshot, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_WARNING);
    }
    
    /**
     * Logs a warning message
     * 
     * @param headline
     *            A short warning message
     * @param additionalInfo
     *            More details on the warning
     */
    public static void logWarning(String headline, String additionalInfo)
    {
        Log.logItem(headline, additionalInfo, null, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_WARNING);
    }
    
    /**
     * Logs a warning message
     * 
     * @param headline
     *            A short warning message
     * @param additionalInfo
     *            More details on the warning
     * @param screenshot
     *            A screenshot to include with the warning message
     */
    public static void logWarning(String headline, String additionalInfo, BufferedImage screenshot)
    {
        Log.logItem(headline, additionalInfo, screenshot, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_WARNING);
    }
    
    /**
     * Logs an informational message
     * 
     * @param headline
     *            A short informational message
     */
    public static void logInfo(String headline)
    {
        Log.logItem(headline, "", null, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_INFORMATION);
    }
    
    /**
     * Logs an informational message
     * 
     * @param headline
     *            A short informational message
     * @param screenshot
     *            A screenshot to include with the informational message
     */
    public static void logInfo(String headline, BufferedImage screenshot)
    {
        Log.logItem(headline, "", screenshot, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_INFORMATION);
    }
    
    /**
     * Logs an informational message
     * 
     * @param headline
     *            A short informational message
     * @param additionalInfo
     *            More details to include with the informational message
     */
    public static void logInfo(String headline, String additionalInfo)
    {
        Log.logItem(headline, additionalInfo, null, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_INFORMATION);
    }
    
    /**
     * Logs an informational message
     * 
     * @param headline
     *            A short informational message
     * @param additionalInfo
     *            More details on the informational message
     * @param screenshot
     *            A screenshot to include with the informational message
     */
    public static void logInfo(String headline, String additionalInfo, BufferedImage screenshot)
    {
        Log.logItem(headline, additionalInfo, screenshot, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_INFORMATION);
    }
    
    /**
     * Logs an exception thrown by something
     * 
     * @param e
     *            The exception to log
     */
    public static void logException(Throwable e)
    {
        Log.logItem(e.getClass().getName() + " - " + e.getStackTrace()[0].toString(), "", null, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_FAILURE, ILog.PROP_EXCEPTION_NAME, e.getClass().getName(), ILog.PROP_EXCEPTION_MESSAGE, e.getMessage(), ILog.PROP_EXCEPTION_STACK, Log.exceptionStackTraceToString(e));
    }
    
    /**
     * Logs an exception thrown by something
     * 
     * @param e
     *            The exception to log
     * @param screenshot
     *            A screenshot to include with the exception in the log message
     */
    public static void logException(Throwable e, BufferedImage screenshot)
    {
        Log.logItem(e.getClass().getName() + " - " + e.getStackTrace()[0].toString(), "", screenshot, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_FAILURE, ILog.PROP_EXCEPTION_NAME, e.getClass().getName(), ILog.PROP_EXCEPTION_MESSAGE, e.getMessage(), ILog.PROP_EXCEPTION_STACK, Log.exceptionStackTraceToString(e));
    }
    
    /**
     * Logs an exception thrown by something
     * 
     * @param e
     *            The exception to log
     * @param additionalInfo
     *            Additional details to include with the exception
     */
    public static void logException(Throwable e, String additionalInfo)
    {
        Log.logItem(e.getClass().getName() + " - " + e.getStackTrace()[0].toString(), additionalInfo, null, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_FAILURE, ILog.PROP_EXCEPTION_NAME, e.getClass().getName(), ILog.PROP_EXCEPTION_MESSAGE, e.getMessage(), ILog.PROP_EXCEPTION_STACK, Log.exceptionStackTraceToString(e));
    }
    
    /**
     * Logs an exception thrown by something
     * 
     * @param e
     *            The exception to log
     * @param additionalInfo
     *            Additional details to include with the exception
     * @param screenshot
     *            A screenshot to include with the exception in the log message
     */
    public static void logException(Throwable e, String additionalInfo, BufferedImage screenshot)
    {
        Log.logItem(e.getClass().getName() + " - " + e.getStackTrace()[0].toString(), additionalInfo, screenshot, ISimpleLog.EVENT_GENERAL, ISimpleLog.LOG_FAILURE, ILog.PROP_EXCEPTION_NAME, e.getClass().getName(), ILog.PROP_EXCEPTION_MESSAGE, e.getMessage(), ILog.PROP_EXCEPTION_STACK, Log.exceptionStackTraceToString(e));
    }
    
    /**
     * Logs the result of some sort of test
     * 
     * @param headline
     *            A brief description of the test
     * @param pass
     *            True if the test passed, False if it failed. The RFT log will say pass or fail accordingly
     */
    public static void logTestResult(String headline, boolean pass)
    {
        Log.logItem(headline, "", null, ISimpleLog.EVENT_GENERAL, pass ? ISimpleLog.LOG_PASS : ISimpleLog.LOG_FAILURE);
    }
    
    /**
     * Logs the result of some sort of test
     * 
     * @param headline
     *            A brief description of the test
     * @param screenshot
     *            A screenshot to include with the test result (e.g. proof it passed, why it failed, etc)
     * @param pass
     *            True if the test passed, False if it failed. The RFT log will say pass or fail accordingly
     */
    public static void logTestResult(String headline, BufferedImage screenshot, boolean pass)
    {
        Log.logItem(headline, "", screenshot, ISimpleLog.EVENT_GENERAL, pass ? ISimpleLog.LOG_PASS : ISimpleLog.LOG_FAILURE);
    }
    
    /**
     * Logs the result of some sort of test
     * 
     * @param headline
     *            A brief description of the test
     * @param additionalInfo
     *            Additional details on the test (e.g. what was done, etc)
     * @param pass
     *            True if the test passed, False if it failed. The RFT log will say pass or fail accordingly
     */
    public static void logTestResult(String headline, String additionalInfo, boolean pass)
    {
        Log.logItem(headline, additionalInfo, null, ISimpleLog.EVENT_GENERAL, pass ? ISimpleLog.LOG_PASS : ISimpleLog.LOG_FAILURE);
    }
    
    /**
     * Logs the result of some sort of test
     * 
     * @param headline
     *            A brief description of the test
     * @param additionalInfo
     *            Additional details on the test (e.g. what was done, etc)
     * @param screenshot
     *            A screenshot to include with the test result (e.g. proof it passed, why it failed, etc)
     * @param pass
     *            True if the test passed, False if it failed. The RFT log will say pass or fail accordingly
     */
    public static void logTestResult(String headline, String additionalInfo, BufferedImage screenshot, boolean pass)
    {
        Log.logItem(headline, additionalInfo, screenshot, ISimpleLog.EVENT_GENERAL, pass ? ISimpleLog.LOG_PASS : ISimpleLog.LOG_FAILURE);
    }
    
    /**
     * Our main log function.
     * 
     * @param headline
     *            A summary of the log message
     * @param additionalInfo
     *            Additional details to include in the log message
     * @param screenshot
     *            A screenshot to include in the log message, or null to not include it
     * @param eventCode
     *            The type of log message (see ILogMessage in the RFT API docs)
     * @param resultCode
     *            See ILogMessage in the RFT API docs, but generally the 4 choices are pass, fail, warning, or info.
     * @param additionalProperties
     *            Additional data to include in the log message in "name", "value" form.
     */
    private static void logItem(String headline, String additionalInfo, BufferedImage screenshot, int eventCode, int resultCode, String...additionalProperties)
    {
        ILogMessage ilm = Log.rft.createMessage();
        ilm.setProperty(ILog.PROP_SCRIPT_NAME, RationalTestScript.getTopScriptName());
        ilm.setHeadline(headline.replace("\"", "&quot;").replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>"));
        ilm.setEvent(eventCode);
        ilm.setResult(resultCode);
        if (screenshot != null)
        {
            ilm.setProperty(ILog.PROP_USER_SCREEN_SNAPSHOT, screenshot);
        }
        if (StringFunctions.ifNull(additionalInfo).length() > 0)
        {
            ilm.setProperty(ILog.PROP_ADDITIONAL_INFO, "<br><br><div style=\"width: 700px;\">" + StringFunctions.ifNull(additionalInfo) + "</div>");
        }
        StackTraceElement[] ste = Log.getFilteredStackTrace();
        for (int i = 0; i < ste.length; i++)
        {
            ilm.setProperty("Stack_" + i, ste[i].toString().replace("<", "&lt;").replace(">", "&gt;"));
        }
        for (int i = 0; i < additionalProperties.length; i += 2)
        {
            ilm.setProperty(additionalProperties[i], additionalProperties[i + 1]);
        }
        Log.rft.write(ilm);
    }
    
    /**
     * Trims out clutter classes from a stack trace
     * 
     * @return A stack trace free of clutter classes
     */
    public static StackTraceElement[] getFilteredStackTrace()
    {
        Throwable t = new Throwable();
        StackTraceElement[] ste = t.getStackTrace();
        ste = Log.removeClassesFromStackTrace(ste, Log.class.getCanonicalName());
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=sun\\.");
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=com\\.ibm");
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=com\\.rational");
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=com\\.hazelcast");
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=org\\.eclipse");
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=java\\.");
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=\\$Proxy");
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=org\\.apache\\.log4j");
        ste = Log.removeClassesFromStackTrace(ste, "startsWith=org\\.python");
        return ste;
    }
    
    /**
     * This function redacts a stack trace by removing all entries with a class name that match a given name or regex from an original stack trace.<br>
     * 
     * @param ste
     *            The original stack trace
     * @param className
     *            The name or regex to match class names against. Matches are removed. This string goes through StringFunctions.stringToMatcher()
     * @return The redacted stack trace array
     */
    private static StackTraceElement[] removeClassesFromStackTrace(StackTraceElement[] ste, String className)
    {
        Vector<StackTraceElement> finalStack = new Vector<StackTraceElement>();
        for (int i = 0; i < ste.length; i++)
        {
            if (!Regex.isMatch2(ste[i].getClassName(), className))
            {
                finalStack.add(ste[i]);
            }
        }
        StackTraceElement[] ste1 = new StackTraceElement[finalStack.size()];
        finalStack.copyInto(ste1);
        return ste1;
    }
    
    /**
     * This function captures the output of e.printStackTrace() into a String
     * 
     * @param e
     *            The Throwable to grab a stack trace from
     * @return The output of e.printStackTrace()
     */
    private static String exceptionStackTraceToString(Throwable e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }
    
    @Override
    public void close()
    {
        return;
    }
    
    @Override
    public boolean requiresLayout()
    {
        return false;
    }
    
    @Override
    protected void append(LoggingEvent event)
    {
        if (event.getLevel().equals(Level.ERROR) || event.getLevel().equals(Level.FATAL))
        {
            Log.logError("<pre>" + event.getMessage() + "</pre>");
        }
        else if (event.getLevel().equals(Level.ERROR) || event.getLevel().equals(Level.FATAL))
        {
            Log.logWarning("<pre>" + event.getMessage() + "</pre>");
        }
        else
        {
            Log.logInfo("<pre>" + event.getMessage() + "</pre>");
        }
    }
}
