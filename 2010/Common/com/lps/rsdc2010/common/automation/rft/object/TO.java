package com.lps.rsdc2010.common.automation.rft.object;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;

import com.lps.rsdc2010.common.automation.rft.PlaybackMonitor;
import com.lps.rsdc2010.common.lang.ArrayFunctions;
import com.lps.rsdc2010.common.lang.Reflection;
import com.lps.rsdc2010.common.lang.Regex;
import com.lps.rsdc2010.common.lang.StringFunctions;
import com.lps.rsdc2010.common.lang.types.Tree;
import com.rational.test.ft.ObjectNotFoundException;
import com.rational.test.ft.UnableToPerformActionException;
import com.rational.test.ft.object.interfaces.DomainTestObject;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.map.MappedTestObject;
import com.rational.test.ft.object.map.ObjectMap;
import com.rational.test.ft.object.map.SpyMappedTestObject;
import com.rational.test.ft.script.Anchor;
import com.rational.test.ft.script.Property;
import com.rational.test.ft.script.RationalTestScript;
import com.rational.test.ft.script.Subitem;
import com.rational.test.ft.script.SubitemFactory;
import com.rational.test.ft.value.MethodInfo;
import com.rational.test.ft.value.RegularExpression;

/**
 * A generic data class that can represent a RFT TestObject.<br>
 * Also contains static helper functions used to find / load / analyze TestObjects.
 */
public class TO
{
    public static enum Subitems
    {
        atDescendant, atChild;
    }
    
    /**
     * Finds the first candidate that can be matched against any descendant of the starting TestObject given search criteria.
     * 
     * @param tries
     *            specifies how many times the function will attempt to find your object.<br>
     * @param sleepBtwnTries
     *            specifies how long in seconds to sleep between attempts. Fractional seconds are supported. <br>
     * @param parent
     *            the TestObject that you start searching from. You can always try the root object by specifying <br>
     *            RootTestObject.getRootTestObject(), but this is a bad idea. Using the root object will be veru slow because RFT has to<br>
     *            search EVERYTHING on your screen.<br>
     * @param searchMappableOnly
     *            Tell RFT whether to search all objects or just ones that can be mapped into the RFT object map<br>
     * @param recognitionProperties
     *            An even-length string array containing a set of recognition properties.<br>
     *            Regular expressions are supported. To indicate that the String value should be converted to a regex object, prepend "regex=".<br>
     *            (e.g. ".class", "regex=Html.TEXTAREA|Html.INPUT.text|Html.INPUT.password", ".id", "ddlList" would be a valid use of a regular expression).<br>
     *            In addition to "regex=", there are some predefined regular expressions: <br>
     *            "equals=" -- does the object's value equal your supplied value?<br>
     *            "equalsIgnoreCase=" -- does the object's value equal your supplied value, when case is ignored?<br>
     *            "contains=" -- does the object's value contain your supplied value?<br>
     *            "containsIgnoreCase=" -- does the object's value contain your supplied value, when case is ignored?<br>
     *            "startsWith=" -- does the object's value start with your supplied value?<br>
     *            "startsWithIgnoreCase=" -- does your value start with the object's value, when case is ignored?<br>
     *            "endsWith=" -- does the object's value end with your supplied value?<br>
     *            "endsWithIgnoreCase=" -- does your value end with the object's value, when case is ignored?<br>
     * @return TestObject matching the recognitionProperties
     */
    public static TestObject findOneObject(int tries, double sleepBtwnTries, TestObject parent, boolean searchMappableOnly, String...recognitionProperties)
    {
        TestObject[] results = TO.findObject(tries, sleepBtwnTries, parent, Subitems.atDescendant, "0", searchMappableOnly, recognitionProperties);
        return results[0];
    }
    
    /**
     * Finds the first candidate that can be matched against any child of the starting TestObject given search criteria.
     * 
     * @param tries
     *            specifies how many times the function will attempt to find your object.<br>
     * @param sleepBtwnTries
     *            specifies how long in seconds to sleep between attempts. Fractional seconds are supported. <br>
     * @param parent
     *            the TestObject that you start searching from. You can always try the root object by specifying <br>
     *            RootTestObject.getRootTestObject(), but this is a bad idea. Using the root object will be veru slow because RFT has to<br>
     *            search EVERYTHING on your screen.<br>
     * @param searchMappableOnly
     *            Tell RFT whether to search all objects or just ones that can be mapped into the RFT object map<br>
     * @param recognitionProperties
     *            An even-length string array containing a set of recognition properties.<br>
     *            Regular expressions are supported. To indicate that the String value should be converted to a regex object, prepend "regex=".<br>
     *            (e.g. ".class", "regex=Html.TEXTAREA|Html.INPUT.text|Html.INPUT.password", ".id", "ddlList" would be a valid use of a regular expression).<br>
     *            In addition to "regex=", there are some predefined regular expressions: <br>
     *            "equals=" -- does the object's value equal your supplied value?<br>
     *            "equalsIgnoreCase=" -- does the object's value equal your supplied value, when case is ignored?<br>
     *            "contains=" -- does the object's value contain your supplied value?<br>
     *            "containsIgnoreCase=" -- does the object's value contain your supplied value, when case is ignored?<br>
     *            "startsWith=" -- does the object's value start with your supplied value?<br>
     *            "startsWithIgnoreCase=" -- does your value start with the object's value, when case is ignored?<br>
     *            "endsWith=" -- does the object's value end with your supplied value?<br>
     *            "endsWithIgnoreCase=" -- does your value end with the object's value, when case is ignored?<br>
     * @return TestObject matching the recognitionProperties
     */
    public static TestObject findOneChildObject(int tries, double sleepBtwnTries, TestObject parent, boolean searchMappableOnly, String...recognitionProperties)
    {
        TestObject[] results = TO.findObject(tries, sleepBtwnTries, parent, Subitems.atChild, "0", searchMappableOnly, recognitionProperties);
        return results[0];
    }
    
