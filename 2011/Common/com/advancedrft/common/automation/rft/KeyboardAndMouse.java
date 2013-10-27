package com.advancedrft.common.automation.rft;

import java.awt.Point;
import java.awt.Rectangle;

import com.advancedrft.common.io.Console;
import com.advancedrft.common.lang.StringFunctions;
import com.rational.test.ft.CoordinateOffScreenException;
import com.rational.test.ft.UnableToPerformActionException;
import com.rational.test.ft.UnsupportedActionException;
import com.rational.test.ft.WindowActivateFailedException;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.script.MouseModifiers;
import com.rational.test.ft.script.RationalTestScript;
import com.rational.test.ft.script.SubitemFactory;
import com.rational.test.ft.sys.graphical.Highlighter;

/**
 * Functions for doing raw mouse clicks and key presses. Uses a combination of RFT anf java.awt.Robot
 */
public class KeyboardAndMouse
{
    /**
     * A list of the possible mouse buttons you can use with click and drag
     */
    public static enum MouseButtons
    {
        /**
         * A list of the possible mouse buttons you can use with click and drag
         */
        Left, Middle, Right;
    }
    
    /**
     * Presses and releases the Windows logo key and then the letter specified<br>
     * (e.g. WinLogo then D to show desktop, WinLogo then R to show run box, etc).
     * 
     * @param letter
     *            The letter to press down.
     */
    public static void pressWinLogoKeyPlusLetter(String letter)
    {
        RootTestObject.getRootTestObject().emitLowLevelEvent(SubitemFactory.keyDown("{Win}"));
        RootTestObject.getRootTestObject().emitLowLevelEvent(SubitemFactory.keyDown(letter));
        RootTestObject.getRootTestObject().emitLowLevelEvent(SubitemFactory.keyUp(letter));
        RootTestObject.getRootTestObject().emitLowLevelEvent(SubitemFactory.keyUp("{Win}"));
    }
    
    /**
     * This function constructs a RFT mouse modifiers object from a set of options.
     * 
     * @param button
     *            {@link MouseButtons#Left}, {@link MouseButtons#Middle}, {@link MouseButtons#Right}
     * @param ctrlOn
     *            True to simulate holding the ctrl key down
     * @param altOn
     *            True to simulate holding the alt key down
     * @param shiftOn
     *            True to simulate holding the shift key down
     * @return A RFT mouse modifiers object.
     */
    public static MouseModifiers buildMouseModifiers(MouseButtons button, boolean ctrlOn, boolean altOn, boolean shiftOn)
    {
        MouseModifiers modifiers = null;
        if (button.equals(MouseButtons.Middle))
        {
            modifiers = MouseModifiers.create(MouseModifiers.MOUSE_MIDDLE);
        }
        else if (button.equals(MouseButtons.Right))
        {
            modifiers = MouseModifiers.create(MouseModifiers.MOUSE_RIGHT);
        }
        else
        {
            modifiers = MouseModifiers.create(MouseModifiers.MOUSE_LEFT);
        }
        if (ctrlOn == true)
        {
            modifiers.setCtrl();
        }
        if (altOn == true)
        {
            modifiers.setAlt();
        }
        if (shiftOn == true)
        {
            modifiers.setShift();
        }
        return modifiers;
    }
    
    /**
     * This function returns a string describing a RFT mouse modifiers object (which button will be pressed, what keys are held down, etc)
     * 
     * @param modifiers
     *            The mouse modifiers object to be described
     * @return The string describing it.
     */
    public static String describeMouseModifiers(MouseModifiers modifiers)
    {
        String mouseButtonName = "Left";
        if (modifiers.isMiddle())
        {
            mouseButtonName = "Middle";
        }
        else if (modifiers.isRight())
        {
            mouseButtonName = "Right";
        }
        String actionDesc = mouseButtonName + " clicked with";
        if (modifiers.isCtrl() == true)
        {
            actionDesc += " ctrl pressed down";
        }
        if (modifiers.isAlt() == true)
        {
            if (modifiers.isCtrl() == true)
            {
                actionDesc += ",";
            }
            actionDesc += " alt pressed down";
        }
        if (modifiers.isShift() == true)
        {
            if (modifiers.isCtrl() == true || modifiers.isAlt() == true)
            {
                actionDesc += ",";
            }
            actionDesc += " shift pressed down";
        }
        if (!modifiers.isCtrl() && !modifiers.isAlt() && !modifiers.isShift())
        {
            actionDesc += " neither ctrl, alt, nor shift pressed down";
        }
        return actionDesc;
    }
    
