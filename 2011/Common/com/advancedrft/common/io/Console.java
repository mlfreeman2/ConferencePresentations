package com.advancedrft.common.io;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.lang.StringFunctions;

/**
 * A collection of helper functions relating to the session the JVM runs in.<br>
 * Not meant for OS-specific functions. Win32 specific functions for sessions belong in {@link com.lps.common.system.win32.Session}.
 */
public class Console
{
    
    /**
     * A single java.awt.Robot instance for all of Common to use. It is recommended by the Robot docs to share an instance to minimize memory usage.<br>
     * The first time this class is accessed we will use a static initializer to try to initialize this variable and log errors if it fails.<br>
     * If we just said "consoleRobot = new Robot();" here we couldn't catch errors.
     */
    public static Robot consoleRobot;
    
    static
    {
        try
        {
            Console.consoleRobot = new Robot();
        }
        catch (Exception e)
        {
            Log.logException(e);
        }
    }
    
    /**
     * See {@link StringFunctions#objectDumper(Object, int)} for details.
     * 
     * @param o
     *            The object to be dumped.
     */
    public static void objectDumper(Object o)
    {
        System.out.print(StringFunctions.objectDumper(o, 0));
    }
    
    /**
     * This function is a quick wrapper around the java.awt.Robot screen capture API, to assist in taking a screenshot of the whole screen.
     * 
     * @return A snapshot of the whole screen as a BufferedImage, or null if an error occured
     */
    public static BufferedImage getScreenshot()
    {
        return Console.getScreenshot(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }
    
    /**
     * This function is a quick wrapper around the java.awt.Robot screen capture API.
     * 
     * @param boundaries
     *            The screen rectangle to capture a snapshot of
     * @return A snapshot of the specified screen rectangle as a BufferedImage, or null if an error occured
     */
    public static BufferedImage getScreenshot(Rectangle boundaries)
    {
        BufferedImage screenshot = null;
        try
        {
            screenshot = Console.consoleRobot.createScreenCapture(boundaries);
        }
        catch (Exception e)
        {
            Log.logException(e);
        }
        return screenshot;
    }
    
}