    /**
     * Finds all candidates that can be matched against any descendant of the starting TestObject given search criteria.
     * 
     * @param tries
     *            specifies how many times the function will attempt to find your object.<br>
     * @param sleepBtwnTries
     *            specifies how long in seconds to sleep between attempts. Fractional seconds are supported. <br>
     * @param parent
     *            the TestObject that you start searching from. You can always try the root object by specifying <br>
     *            RootTestObject.getRootTestObject(), but this is a bad idea. Using the root object will be veru slow because RFT has to<br>
     *            search EVERYTHING on your screen.<br>
     * @param searchMappableOnly
     *            Tell RFT whether to search all objects or just ones that can be mapped into the RFT object map<br>
     * @param subitemName
     *            "atDescendant" or "atChild" to use the RFT atDescendant() or atChild() subitem types as appropriate.
     * @param itemsToInclude
     *            Which elements of the results array to include.<br>
     *            You supply the indexes (starting from 0, and of the form "0", "0-3", "1-3,5", "1,2,4,6", "ALL", etc).<br>
     *            The excess items will be unregistered.<br>
     *            See trimFindArray() or ArrayFunctions.calculateIndexesToInclude() for more details.<br>
     *            Here, though, supplying a null or empty string is the same as saying "ALL".<br>
     *            There's no point in calling this function if you don't intend to use any of the results.
     * @param recognitionProperties
     *            An even-length string array containing a set of recognition properties.<br>
     *            Regular expressions are supported. To indicate that the String value should be converted to a regex object, prepend "regex=".<br>
     *            (e.g. ".class", "regex=Html.TEXTAREA|Html.INPUT.text|Html.INPUT.password", ".id", "ddlList" would be a valid use of a regular expression).<br>
     *            In addition to "regex=", there are some predefined regular expressions: <br>
     *            "equals=" -- does the object's value equal your supplied value?<br>
     *            "equalsIgnoreCase=" -- does the object's value equal your supplied value, when case is ignored?<br>
     *            "contains=" -- does the object's value contain your supplied value?<br>
     *            "containsIgnoreCase=" -- does the object's value contain your supplied value, when case is ignored?<br>
     *            "startsWith=" -- does the object's value start with your supplied value?<br>
     *            "startsWithIgnoreCase=" -- does your value start with the object's value, when case is ignored?<br>
     *            "endsWith=" -- does the object's value end with your supplied value?<br>
     *            "endsWithIgnoreCase=" -- does your value end with the object's value, when case is ignored?<br>
     * @return TestObject[] of objects matching the recognitionProperties
     */
    public static TestObject[] findObject(int tries, double sleepBtwnTries, TestObject parent, Subitems subitemName, String itemsToInclude, boolean searchMappableOnly, String...recognitionProperties)
    {
        String propertyList = "";
        boolean foundObject = false;
        int maxPropLen = 0;
        int maxValLen = 0;
        for (int i = 0; i < recognitionProperties.length; i++)
        {
            if (i % 2 == 0 && recognitionProperties[i].length() > maxPropLen)
            {
                maxPropLen = recognitionProperties[i].length();
            }
            else if (recognitionProperties[i].length() > maxValLen)
            {
                maxValLen = recognitionProperties[i].length();
            }
        }
        Property[] objProperties = TO.getPropertyArray(recognitionProperties);
        for (Property p : objProperties)
        {
            propertyList += String.format("%-" + maxPropLen + "s\t%-" + maxValLen + "s", p.getPropertyName(), p.getPropertyValue()) + "\n";
        }
        TestObject[] foundTOs = null;
        int attempt = 1;
        if (parent == null)
        {
            throw new UnableToPerformActionException("Parent was null for an unknown reason.");
        }
        Subitem theSubitem = null;
        if (subitemName.name().equals("atDescendant"))
        {
            theSubitem = SubitemFactory.atDescendant(objProperties);
        }
        else if (subitemName.name().equals("atChild"))
        {
            theSubitem = SubitemFactory.atChild(objProperties);
        }
        if (theSubitem == null)
        {
            throw new UnableToPerformActionException("Invalid subitem type specified.");
        }
        while (foundObject == false && attempt <= tries)
        {
            PlaybackMonitor.lockPlaybackMonitor(false);
            PlaybackMonitor.setPlaybackMonitorContents("TO.java", 0, "On attempt " + attempt + " of " + tries + " to find:\n" + propertyList);
            PlaybackMonitor.lockPlaybackMonitor(true);
            foundTOs = parent.find(theSubitem, searchMappableOnly);
            if (foundTOs.length > 0)
            {
                foundObject = true;
                break;
            }
            else
            {
                RationalTestScript.sleep(sleepBtwnTries);
            }
            attempt++;
        }
        PlaybackMonitor.lockPlaybackMonitor(false);
        PlaybackMonitor.setPlaybackMonitorContents("", 0, "");
        if (foundObject == true)
        {
            if (itemsToInclude == null || itemsToInclude.equals("") || itemsToInclude.equalsIgnoreCase("ALL") || itemsToInclude.equals("0") && foundTOs.length == 1)
            {// performance shortcut.
                return foundTOs;
            }
            return TO.trimFindArray(itemsToInclude, foundTOs);
        }
        else
        {
            throw new ObjectNotFoundException();
        }
    }
    
