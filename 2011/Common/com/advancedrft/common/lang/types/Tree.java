package com.advancedrft.common.lang.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.advancedrft.common.lang.Regex;
import com.advancedrft.common.lang.StringFunctions;

/**
 * A generic unbalanced tree class. Useful for modeling things like HTML DOMs.<br />
 * Each <code>Tree</code> node holds a name String, a generic value, and an arbitrary number of "branch" / "child" nodes.
 * 
 * @param <V>
 *            The type you plan on containing in the tree
 */
public class Tree<V>
{
    protected List<Tree<V>> children = new ArrayList<Tree<V>>();
    
    protected String        name     = "";
    
    protected Tree<V>       parent;
    
    protected V             value;
    
    /**
     * Generic no-argument constructor for creating a new empty instance of <code>Tree&lt;X&gt;</code>.<br />
     * Useful for adding empty nodes (e.g. branches) to this tree.
     */
    public Tree()
    {
        
    }
    
    /**
     * A constructor to create a new <code>Tree</code> node and populate it with a given value.
     * 
     * @param value
     *            The value to put in this <code>Tree</code> node.
     */
    public Tree(V value)
    {
        this.value = value;
    }
    
    /**
     * Adds a child node to the current <code>Tree</code> object
     * 
     * @param branch
     *            The child node to add
     */
    public void addBranch(Tree<V> branch)
    {
        branch.setParent(this);
        children.add(branch);
    }
    
