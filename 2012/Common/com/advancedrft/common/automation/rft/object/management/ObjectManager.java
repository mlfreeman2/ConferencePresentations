package com.advancedrft.common.automation.rft.object.management;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.advancedrft.common.automation.rft.KeyboardAndMouse;
import com.advancedrft.common.automation.rft.KeyboardAndMouse.MouseButtons;
import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.TO;
import com.advancedrft.common.automation.rft.object.TO.Subitems;
import com.advancedrft.common.automation.rft.object.actions.Actions;
import com.advancedrft.common.io.Console;
import com.advancedrft.common.lang.MapFunctions;
import com.advancedrft.common.lang.Regex;
import com.advancedrft.common.lang.StringFunctions;
import com.rational.test.ft.CoordinateOffScreenException;
import com.rational.test.ft.ObjectIsDisposedException;
import com.rational.test.ft.ObjectNotFoundException;
import com.rational.test.ft.SubitemNotFoundException;
import com.rational.test.ft.UnableToPerformActionException;
import com.rational.test.ft.UnregisteredObjectException;
import com.rational.test.ft.UnsupportedActionException;
import com.rational.test.ft.WindowActivateFailedException;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.map.MappedTestObject;
import com.rational.test.ft.object.map.ObjectMap;
import com.rational.test.ft.script.RationalTestScript;

/**
 * This class is a basic non-object-map object management system. <br>
 * It can load objects from maps, have them programatically added, or load them from another source such as an Excel file.<br>
 * The primary goals here are:<br>
 * 1. Enable automation developers to address the objects in the applications under test with arbitrary Java string names.<br>
 * --> This enables names to be exponentially more descriptive than you can be with Java variables or IBM's name generation routines.<br>
 * 2. Encapsulate the find-act-release cycle so that the automation team doesn't have to remember to clean up testobjects themselves.<br>
 * --> This leads to more stable, efficient, and consistent code.<br>
 * 3. Automatically retry actions if necessary.<br>
 * --> This vastly improves script stability.<br>
 * 4. Improve script readability.<br>
 * --> Scripts typically will have lines like "click("Google Search Link");" or "type("Google Search Box", "Search String");" instead of "_googSrL().click();" or "_txt_googSB().type("Search String");".<br>
 * <br>
 * <br>
 * <br>
 * Typical usage is to create one subclass of this class for each domain you work with (e.g. Web, .NET, Win32, Java, etc).<br>
 * Each one of those subclasses should override functions here with domain specific implementations (if necessary) and have additional functions that represent common tasks you do in that domain no matter what exact app you happen to be testing.<br>
 * <br>
 * For each application you test you should subclass the relevant domain-specific class.<br>
 * In that level you should override common actions to handle application-wide custom controls and have additional functions that represent common tasks that can happen on any screen in that application.<br>
 * <br>
 * Finally you should subclass the application-specific class and make one class for each screen or major area in your app.<br>
 * In that layer you override common actions to handle custom controls on that screen and create additional actions to do often-repeated tasks on that screen.<br>
 * <br>
 * <br>
 * Since finding all your objects from RootTestObject is not recommended by IBM, this class has a "base object" placeholder in it.<br>
 * It needs to be set to an object above (but not too far above) the objects you want to work with on that screen or page.<br>
 * This will reduce your expensive find() calls down to the minimum number: 1.<br>
 */
public abstract class ObjectManager
{
    /**
     * Maps a friendly name to a collection of object properties.<br />
     * The object properties are represented as a LinkedHashMap<String, String>
     */
    private LinkedHashMap<String, LinkedHashMap<String, String>> objectDefinitions    = new LinkedHashMap<String, LinkedHashMap<String, String>>();
    
    /**
     * A reference to a Functional Tester object map.
     */
    private ObjectMap                                            om;
    
    /**
     * A pointer to our library of generic actions. Subclasses of this point to domain specific subclasses of {@link Actions}
     */
    protected Actions                                            actionLib;
    
    /**
     * The object we start from in doing all our finds.
     */
    private TestObject                                           baseObject;
    
    /**
     * How many times we try each find-act-release cycle
     */
    private int                                                  numberOfActionTries  = 3;
    
    /**
     * How many times we try to find an object
     */
    private int                                                  numberOfFindTries    = 30;
    
    /**
     * How long we wait between find attempts
     */
    private double                                               delayBetweenTries    = 0.5;
    
    /**
     * The name of the page or screen this instance of ObjectManager represents
     */
    protected String                                             screenName           = "";
    
    /**
     * The properties used to find (or re-find) the base object
     */
    private LinkedHashMap<String, String>                        baseObjectProperties = new LinkedHashMap<String, String>();
    
