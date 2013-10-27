package com.advancedrft.common.hazelcast;

import java.io.Serializable;

/**
 * A generic data class describing a script to be run by the Hazelcast runner framework
 */
public class ScriptExecutionRequest implements Serializable
{
    
    private static final long serialVersionUID = -4269969182916821601L;
    
    private String[]          args;
    
    private String            className;
    
    private String            name;
    
    public ScriptExecutionRequest(String name, String className, String...args)
    {
        this.className = className;
        this.args = args;
        this.name = name;
    }
    
    public String[] getArgs()
    {
        return args;
    }
    
    public String getClassName()
    {
        return className;
    }
    
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets the arguments to be fed to the script
     * 
     * @param args
     *            The arguments to be fed to the script
     */
    public void setArgs(String...args)
    {
        if (args == null || args.length == 0)
        {
            return;
        }
        this.args = args;
    }
    
    public void setClassName(String className)
    {
        this.className = className == null ? "" : className;
    }
    
    public void setName(String name)
    {
        this.name = name == null ? "" : name;
    }
}
