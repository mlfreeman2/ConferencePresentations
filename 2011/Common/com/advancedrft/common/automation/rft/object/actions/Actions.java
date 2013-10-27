package com.advancedrft.common.automation.rft.object.actions;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

import com.advancedrft.common.automation.rft.KeyboardAndMouse;
import com.advancedrft.common.automation.rft.KeyboardAndMouse.MouseButtons;
import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.io.Console;
import com.advancedrft.common.lang.Regex;
import com.advancedrft.common.lang.StringFunctions;
import com.rational.test.ft.CoordinateOffScreenException;
import com.rational.test.ft.ObjectIsDisposedException;
import com.rational.test.ft.SubitemNotFoundException;
import com.rational.test.ft.UnableToPerformActionException;
import com.rational.test.ft.UnregisteredObjectException;
import com.rational.test.ft.UnsupportedActionException;
import com.rational.test.ft.WindowActivateFailedException;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.SelectGuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextGuiTestObject;
import com.rational.test.ft.object.manager.ObjectManager;
import com.rational.test.ft.script.Action;
import com.rational.test.ft.script.MouseModifiers;
import com.rational.test.ft.script.RationalTestScriptConstants;
import com.rational.test.ft.script.Subitem;
import com.rational.test.ft.script.SubitemFactory;
import com.rational.test.ft.vp.ITestDataElement;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;

/**
 * This class is a library of wrappers around generic RFT actions.<br />
 * It is abstract because certain functions need to be filled in differently depending on which RFT domain you're working in.<br />
 * The recommended usage is to simply create a new instance of one of the domain specific subclasses of this class and then call the actions for every click / type / etc.<br />
 * There is no harm in creating as many instances as you want. <br />
 * <br />
 * This class can be extended and individual methods overridden as needed to create action libraries for various RFT domains. A version for web stuff is included...see WebActions
 */
public abstract class Actions
{
    