    /**
     * This function clicks on the specified point on screen the specified number of times using the specified mouse button and holding down the specified keys.
     * 
     * @param xCoord
     *            X coordinate on screen to click on (relative to upper left corner)
     * @param yCoord
     *            Y coordinate on screen to click on (relative to upper left corner, values increase going down)
     * @param numOfClicks
     *            Number of times to click the mouse
     * @param mouseButtonName
     *            {@link MouseButtons#Left}, {@link MouseButtons#Middle}, {@link MouseButtons#Right}
     * @param ctrlOn
     *            True to simulate holding down Ctrl, False not to.
     * @param altOn
     *            True to simulate holding down Alt, False not to.
     * @param shiftOn
     *            True to simulate holding down Shift, False not to.
     * @param logErrors
     *            True to log all errors, False to not log them
     * @return 0 if successful, -1 if error
     */
    public static int click(int xCoord, int yCoord, int numOfClicks, MouseButtons mouseButtonName, boolean ctrlOn, boolean altOn, boolean shiftOn, boolean logErrors)
    {
        MouseModifiers modifiers = KeyboardAndMouse.buildMouseModifiers(mouseButtonName, ctrlOn, altOn, shiftOn);
        int requiredResult = -1;
        try
        {
            RootTestObject.getRootTestObject().getScreen().nClick(numOfClicks, modifiers, new Point(xCoord, yCoord));
            requiredResult = 0;
        }
        catch (WindowActivateFailedException e)
        {
            if (logErrors)
            {
                String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on the screen because it is disabled");
                Log.logError(error, Console.getScreenshot());
            }
            requiredResult = -1;
        }
        catch (CoordinateOffScreenException e)
        {
            if (logErrors)
            {
                Log.logError("Unable to click on (" + xCoord + "," + yCoord + ") because it is off the screen", Console.getScreenshot());
            }
            requiredResult = -1;
        }
        catch (UnableToPerformActionException e)
        {
            if (logErrors)
            {
                String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on (" + xCoord + "," + yCoord + ") because of a general UnableToPerformActionException");
                Log.logError(error, Console.getScreenshot());
            }
            requiredResult = -1;
        }
        catch (Exception e)
        {
            if (logErrors)
            {
                Log.logException(e);
            }
            requiredResult = -1;
        }
        return requiredResult;
    }
    
    /**
     * This function drags the mouse between two arbitrary points on screen.
     * 
     * @param startX
     *            X coordinate of where to click to start the drag (on screen).
     * @param startY
     *            Y coordinate of where to click to start the drag (on screen).
     * @param endX
     *            X coordinate of where to drag the object to (on screen).
     * @param endY
     *            Y coordinate of where to drag the object to (on screen).
     * @param numOfTries
     *            Number of times to try to drag the item.
     * @param mouseButtonName
     *            {@link MouseButtons#Left}, {@link MouseButtons#Middle}, {@link MouseButtons#Right}
     * @param ctrlOn
     *            True to CTRL+drag the item. Can be mixed with ALT and SHIFT.
     * @param altOn
     *            True to ALT+drag the item. Can be mixed with CTRL and SHIFT.
     * @param shiftOn
     *            True to SHIFT+drag the item. Can be mixed with CTRL and ALT.
     * @return 0 if successful, -1 if failed
     */
    public static int drag(int startX, int startY, int endX, int endY, int numOfTries, MouseButtons mouseButtonName, boolean ctrlOn, boolean altOn, boolean shiftOn)
    {
        MouseModifiers m = KeyboardAndMouse.buildMouseModifiers(mouseButtonName, ctrlOn, altOn, shiftOn);
        int attempts = 0, requiredResult = -1;
        do
        {
            Point start = new Point(startX, startY), end = new Point(endX, endY);
            try
            {
                RootTestObject.getRootTestObject().getScreen().drag(m, start, end);
                requiredResult = 0;
            }
            catch (Exception e)
            {
                if (attempts == numOfTries - 1)
                {
                    Log.logException(e);
                }
                requiredResult = -1;
            }
            finally
            {
                attempts++;
            }
        } while (requiredResult != 0 && attempts < numOfTries);
        return requiredResult;
    }
    
