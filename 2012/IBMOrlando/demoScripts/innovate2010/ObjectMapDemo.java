package demoScripts.innovate2010;

import java.io.File;

import resources.demoScripts.innovate2010.ObjectMapDemoHelper;

import com.advancedrft.common.automation.rft.PlaybackMonitor;
import com.advancedrft.common.automation.rft.object.TO;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.ITopWindow;
import com.rational.test.ft.object.interfaces.ProcessTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextGuiTestObject;
import com.rational.test.ft.object.map.ObjectMap;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Functional Test Script
 * 
 * @author Administrator
 */
public class ObjectMapDemo extends ObjectMapDemoHelper
{
    /**
     * Script Name : <b>ObjectMapDemo</b><br>
     * Generated : <b>May 29, 2010 9:22:47 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 5.1 Build 2600 (S)
     * 
     * @since 2010/05/29
     * @author Administrator
     * @param args
     *            The script arguments
     */
    public void testMain(Object[] args)
    {
        // Open a browser to google.com
        ProcessTestObject pto = RationalTestScript.startBrowser("http://www.google.com");
        PlaybackMonitor.bringPlaybackMonitorForward();
        PlaybackMonitor.sleepWithMessage("Object Map Demo", 64, 20, "Waiting for browser to load");
        PlaybackMonitor.bringPlaybackMonitorForward();
        PlaybackMonitor.setPlaybackMonitorContents("", 0, "");
        //
        // Calculate the full path to the object map file
        String objectMapFilePath = RationalTestScript.getCurrentProject().getLocation() + "\\demoScripts\\innovate2010\\OMDemo.rftmap";
        //
        // Load the object map file into memory
        ObjectMap om = ObjectMap.load(new File(objectMapFilePath));
        //
        // Use our function to load the object map object with #name equal to "Google Search Query"
        TestObject[] tos = TO.getMappedObject(om, "Google Search Query");
        TextGuiTestObject gto = new TextGuiTestObject(tos[0]);
        gto.setText("hello world");
        //
        // Use our function to load the object map object with #id equal to "2.TJtAm0FjMHA:1xUoI:MRuxAAt:8WW"
        // This should be the same object as before
        TestObject[] tos2 = TO.getMappedObjectByID(om, "A.TJtAm0FjMHA:1xUoI:MRuxAAt:8WV");
        TextGuiTestObject gto2 = new TextGuiTestObject(tos2[0]);
        gto2.setText("rational functional tester");
        //
        // Hit Esc to get rid of Google suggest, but sleep first because otherwise the Google Search box gets cleared
        RationalTestScript.sleep(2);
        ((ITopWindow) gto2.getTopParent()).inputKeys("{esc}");
        //
        // Use our function to load the object map object with #name equal to "Google Search"
        TestObject[] tos4 = TO.getMappedObject(om, "Google Search");
        GuiTestObject gto4 = new GuiTestObject(tos4[0]);
        gto4.click();
        //
        // Sleep while we wait for the results to load.
        PlaybackMonitor.bringPlaybackMonitorForward();
        PlaybackMonitor.sleepWithMessage("Object Map Demo", 64, 20, "Waiting for search results to load");
        PlaybackMonitor.bringPlaybackMonitorForward();
        PlaybackMonitor.setPlaybackMonitorContents("", 0, "");
        //
        // Use our function to load the object map object with #id equal to "4.TJtAm0FjMHA:1xUoI:MRuxAAt:8WW"
        TestObject[] tos5 = TO.getMappedObjectByID(om, "G.TJtAm0FjMHA:1xUoI:MRuxAAt:8WV");
        GuiTestObject gto5 = new GuiTestObject(tos5[0]);
        RationalTestScript.logInfo("Google search results text: " + gto5.getProperty(".text").toString());
        pto.kill();
    }
}
