package com.lps.rsdc2010.common.automation.rft.object;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import com.lps.rsdc2010.common.lang.MapFunctions;
import com.lps.rsdc2010.common.lang.Regex;
import com.lps.rsdc2010.common.lang.types.Tree;
import com.rational.test.ft.object.interfaces.IWindow;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.sys.graphical.Window;

public class IW
{
    
    public static Vector<IWindow> findIWindowFromRoot(int numberOfTries, double sleepBetweenTries, String...properties)
    {
        return IW.findIWindow(numberOfTries, sleepBetweenTries, null, properties);
    }
    
    public static Vector<IWindow> findIWindow(int numberOfTries, double sleepBetweenTries, IWindow parent, String...properties)
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
            attempts++;
        }
        Vector<IWindow> results = new Vector<IWindow>();
        for (String s : iwindows.keySet())
        {
            results.add(iwindows.get(s));
        }
        return results;
    }
    
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
    
    public static String[] getRecognitionProperties(IWindow iw1)
    {
        LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
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
        return MapFunctions.getStringArray(properties);
    }
    
    private LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
    
    public IW()
    {
        
    }
    
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
    
    public String getProperty(String propertyName)
    {
        return properties.get(propertyName);
    }
    
    public void setProperty(String propertyName, String value)
    {
        properties.put(propertyName, value);
    }
    
    public boolean doPropertiesMatch(String...props)
    {
        boolean result = true;
        for (int i = 0; i < props.length; i += 2)
        {
            String propName = props[i];
            String propVal = props[i + 1];
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
            if (Regex.isStringToPattern(propVal))
            {
                if (!Regex.isMatch(currentVal, propVal))
                {
                    result = false;
                }
            }
            else
            {
                result = result == false ? false : propVal.equals(currentVal);
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