    /**
     * Given a starting object and a path, this function will walk along the path and return the object at the end of the path
     * 
     * @param tries
     *            specifies how many times the function will attempt to find your object.<br>
     * @param sleepBtwnTries
     *            specifies how long in seconds to sleep between attempts. Fractional seconds are supported. <br>
     * @param startingObject
     *            the TestObject that you start searching from. You can always try the root object by specifying <br>
     * @param itemsToInclude
     *            Which elements of the results array to include.<br>
     *            You supply the indexes (starting from 0, and of the form "0", "0-3", "1-3,5", "1,2,4,6", "ALL", etc).<br>
     *            The excess items will be unregistered.<br>
     *            See trimFindArray() or ArrayFunctions.calculateIndexesToInclude() for more details.<br>
     *            Here, though, supplying a null or empty string is the same as saying "ALL".<br>
     *            There's no point in calling this function if you don't intend to use any of the results.
     * @param searchMappable
     *            Tell RFT whether to search all objects or just ones that can be mapped into the RFT object map<br>
     * @param path
     *            The path from the startingObject to the desired object<br>
     * <br>
     *            <table border="1">
     *            <caption>Path Syntax</caption>
     *            <tr>
     *            <td>Expression</td>
     *            <td>Description</td>
     *            </tr>
     *            <tr>
     *            <td>/</td>
     *            <td>selects all child nodes</td>
     *            </tr>
     *            <tr>
     *            <td>//</td>
     *            <td>selects all descendants nodes</td>
     *            </tr>
     *            <tr>
     *            <td>..</td>
     *            <td>selects the parent node</td>
     *            </tr>
     *            </table>
     * @return TestObject[] of objects matching the path
     * 
     *         <pre>
     * b&gt;Examples&lt;/b&gt;
     * Given the following hierarchy
     * &lt;Html&gt;
     * 	&lt;Body&gt;
     * 	 &lt;FORM&gt;
     * 	  &lt;DIV&gt;
     * 	   &lt;DIV id=pnlLoginForm&gt;
     * 		&lt;TABLE&gt;
     * 		 &lt;TBODY&gt;
     * 		  &lt;TR&gt;
     * 		   &lt;TD&gt;
     * 			&lt;SPAN class=blackBold8&gt;
     * 			&lt;INPUT type=text name=txtUsername&gt;
     * 	  &lt;TR&gt;
     * 		   &lt;TD&gt;
     * 			&lt;SPAN class=blackBold8&gt;
     * 			&lt;INPUT type=password name=txtPassword&gt;&lt;br&gt;
     * </pre>
     * 
     *         <b>Usage 1 Walk Up:</b> Suppose I want to find the form tag.<br>
     *         One could search from top HTML tag downwards or use the DIV with an id of "pnlLoginForm" as a starting object, and walk up two levels.<br>
     *         What would the path look like?<br>
     *         path = /../../<br>
     *         What would the findObjectByPath call look like?<br>
     *         TO.findObjectByPath(3, .5, startingObject, "All", false, "/../../");<br>
     * <br>
     *         <b>Usage 2 Walk down:</b> Suppose I want to find all the TR tags underneath the DIV with an id of "pnlLoginForm".<br>
     *         What would the path look like?<br>
     *         (very explicit)<i> path = /.class,Html.TABLE/.class,Html.TBODY/.class,Html.TR/</i><br>
     *         (less explicit)<i> path = //.class,Html.TR/</i><br>
     *         Yet both paths return the same test objects.<br>
     *         What would the findObjectByPath call look like?<br>
     *         <i>TO.findObjectByPath(3, .5, startingObject, "All", false, "//.class,Html.TR/");</i><br>
     *         where startingObject is the DIV with an id of "pnlLoginForm".<br>
     * <br>
     *         <b>Usage 3 Mixed:</b> Suppose I want to find the span tag underneath the first TD tag.<br>
     *         There are many ways to solve this however the simplest is to use the INPUT tag (aka username textbox) as a starting object, walk up to the TD tag, from TD tag search for children with SPAN tag. If findObjectByPath was used this could be accomplished in one call. What would the path look like?<br>
     *         <i>path = /../.class,Html.SPAN/</i><br>
     *         What would the findObjectByPath call look like?<br>
     *         <i>TO.findObjectByPath(3, .5, startingObject, "All", false, "/../.class,Html.SPAN/");</i><br>
     */
    public static TestObject[] findObjectByPath(int tries, double sleepBtwnTries, TestObject startingObject, String itemsToInclude, boolean searchMappable, String path)
    {
        // Remove trailing /
        if (path.endsWith("/"))
        {
            int length = path.length();
            path = path.substring(0, length - 1);
        }
        
        // For some reason, I couldn't get / to work properly with regex so I substituted a sequence of chars
        String p = path.replace("//", "@@D(");
        p = p.replace("/", "@@C(");
        String[] splitPath = p.split("@@");
        
        // Usage 1: walk up
        if (p.contains("..") && !p.contains(","))
        {
            ArrayList<TestObject> placeHolder = new ArrayList<TestObject>();
            for (int i = 0; i <= splitPath.length - 1; i++)
            {
                if (splitPath[i].contains(".."))
                {
                    if (placeHolder.isEmpty())
                    {
                        placeHolder.add(startingObject.getParent());
                    }
                    else
                    {
                        placeHolder.add(placeHolder.get(placeHolder.size() - 1).getParent());
                    }
                }
            }
            return TO.trimFindArray(Integer.toString(placeHolder.size() - 1), placeHolder.toArray(new TestObject[0]));
            
        }
        // Usage 2: walk down
        else if (!p.contains("..") && p.contains(","))
        {
            ArrayList<Subitem> parsedPath = new ArrayList<Subitem>();
            for (int i = 0; i < splitPath.length; i++)
            {
                String subItem = splitPath[i];
                if (subItem.startsWith("C("))
                {
                    parsedPath.add(SubitemFactory.atChild(TO.getPropertyArray(subItem.replace("C(", "").split(","))));
                }
                else if (subItem.startsWith("D("))
                {
                    parsedPath.add(SubitemFactory.atDescendant(TO.getPropertyArray(subItem.replace("D(", "").split(","))));
                }
            }
            return TO.findObject(tries, sleepBtwnTries, startingObject, itemsToInclude, searchMappable, parsedPath.toArray(new Subitem[0]));
        }
        // Usage 3: Mixed mode walk up|down
        else
        {
            TestObject actingStartingObject = startingObject;// new TestObject[splitPath.length];
            for (int i = 0; i < splitPath.length; i++)
            {
                String item = splitPath[i];
                if (item.contains("..") && !item.contains(","))
                {
                    actingStartingObject = actingStartingObject.getParent();
                }
                else
                {
                    if (item.startsWith("C("))
                    {
                        actingStartingObject = TO.findOneChildObject(tries, sleepBtwnTries, actingStartingObject, searchMappable, item.replace("C(", "").split(","));
                    }
                    else if (item.startsWith("D("))
                    {
                        actingStartingObject = TO.findOneObject(tries, sleepBtwnTries, actingStartingObject, searchMappable, item.replace("D(", "").split(","));
                    }
                }
            }
            TestObject[] result = {actingStartingObject};
            return result;
        }
    }
    
    /**
     * Finds all candidates that can be matched from the starting TestObject using the supplied search criteria.
     * 
     * @param tries
     *            specifies how many times the function will attempt to find your object.<br>
     * @param sleepBtwnTries
     *            specifies how long in seconds to sleep between attempts. Fractional seconds are supported. <br>
     * @param parent
     *            the TestObject that you start searching from. You can always try the root object by specifying <br>
     *            RootTestObject.getRootTestObject(), but this is a bad idea. Using the root object will be veru slow because RFT has to<br>
     *            search EVERYTHING on your screen.<br>
     * @param searchMappableOnly
     *            Tell RFT whether to search all objects or just ones that can be mapped into the RFT object map<br>
     * @param itemsToInclude
     *            Which elements of the results array to include.<br>
     *            You supply the indexes (starting from 0, and of the form "0", "0-3", "1-3,5", "1,2,4,6", "ALL", etc).<br>
     *            The excess items will be unregistered.<br>
     *            See trimFindArray() or ArrayFunctions.calculateIndexesToInclude() for more details.<br>
     *            Here, though, supplying a null or empty string is the same as saying "ALL".<br>
     *            There's no point in calling this function if you don't intend to use any of the results.
     * @param properties
     *            a Subitem array of anchor(Property[] array) types.
     * @return TestObject[] of objects matching the recognitionProperties
     */
    public static TestObject[] findObject(int tries, double sleepBtwnTries, TestObject parent, String itemsToInclude, boolean searchMappableOnly, Subitem...properties)
    {
        boolean foundObject = false;
        TestObject[] foundTOs = null;
        int attempt = 1;
        if (parent == null)
        {
            throw new UnableToPerformActionException("Parent was null for an unknown reason.");
        }
        while (foundObject == false && attempt <= tries)
        {
            foundTOs = parent.find(SubitemFactory.atList(properties), searchMappableOnly);
            if (foundTOs.length > 0)
            {
                foundObject = true;
                break;
            }
            else
            {
                RationalTestScript.sleep(sleepBtwnTries);
            }
            attempt++;
        }
        if (foundObject == true)
        {
            if (itemsToInclude == null || itemsToInclude.equals("") || itemsToInclude.equalsIgnoreCase("ALL"))
            {// performance shortcut.
                return foundTOs;
            }
            return TO.trimFindArray(itemsToInclude, foundTOs);
        }
        else
        {
            throw new ObjectNotFoundException();
        }
    }
    