    /**
     * @param screenName
     *            The name of the page or screen this instance of ObjectManager represents
     * @param actionLib
     *            A pointer to our specific library of actions.
     */
    public ObjectManager(String screenName, Actions actionLib)
    {
        this.screenName = screenName;
        this.actionLib = actionLib;
    }
    
    /**
     * This function loads an object map's contents for easy access.<br>
     * The #name field in the object map entries gets used as the friendly name for the object
     * 
     * @param path
     *            String to the object map to load.
     */
    protected final void loadObjectMap(String path)
    {
        File f = new File(path);
        if (f.canRead())
        {
            om = ObjectMap.load(f);
            Enumeration<?> e = om.elements();
            while (e.hasMoreElements())
            {
                MappedTestObject mto = (MappedTestObject) e.nextElement();
                addItemToTable(mto.getDescriptiveName(), "nf==ObjectMapID", mto.getId());
            }
        }
        else
        {
            Log.logError("Unable to find the object map file located at " + path);
        }
    }
    
    /**
     * This function unloads the object map and removes all the auto-added items from objectDefinitions.<br>
     */
    protected final void unloadObjectMap()
    {
        if (om != null)
        {
            om.freeSharedMemory();
            for (String s : getItemsPresentInTable())
            {
                if (isItemPropertySet(s, "nf==ObjectMapID"))
                {
                    removeItemFromTable(s);
                }
            }
        }
    }
    
    // ==============Object Table Manipulation Functions
    // =================================================
    
    /**
     * This function adds a Friendly Name => Properties mapping to the class.<br>
     * Properties that have a name beginning with "nf==" will not be used in RFT find() calls.<br>
     * You can essentially store arbitrary info with each object definition if you prepend "nf==" to the name.<br>
     * 
     * @param friendlyName
     *            The friendly name of the object
     * @param properties
     *            The properties of the object you're adding as alternating name,value strings.
     */
    protected final void addItemToTable(String friendlyName, String...properties)
    {
        if (properties.length % 2 != 0)
        {
            Log.logWarning("An odd number of property name/value entries were supplied for " + friendlyName + " in " + screenName);
        }
        for (int i = 0; i < properties.length; i++)
        {
            properties[i] = properties[i].replace("NF==", "nf==").replace("Nf==", "nf==").replace("nF==", "nf==");
        }
        objectDefinitions.put(friendlyName, MapFunctions.arrayToLinkedHashMap(properties));
    }
    
    /**
     * This function returns a list of every friendly name currently present in the Section object table.
     * 
     * @return The friendlyName of every object in the table
     */
    protected final List<String> getItemsPresentInTable()
    {
        return new ArrayList<String>(objectDefinitions.keySet());
    }
    
    /**
     * This function indicates if a given entry is present in the table.
     * 
     * @param friendlyName
     *            The friendly name of the entry
     * @return True if present, False if not.
     */
    protected final boolean isItemPresentInTable(String friendlyName)
    {
        return objectDefinitions.containsKey(friendlyName);
    }
    
    /**
     * This function removes a Friendly Name => Properties mapping from the class.
     * 
     * @param friendlyName
     *            The friendly name of the object
     */
    protected final void removeItemFromTable(String friendlyName)
    {
        objectDefinitions.remove(friendlyName);
    }
    
    /**
     * This function returns a given property's value for a given object or null if the property or object isn't present
     * 
     * @param friendlyName
     *            The object
     * @param propertyName
     *            The property you want
     * @return The property's value as a string or null if the property or object isn't present
     */
    protected final String getItemProperty(String friendlyName, String propertyName)
    {
        if (isItemPresentInTable(friendlyName) == false)
        {
            return null;
        }
        if (isItemPropertySet(friendlyName, propertyName) == false)
        {
            return null;
        }
        return objectDefinitions.get(friendlyName).get(propertyName);
    }
    
    /**
     * This function checks to see if a given property is defined for a given entry in objectDefinitions.
     * 
     * @param visiblename
     *            Name of the entry
     * @param propertyName
     *            Name of the property
     * @return boolean True if the given property is defined for the given entry and False if it isn't defined, or if the entry itself isn't defined.
     */
    protected final boolean isItemPropertySet(String friendlyName, String propertyName)
    {
        if (isItemPresentInTable(friendlyName) == false)
        {
            return false;
        }
        return objectDefinitions.get(friendlyName).containsKey(propertyName);
    }
    
