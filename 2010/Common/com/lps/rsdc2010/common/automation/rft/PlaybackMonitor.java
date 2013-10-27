package com.lps.rsdc2010.common.automation.rft;

import com.rational.test.ft.services.IPlaybackMonitor;
import com.rational.test.ft.services.PlaybackMonitorPreferences;
import com.rational.test.util.ServiceBroker;

public class PlaybackMonitor
{
    
    /**
     * Controls whether or not the playback monitor window is visible.
     * 
     * @param yes
     *            True to make the little playback monitor window visible, False to hide it.
     */
    public static void setPlaybackMonitorVisible(boolean yes)
    {
        IPlaybackMonitor pbmon = (IPlaybackMonitor) ServiceBroker.getServiceBroker().findService(com.rational.test.ft.services.IPlaybackMonitor.class.getName());
        pbmon.setVisible(yes);
        PlaybackMonitorPreferences.getPlaybackMonitorPreferences().setVisible(yes);
    }
    
    /**
     * Controls whether the playback monitor accepts new input or not.
     * 
     * @param yes
     *            True to lock the playback monitor window so that nothing updates it, False to unlock it so anything can update it.
     */
    public static void lockPlaybackMonitor(boolean yes)
    {
        IPlaybackMonitor pbmon = (IPlaybackMonitor) ServiceBroker.getServiceBroker().findService(com.rational.test.ft.services.IPlaybackMonitor.class.getName());
        pbmon.setDisplayLocked(yes);
    }
    
    /**
     * Indicates if the playback monitor is locked or not.
     * 
     * @return True if the playback monitor is locked, False if it isn't
     */
    public static boolean isPlaybackMonitorLocked()
    {
        IPlaybackMonitor pbmon = (IPlaybackMonitor) ServiceBroker.getServiceBroker().findService(com.rational.test.ft.services.IPlaybackMonitor.class.getName());
        return pbmon.getDisplayLocked();
    }
    
    /**
     * Sets the currently displayed contents of the playback monitor window by unlocking it, changing the fields, and locking it.
     * 
     * @param scriptName
     *            The file name you're currently in
     * @param scriptLine
     *            The line inside the file.
     * @param description
     *            A message to put in the description textbox.
     */
    public static void setPlaybackMonitorContents(String scriptName, int scriptLine, String description)
    {
        IPlaybackMonitor pbmon = (IPlaybackMonitor) ServiceBroker.getServiceBroker().findService(com.rational.test.ft.services.IPlaybackMonitor.class.getName());
        pbmon.setDescription(0, description);
        pbmon.setScriptLine(scriptLine);
        pbmon.setScriptName(scriptName);
    }
    
    /**
     * Brings the playback monitor window to the foreground
     */
    public static void bringPlaybackMonitorForward()
    {
        IPlaybackMonitor pbmon = (IPlaybackMonitor) ServiceBroker.getServiceBroker().findService(com.rational.test.ft.services.IPlaybackMonitor.class.getName());
        pbmon.setVisible(true);
    }
    
    /**
     * This function sleeps for the specified time, while displaying the specified message in the playback monitor window.
     * 
     * @param scriptName
     *            The file name you're currently in
     * @param scriptLine
     *            The line inside the file.
     * @param description
     *            A message to put in the description textbox.
     * @param sleepDuration
     *            How long to sleep for
     */
    public static void sleepWithMessage(String scriptName, int scriptLine, double sleepDuration, String description)
    {
        PlaybackMonitor.lockPlaybackMonitor(false);
        PlaybackMonitor.setPlaybackMonitorContents(scriptName, scriptLine, description);
        PlaybackMonitor.lockPlaybackMonitor(true);
        PlaybackMonitor.bringPlaybackMonitorForward();
        try
        {
            Thread.sleep(Math.round(sleepDuration * 1000));
        }
        catch (InterruptedException e)
        {
            
        }
        PlaybackMonitor.lockPlaybackMonitor(false);
    }
}
