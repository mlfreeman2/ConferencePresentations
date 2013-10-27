package demoScripts;

import resources.demoScripts.PlaybackMonitorDemoHelper;

import com.lps.rsdc2010.common.automation.rft.PlaybackMonitor;

/**
 * Description : A basic script to demonstrate control over the main Playback Monitor window
 * 
 * @author mlfreeman
 */
public class PlaybackMonitorDemo extends PlaybackMonitorDemoHelper
{
    /**
     * Script Name : <b>PlaybackMonitorDemo</b><br>
     * Generated : <b>May 30, 2010 10:09:09 AM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 5.1 Build 2600 (S)
     * 
     * @since 2010/05/30
     * @author mlfreeman
     */
    public void testMain(Object[] args)
    {
        // First thing we do is set the playback monitor's contents
        PlaybackMonitor.setPlaybackMonitorContents("RSDC", 2010, "This is a playback monitor message set by the driver script.");
        // Then we lock it to ensure nothing else will set the
        PlaybackMonitor.lockPlaybackMonitor(true);
        PlaybackMonitor.bringPlaybackMonitorForward();
        sleep(30);
        PlaybackMonitor.lockPlaybackMonitor(false);
        PlaybackMonitor.setPlaybackMonitorContents("", 0, "");
        sleep(10);
        // Also you can create a brand new Playback Monitor by simply creating a new instance of this class below:
        // com.rational.test.ft.services.PlaybackMonitor pm = new com.rational.test.ft.services.PlaybackMonitor();
        // sleep(20);
    }
}