    /**
     * This function trims a TestObject array down to just the requested items.<br>
     * You supply the indexes (starting from 0, and of the form "0", "0-3", "1-3,5", "1,2,4,6", "ALL", etc).<br>
     * The excess items will be unregistered.
     * 
     * @param objects
     *            The array of TestObjects
     * @param ranges
     *            The indexes you want to keep.
     * @return The shrunken array.
     */
    public static TestObject[] trimFindArray(String ranges, TestObject...objects)
    {
        Vector<Integer> indexes = ArrayFunctions.calculateIndexesToInclude(objects.length, ranges);
        TestObject[] returns = new TestObject[indexes.size()], unregisters = new TestObject[objects.length - indexes.size()];
        int returnCounter = 0, unregistersCounter = 0;
        for (int i = 0; i < objects.length; i++)
        {
            if (indexes.contains(i))
            {
                returns[returnCounter] = objects[i];
                returnCounter++;
            }
            else
            {
                unregisters[unregistersCounter] = objects[i];
                unregistersCounter++;
            }
        }
        objects = null;
        RationalTestScript.unregister(unregisters);
        return returns;
    }
    
    /**
     * Converts String array to an array of Property objects (each object is a name/value pair)<br>
     * The Property array is used by TestObject.find() [e.g. find(atDescendant(Property[])) or find(atChild((Property[]))]<br>
     * 
     * @param properties
     *            An even-length string array containing an Object's recognition properties.<br>
     *            Regular expressions are supported. To indicate that the String value should be converted to a regex object, prepend "regex=".<br>
     *            (e.g. getPropertyArray{".class", "regex=Html.TEXTAREA|Html.INPUT.text|Html.INPUT.password", ".id", "ddlList"} would be a valid use of a regular expression).<br>
     *            In addition to "regex=", there are some predefined regular expressions: <br>
     *            "equals=" -- does the object's value equal your supplied value?<br>
     *            "equalsIgnoreCase=" -- does the object's value equal your supplied value, when case is ignored?<br>
     *            "contains=" -- does the object's value contain your supplied value?<br>
     *            "containsIgnoreCase=" -- does the object's value contain your supplied value, when case is ignored?<br>
     *            "startsWith=" -- does the object's value start with your supplied value?<br>
     *            "startsWithIgnoreCase=" -- does your value start with the object's value, when case is ignored?<br>
     *            "endsWith=" -- does the object's value end with your supplied value?<br>
     *            "endsWithIgnoreCase=" -- does your value end with the object's value, when case is ignored?<br>
     * @return a Property array
     */
    public static Property[] getPropertyArray(String...properties)
    {
        try
        {
            Property[] propArray = new Property[properties.length / 2];
            int index = 0;
            for (int eachKeyPairValue = 0; eachKeyPairValue < properties.length; eachKeyPairValue += 2)
            {
                try
                {
                    Property prop = null;
                    String propertyName = properties[eachKeyPairValue];
                    String propertyValue = properties[eachKeyPairValue + 1];
                    
                    String regexType = propertyValue.substring(0, propertyValue.indexOf("=") + 1);
                    String regexString = propertyValue.substring(propertyValue.indexOf("=") + 1);
                    boolean isRegexCaseSensitive = !regexType.endsWith("IgnoreCase=");
                    
                    if (propertyName.equals(".hwnd"))
                    {
                        Long l = Long.parseLong(propertyValue);
                        prop = new Property(propertyName, l);
                    }
                    else if (propertyName.equals(".processId"))
                    {
                        Integer i = Integer.parseInt(propertyValue);
                        prop = new Property(propertyName, i);
                    }
                    else if (regexType.startsWith("equals"))
                    {
                        RegularExpression valueRegExpObj = new RegularExpression("^" + regexString + "$", isRegexCaseSensitive);
                        prop = new Property(propertyName, valueRegExpObj);
                    }
                    else if (regexType.startsWith("contains"))
                    {
                        RegularExpression valueRegExpObj = new RegularExpression("(^.*|^)" + regexString + ".*$", isRegexCaseSensitive);
                        prop = new Property(propertyName, valueRegExpObj);
                    }
                    else if (regexType.startsWith("startsWith"))
                    {
                        RegularExpression valueRegExpObj = new RegularExpression("^" + regexString + ".*$", isRegexCaseSensitive);
                        prop = new Property(propertyName, valueRegExpObj);
                    }
                    else if (regexType.startsWith("endsWith"))
                    {
                        RegularExpression valueRegExpObj = new RegularExpression("^.*" + regexString + "$", isRegexCaseSensitive);
                        prop = new Property(propertyName, valueRegExpObj);
                    }
                    else if (regexType.startsWith("regex"))
                    {
                        RegularExpression valueRegExpObj = new RegularExpression(regexString, isRegexCaseSensitive);
                        prop = new Property(propertyName, valueRegExpObj);
                    }
                    else
                    {
                        prop = new Property(propertyName, propertyValue);
                    }
                    propArray[index++] = prop;
                }
                catch (Exception e)
                {
                    RationalTestScript.logError("Error while trying to create an Property array for object map.");
                    RationalTestScript.logException(e);
                }
            }
            return propArray;
        }
        catch (Exception e)
        {
            RationalTestScript.logError("Error while trying to create an Property array for object map.");
            RationalTestScript.logException(e);
        }
        return null;
    }
    
