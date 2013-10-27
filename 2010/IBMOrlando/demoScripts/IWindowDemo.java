package demoScripts;

import java.awt.Point;
import java.io.IOException;
import java.util.Vector;

import resources.demoScripts.IWindowDemoHelper;

import com.lps.rsdc2010.common.automation.rft.KeyboardAndMouse;
import com.lps.rsdc2010.common.automation.rft.KeyboardAndMouse.MouseButtons;
import com.lps.rsdc2010.common.automation.rft.object.Browser;
import com.lps.rsdc2010.common.automation.rft.object.IW;
import com.lps.rsdc2010.common.io.File;
import com.lps.rsdc2010.google.Environments;
import com.rational.test.ft.object.interfaces.IWindow;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Functional Test Script
 * 
 * @author Administrator
 */
public class IWindowDemo extends IWindowDemoHelper
{
    /**
     * Script Name : <b>IWindowDemoOne</b><br>
     * Generated : <b>May 23, 2010 1:24:24 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 5.1 Build 2600 (S)
     * 
     * @since 2010/05/23
     * @author Administrator
     */
    public void testMain(Object[] args)
    {
        Browser b = new Browser(Environments.News.getURL());
        b.maximize();
        b.inputKeys("^p");
        sleep(3);
        //
        // We should be able to find the Print dialog as an IWindow object, using my IW.findIWindowFromRoot()
        // We know that the title is "Print" and the window class is "#32770" and we know what process it will be in because it's in the IE7 process.
        // We don't really *need* the process unless for some reason two print dialogs would be open.
        RationalTestScript.timerStart("IW.findIWindowFromRoot(2, 1, \"#32770\", \"contains=Print\", Long.toString(b.getProcessID()), null) print dialog.");
        Vector<IWindow> windows = IW.findIWindowFromRoot(2, 1, "windowClassName", "#32770", "text", "contains=Print", "processID", Long.toString(b.getProcessID()));
        RationalTestScript.timerStop("IW.findIWindowFromRoot(2, 1, \"#32770\", \"contains=Print\", Long.toString(b.getProcessID()), null) print dialog.");
        logTestResult("Did the printer dialog open and appear to IWindow find()?", windows.size() > 0);
        //
        // We're going to dump a snapshot of all the IWindow objects on screen now
        File f = new File("c:\\iWindowChildDump.txt");
        String iWindowTree = IW.dumpIWChildTree().toContentString();
        f.saveTextToFile(iWindowTree);
        File f1 = new File("c:\\iWindowOwnedDump.txt");
        String iWindowTree1 = IW.dumpIWOwnedTree().toContentString();
        f1.saveTextToFile(iWindowTree1);
        //
        // Find the "Pages" radio button and click on it...the "All" radio button is set by default, but we want to change that
        RationalTestScript.timerStart("IWindow find Pages radio button");
        // There is a & in the word "Pages" to make the g underlined in the dialog. We have to include that...but it shows up in the IWindow tree dump
        Vector<IWindow> pagesButton = IW.findIWindow(2, 1, windows.get(0), "windowClassName", "Button", "text", "Pa&ges:");
        RationalTestScript.timerStop("IWindow find Pages radio button");
        if (pagesButton.size() > 0)
        {
            // Highlight where it is on screen
            KeyboardAndMouse.highlight(pagesButton.get(0).getScreenRectangle(), 3);
            // Unfortunately sometimes you can't just do IWindow.click(). You get an UnableToPerformActionException.
            // So we'll do the next best thing....look up the object's screen coordinates and do a raw screen coordinate click.
            // This isn't great, but it should be stable since you're looking up screen coordinates at runtime as opposed to hard coding them in permanently
            // We will do an activate() on the print dialog to ensure the window is at the top before clicking
            IW.activate(windows.get(0));
            Point p = pagesButton.get(0).getScreenPoint(new Point(1, 1));
            KeyboardAndMouse.click(p.x, p.y, 1, MouseButtons.Left, false, false, false, true);
            // Now that we have clicked on the Pages radio button the focus is on the textbox where you get to type in page numbers.
            // So we'll send a few keystrokes to the print dialog.
            // Unfortunately, inputKeys only works with top level IWindow objects.
            windows.get(0).inputKeys("1-3,5,99");
        }
        logTestResult("Did the Pages radio button on the printer dialog get located by IWindow find()?", pagesButton.size() > 0);
        //
        //
        // Now we'll find the Cancel button under the Print dialog and click it.
        RationalTestScript.timerStart("IWindow find Cancel button");
        Vector<IWindow> cancelButton = IW.findIWindow(2, 1, windows.get(0), "windowClassName", "Button", "text", "Cancel");
        RationalTestScript.timerStop("IWindow find Cancel button");
        if (cancelButton.size() > 0)
        {
            KeyboardAndMouse.highlight(cancelButton.get(0).getScreenRectangle(), 3);
            cancelButton.get(0).click();
        }
        logTestResult("Did the Cancel button on the printer dialog get located by IWindow find()?", cancelButton.size() > 0);
        b.kill();
        //
        // We'll cause the IWindow dump to open in Notepad so we can study it.
        try
        {
            java.awt.Desktop.getDesktop().edit(f);
            java.awt.Desktop.getDesktop().edit(f1);
        }
        catch (IOException e)
        {
            logException(e);
        }
    }
}
