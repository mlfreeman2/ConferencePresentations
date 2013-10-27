package com.advancedrft.common.automation.rft.object;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import com.advancedrft.common.lang.MapFunctions;
import com.advancedrft.common.lang.Regex;
import com.advancedrft.common.lang.StringFunctions;
import com.advancedrft.common.lang.types.Tree;
import com.rational.test.ft.object.interfaces.IWindow;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.script.RationalTestScript;
import com.rational.test.ft.sys.graphical.Window;

/**
 * A data class designed to represent RFT IWindow objects.<br>
 * Also contains several helper functions for use with them.
 */
public class IW
{
    
    /**
     * Finds owned or child windows from the root test object with the given properties
     * 
     * @param numberOfTries
     *            number of times to attempt to find the window
     * @param sleepBetweenTries
     *            sleep between find attempts
     * @param properties
     *            Name-value pairs of recognition properties for the given window
     * @return A list of all the windows found matching the given properties
     */
    public static List<IWindow> findIWindowFromRoot(int numberOfTries, double sleepBetweenTries, String...properties)
    {
        return IW.findIWindow(numberOfTries, sleepBetweenTries, null, properties);
    }
    
    /**
     * Finds owned or child windows from the parent with the given properties
     * 
     * @param numberOfTries
     *            number of times to attempt to find the window
     * @param sleepBetweenTries
     *            sleep between find attempts
     * @param parent
     *            parent window to start search from, if passed null starts from root test object
     * @param properties
     *            Name-value pairs of recognition properties for the given window
     * @return A list of all the windows found matching the given properties
     */
    public static List<IWindow> findIWindow(int numberOfTries, double sleepBetweenTries, IWindow parent, String...properties)
    {
        int attempts = 0;
        Hashtable<String, IWindow> iwindows = new Hashtable<String, IWindow>();
        
        while (attempts < numberOfTries)
        {
            for (IWindow candidate : IW.grabChildIWindows(parent))
            {
                if (new IW(candidate).doPropertiesMatch(properties) == true)
                {
                    iwindows.put(Long.toString(candidate.getHandle()), candidate);
                }
            }
            for (IWindow candidate : IW.grabOwnedIWindows(parent))
            {
                if (new IW(candidate).doPropertiesMatch(properties) == true && !iwindows.containsKey(Long.toString(candidate.getHandle())))
                {
                    iwindows.put(Long.toString(candidate.getHandle()), candidate);
                }
            }
            if (iwindows.size() > 0)
            {
                break;
            }
            RationalTestScript.sleep(sleepBetweenTries);
            attempts++;
        }
        Vector<IWindow> results = new Vector<IWindow>();
        for (String s : iwindows.keySet())
        {
            results.add(iwindows.get(s));
        }
        return results;
    }
    