    /**
     * This function tries a user-specified number of times to click on a user specified point (relative to the upper left corner of an object). <br>
     * It supports clicking any number of times with any mouse button as well as any combination of control, alt, or shift pressed.
     * 
     * @param theObject
     *            The GuiTestObject to click on/with
     * @param friendlyName
     *            Friendly name of object to click on, used for logging purposes
     * @param numOfTries
     *            Number of times to try to complete this whole function
     * @param numOfClicks
     *            Number of clicks to apply (e.g. 1 for simulating single clicking, 2 for simulating double clicking, etc)
     * @param mouseButtonName
     *            MouseButtons.Left, MouseButtons.Middle, or MouseButtons.Right to indicate which mouse button to pretend to click with
     * @param ctrlOn
     *            True to CTRL+click the item. Can be mixed with ALT and SHIFT.
     * @param altOn
     *            True to ALT+click the item. Can be mixed with CTRL and SHIFT.
     * @param shiftOn
     *            True to SHIFT+click the item. Can be mixed with CTRL and ALT.
     * @param xCoord
     *            X coordinate to click on (how far right of the object's left side) (-1 clicks on center, -2 on upper left, -3 on upper right, -4 on lower right, and -5 on lower left)
     * @param yCoord
     *            Y coordinate to click on (how far below the object's top) (-1 clicks on center, -2 on upper left, -3 on upper right, -4 on lower right, and -5 on lower left)
     * @param logErrors
     *            False to hide all errors
     * @return True if successful, False if failed
     */
    public boolean click(TestObject theObject, String friendlyName, int numOfTries, int numOfClicks, MouseButtons mouseButtonName, boolean ctrlOn, boolean altOn, boolean shiftOn, int xCoord, int yCoord, boolean logErrors)
    {
        friendlyName = StringFunctions.ifEmpty(friendlyName, "the object");
        MouseModifiers modifiers = KeyboardAndMouse.buildMouseModifiers(mouseButtonName, ctrlOn, altOn, shiftOn);
        int attempts = 0;
        boolean requiredResult = false;
        do
        {
            GuiTestObject gto = null;
            try
            {
                gto = new GuiTestObject(theObject);
                gto.ensureObjectIsVisible();
                if (xCoord == -1 || yCoord == -1)
                {
                    Rectangle bounds = gto.getClippedScreenRectangle();
                    xCoord = bounds.width / 2;
                    yCoord = bounds.height / 2;
                }
                else if (xCoord == -2 || yCoord == -2)
                {
                    xCoord = 0;
                    yCoord = 0;
                }
                else if (xCoord == -3 || yCoord == -3)
                {
                    Rectangle bounds = gto.getClippedScreenRectangle();
                    xCoord = bounds.width;
                    yCoord = 0;
                }
                else if (xCoord == -4 || yCoord == -4)
                {
                    Rectangle bounds = gto.getClippedScreenRectangle();
                    xCoord = 0;
                    yCoord = bounds.height;
                }
                else if (xCoord == -5 || yCoord == -5)
                {
                    Rectangle bounds = gto.getClippedScreenRectangle();
                    xCoord = bounds.width;
                    yCoord = bounds.height;
                }
                gto.nClick(numOfClicks, modifiers, new Point(xCoord, yCoord));
                requiredResult = true;
            }
            catch (WindowActivateFailedException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on " + friendlyName + " because it is disabled");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (CoordinateOffScreenException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    Log.logError("Unable to click on " + friendlyName + " because it is off the screen", Console.getScreenshot());
                }
            }
            catch (UnregisteredObjectException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    Log.logError("Unable to click on " + friendlyName + " because it became unregistered", Console.getScreenshot());
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on " + friendlyName + " because of a general UnableToPerformActionException");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (Exception e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                attempts++;
            }
        } while (requiredResult != true && attempts < numOfTries);
        return requiredResult;
    }
    
    /**
     * Gets the list of options in a listbox or dropdown
     * 
     * @param object
     *            The listbox or dropdown test object to extract the items from
     * @return String array of all the options in the listbox or dropdown test object (in the order they appear)
     */
    public String[] getItemsInList(TestObject object)
    {
        ITestDataList nameList = (ITestDataList) object.getTestData(getDomainSpecificTestDataTypeName(getGenericControlType(object.getProperty(".class").toString()), "List Items"));
        String[] options = new String[nameList.getElementCount()];
        ITestDataElementList nameListElements = nameList.getElements();
        for (int i = 0; i < options.length; i++)
        {
            try
            {
                ITestDataElement option = nameListElements.getElement(i);
                options[i] = option.getElement().toString();
            }
            catch (Exception e)
            {
                
            }
        }
        return options;
    }
    
    /**
     * Gets the list of selected options in a listbox or dropdown
     * 
     * @param object
     *            The listbox or dropdown test object to extract the items from
     * @return String array of all the selected options in the listbox or dropdown test object (in the order they appear)
     */
    public String[] getSelectedItemsInList(TestObject object)
    {
        ITestDataList nameList = (ITestDataList) object.getTestData(getDomainSpecificTestDataTypeName(getGenericControlType(object.getProperty(".class").toString()), "Selected List Items"));
        String[] options = new String[nameList.getElementCount()];
        ITestDataElementList nameListElements = nameList.getElements();
        for (int i = 0; i < options.length; i++)
        {
            try
            {
                ITestDataElement option = nameListElements.getElement(i);
                options[i] = option.getElement().toString();
            }
            catch (Exception e)
            {
                
            }
        }
        return options;
    }
    
    /**
     * This function tries a user-specified number of times to hover on a user specified point (relative to the upper left corner of an object)
     * 
     * @param object
     *            The TestObject to hover over
     * @param friendlyName
     *            A name for the object, used in error messages
     * @param numOfTries
     *            Number of times to try to hover over the object
     * @param hoverTime
     *            How long, in seconds, to hover over the object
     * @param xCoord
     *            X coordinate to hover over (how far right of the object's left side)
     * @param yCoord
     *            Y coordinate to hover over (how far below the object's top)
     * @param logErrors
     *            True to log any errors encountered, False not to.
     * @return True if successful, False if failed
     */
    public boolean hover(TestObject object, String friendlyName, int numOfTries, double hoverTime, int xCoord, int yCoord, boolean logErrors)
    {
        GuiTestObject gto = null;
        boolean result = false;
        int attempts = 0;
        do
        {
            try
            {
                gto = new GuiTestObject(object);
                gto.hover(hoverTime, new Point(xCoord, yCoord));
                result = true;
            }
            catch (UnsupportedActionException e)
            {
                com.rational.test.ft.script.RationalTestScript.sleep(0.2);
            }
            catch (CoordinateOffScreenException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to hover on " + friendlyName + " at (" + xCoord + "," + yCoord + ") because the requested coordinates are off screen.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (WindowActivateFailedException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to hover on " + friendlyName + " at (" + xCoord + "," + yCoord + ") because the object is disabled.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (Exception e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                attempts++;
            }
        } while (result != true && attempts < numOfTries);
        return result;
    }
    
    /**
     * Selects one or more items based on either text or index.<br>
     * Prefix the string with "multi=" to select multiple items.<br>
     * Prefix the string with "index=" and use comma separated integers to select items by their indexes.<br>
     * Prefix the string with "like=" to select the item closest matching the string.<br>
     * Append ";threshold=" and include the number of allowable differences the match can allow.<br>
     * Use "index=random" to randomly select an item. <br>
     * Append ";exclude=" and a comma separated list of indexes (starting with 0) to exclude certain elements from possibly being selected.<br>
     * Append ";exclude=" and include the word "selected" as one of the items to automatically exclude any already-selected items.<br>
     * 
     * @param theObject
     *            TestObject to perform the selection on
     * @param friendlyName
     *            A name for the object, used in error messages
     * @param numOfTries
     *            Number of times to try to select the data
     * @param logErrors
     *            False to hide all errors
     * @param data
     *            Item(s) to be selected.<br>
     * @return The selected text
     */
    public String select(TestObject theObject, String friendlyName, int numOfTries, boolean logErrors, String data)
    {
        Map<String, String> attributes = Regex.getNameValuePairs(data);
        boolean isMulti = false, isDataIndex = false;
        if (attributes.containsKey("multi"))
        {
            isMulti = true;
            data = attributes.get("multi");
        }
        else if (attributes.containsKey("like"))
        {
            String[] items = getItemsInList(theObject);
            if (attributes.containsKey("threshold"))
            {
                data = StringFunctions.getBestMatch(attributes.get("like"), Integer.parseInt(attributes.get("threshold")), items);
            }
            else
            {
                data = StringFunctions.getBestMatch(attributes.get("like"), 99999, items);
            }
        }
        else if (attributes.containsKey("index"))
        {
            isDataIndex = true;
            if (attributes.get("index").contains(","))
            {
                isMulti = true;
            }
            
            if (isMulti == false && !attributes.get("index").contains("random"))
            {
                data = attributes.get("index");
            }
            else
            {
                if (attributes.containsKey("multi"))
                {
                    if (!attributes.get("multi").contains("random"))
                    {
                        data = attributes.get("multi");
                    }
                }
                else
                {
                    Vector<Integer> excludedIndexes = new Vector<Integer>();
                    if (attributes.containsKey("exclude"))
                    {
                        String[] excludeItems = attributes.get("exclude").split(",");
                        
                        for (int i = 0; i < excludeItems.length; i++)
                        {
                            try
                            {
                                excludedIndexes.add(Integer.valueOf(excludeItems[i]));
                            }
                            catch (NumberFormatException e)
                            {
                                if (excludeItems[i].equalsIgnoreCase("selected"))
                                {
                                    String[] selected = getSelectedItemsInList(theObject);
                                    String[] items = getItemsInList(theObject);
                                    for (String s : selected)
                                    {
                                        excludedIndexes.add(new Integer(Arrays.asList(items).indexOf(s)));
                                    }
                                }
                            }
                        }
                    }
                    if (attributes.get("index").contains("random"))
                    {
                        ArrayList<String> finalItems = new ArrayList<String>();
                        String[] itemsInList = getItemsInList(theObject);
                        String[] desiredItems = attributes.get("index").split(",");
                        for (int i = 0; i < desiredItems.length; i++)
                        {
                            if (desiredItems[i].equals("random"))
                            {
                                Integer result = null;
                                do
                                {
                                    result = new Integer(new java.util.Random().nextInt(itemsInList.length));
                                } while (excludedIndexes.indexOf(result) != -1);
                                finalItems.add(result.toString());
                            }
                            else
                            {
                                finalItems.add(desiredItems[i]);
                            }
                        }
                        data = StringFunctions.join(",", finalItems);
                    }
                }
            }
        }
        
        int attempts = 0;
        String requiredResult = null;
        SelectGuiSubitemTestObject sgsto = null;
        do
        {
            try
            {
                sgsto = new SelectGuiSubitemTestObject(theObject);
                try
                {
                    sgsto.ensureObjectIsVisible();
                    ObjectManager.findObjectAndInvoke(true, sgsto, "scrollIntoView", null, null);
                }
                catch (Exception e)
                {
                    
                }
                if (isMulti)
                {
                    for (String item : data.split(","))
                    {
                        Subitem position = isDataIndex ? SubitemFactory.atIndex(Integer.parseInt(item)) : SubitemFactory.atText(item);
                        sgsto.click(RationalTestScriptConstants.CTRL_LEFT, position);
                    }
                    requiredResult = (String) sgsto.getProperty(".value");
                }
                else
                {
                    Subitem position = isDataIndex ? SubitemFactory.atIndex(Integer.parseInt(data)) : SubitemFactory.atText(data);
                    if (isDataIndex == false)
                    {
                        sgsto.select(data);
                    }
                    else
                    {
                        sgsto.select(Integer.parseInt(data));
                    }
                    requiredResult = (String) sgsto.getProperty(".value");
                    if (!requiredResult.equals(data))
                    {
                        sgsto.setState(Action.select(), position);
                    }
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to select anything in " + friendlyName + " because of an UnableToPerformActionException.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnsupportedActionException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to select anything in " + friendlyName + " because of an UnsupportedActionException.");
                    error = error.replace("CRFCP0050E: No screen point found for object", "Unable to select anything in " + friendlyName + " because it is not visible.");
                    error = error.replace("CRFCN0153E: Cannot get screen point for subitem", "Unable to select " + data + " in " + friendlyName + " because that bit of data is not visible.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (ObjectIsDisposedException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to select anything in " + friendlyName + " because the object was disposed of.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnregisteredObjectException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to select anything in " + friendlyName + " because the object became unregistered.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (SubitemNotFoundException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    Log.logError("In " + friendlyName + ", it was impossible to select the requested data " + data + " because it was not in the list.", Console.getScreenshot());
                }
                return null;
            }
            catch (Exception e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                attempts++;
            }
        } while (requiredResult == null && attempts < numOfTries);
        return requiredResult;
    }
    
    /**
     * This function allows you to try to type into a field up to as many times as you specify. On the last try it logs any errors it hit.
     * 
     * @param theObject
     *            The TestObject to type on
     * @param friendlyName
     *            A name for the object, used in error messages
     * @param data
     *            The data to type in
     * @param numOfTries
     *            The number of times to try to type
     * @param logErrors
     *            False to hide all errors
     * @param compareAndRetype
     *            True to check the contents of the box after typing and retype 0.7 seconds later if the contents don't match the supplied string
     * @return The contents of the textbox
     */
    public String type(TestObject theObject, String friendlyName, String data, int numOfTries, boolean logErrors, boolean compareAndRetype)
    {
        friendlyName = StringFunctions.ifEmpty(friendlyName, "the object");
        String requiredResult = null;
        TextGuiTestObject tgto = null;
        int attempts = 0;
        do
        {
            try
            {
                tgto = new TextGuiTestObject(theObject);
                tgto.setText(data);
                if (compareAndRetype)
                {
                    com.rational.test.ft.script.RationalTestScript.sleep(0.2);
                    if (tgto.getText().equals(data) == false)
                    {
                        tgto.setText(data);
                    }
                }
                requiredResult = tgto.getText();
            }
            catch (UnregisteredObjectException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = "Unable to type \"" + data + "\" into " + friendlyName + " because the object became unregistered.";
                    Log.logError(error, Console.getScreenshot());
                }
                requiredResult = null;
            }
            catch (UnableToPerformActionException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to type \"" + data + "\" into " + friendlyName + " because of an UnableToPerformActionException");
                    Log.logError(error, Console.getScreenshot());
                }
                requiredResult = null;
            }
            catch (WindowActivateFailedException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    Log.logError("Unable to type \"" + data + "\" into " + friendlyName + " because it is disabled", Console.getScreenshot());
                }
                requiredResult = null;
            }
            catch (ObjectIsDisposedException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = "Unable to type \"" + data + "\" into " + friendlyName + " because the object was disposed";
                    Log.logError(error, Console.getScreenshot());
                }
                requiredResult = null;
            }
            catch (UnsupportedActionException e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to type \"" + data + "\" into " + friendlyName + " because of an UnsupportedActionException");
                    error = error.replace("CRFCP0050E: No screen point found for object", "Unable to type \"" + data + "\" into " + friendlyName + " because it is not visible on screen now.");
                    Log.logError(error, Console.getScreenshot());
                }
                requiredResult = null;
            }
            catch (Exception e)
            {
                if (logErrors && attempts == numOfTries - 1)
                {
                    Log.logException(e);
                }
                requiredResult = null;
            }
            finally
            {
                attempts++;
            }
        } while (requiredResult == null && attempts < numOfTries);
        return requiredResult;
    }
    
    /**
     * This function will look up a domain specific name for certain properties.<br>
     * Each domain needs to implement this function to return things such as ".value", ".disabled", etc for the following.<br>
     * <ul>
     * <li>Disabled</li>
     * <li>Text</li>
     * <li>Value</li>
     * <li>Index</li>
     * </ul>
     * The results of this are later used in TestObject.getProperty() calls.<br>
     * 
     * @param property
     *            The domain specific property that a function is asking for
     * @return The name of said property (e.g. for Html domain stuff "Disabled" should return ".disabled" and "Text" should return ".text")
     */
    public abstract String getDomainSpecificPropertyName(String property);
    
    /**
     * This function will look up a domain specific name for certain test data types.<br>
     * Each domain to implement this function to return things such as "list", "selected", etc for the following.<br>
     * The results of this are later used in TestObject.getTestDataType() calls.<br>
     * 
     * @param control
     *            This is the type of object you are dealing with (e.g. Table, List, etc). Each object type has a recommended default test daya type, defined as "Default"
     * @param property
     *            The domain specific property that a Section.java function is asking for
     * @return The name of said test data type <br>
     *         (e.g. for a List in the Html domain, the generic test data type "List Items" should return "list", but for a List in the Flex domain it should return "All Items")
     */
    public abstract String getDomainSpecificTestDataTypeName(String control, String property);
    
    /**
     * This function will tell you whether a given .class is a Table, List, Button, etc. <br>
     * It will return an empty string if there is currently no mapping for the supplied .class value
     * 
     * @param className
     *            The .class property of your TestObject
     * @return A generic object type (Table, List, Button, Link, Image, etc)
     */
    public abstract String getGenericControlType(String className);
}
