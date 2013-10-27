package com.lps.rsdc2010.common.io;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.lps.rsdc2010.common.lang.StringFunctions;
import com.rational.test.ft.script.RationalTestScript;

public class Console
{
    
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
            Robot robot = new Robot();
            screenshot = robot.createScreenCapture(boundaries);
        }
        catch (Exception e)
        {
            RationalTestScript.logException(e);
        }
        return screenshot;
    }
}
