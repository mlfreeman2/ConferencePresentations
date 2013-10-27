package org.mlfreeman.innovate2013;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * Helpful routines for getting log4j initialized and running
 */
public class Log4J_Helpers
{
    /**
     * Adds an appender to log4j that will render output to the console
     */
    public static void addConsoleAppender()
    {
        Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN)));
    }
    
    /**
     * Adds an appender to log4j that will render output to a file and start a new log when it hits a certain size.
     */
    public static void addRollingFileAppender()
    {
        // Initialize log4j.
        // Set up the rolling log file appender.
        // Note the last arg, false, which tells this RollingFileAppender to overwrite what's already there instead of appending to existing logs.
        try
        {
            RollingFileAppender rfp = new RollingFileAppender(new PatternLayout("%d{ISO8601} %5p %c{1}:%L - %m%n"), new File("logs/demo.log").getCanonicalPath(), false);
            
            // We want the logger to flush its output to the log file stream immediately.
            // if you don't have this set, then log4j will buffer the log file output which isn't ideal.
            rfp.setImmediateFlush(true);
            rfp.setBufferedIO(false);
            rfp.setBufferSize(1024);
            
            // Set the Max number of files and max size of each log file to keep around.
            rfp.setMaxBackupIndex(Integer.parseInt(System.getProperty("log.backup.files", "200")));
            rfp.setMaxFileSize(System.getProperty("log.individual.file.size", "1024KB"));
            Logger.getRootLogger().addAppender(rfp);
        }
        catch (IOException e2)
        {
            System.out.println("Unable to initialize rolling file appender.");
            e2.printStackTrace();
        }
    }
}