    /**
     * This function adds/sets multiple properties in objectDefinitions for a given object definition.<br>
     * If the friendlyName entry isn't present, nothing happens.
     * 
     * @param friendlyName
     *            Name of the object you want to add the property to
     * @param properties
     *            The properties to set for the existing object
     */
    protected final void setItemProperties(String friendlyName, String...properties)
    {
        int length = properties.length;
        if (length % 2 != 0)
        {
            Log.logWarning("An odd number of property name/value entries were supplied for " + friendlyName + " in " + screenName);
            length -= 1;
        }
        for (int i = 0; i < length; i += 2)
        {
            String propertyName = properties[i];
            String propertyValue = properties[i + 1];
            setItemProperty(friendlyName, propertyName, propertyValue);
        }
    }
    
    /**
     * This function adds/sets a property in objectDefinitions for a given object definition.<br>
     * If the friendlyName entry isn't present, nothing happens.
     * 
     * @param friendlyName
     *            Name of the object you want to add the property to
     * @param propertyName
     *            Name of the property to add
     * @param propertyValue
     *            Value of the property to add
     */
    protected final void setItemProperty(String friendlyName, String propertyName, String propertyValue)
    {
        if (isItemPresentInTable(friendlyName))
        {
            propertyName = propertyName.replace("NF==", "nf==").replace("Nf==", "nf==").replace("nF==", "nf==");
            objectDefinitions.get(friendlyName).put(propertyName, propertyValue);
        }
    }
    
    /**
     * This function updates a given property from a given object definition in objectDefinitions.<br>
     * If the friendlyName entry isn't present, nothing happens.
     * 
     * @param friendlyName
     *            Name of the object you want to update the property from
     * @param property
     *            Names of the property to be updated
     * @param value
     *            The value to update
     * @return a string the original value of the property
     */
    protected final String updateItemProperty(String friendlyName, String property, String value)
    {
        if (isItemPresentInTable(friendlyName))
        {
            String originalID = getItemProperty(friendlyName, ".id");
            setItemProperty(friendlyName, property, value);
            return originalID;
        }
        return "";
    }
    
    /**
     * This function removes a given property from a given object definition in objectDefinitions.<br>
     * If the friendlyName entry isn't present, nothing happens.
     * 
     * @param visiblename
     *            Name of the object you want to remove the property from
     * @param properties
     *            Names of the properties to be deleted
     */
    protected final void deleteItemProperties(String friendlyName, String...properties)
    {
        for (String propertyName : properties)
        {
            deleteItemProperty(friendlyName, propertyName);
        }
    }
    
    /**
     * This function removes a given property from a given object definition in objectDefinitions.<br>
     * If the friendlyName entry isn't present, nothing happens.
     * 
     * @param visiblename
     *            Name of the object you want to remove the property from
     * @param propertyName
     *            Name of the property to be deleted
     */
    protected final void deleteItemProperty(String friendlyName, String propertyName)
    {
        if (isItemPresentInTable(friendlyName))
        {
            objectDefinitions.get(friendlyName).remove(propertyName);
        }
    }
    
    /**
     * This function copies the source entry's properties into the destination entry, first removing the destination entry if it exists
     * 
     * @param sourceFriendlyName
     *            The source entry to copy the properties from
     * @param destinationFriendlyName
     *            The destination entry to copy the properties to
     */
    protected final void copyItemProperties(String sourceFriendlyName, String destinationFriendlyName)
    {
        if (isItemPresentInTable(destinationFriendlyName))
        {
            removeItemFromTable(destinationFriendlyName);
        }
        addItemToTable(destinationFriendlyName, getItemPropertiesAsArray(sourceFriendlyName));
    }
    
    /**
     * This function gets all of the properties for a given object that don't match the exclusion patterns<br>
     * If you want to include everything in the object's properties, do not supply any exclusion patterns.<br>
     * Supplying an empty string ("") will cause everything to be excluded.
     * 
     * @param friendlyName
     *            Friendly name of the object.
     * @param propsToExclude
     *            The names or wildcard patterns of properties to exclude from the results
     * @return String array All of the properties defined in objectDefinitions for a given object.
     */
    protected final String[] getItemPropertiesAsArray(String friendlyName, String...propsToExclude)
    {
        return MapFunctions.getStringArray(objectDefinitions.get(friendlyName), propsToExclude);
    }
    
    /**
     * This function gets a COPY of all of the properties for a given object that don't match the exclusion patterns<br>
     * If you want to include everything in the object's properties, do not supply any exclusion patterns.<br>
     * Supplying an empty string ("") will cause everything to be excluded.
     * 
     * @param friendlyName
     *            Friendly name of the object.
     * @param propsToExclude
     *            The names or wildcard patterns of properties to exclude from the results
     * @return LinkedHashMap All of the properties defined in objectDefinitions for a given object.
     */
    protected final LinkedHashMap<String, String> getItemPropertiesAsLinkedHashMap(String friendlyName, String...propsToExclude)
    {
        // In order to allow the exclusion properties to work, we go [main LHM] -> [array] -> [LHM minus excluded properties].
        return MapFunctions.arrayToLinkedHashMap(getItemPropertiesAsArray(friendlyName, propsToExclude));
    }
    