    /**
     * <pre>
     * Determines the recognition properties for given object at runtime based on the following rules:
     * Properties returned for all Test Objects.
     * 		1) .domain
     * 		2) .class
     * In addition
     *  	if domain is Net
     *  		Property(s) most often used in a Net domain.
     * 				1) Name
     * 		if domain is Html or Win
     * 			Property(s) most often used in a BrowserTestObject.
     * 				1) .window
     *  		Properties most often used in a TopLevelTestObject.
     * 			        1) .window
     * 				2) .caption
     * 			Properties most often used in all other TestObject instances in order of reliablity.
     * 				1) .id
     * 				2) .name
     * 				3) .value
     * 				4) .text
     * 				5) .classIndex
     * </pre>
     * 
     * @param object
     *            test object
     * @param nonValueProperties
     * @return a String array of properties that conforms to String...recognitionProperties used in TO.findObject()
     */
    public static String[] getRecognitionProperties(TestObject object, String...nonValueProperties)
    {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < nonValueProperties.length; i += 2)
        {
            try
            {
                String property = nonValueProperties[i];
                String value = nonValueProperties[i + 1];
                result.add(property);
                result.add(value);
            }
            catch (Exception e)
            {
                break;
            }
        }
        String className = object.getClass().getSimpleName();
        String domain = object.getDomain().getName().toString();
        java.util.Hashtable<?, ?> properties = object.getProperties();
        //
        // Properties used in all Test Objects.
        // 1) .domain
        // 2) .class
        //
        result.add(".domain");
        result.add(domain);
        result.add(".class");
        result.add(StringFunctions.ifNull(properties.get(".class")));
        if (domain.equals("Net"))
        {
            //
            // Properties most often used in a Net domain.
            // 1) Name
            //
            result.add("Name");
            result.add(StringFunctions.ifNull(properties.get("Name")));
        }
        else if (domain.equals("Flex"))
        {
            result.add("className");
            result.add(StringFunctions.ifNull(properties.get("className")));
            if (!className.equals("FlexApplicationTestObject"))
            {
                result.add("automationName");
                result.add(StringFunctions.ifNull(properties.get("automationName")));
            }
        }
        else if (domain.equals("Html") || domain.equals("Win"))
        {
            if (className.equals("BrowserTestObject"))
            {
                //
                // Properties most often used in a BrowserTestObject.
                // 1) .window
                //
                result.add(".window");
                result.add(StringFunctions.ifNull(properties.get(".window")));
            }
            else if (className.equals("TopLevelTestObject"))
            {
                //
                // Properties most often used in a TopLevelTestObject.
                // 1) .window
                // 2) .caption
                //
                result.add(".window");
                result.add(StringFunctions.ifNull(properties.get(".window")));
                if (!StringFunctions.ifNull(properties.get(".caption")).equals(""))
                {
                    result.add(".caption");
                    result.add(StringFunctions.ifNull(properties.get(".caption")));
                }
            }
            else
            {
                //
                // Properties most often used in a HTML or Win object, in order of reliablity.
                // 1) .id
                // 2) .name
                // 3) .value
                // 4) .text
                // 5) .classIndex
                //
                if (!StringFunctions.ifNull(properties.get(".id")).equals(""))
                {
                    result.add(".id");
                    result.add(StringFunctions.ifNull(properties.get(".id")));
                }
                else if (!StringFunctions.ifNull(properties.get(".name")).equals(""))
                {
                    result.add(".name");
                    result.add(StringFunctions.ifNull(properties.get(".name")));
                }
                else if (!StringFunctions.ifNull(properties.get(".value")).equals(""))
                {
                    result.add(".value");
                    result.add(StringFunctions.ifNull(properties.get(".value")));
                }
                else if (!StringFunctions.ifNull(properties.get(".text")).equals(""))
                {
                    result.add(".text");
                    result.add(StringFunctions.ifNull(properties.get(".text")));
                }
                else if (!StringFunctions.ifNull(properties.get(".classIndex")).equals(""))
                {
                    result.add(".classIndex");
                    result.add(StringFunctions.ifNull(properties.get(".classIndex")));
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }
    
    /**
     * A nice formatted representation of the Subitem array
     * 
     * @param properties
     *            a Subitem array that contains anchor with associated Properties
     * @return a String
     */
    public static String getDescription(Subitem...properties)
    {
        String propertyList = new String();
        for (int i = 0; i < properties.length; i++)
        {
            Subitem subitem = properties[i];
            Anchor a = (Anchor) subitem;
            if (a.getAnchored())
            {
                propertyList += "atChild(";
            }
            else
            {
                propertyList += "atDescendant(";
            }
            Property[] objProperties = a.getProperties();
            for (Property p : objProperties)
            {
                propertyList += p.getPropertyName() + "," + p.getPropertyValue() + ",";
            }
            if (propertyList.endsWith(","))
            {
                propertyList = propertyList.substring(0, propertyList.length() - 1);
            }
            propertyList += ")";
        }
        return propertyList;
    }
    
    /**
     * Converts a SpyMappedTestObject into its companion regular RFT TestObject (or subtype)
     * 
     * @param smto
     *            The SpyMappedTestObject to convert
     * @return A 1-element array with the converted TO in it, if the conversion worked. If the array has 0 elements, conversion failed.
     */
    public static TestObject[] convertMappedObject(SpyMappedTestObject smto)
    {
        String testObjectName = smto.getTestObjectClassName();
        String basePackage = "com.rational.test.ft.object.interfaces";
        String[] subPackages = {"dojo", "flex", "generichtmlsubdomain", "SAP", "siebel", "WPF"};
        boolean classFound = false;
        String fullTOClassName = "";
        Object theTO = null;
        try
        {
            Class.forName(basePackage + "." + testObjectName);
            classFound = true;
            fullTOClassName = basePackage + "." + testObjectName;
        }
        catch (Exception e)
        {
            for (String subPackage : subPackages)
            {
                try
                {
                    Class.forName(basePackage + "." + subPackage + "." + testObjectName);
                    classFound = true;
                    fullTOClassName = basePackage + "." + subPackage + "." + testObjectName;
                }
                catch (Exception ex)
                {
                    classFound = false;
                }
                if (classFound == true)
                {
                    break;
                }
            }
        }
        
        try
        {
            theTO = Reflection.createInstanceOf(fullTOClassName, smto);
        }
        catch (Exception e)
        {
            RationalTestScript.logException(e);
            return new TestObject[0];
        }
        TestObject[] foundTOs = new TestObject[1];
        Array.set(foundTOs, 0, theTO);
        return foundTOs;
    }
    
    /**
     * This function looks through the objects in an RFT ObjectMap object to find the first one with a #name property matching niceName. <br>
     * The #name and #id properties are found on the administrative tab for an object.
     * 
     * @param om
     *            RFT ObjectMap object to load the object from
     * @param niceName
     *            The value of #name to look for
     * @return A 1-element array with the TO in it, if the find-and-convert worked. If the array has 0 elements, find-and-conver failed.
     */
    public static TestObject[] getMappedObject(ObjectMap om, String niceName)
    {
        Enumeration<?> e = om.elements();
        while (e.hasMoreElements())
        {
            MappedTestObject mto = (MappedTestObject) e.nextElement();
            if (mto.getDescriptiveName().equals(niceName))
            {
                SpyMappedTestObject smto = om.getSharedInstance(mto.getId());
                return TO.convertMappedObject(smto);
            }
        }
        return new TestObject[0];
    }
    
    /**
     * This function loads an RFT object map file from disk into a RFT ObjectMap object, looks through the objects in it to find the first one with a #name property matching niceName. <br>
     * The #name and #id properties are found on the administrative tab for an object.
     * 
     * @param path
     *            The path to the object map file on disk
     * @param niceName
     *            The value of #name to look for
     * @return A 1-element array with the TO in it, if the find-and-convert worked. If the array has 0 elements, find-and-conver failed.
     */
    public static TestObject[] getMappedObject(String path, String niceName)
    {
        ObjectMap om = ObjectMap.load(new File(path));
        return TO.getMappedObject(om, niceName);
    }
    
    /**
     * This function looks through the objects in an RFT ObjectMap object to find the first one with a #id property matching niceName. <br>
     * The #name and #id properties are found on the administrative tab for an object.
     * 
     * @param om
     *            RFT ObjectMap object to load the object from
     * @param id
     *            The value of #id to look for
     * @return A 1-element array with the TO in it, if the find-and-convert worked. If the array has 0 elements, find-and-conver failed.
     */
    public static TestObject[] getMappedObjectByID(ObjectMap om, String id)
    {
        Enumeration<?> e = om.elements();
        while (e.hasMoreElements())
        {
            MappedTestObject mto = (MappedTestObject) e.nextElement();
            if (mto.getId().equals(id))
            {
                SpyMappedTestObject smto = om.getSharedInstance(mto.getId());
                return TO.convertMappedObject(smto);
            }
        }
        return new TestObject[0];
    }
    
    /**
     * This function loads an RFT object map file from disk into a RFT ObjectMap object, looks through the objects in it to find the first one with a #id property matching niceName. <br>
     * The #name and #id properties are found on the administrative tab for an object.
     * 
     * @param path
     *            The path to the object map file on disk
     * @param id
     *            The value of #id to look for
     * @return A 1-element array with the TO in it, if the find-and-convert worked. If the array has 0 elements, find-and-conver failed.
     */
    public static TestObject[] getMappedObjectByID(String path, String id)
    {
        ObjectMap om = ObjectMap.load(new File(path));
        return TO.getMappedObject(om, id);
    }
    
    /**
     * This function dumps all the TestObject details for the given TestObject.
     * 
     * @param start
     *            The TestObject to start with
     * @param desiredProperties
     *            The properties of each TestObject that you want returned. <br>
     *            "All" brings back every available property<br>
     *            "toString" executes TestObject.toString()<br>
     *            "methods" executes TestObject.getMethods()<br>
     *            "testDataTypes" executes RFT.dumpTestDataTypeDetails(TestObject object)<br>
     *            Otherwise the returned linkedhashmap has just the properties you supply, and "Not Defined" set any time a property doesn't exist on a TestObject we run into.
     * @return A LinkedHashMap with the details on the TestObject and up to 20 layers of children below.
     */
    public static LinkedHashMap<String, Object> dumpTestObjectDetails(TestObject start, String...desiredProperties)
    {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        if (start == null)
        {
            result.put("Null Object Ref Specified", "null");
            return result;
        }
        if (start instanceof RootTestObject)
        {
            result.put(".class", "RootTestObject");
            TestObject[] domains = ((RootTestObject) start).getDomains();
            int i = 1;
            for (TestObject t : domains)
            {
                result.put("Domain " + i, ((DomainTestObject) t).getName().toString());
                i++;
            }
            RationalTestScript.unregister(domains);
        }
        else if (start instanceof DomainTestObject)
        {
            result.put(".class", "DomainTestObject");
            result.put("Domain Name", ((DomainTestObject) start).getName().toString());
        }
        else
        {
            Vector<Object> properties = new Vector<Object>();
            if (desiredProperties.length == 0)
            {
                properties.add("toString");
            }
            else if (desiredProperties.length == 1 && desiredProperties[0].equalsIgnoreCase("All"))
            {
                if (start.getProperties() != null)
                {
                    for (Enumeration<?> propNames = start.getProperties().keys(); propNames.hasMoreElements();)
                    {
                        Object s = propNames.nextElement();
                        properties.add(s);
                    }
                }
                if (start.getNonValueProperties() != null)
                {
                    for (Enumeration<?> nonValuePropNames = start.getNonValueProperties().keys(); nonValuePropNames.hasMoreElements();)
                    {
                        Object s = nonValuePropNames.nextElement();
                        properties.add(s);
                    }
                }
                properties.add("toString");
                properties.add("methods");
                properties.add("testDataTypes");
            }
            else
            {
                for (String s : desiredProperties)
                {
                    properties.add(s);
                }
            }
            for (Object s : properties)
            {
                if (s.equals("toString"))
                {
                    result.put("toString", start.toString());
                }
                else if (s.equals("methods"))
                {
                    LinkedHashMap<String, Object> invokeableMethods = new LinkedHashMap<String, Object>();
                    if (start.getMethods() != null)
                    {
                        for (MethodInfo mi : start.getMethods())
                        {
                            LinkedHashMap<String, String> methodInfo = new LinkedHashMap<String, String>();
                            methodInfo.put("Declaring Class", mi.getDeclaringClass());
                            methodInfo.put("Signature", mi.getSignature());
                            invokeableMethods.put(mi.getName(), methodInfo);
                        }
                        result.put("Invokeable Methods", invokeableMethods);
                    }
                }
                else if (s.equals("testDataTypes"))
                {
                    result.put("Test Data Types", TestDataType.dumpTestDataTypeDetails(start));
                }
                else
                {
                    Object o = null;
                    try
                    {
                        o = start.getProperty(s.toString());
                        if (o == null)
                        {
                            result.put(s.toString(), "Not Defined");
                        }
                        else
                        {
                            try
                            {
                                TestObject to1 = (TestObject) o;
                                result.put(s.toString(), to1.getProperties());
                                to1.unregister();
                            }
                            catch (Exception e)
                            {
                                result.put(s.toString(), o);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        result.put(s.toString(), "Exception: " + e.getMessage());
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Checks if the supplied TestObject run time properties match the supplied properties.<br>
     * All run time properties are toString before property is passed to Regex.isMatch(String, String).<br>
     * If all supplied properties match, true
     * 
     * @param object
     *            a test object
     * @param properties
     *            test object properties to match against (e.g. {".class", "Html.TABLE"})
     * @return If all supplied properties match the run time properties returns true, otherwise false
     */
    public static boolean isMatch(TestObject object, String...properties)
    {
        boolean isMatch = true;
        if (object.getProperties() == null)
        {
            return false;
        }
        for (int i = 0; i < properties.length - 1; i += 2)
        {
            if (Regex.isStringToPattern(properties[i + 1]))
            {
                if (!Regex.isMatch(StringFunctions.ifNull(object.getProperties().get(properties[i])), properties[i + 1]))
                {
                    isMatch = false;
                }
            }
            else if (!StringFunctions.ifNull(object.getProperties().get(properties[i])).equals(properties[i + 1]))
            {
                isMatch = false;
            }
        }
        return isMatch;
    }
    
    /**
     * Goes through a TestObject hierarchy and extracts it out into a generic tree structure.<br>
     * Is capable of looking at TestObjects returned by getChildren(), getOwnedObjects(), and non-value property TestObjects.<br>
     * The returned tree models the TestObject hierarchy.<br>
     * In some cases, an error may be encountered while going through the owned and/or non-value property TestObjects.<br>
     * If that happens, we simply leave the offending object out.<br>
     * If an error is encountered while going through the child TestObjects, this function will probably blow up.<br>
     * The different handling is a conscious design decision.<br>
     * In most cases, failure to work with a getChildren() TestObject would be significant (e.g possible RFT bug), but failure for the other two categories may just mean a UI element RT can't handle.<br>
     * Therefore, we want the two scenarios to act differently.<br>
     * 
     * @param startingNode
     *            the TestObject to start from
     * @param includeOwnedTOs
     *            True to include TestObjects returned by calling TestObject.getOwnedObjects(), False to ignore those.<br>
     *            In some cases, an error may be encountered while going through these TestObjects.<br>
     *            If that happens, we simply leave the offending object out.
     * @param includePropertyTOs
     *            True to include TestObjects that represent non-value properties, False to ignore them.<br>
     *            In some cases, an error may be encountered while going through these TestObjects.<br>
     *            If that happens, we simply leave the offending object out.
     * @return The tree of TestObjects
     */
    public static Tree<TestObject> extractTestObjectTree(TestObject startingNode, boolean includeOwnedTOs, boolean includePropertyTOs)
    {
        // Create our root tree node
        Tree<TestObject> theTree = new Tree<TestObject>();
        // As we call getChildren() and encounter TestObjects, we will need to add nodes to the tree for them.
        // We will also store them in this queue, because this queue is the queue of tree nodes that need to have a TestObject added to them
        Queue<Tree<TestObject>> treeNodeQueue = new LinkedList<Tree<TestObject>>();
        // Add the root tree node to the aforementioned queue of tree nodes
        treeNodeQueue.add(theTree);
        // Create a master queue for our TestObjects.
        Queue<TestObject> workingTOstack = new LinkedList<TestObject>();
        // Now we add our starting TestObject to our master queue
        workingTOstack.add(startingNode);
        // We have work to do as long as there are TestObjects in our master queue
        while (workingTOstack.size() != 0)
        {
            // First we need to get the tree node we'll work with from our tree node queue
            Tree<TestObject> currentTreeNode = treeNodeQueue.peek();
            // Then we add the object to the tree
            currentTreeNode.setValue(workingTOstack.peek());
            try
            {
                // Then we set the tree node's name to the TestObject's .class property
                currentTreeNode.setName(workingTOstack.peek().getProperty(".class").toString());
            }
            catch (Throwable e)
            {
                try
                {
                    // If for some reason that fails, set it to the toString() of the TestObject's properties
                    currentTreeNode.setName(workingTOstack.peek().getProperties().toString());
                }
                catch (Throwable ex)
                {
                    // If for some reason that fails, set it to the TestObject's toString()
                    currentTreeNode.setName(workingTOstack.peek().toString());
                }
            }
            for (TestObject child : workingTOstack.peek().getChildren())
            {
                // Then we add the TestObject's children to the main queue
                workingTOstack.add(child);
                Tree<TestObject> childNode = new Tree<TestObject>();
                // Then we add branches to the current tree node, one for each child TestObject
                currentTreeNode.addBranch(childNode);
                // Then we add those branches to the tree node queue
                treeNodeQueue.add(childNode);
            }
            // Optionally we can include TestObjects owned by our current TO, since they are still below it in the tree in a sense
            if (includeOwnedTOs == true)
            {
                try
                {
                    if (workingTOstack.peek().getOwnedObjects() != null)
                    {
                        for (TestObject child : workingTOstack.peek().getOwnedObjects())
                        {
                            try
                            {
                                // Then we add the TestObject's children to the main queue
                                workingTOstack.add(child);
                                Tree<TestObject> childNode = new Tree<TestObject>();
                                // Then we add branches to the current tree node, one for each child TestObject
                                currentTreeNode.addBranch(childNode);
                                // Then we add those branches to the tree node queue
                                treeNodeQueue.add(childNode);
                            }
                            catch (Throwable e)
                            {
                                // If for some reason the object can't be processed, simply leave it out and go on.
                            }
                        }
                    }
                }
                catch (Throwable e)
                {
                    // If for some reason the objects can't be processed, simply leave it out and go on.
                }
            }
            // Optionally we can include non-value properties of our current TO, since they show up as TestObjects and are below it in the tree in a sense.
            // Sometimes they even have children, owned, and/or non-value property TOs of their own.
            if (includePropertyTOs == true)
            {
                try
                {
                    Vector<String> propertyNames = new Vector<String>();
                    if (workingTOstack.peek().getProperties() != null)
                    {
                        for (Object o : workingTOstack.peek().getProperties().keySet())
                        {
                            propertyNames.add(StringFunctions.ifNull(o));
                        }
                    }
                    if (workingTOstack.peek().getNonValueProperties() != null)
                    {
                        for (Object o : workingTOstack.peek().getNonValueProperties().keySet())
                        {
                            propertyNames.add(StringFunctions.ifNull(o));
                        }
                    }
                    try
                    {
                        for (String s : propertyNames)
                        {
                            TestObject casted = null;
                            try
                            {
                                casted = (TestObject) workingTOstack.peek().getProperty(s);
                            }
                            catch (Throwable e)
                            {
                                // If we can't cast the result into a TestObject, go on to the next item.
                            }
                            if (casted != null)
                            {
                                // Then we add the TestObject's children to the main queue
                                workingTOstack.add(casted);
                                Tree<TestObject> childNode = new Tree<TestObject>();
                                // Then we add branches to the current tree node, one for each child TestObject
                                currentTreeNode.addBranch(childNode);
                                // Then we add those branches to the tree node queue
                                treeNodeQueue.add(childNode);
                            }
                        }
                    }
                    catch (Throwable e)
                    {
                        // If for some reason the object can't be processed, simply leave it out and go on.
                    }
                }
                catch (Throwable e)
                {
                    // If for some reason the object can't be processed, simply leave it out and go on.
                }
            }
            // Finally we remove the current TestObject and Tree nodes from their respective queues
            treeNodeQueue.remove();
            workingTOstack.remove();
        }
        return theTree;
    }
    
    public static Tree<TO> describeTestObjectTree(TestObject startingNode)
    {
        ArrayList<TestObject> unregisterCollection = new ArrayList<TestObject>();
        // Create our root tree node
        Tree<TO> theTree = new Tree<TO>();
        // As we call getChildren() and encounter TestObjects, we will need to add nodes to the tree for them.
        // We will also store them in this queue, because this queue is the queue of tree nodes that need to have a TestObject added to them
        Queue<Tree<TO>> treeNodeQueue = new LinkedList<Tree<TO>>();
        // Add the root tree node to the aforementioned queue of tree nodes
        treeNodeQueue.add(theTree);
        // Create a master queue for our TestObjects.
        Queue<TestObject> workingTOstack = new LinkedList<TestObject>();
        // Now we add our starting TestObject to our master queue
        workingTOstack.add(startingNode);
        // We have work to do as long as there are TestObjects in our master queue
        while (workingTOstack.size() != 0)
        {
            // First we need to get the tree node we'll work with from our tree node queue
            Tree<TO> currentTreeNode = treeNodeQueue.peek();
            // Then we add the object to the tree
            currentTreeNode.setValue(new TO(workingTOstack.peek()));
            // Then we add the object to the unregister collection
            unregisterCollection.add(workingTOstack.peek());
            try
            {
                // Then we set the tree node's name to the TestObject's .class property
                currentTreeNode.setName(workingTOstack.peek().getProperty(".class").toString());
            }
            catch (Throwable e)
            {
                try
                {
                    // If for some reason that fails, set it to the toString() of the TestObject's properties
                    currentTreeNode.setName(workingTOstack.peek().getProperties().toString());
                }
                catch (Throwable ex)
                {
                    // If for some reason that fails, set it to the TestObject's toString()
                    currentTreeNode.setName(workingTOstack.peek().toString());
                }
            }
            for (TestObject child : workingTOstack.peek().getChildren())
            {
                // Then we add the TestObject's children to the main queue
                workingTOstack.add(child);
                Tree<TO> childNode = new Tree<TO>();
                // Then we add branches to the current tree node, one for each child TestObject
                currentTreeNode.addBranch(childNode);
                // Then we add those branches to the tree node queue
                treeNodeQueue.add(childNode);
                // Finally we add the TestObject to the unregister collection
                unregisterCollection.add(child);
            }
            // Finally we remove the current TestObject and Tree nodes from their respective queues
            treeNodeQueue.remove();
            workingTOstack.remove();
        }
        RationalTestScript.unregister(unregisterCollection.toArray(new TestObject[0]));
        return theTree;
    }
    
    public static void free(Object...o1)
    {
        for (Object o : o1)
        {
            if (o == null)
            {
                continue;
            }
            else if (o instanceof Map<?, ?>)
            {
                Map<?, ?> results = (Map<?, ?>) o;
                for (Object k : results.keySet())
                {
                    free(k, results.get(k));
                }
            }
            else if (o instanceof Dictionary<?, ?>)
            {
                Dictionary<?, ?> results = (Dictionary<?, ?>) o;
                Enumeration<?> keys = results.keys();
                while (keys.hasMoreElements())
                {
                    Object key = keys.nextElement();
                    Object value = results.get(key);
                    free(key, value);
                }
            }
            else if (o instanceof Collection<?>)
            {
                Collection<?> c = (Collection<?>) o;
                Iterator<?> it = c.iterator();
                while (it.hasNext())
                {
                    free(it.next());
                }
            }
            else if (o instanceof Enumeration<?>)
            {
                Enumeration<?> e = (Enumeration<?>) o;
                while (e.hasMoreElements())
                {
                    free(e.nextElement());
                }
            }
            else if (o.getClass().isArray())
            {
                for (int i = 0; i < Array.getLength(o); i++)
                {
                    free(Array.get(o, i));
                }
            }
            else if (o instanceof TestObject)
            {
                try
                {
                    ((TestObject) o).unregister();
                }
                catch (Exception e)
                {
                    
                }
            }
        }
    }
    
    /**
     * A friendly name for the TestObject youre using this instance to represent
     */
    protected String          friendlyName          = new String();
    /**
     * The actual runtime properties for the TestObject
     */
    protected Hashtable<?, ?> properties            = null;
    /**
     * The properties used to find this TO.
     */
    protected String[]        recognitionProperties = new String[0];
    
    /**
     * Construct a
     * 
     * @param friendlyName
     * @param recognitionProperties
     */
    public TO(String friendlyName, String...recognitionProperties)
    {
        setFriendlyName(friendlyName);
        setRecognitionProperties(recognitionProperties);
    }
    
    public TO(String friendlyName, Hashtable<?, ?> properties, String...recognitionProperties)
    {
        setFriendlyName(friendlyName);
        setProperties(properties);
        setRecognitionProperties(recognitionProperties);
    }
    
    public TO(String friendlyName, com.rational.test.ft.object.interfaces.TestObject object, String...nonValueProperties)
    {
        setFriendlyName(friendlyName);
        setProperties(object.getProperties());
        setRecognitionProperties(TO.getRecognitionProperties(object, nonValueProperties));
    }
    
    public TO(com.rational.test.ft.object.interfaces.TestObject object, String...nonValueProperties)
    {
        setFriendlyName(object.getProperty(".class").toString());
        setProperties(object.getProperties());
        setRecognitionProperties(TO.getRecognitionProperties(object, nonValueProperties));
    }
    
    public String getFriendlyName()
    {
        return friendlyName;
    }
    
    public void setFriendlyName(String friendlyName)
    {
        this.friendlyName = friendlyName;
    }
    
    public Hashtable<?, ?> getProperties()
    {
        return properties;
    }
    
    public void setProperties(Hashtable<?, ?> objProps)
    {
        properties = objProps;
    }
    
    public String[] getRecognitionProperties()
    {
        return recognitionProperties;
    }
    
    public void setRecognitionProperties(String...recognitionProperties)
    {
        this.recognitionProperties = recognitionProperties;
    }
    
    public boolean isMatch(String...comparisonProperties)
    {
        boolean isMatch = true;
        if (properties == null)
        {
            return false;
        }
        for (int i = 0; i < comparisonProperties.length - 1; i += 2)
        {
            if (Regex.isStringToPattern(comparisonProperties[i + 1]))
            {
                if (!Regex.isMatch(StringFunctions.ifNull(properties.get(comparisonProperties[i])), comparisonProperties[i + 1]))
                {
                    isMatch = false;
                }
            }
            else if (!StringFunctions.ifNull(properties.get(comparisonProperties[i])).equals(comparisonProperties[i + 1]))
            {
                isMatch = false;
            }
        }
        return isMatch;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[friendlyName:");
        builder.append(friendlyName);
        builder.append(", properties: { ");
        for (Object property : properties.keySet())
        {
            builder.append(StringFunctions.ifNull(property) + " => " + StringFunctions.ifNull2(properties.get(property), "null") + " ");
        }
        builder.append("}]");
        return builder.toString();
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (friendlyName == null ? 0 : friendlyName.hashCode());
        result = prime * result + (properties == null ? 0 : properties.hashCode());
        result = prime * result + Arrays.hashCode(recognitionProperties);
        return result;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj.getClass().isArray())
        {
            String[] stringProps = new String[Array.getLength(obj)];
            for (int i = 0; i < Array.getLength(obj); i++)
            {
                stringProps[i] = StringFunctions.ifNull(Array.get(obj, i));
            }
            return isMatch(stringProps);
        }
        else if (obj instanceof List<?>)
        {
            List<?> objectList = (List<?>) obj;
            String[] stringProps = new String[objectList.size()];
            for (int i = 0; i < objectList.size(); i++)
            {
                stringProps[i] = StringFunctions.ifNull(objectList.get(i));
            }
            return isMatch(stringProps);
        }
        
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj))
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        TO other = (TO) obj;
        for (Object property : other.getProperties().keySet())
        {
            if (!getProperties().get(property).equals(other.getProperties().get(property)))
            {
                String otherStrFormVal = StringFunctions.ifNull(other.getProperties().get(property));
                if (Regex.isStringToPattern(otherStrFormVal))
                {
                    String myStrFormVal = StringFunctions.ifNull(getProperties().get(property));
                    if (!Regex.isMatch(myStrFormVal, otherStrFormVal))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
}