    /**
     * Fetches a given branch node based on the supplied path.<br />
     * Paths are calculated by taking the name String from each node and concatenating them with " -> " in between.<br />
     * The first path needs to be the name of the current node (<code>this</code>). If multiple nodes at a level have the same name, simply append <code>##</code> and a number to the name to get the <i>n</i>th node with that name.
     * 
     * @param path
     *            The path to a lower level node (e.g "a -> b -> d -> e")
     * @return The node, or null if it can't ultimately be found
     */
    public Tree<V> fetchNodeByPath(String path)
    {
        String[] parts = path.split(" -> ");
        if (parts[0].equals(getName()) && parts.length == 1)
        {
            return this;
        }
        else if (parts[0].equals(getName()) && parts.length > 1)
        {
            ArrayList<String> pathElements = new ArrayList<String>(Arrays.asList(parts));
            pathElements.remove(0);
            for (Tree<V> child : children)
            {
                Tree<V> result = child.fetchNodeByPath(StringFunctions.join(" -> ", pathElements));
                if (result != null)
                {
                    return result;
                }
            }
        }
        else
        {
            int desiredPosition = 1, position = 1;
            String stepName = "";
            if (Regex.isMatch(parts[0], "regex=^(.+) ##(\\d+)$"))
            {
                ArrayList<String> matches = Regex.matchString(parts[0], "regex=^(.+) ##(\\d+)$");
                stepName = matches.get(0);
                desiredPosition = Integer.parseInt(matches.get(1));
                if (desiredPosition <= 1)
                {
                    desiredPosition = 1;
                }
            }
            if (stepName.equals(getName()))
            {
                for (int i = 0; i < parent.getNumOfChildren(); i++)
                {
                    if (parent.getChild(i).getName().equals(getName()))
                    {
                        if (parent.getChild(i) == this && desiredPosition == position)
                        {
                            ArrayList<String> pathElements = new ArrayList<String>(Arrays.asList(parts));
                            pathElements.remove(0);
                            for (Tree<V> child : children)
                            {
                                Tree<V> result = child.fetchNodeByPath(StringFunctions.join(" -> ", pathElements));
                                if (result != null)
                                {
                                    return result;
                                }
                            }
                            break;
                        }
                        else
                        {
                            position += 1;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * This function descends through the tree looking for all nodes with the given value.
     * 
     * @param query
     *            The value to look for
     * @return A list of all the nodes with this value (0 or more items in this list)
     */
    public List<Tree<V>> findMatchingNodes(V query)
    {
        List<Tree<V>> results = new ArrayList<Tree<V>>();
        if (query.equals(value))
        {
            results.add(this);
        }
        for (Tree<V> branch : children)
        {
            results.addAll(branch.findMatchingNodes(query));
        }
        return results;
    }
    
    /**
     * Returns the <i>n</i>th overall child of this node. The indexes start with 0.
     * 
     * @param n
     *            The index of the child node you want. Starts with 0.
     * @return The <i>n</i>th child node or null if the index is invalid
     */
    public Tree<V> getChild(int n)
    {
        if (n > 0 && n < children.size())
        {
            return children.get(n);
        }
        return null;
    }
    
    /**
     * Returns the name of this node.
     * 
     * @return The name of this node
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the number of child nodes.
     * 
     * @return The number of child nodes
     */
    public int getNumOfChildren()
    {
        return children.size();
    }
    
    /**
     * Returns the parent of this node. This will be <code>null</code> if the current node is the root node.
     * 
     * @return The parent of this node, or null if this node is the root node.
     */
    public Tree<V> getParent()
    {
        return parent;
    }
    
    /**
     * Returns the path down the tree to this node by working backwards until {@link #parent} is equal to null (in other words the root node is hit).<br>
     * 
     * @return The path from the root down to this node, as a series of names concatenated together by " -> ".<br>
     *         If multiple nodes with the same name exist on a given level this function will append "##" and a number to indicate which one of those you'd have to go through.<br>
     *         These indices start at 1.<br>
     *         If the tree were to be visualized, index 1 would be the leftmost node with the name and the indices would count up as you went to the right.<br>
     *         Think of "Something -> Foo ##3 -> Something Else" as "the third node named Foo under Something", not "thr third node under Something...and it happens to be named Foo".<br>
     */
    public String getPath()
    {
        Tree<V> current = this;
        ArrayList<String> pathElements = new ArrayList<String>();
        // If we're at the root node, getParent() will be null and the path to this node will be its name, so we return that.
        if (current.getParent() == null)
        {
            return getName();
        }
        // Otherwise we have to climb up to the root node.
        while (current != null)
        {
            int index = 1;
            Tree<V> parent = current.getParent();
            if (parent != null)
            {
                // If we haven't climbed to the root node, we have to look at our siblings to see if any of them have the same name
                for (int i = 0; i < parent.getNumOfChildren(); i++)
                {
                    // If the given node has the same name, we have to see if it's actually us.
                    // If it is, we are done.
                    // If it isn't, add 1 to the counter counting how many nodes with the same name exist.
                    if (parent.getChild(i).getName().equals(current.getName()))
                    {
                        if (parent.getChild(i) == current)
                        {
                            break;
                        }
                        else
                        {
                            index += 1;
                        }
                    }
                }
            }
            // If we aren't the first node at this level with our given name, append the index to the path
            if (index != 1)
            {
                pathElements.add(0, current.getName() + " ##" + index);
            }
            else
            {
                // Just append our name and go on.
                pathElements.add(0, current.getName());
            }
            // Climb to our parent. If we just processed the root node, current will be null next time we hit the while and the loop will end.
            current = current.getParent();
        }
        // Join the parts of the path with " -> " in between them.
        return StringFunctions.join(" -> ", pathElements);
    }
    
    /**
     * Returns the current value of this node
     * 
     * @return The current value of this node.
     */
    public V getValue()
    {
        return value;
    }
    
    /**
     * Sets the name of this node.
     * 
     * @param name
     *            The name to set this node to.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Sets the parent node of this one.
     * 
     * @param parent
     *            The node to assign as the parent of this node.
     */
    public void setParent(Tree<V> parent)
    {
        this.parent = parent;
    }
    
    /**
     * Sets the value of this node.
     * 
     * @param value
     *            The value to set this node to
     */
    public void setValue(V value)
    {
        this.value = value;
    }
    
    /**
     * Returns a nicely indented String representation of this tree, using the names of each node.
     * 
     * @return A nicely indented String representation of this tree, using the names of each node.
     */
    @Override
    public String toString()
    {
        Tree<V> current = this;
        String s = "";
        if (current.getParent() != null)
        {
            while (current != null)
            {
                s += "\t";
                current = current.getParent();
            }
        }
        s += getName() + System.getProperty("line.separator");
        for (Tree<V> n : children)
        {
            s += n.toString();
        }
        return s;
    }
    
    /**
     * Returns a nicely indented String representation of this tree, using the toString() results of each node's value.
     * 
     * @return A nicely indented String representation of this tree, using the toString() results of each node's value.
     */
    public String toContentString()
    {
        Tree<V> current = this;
        String s = "";
        if (current.getParent() != null)
        {
            while (current != null)
            {
                s += "\t";
                current = current.getParent();
            }
        }
        s += StringFunctions.ifNull(getValue()) + System.getProperty("line.separator");
        for (Tree<V> n : children)
        {
            s += n.toContentString();
        }
        return s;
    }
    
    /**
     * Returns a nicely indented String representation of this tree, using the path of each node.
     * 
     * @return A nicely indented String representation of this tree, using the path of each node.
     */
    public String toPathString()
    {
        String s = getPath() + System.getProperty("line.separator");
        for (Tree<V> n : children)
        {
            s += n.toPathString();
        }
        return s;
    }
    
    /**
     * Flattens the tree into a list of nodes.
     * 
     * @return A list of nodes
     */
    public ArrayList<Tree<V>> flatten()
    {
        ArrayList<Tree<V>> flattened = new ArrayList<Tree<V>>();
        flattened.add(this);
        for (Tree<V> child : children)
        {
            flattened.addAll(child.flatten());
        }
        Set<Tree<V>> set = new HashSet<Tree<V>>(flattened);
        flattened.clear();
        flattened.addAll(set);
        return flattened;
    }
}