    /**
     * This function is responsible for taking in a friendly name and finding the object that goes with it, if it's there.
     * 
     * @param friendlyName
     *            Name of the object you're looking for
     * @return The object itself.
     * @throws UnableToPerformActionException
     *             A generic exception thrown if something can't be done. Check the message for details.
     * @throws ObjectNotFoundException
     *             A generic exception thrown if the object can't be found. Check the message for details.
     */
    protected TestObject findDefinedItem(String friendlyName) throws UnableToPerformActionException, ObjectNotFoundException
    {
        if (!isItemPresentInTable(friendlyName))
        {
            throw new UnableToPerformActionException(friendlyName + " is not defined in your object table for " + screenName);
        }
        if (isItemPropertySet(friendlyName, "nf==ObjectMapID"))
        {
            return TO.getMappedObjectByID(om, getItemProperty(friendlyName, "nf==ObjectMapID"))[0];
        }
        TestObject findStart = baseObject;
        if (isItemPropertySet(friendlyName, "nf==externalStartingPoint") == true)
        {
            try
            {
                findStart = findDefinedItem(getItemProperty(friendlyName, "nf==externalStartingPoint"));
            }
            catch (ObjectNotFoundException e)
            {
                throw new ObjectNotFoundException("Unable to find the external starting point for " + friendlyName);
            }
        }
        
        String[] properties = MapFunctions.getStringArray(objectDefinitions.get(friendlyName), "nf==");
        if (isItemPropertySet(friendlyName, "nf==path") == true)
        {
            TestObject[] items = TO.findObjectByPath(numberOfFindTries, delayBetweenTries, findStart, "0", false, getItemProperty(friendlyName, "nf==path"));
            return items[0];
        }
        return TO.findOneObject(numberOfFindTries, delayBetweenTries, baseObject, false, properties);
    }
    
    /**
     * This function is responsible for taking in a friendly name and finding a group of objects that go with it, if any are there.
     * 
     * @param friendlyName
     *            Name of the object you're looking for
     * @return The objects themselves.
     * @throws UnableToPerformActionException
     *             A generic exception thrown if something can't be done. Check the message for details.
     * @throws ObjectNotFoundException
     *             A generic exception thrown if the object can't be found. Check the message for details.
     */
    protected TestObject[] findDefinedItems(String friendlyName) throws UnableToPerformActionException, ObjectNotFoundException
    {
        if (!isItemPresentInTable(friendlyName))
        {
            throw new UnableToPerformActionException(friendlyName + " is not defined in your object table for " + screenName);
        }
        if (isItemPropertySet(friendlyName, "nf==ObjectMapID"))
        {
            return TO.getMappedObjectByID(om, getItemProperty(friendlyName, "nf==ObjectMapID"));
        }
        TestObject findStart = baseObject;
        if (isItemPropertySet(friendlyName, "nf==externalStartingPoint") == true)
        {
            try
            {
                findStart = findDefinedItem(getItemProperty(friendlyName, "nf==externalStartingPoint"));
            }
            catch (ObjectNotFoundException e)
            {
                throw new ObjectNotFoundException("Unable to find the external starting point for " + friendlyName);
            }
        }
        
        String[] properties = MapFunctions.getStringArray(objectDefinitions.get(friendlyName), "nf==");
        if (isItemPropertySet(friendlyName, "nf==path") == true)
        {
            TestObject[] items = TO.findObjectByPath(numberOfFindTries, delayBetweenTries, findStart, "ALL", false, getItemProperty(friendlyName, "nf==path"));
            return items;
        }
        return TO.findObject(numberOfFindTries, delayBetweenTries, baseObject, Subitems.atDescendant, "ALL", false, properties);
    }
    
    /**
     * Actually finds the base object.
     * 
     * @return True if the base object is found, False if not
     */
    protected boolean findBaseObject()
    {
        try
        {
            baseObject = TO.findOneObject(numberOfFindTries, delayBetweenTries, RootTestObject.getRootTestObject(), false, MapFunctions.getStringArray(baseObjectProperties, "nf=="));
            return true;
        }
        catch (UnableToPerformActionException e)
        {
        }
        catch (ObjectNotFoundException e)
        {
        }
        return false;
    }
    