    /**
     * Brings the given IWindow to the top of all the windows and sets it as the active window
     * 
     * @param w
     *            IWindow you want to activate
     * @return true if successfully activated, false if an exception was thrown (failed to activate)
     */
    public static boolean activate(IWindow w)
    {
        try
        {
            Window win = new Window(w.getHandle());
            win.getTopLevelWindow().bringToTop();
            win.getTopLevelWindow().activate();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * Gets all the child objects of an IWindow<br>
     * For more information on Parent-Child and Owner-Owned Hierarchies see: <A href="http://msdn.microsoft.com/en-us/library/ff381403%28v=vs.85%29.aspx">What Is a Window?</A>
     * 
     * @param parent
     *            parent IWindow to get child objects from, if passed null will use the root and return all child IWindows for all the top level windows
     * @return List of child IWindows found
     */
    public static List<IWindow> grabChildIWindows(IWindow parent)
    {
        List<IWindow> windowList = new ArrayList<IWindow>();
        Queue<IWindow> iWindowQueue = new LinkedList<IWindow>();
        if (parent == null)
        {
            IWindow[] windows = RootTestObject.getRootTestObject().getTopWindows();
            for (int i = 0; i < windows.length; i++)
            {
                iWindowQueue.add(windows[i]);
            }
        }
        else
        {
            iWindowQueue.add(parent);
        }
        while (!iWindowQueue.isEmpty())
        {
            IWindow current = iWindowQueue.peek();
            windowList.add(current);
            for (IWindow w : current.getChildren())
            {
                iWindowQueue.add(w);
            }
            iWindowQueue.remove();
        }
        return windowList;
    }
    
    /**
     * Gets all the owned objects of an IWindow<br>
     * For more information on Parent-Child and Owner-Owned Hierarchies see: <A href="http://msdn.microsoft.com/en-us/library/ff381403%28v=vs.85%29.aspx">What Is a Window?</A>
     * 
     * @param parent
     *            parent IWindow to get owned objects from, if passed null will use the root and return all owned IWindows for all the top level windows
     * @return List of owned IWindows found
     */
    public static List<IWindow> grabOwnedIWindows(IWindow parent)
    {
        List<IWindow> windowList = new ArrayList<IWindow>();
        Queue<IWindow> iWindowQueue = new LinkedList<IWindow>();
        if (parent == null)
        {
            IWindow[] windows = RootTestObject.getRootTestObject().getTopWindows();
            for (int i = 0; i < windows.length; i++)
            {
                iWindowQueue.add(windows[i]);
            }
        }
        else
        {
            iWindowQueue.add(parent);
        }
        while (!iWindowQueue.isEmpty())
        {
            IWindow current = iWindowQueue.peek();
            windowList.add(current);
            for (IWindow w : current.getOwned())
            {
                iWindowQueue.add(w);
            }
            iWindowQueue.remove();
        }
        return windowList;
    }
    
    /**
     * Does a breadth-first search through the Parent-child hierarchy with the second level of the tree being the the top level windows of the root object.<br>
     * The name of each node is the handle of the IW and the Value is the IW itself.<br>
     * The root node has a Name of {root} and value of an empty IW.<br>
     * For more information on Parent-Child and Owner-Owned Hierarchies see: <A href="http://msdn.microsoft.com/en-us/library/ff381403%28v=vs.85%29.aspx">What Is a Window?</A>
     * 
     * @return Parent-child hierarchy Tree
     */
    public static Tree<IW> dumpIWChildTree()
    {
        Tree<IW> root = new Tree<IW>();
        root.setName("{root}");
        root.setValue(new IW());
        Queue<IWindow> iWindowQueue = new LinkedList<IWindow>();
        Queue<Tree<IW>> treeNodeQueue = new LinkedList<Tree<IW>>();
        IWindow[] windows = RootTestObject.getRootTestObject().getTopWindows();
        for (int i = 0; i < windows.length; i++)
        {
            iWindowQueue.add(windows[i]);
            Tree<IW> newChild = new Tree<IW>();
            root.addBranch(newChild);
            treeNodeQueue.add(newChild);
        }
        while (!iWindowQueue.isEmpty() && !treeNodeQueue.isEmpty())
        {
            IWindow current = iWindowQueue.peek();
            Tree<IW> currTreeNode = treeNodeQueue.peek();
            IW newIW = new IW(current);
            currTreeNode.setValue(newIW);
            currTreeNode.setName(newIW.getProperty("handle"));
            for (IWindow w : current.getChildren())
            {
                iWindowQueue.add(w);
                Tree<IW> newChild = new Tree<IW>();
                currTreeNode.addBranch(newChild);
                treeNodeQueue.add(newChild);
            }
            iWindowQueue.remove();
            treeNodeQueue.remove();
        }
        return root;
    }
    
    /**
     * Does a breadth-first search through the Owner-owned hierarchy with the second level of the tree being the the top level windows of the root object.<br>
     * The name of each node is the handle of the IW and the Value is the IW itself.<br>
     * The root node has a Name of {root} and value of an empty IW.<br>
     * For more information on Parent-Child and Owner-Owned Hierarchies see: <A href="http://msdn.microsoft.com/en-us/library/ff381403%28v=vs.85%29.aspx">What Is a Window?</A>
     * 
     * @return Owner-owned hierarchy Tree
     */
    public static Tree<IW> dumpIWOwnedTree()
    {
        Tree<IW> root = new Tree<IW>();
        root.setName("{root}");
        root.setValue(new IW());
        Queue<IWindow> iWindowQueue = new LinkedList<IWindow>();
        Queue<Tree<IW>> treeNodeQueue = new LinkedList<Tree<IW>>();
        IWindow[] windows = RootTestObject.getRootTestObject().getTopWindows();
        for (int i = 0; i < windows.length; i++)
        {
            iWindowQueue.add(windows[i]);
            Tree<IW> newChild = new Tree<IW>();
            root.addBranch(newChild);
            treeNodeQueue.add(newChild);
        }
        while (!iWindowQueue.isEmpty() && !treeNodeQueue.isEmpty())
        {
            IWindow current = iWindowQueue.peek();
            Tree<IW> currTreeNode = treeNodeQueue.peek();
            IW newIW = new IW(current);
            currTreeNode.setValue(newIW);
            currTreeNode.setName(newIW.getProperty("handle"));
            for (IWindow w : current.getOwned())
            {
                iWindowQueue.add(w);
                Tree<IW> newChild = new Tree<IW>();
                currTreeNode.addBranch(newChild);
                treeNodeQueue.add(newChild);
            }
            iWindowQueue.remove();
            treeNodeQueue.remove();
        }
        return root;
    }
    
    /**
     * Gets the properties of the given IW
     * 
     * @param iw1
     *            IWindow you want to get properties from
     * @return String array of properties as Name-value pairs
     */
    public static String[] getRecognitionProperties(IWindow iw1)
    {
        return MapFunctions.getStringArray(new IW(iw1).properties);
    }
    
    private LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
    
    public IW()
    {
        
    }
    
    /**
     * Extracts the relevant properties from the IWindow object to supply to this IW object
     * 
     * @param iw1
     *            The IWindow object to extract properties from
     */
    public IW(IWindow iw1)
    {
        properties.put("text", iw1.getText());
        properties.put("windowClassName", iw1.getWindowClassName());
        properties.put("processId", Integer.toString(iw1.getPid()));
        properties.put("handle", Long.toString(iw1.getHandle()));
        properties.put("controlId", Long.toString(iw1.getId()));
        properties.put("isEnabled", Boolean.toString(iw1.isEnabled()));
        properties.put("isOpaque", Boolean.toString(iw1.isOpaque()));
        properties.put("isShowing", Boolean.toString(iw1.isShowing()));
        properties.put("isTopWindow", Boolean.toString(iw1.isTopLevel()));
        if (iw1.isTopLevel())
        {
            properties.put("isIconified", Boolean.toString(iw1.isIconified()));
        }
        else
        {
            properties.put("isIconified", "false");
        }
    }
    
    /**
     * Gets a property on this IW object
     * 
     * @param propertyName
     *            The name of the property to fetch
     * @return The value of the property
     */
    public String getProperty(String propertyName)
    {
        return properties.get(propertyName);
    }
    
    /**
     * Sets a property on this IW object
     * 
     * @param propertyName
     *            The name of the property to set
     * @param value
     *            The value to set the property to
     */
    public void setProperty(String propertyName, String value)
    {
        properties.put(propertyName, value);
    }
    
    /**
     * Compares this object's IW properties with the given set of properties
     * 
     * @param props
     *            name-value pairs with the name and values of the properties you want to verify.<br>
     *            Accepted Properties:<br>
     *            <LI>"text" - the window/object's title</LI><br>
     *            <LI>"windowClassName" - class name of the window.<br>
     *            IE Javascript Dialogs are normally "#32770"</LI><br>
     *            <LI>"processId"</LI><br>
     *            <LI>"handle"</LI><br>
     *            <LI>"controlId"</LI><br>
     *            <LI>"isEnabled"</LI><br>
     *            <LI>"isOpaque"</LI><br>
     *            <LI>"isShowing"</LI><br>
     *            <LI>"isTopWindow" - if the window has no Parent</LI><br>
     *            <LI>"isIconified"</LI><br>
     * @return true if all the given properties match this IW, false if any properties do not match
     */
    public boolean doPropertiesMatch(String...props)
    {
        boolean result = false;
        for (int i = 0; i < props.length; i += 2)
        {
            String propName = StringFunctions.ifNull(props[i]);
            String propVal = StringFunctions.ifNull(props[i + 1]);
            String currentVal = null;
            for (String s : properties.keySet())
            {
                if (s.equalsIgnoreCase(propName))
                {
                    currentVal = properties.get(s);
                    break;
                }
            }
            if (currentVal == null)
            {
                continue;
            }
            if (!Regex.isMatch2(currentVal, propVal))
            {
                return false;
            }
            else
            {
                result = true;
            }
        }
        return result;
    }
    
    @Override
    public int hashCode()
    {
        return Integer.parseInt(properties.get("handle"));
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        IW other = (IW) obj;
        if (properties == null)
        {
            if (other.properties != null)
            {
                return false;
            }
        }
        else if (!properties.equals(other.properties))
        {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return "IW " + properties.toString();
    }
}