    /**
     * Moves the mouse cursor to the specified screen coordinates
     * 
     * @param x
     *            X coordinate on screen to move the mouse to (relative to upper left corner)
     * @param y
     *            Y coordinate on screen to move the mouse to (relative to upper left corner, values increase going down)
     * @param logErrors
     *            True to log errors encountered in the process, False not to
     * @return 0 if it worked, -1 if it didn't
     */
    public static int mouseMove(int x, int y, boolean logErrors)
    {
        int requiredResult = -1;
        try
        {
            RootTestObject.getRootTestObject().getScreen().mouseMove(new Point(x, y));
            requiredResult = 0;
        }
        catch (WindowActivateFailedException e)
        {
            if (logErrors)
            {
                String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on the screen because it is disabled");
                Log.logError(error, Console.getScreenshot());
            }
            requiredResult = -1;
        }
        catch (CoordinateOffScreenException e)
        {
            if (logErrors)
            {
                Log.logError("Unable to click on (" + x + "," + y + ") because it is off the screen", Console.getScreenshot());
            }
            requiredResult = -1;
        }
        catch (UnableToPerformActionException e)
        {
            if (logErrors)
            {
                String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on (" + x + "," + y + ") because of a general UnableToPerformActionException");
                Log.logError(error, Console.getScreenshot());
            }
            requiredResult = -1;
        }
        catch (Exception e)
        {
            if (logErrors)
            {
                Log.logException(e);
            }
            requiredResult = -1;
        }
        return requiredResult;
    }
    
    /**
     * This function highlights the specified rectangle on screen.
     * 
     * @param screenRect
     *            The rectangle on screen to highlight
     * @param duration
     *            How long to highlight it for
     */
    public static void highlight(Rectangle screenRect, double duration)
    {
        Highlighter h = Highlighter.create(screenRect);
        h.show();
        RationalTestScript.sleep(duration);
        h.hide();
    }
    
    /**
     * This function tries a user-specified number of times to hover on a user specified point (relative to the upper left corner of the screen)
     * 
     * @param numOfTries
     *            The number of times to try to hover on the specified screen coordinates
     * @param hoverTime
     *            The amount of time to hover on the specified coordinates
     * @param xCoord
     *            The screen x coordinate
     * @param yCoord
     *            The screen y coordinate
     * @param logErrors
     *            True to log errors, False to not log them.
     * @return 0 if successful, -1 if failed.
     */
    public static int hover(int numOfTries, double hoverTime, int xCoord, int yCoord, boolean logErrors)
    {
        int result = -1, attempts = 0;
        do
        {
            try
            {
                RootTestObject.getRootTestObject().getScreen().hover(hoverTime, new Point(xCoord, yCoord));
                result = 0;
            }
            catch (UnsupportedActionException e)
            {
                com.rational.test.ft.script.RationalTestScript.sleep(hoverTime);
            }
            catch (CoordinateOffScreenException e)
            {
                if (attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to hover at (" + xCoord + "," + yCoord + ") because the requested coordinates are off screen.");
                    Log.logError(error, Console.getScreenshot());
                }
                result = -1;
            }
            catch (Exception e)
            {
                if (attempts == numOfTries - 1)
                {
                    Log.logException(e);
                }
                result = -1;
            }
            finally
            {
                attempts++;
            }
        } while (result != 0 && attempts < numOfTries);
        return result;
    }
    
}