    /**
     * This function clicks on an object. It will try up to {@link #numberOfActionTries} times to find the object, click on it, and release it.
     * 
     * @param friendlyName
     *            Friendly name of the field to click on.
     * @return True if successful, False if failed
     */
    public boolean click(String friendlyName)
    {
        int attempts = 0;
        boolean requiredResult = false;
        do
        {
            GuiTestObject gto = null;
            TestObject to = null;
            try
            {
                to = findDefinedItem(friendlyName);
                gto = new GuiTestObject(to);
                gto.click();
                requiredResult = true;
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to find " + friendlyName + " while attempting to click on it.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (WindowActivateFailedException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on " + friendlyName + " because it is disabled");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (CoordinateOffScreenException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logError("Unable to click on " + friendlyName + " because it is off the screen", Console.getScreenshot());
                }
            }
            catch (UnsupportedActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on " + friendlyName + " because of an UnsupportedActionException");
                    error = error.replace("CRFCP0050E: No screen point found for object", "Unable to click on " + friendlyName + " because it is not visible on screen now.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnregisteredObjectException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logError("Unable to click on " + friendlyName + " because it became unregistered", Console.getScreenshot());
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on " + friendlyName + " because of a general UnableToPerformActionException");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {to, gto});
                attempts++;
            }
        } while (requiredResult != true && attempts < numberOfActionTries);
        return requiredResult;
    }
    
    /**
     * This function tries a user-specified number of times to click on a user specified point (relative to the upper left corner of an object). <br>
     * It supports clicking any number of times with any mouse button as well as any combination of control, alt, or shift pressed.
     * 
     * @param friendlyName
     *            Friendly name of object to click on
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
     * @return True if successful, False if failed
     */
    public boolean click(String friendlyName, int numOfClicks, MouseButtons mouseButtonName, boolean ctrlOn, boolean altOn, boolean shiftOn, int xCoord, int yCoord)
    {
        int attempts = 0;
        boolean requiredResult = false;
        do
        {
            TestObject object = null;
            GuiTestObject gto = null;
            try
            {
                object = findDefinedItem(friendlyName);
                boolean logTOErrors = attempts == numberOfActionTries - 1;
                requiredResult = actionLib.click(object, friendlyName, 1, numOfClicks, mouseButtonName, ctrlOn, altOn, shiftOn, xCoord, yCoord, logTOErrors);
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to find " + friendlyName + " in order to click on it.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to click on " + friendlyName + " because of a general UnableToPerformActionException");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {gto, object});
                gto = null;
                object = null;
                attempts++;
            }
        } while (requiredResult != true && attempts < numberOfActionTries);
        return requiredResult;
    }
    
    /**
     * This function highlights the given object on screen, even if it is buried under another object.
     * 
     * @param friendlyName
     *            The name of the object to highlight
     * @param duration
     *            How long to highlight it for
     * @return True if the highlight command succeeded, False if it didn't (e.g. object is completely off screen)
     */
    public boolean highlight(String friendlyName, double duration)
    {
        TestObject object = null;
        GuiTestObject gto = null;
        boolean result = false;
        int attempts = 0;
        do
        {
            try
            {
                object = findDefinedItem(friendlyName);
                gto = new GuiTestObject(object);
                Rectangle targetObjectScreenRect = gto.getScreenRectangle();
                if (targetObjectScreenRect.equals(new Rectangle(0, 0, 0, 0)))
                {
                    result = false;
                }
                KeyboardAndMouse.highlight(targetObjectScreenRect, duration);
                result = true;
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to find " + friendlyName + " while attempting to highlight it.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to highlight " + friendlyName + " because of an UnableToPerformActionException.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnregisteredObjectException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = "Unable to highlight " + friendlyName + " because it became unregistered.";
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (ObjectIsDisposedException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = "Unable to highlight " + friendlyName + " because it was disposed.";
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnsupportedActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to highlight " + friendlyName + " because of a generic UnsupportedActionException.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {gto, object});
                object = null;
                gto = null;
                attempts++;
            }
        } while (result != true && attempts < numberOfActionTries);
        return result;
    }
    
    /**
     * This function tries a user-specified number of times to hover on a user specified point (relative to the upper left corner of an object)
     * 
     * @param friendlyName
     *            Friendly name of object to hover over
     * @param hoverTime
     *            How long, in seconds, to hover over the object
     * @param xCoord
     *            X coordinate to hover over (how far right of the object's left side)
     * @param yCoord
     *            Y coordinate to hover over (how far below the object's top)
     * @return 0 if successful, -1 if failed
     */
    public boolean hover(String friendlyName, double hoverTime, int xCoord, int yCoord)
    {
        TestObject theObject = null;
        boolean result = false;
        int attempts = 0;
        do
        {
            try
            {
                theObject = findDefinedItem(friendlyName);
                result = actionLib.hover(theObject, friendlyName, 1, hoverTime, xCoord, yCoord, attempts == numberOfActionTries - 1);
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to hover on " + friendlyName + " at (" + xCoord + "," + yCoord + ") because it was not found.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to hover on " + friendlyName + " at (" + xCoord + "," + yCoord + ") because of an UnableToPerformActionException.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnsupportedActionException e)
            {
                com.rational.test.ft.script.RationalTestScript.sleep(delayBetweenTries);
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {theObject});
                theObject = null;
                attempts++;
            }
        } while (result != true && attempts < numberOfActionTries);
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
     * @param friendlyName
     *            Friendly name of the object
     * @param data
     *            Item(s) to be selected.<br>
     * @return The selected text
     */
    public String select(String friendlyName, String data)
    {
        TestObject theObject = null;
        int attempts = 0;
        String requiredResult = null;
        Map<String, String> attributes = Regex.getNameValuePairs(data);
        friendlyName = friendlyName.replace("Section.java Base Object Entry", "Section.java Base Object Entry As Object");
        do
        {
            try
            {
                theObject = findDefinedItem(friendlyName);
                String[] items = actionLib.getItemsInList(theObject);
                if (attributes.containsKey("multi"))
                {
                    for (String item : attributes.get("multi").split(","))
                    {
                        if (Arrays.asList(items).indexOf(item) == -1)
                        {
                            throw new SubitemNotFoundException(item + " is not present in the options for " + friendlyName);
                        }
                    }
                }
                else if (attributes.containsKey("like"))
                {
                    if (attributes.containsKey("threshold"))
                    {
                        if (StringFunctions.getBestMatch(attributes.get("like"), Integer.parseInt(attributes.get("threshold")), items) == null)
                        {
                            throw new SubitemNotFoundException("No possible Levenshtein match for " + data + " in " + friendlyName + " within the threshold " + attributes.get("threshold"));
                        }
                        
                    }
                    else
                    {
                        if (StringFunctions.getBestMatch(attributes.get("like"), 99999, items) == null)
                        {
                            throw new SubitemNotFoundException("No possible Levenshtein match for " + data + " in " + friendlyName + " within the threshold 99999");
                        }
                    }
                }
                else if (attributes.containsKey("index"))
                {
                    String[] selectAtIndex = attributes.get("index").split(",");
                    for (int i = 0; i < selectAtIndex.length; i++)
                    {
                        String indexString = selectAtIndex[i];
                        if (!indexString.equalsIgnoreCase("random"))
                        {
                            try
                            {
                                int index = Integer.parseInt(indexString);
                                if (index >= items.length)
                                {
                                    throw new SubitemNotFoundException(indexString + " is not a valid index for" + friendlyName);
                                }
                            }
                            catch (Exception e)
                            {
                                throw new SubitemNotFoundException(indexString + " is not a valid number (" + friendlyName + ")");
                            }
                        }
                    }
                    
                }
                else
                {
                    if (Arrays.asList(items).indexOf(data) == -1)
                    {
                        throw new SubitemNotFoundException(data + " is not a valid item in " + friendlyName);
                    }
                }
                requiredResult = actionLib.select(theObject, friendlyName, 1, attempts == numberOfActionTries - 1, data);
            }
            catch (SubitemNotFoundException e)
            {
                Log.logError(e.getMessage(), Console.getScreenshot());
                return null;
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to select anything in " + friendlyName + " because it was not found.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to select anything in " + friendlyName + " because of an UnableToPerformActionException.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {theObject});
                theObject = null;
                attempts++;
            }
        } while (requiredResult == null && attempts < numberOfActionTries);
        return requiredResult;
    }
    
