package com.advancedrft.common.hazelcast;

import com.advancedrft.common.lang.Regex;

/**
 * A generic class that represents a system command that can be sent to all the script execution handler instances running.<br>
 * Each script will compare the pattern in {@link #machinesToApplyTo} with its host name to see if it should execute or ignore this command.<br>
 * Commands are just names, not literal command lines.
 */
public class SystemMessage
{
    private String machinesToApplyTo = ".*";
    
    private String command           = "";
    
    public SystemMessage(String machinesToApplyTo, String command)
    {
        super();
        this.machinesToApplyTo = machinesToApplyTo;
        this.command = command;
    }
    
    public String getCommand()
    {
        return command;
    }
    
    public String getMachinesToApplyTo()
    {
        return machinesToApplyTo;
    }
    
    public void setCommand(String command)
    {
        this.command = command;
    }
    
    public void setMachinesToApplyTo(String machinesToApplyTo)
    {
        this.machinesToApplyTo = machinesToApplyTo;
    }
    
    /**
     * Indicates if this machine should run the command contained in here
     * 
     * @return True if this machine should run the command contained in here, False if not
     */
    public boolean doesCommandApplyHere()
    {
        String host = "127.0.0.1";
        try
        {
            java.net.InetAddress i = java.net.InetAddress.getLocalHost();
            try
            {
                host = i.getHostName();
            }
            catch (Exception ex)
            {
                host = i.getHostAddress();
            }
        }
        catch (Exception e)
        {
        }
        return Regex.isMatch2(host, machinesToApplyTo);
    }
}