    /**
     * This function allows you to try to type into a field up to as many times as you specify. On the last try it logs any errors it hit.
     * 
     * @param friendlyName
     *            Name of field to type into
     * @param data
     *            Data to type into it
     * @param compareAndRetype
     *            True to check the box after typing and retype if necessary, False to ignore it
     * @return textbox contents if successful, null if not
     */
    public String type(String friendlyName, String data, boolean compareAndRetype)
    {
        int attempts = 0;
        String requiredResult = null;
        TestObject object = null;
        do
        {
            try
            {
                friendlyName = friendlyName.replace("Section.java Base Object Entry", "Section.java Base Object Entry As Object");
                object = findDefinedItem(friendlyName);
                requiredResult = actionLib.type(object, friendlyName, data, 1, attempts == numberOfActionTries - 1, compareAndRetype);
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to type \"" + data + "\" into " + friendlyName + " because the object was not found.");
                    Log.logError(error, Console.getScreenshot());
                }
                requiredResult = null;
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to type \"" + data + "\" into " + friendlyName + " because of an UnableToPerformActionException");
                    Log.logError(error, Console.getScreenshot());
                }
                requiredResult = null;
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
                requiredResult = null;
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {object});
                object = null;
                attempts++;
            }
        } while (requiredResult == null && attempts < numberOfActionTries);
        return requiredResult;
    }
    
    /**
     * Returns the selected item(s) as a string from the given object
     * 
     * @param friendlyName
     *            Friendly name of the object
     * @return The item(s) selected as a single string. "" as the string could mean an error occured or that no items were selected.
     */
    public String[] getSelectedItemsInList(String friendlyName)
    {
        TestObject object = null;
        String[] result = null;
        int status = -1, attempts = 0;
        do
        {
            try
            {
                object = findDefinedItem(friendlyName);
                result = actionLib.getSelectedItemsInList(object);
                status = 0;
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to get selected items from " + friendlyName + " because it was not found.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to get selected items from " + friendlyName + " because of an UnableToPerformActionException.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {object});
                object = null;
                attempts++;
            }
        } while (status != 0 && attempts < numberOfActionTries);
        return result;
    }
    
    /**
     * Gets the options in a list
     * 
     * @param friendlyName
     *            Friendly name of the list
     * @return The options as a string array or empty string array if an error happens
     */
    public String[] getItemsInList(String friendlyName)
    {
        TestObject object = null;
        String[] result = null;
        int status = -1, attempts = 0;
        do
        {
            try
            {
                object = findDefinedItem(friendlyName);
                result = actionLib.getItemsInList(object);
                status = 0;
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to note the items in " + friendlyName + " because it was not found.");
                    Log.logError(error, Console.getScreenshot());
                }
                result = new String[0];
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to note the items in " + friendlyName + " because of an UnableToPerformActionException.");
                    Log.logError(error, Console.getScreenshot());
                }
                result = new String[0];
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
                result = new String[0];
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {object});
                object = null;
                attempts++;
            }
        } while (status != 0 && attempts < numberOfActionTries);
        return result;
    }
    
    /**
     * @param friendlyName
     *            Friendly name of the object
     * @param propName
     *            Property to extract from it
     * @return The property obtained from the actual object
     */
    public Object getItemRuntimeProperty(String friendlyName, String propName)
    {
        TestObject object = null;
        Object result = null;
        int status = -1, attempts = 0;
        do
        {
            try
            {
                object = findDefinedItem(friendlyName);
                result = object.getProperties().get(propName);
                status = 0;
            }
            catch (ObjectNotFoundException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to get " + propName + " from " + friendlyName + " in " + screenName + " because it was not found.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (UnableToPerformActionException e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    String error = StringFunctions.ifEmpty(e.getMessage(), "Unable to get " + propName + " from " + friendlyName + " in " + screenName + " because of an UnableToPerformActionException.");
                    Log.logError(error, Console.getScreenshot());
                }
            }
            catch (Exception e)
            {
                if (attempts == numberOfActionTries - 1)
                {
                    Log.logException(e);
                }
            }
            finally
            {
                RationalTestScript.unregister(new TestObject[] {object});
                object = null;
                attempts++;
            }
        } while (status != 0 && attempts < numberOfActionTries);
        return result;
    }
    
    /**
     * This function spends a few tries checking to see if one or more instance of an object are present.
     * 
     * @param friendlyName
     *            Friendly name of field to check on
     * @return true if exists, false if doesn't exist
     */
    public boolean itemExists(String friendlyName)
    {
        int orgNumberOfFind = getNumberOfFindTries();
        setNumberOfFindTries(1);
        boolean result = countAppearances(friendlyName, false) > 0;
        setNumberOfFindTries(orgNumberOfFind);
        return result;
    }
    
    /**
     * This function counts the number of times a given entry appears out there. It is useful with regular expressions. <br>
     * 
     * @param friendlyName
     *            Name of the entry you want to count instances of
     * @param logErrors
     *            True to log errors, False to not log them.
     * @return Number of times an item exists, or -1 if an error happened.
     */
    public int countAppearances(String friendlyName, boolean logErrors)
    {
        TestObject[] object = null;
        int result = 0;
        
        try
        {
            friendlyName = friendlyName.replace("Section.java Base Object Entry", "Section.java Base Object Entry As Object");
            object = findDefinedItems(friendlyName);
            result = object.length;
        }
        catch (ObjectNotFoundException e)
        {
            result = 0;
        }
        catch (UnableToPerformActionException e)
        {
            result = 0;
        }
        catch (UnregisteredObjectException e)
        {
            result = 0;
        }
        catch (ObjectIsDisposedException e)
        {
            result = 0;
        }
        catch (Exception e)
        {
            result = -1;
        }
        finally
        {
            RationalTestScript.unregister(object);
            object = null;
        }
        return result;
    }
    
    /**
     * @return The number of times we try one of the action functions here.
     */
    public final int getNumberOfActionTries()
    {
        return numberOfActionTries;
    }
    
    /**
     * @param numberOfActionTries
     *            The number of times we try one of the action functions here
     */
    public final void setNumberOfActionTries(int numberOfActionTries)
    {
        this.numberOfActionTries = numberOfActionTries;
    }
    
    /**
     * @return How many times we try to find an object
     */
    public final int getNumberOfFindTries()
    {
        return numberOfFindTries;
    }
    
    /**
     * @param numberOfFindTries
     *            How many times we try to find an object
     */
    public final void setNumberOfFindTries(int numberOfFindTries)
    {
        this.numberOfFindTries = numberOfFindTries;
    }
    
    /**
     * @return How long we wait between find attempts
     */
    public final double getDelayBetweenTries()
    {
        return delayBetweenTries;
    }
    
    /**
     * @param delayBetweenTries
     *            How long we wait between find attempts
     */
    public final void setDelayBetweenTries(double delayBetweenTries)
    {
        this.delayBetweenTries = delayBetweenTries;
    }
    
    /**
     * @return The properties used to find (or re-find) the base object
     */
    protected final Map<String, String> getBaseObjectProperties()
    {
        return Collections.unmodifiableMap(baseObjectProperties);
    }
    
    /**
     * @param baseObjectProperties
     *            The properties used to find (or re-find) the base object
     */
    protected final void setBaseObjectProperties(String...baseObjectProperties)
    {
        this.baseObjectProperties = MapFunctions.arrayToLinkedHashMap(baseObjectProperties);
    }
    
    /**
     * This function returns a given property's value for the base object
     * 
     * @param propertyName
     *            The property you want
     * @return The property's value as a string or null if the property or object isn't present
     */
    protected final String getBaseObjectProperty(String propertyName)
    {
        if (isBaseObjectPropertySet(propertyName) == false)
        {
            return null;
        }
        return baseObjectProperties.get(propertyName);
    }
    
    /**
     * This function checks to see if a given property is defined for a given entry in objectDefinitions.
     * 
     * @param visiblename
     *            Name of the entry
     * @param propertyName
     *            Name of the property
     * @return boolean True if the given property is defined for the given entry and False if it isn't defined, or if the entry itself isn't defined.
     */
    protected final boolean isBaseObjectPropertySet(String propertyName)
    {
        return baseObjectProperties.containsKey(propertyName);
    }
    
    /**
     * This function adds/sets a property in objectDefinitions for a given object definition.<br>
     * If the friendlyName entry isn't present, nothing happens.
     * 
     * @param friendlyName
     *            Name of the object you want to add the property to
     * @param propertyName
     *            Name of the property to add
     * @param propertyValue
     *            Value of the property to add
     */
    protected final void setBaseObjectProperty(String propertyName, String propertyValue)
    {
        propertyName = propertyName.replace("NF==", "nf==").replace("Nf==", "nf==").replace("nF==", "nf==");
        baseObjectProperties.put(propertyName, propertyValue);
    }
    
    /**
     * This function removes a given property from a given object definition in objectDefinitions.<br>
     * If the friendlyName entry isn't present, nothing happens.
     * 
     * @param visiblename
     *            Name of the object you want to remove the property from
     * @param propertyName
     *            Name of the property to be deleted
     */
    protected final void deleteBaseObjectProperty(String friendlyName, String propertyName)
    {
        baseObjectProperties.remove(propertyName);
    }
    
    /**
     * @return The actual base object, or null if not set
     */
    protected final TestObject getBaseObject()
    {
        return baseObject;
    }
    
    /**
     * @param baseObject
     *            The actual base object, or null if not set
     */
    protected final void setBaseObject(TestObject baseObject)
    {
        this.baseObject = baseObject;
    }
    
    /**
     * @return The actual base object, or null if not set
     */
    protected final Actions getActionLib()
    {
        return actionLib;
    }
    
    /**
     * @param baseObject
     *            The actual base object, or null if not set
     */
    protected final void setActionLib(Actions actionLib)
    {
        this.actionLib = actionLib;
    }
}
