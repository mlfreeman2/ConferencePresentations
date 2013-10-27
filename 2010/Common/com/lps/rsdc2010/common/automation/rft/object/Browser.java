package com.lps.rsdc2010.common.automation.rft.object;

import java.awt.image.BufferedImage;

import com.lps.rsdc2010.common.automation.rft.object.TO.Subitems;
import com.lps.rsdc2010.common.io.Console;
import com.lps.rsdc2010.common.lang.Regex;
import com.lps.rsdc2010.common.lang.StringFunctions;
import com.rational.test.ft.ObjectNotFoundException;
import com.rational.test.ft.object.interfaces.BrowserTestObject;
import com.rational.test.ft.object.interfaces.DocumentTestObject;
import com.rational.test.ft.object.interfaces.DomainTestObject;
import com.rational.test.ft.object.interfaces.ProcessTestObject;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;
import com.rational.test.ft.sys.graphical.TopLevelWindow;

/**
 * A basic wrapper around BrowserTestObject. Currently tested with IE6 and IE7 and somewhat with IE8.
 */
public class Browser
{
    /**
     * The number of times to check .readyState to see if it's complete / interactive yet
     */
    private int              sync_tries                 = 30;
    /**
     * How long to wait between .readyState polling attempts
     */
    private double           sync_sleepBtwnTries        = 3;
    /**
     * The BrowserTestObject this class wraps around
     */
    public BrowserTestObject browser                    = null;
    /**
     * True to allow the .readyState value of "interactive" to be treated as equal to "complete".<br />
     * Some ASP.NET apps have frames that never manage to escape the state of "interactive", so sync() would hang forever on those pages
     */
    public boolean           allowInteractiveAsComplete = false;
    
    /**
     * Basic constructor from a BrowserTestObject
     * 
     * @param b
     *            The BrowserTestObject that you want to use inside this Browser object
     */
    public Browser(BrowserTestObject b)
    {
        browser = b;
    }
    
    /**
     * Basic constructor from a TestObject
     * 
     * @param b
     *            The TestObject that you want to convert to a BrowserTestObject and use inside this Browser object
     */
    public Browser(TestObject b)
    {
        browser = new BrowserTestObject(b);
    }
    
    /**
     * A constructor that will attempt to find a browser with the specified properties and set it as the BrowserTestObject for this instance of Browser
     * 
     * @param recognitionProp
     *            Properties of the BrowserTestObject to search for
     */
    public Browser(String...recognitionProp)
    {
        TestObject foundTO = TO.findOneObject(2, 1, RootTestObject.getRootTestObject(), false, recognitionProp);
        browser = new BrowserTestObject(foundTO);
    }
    
    /**
     * A constructor that opens a new browser to the specified URL and waits 2 minutes for the page to initially load
     * 
     * @param url
     *            The URL to open a browser to
     */
    public Browser(String url)
    {
        this(url, 120);
    }
    
    /**
     * A constructor that opens a new browser to the specified URL and waits a user specified number of seconds for the page to initially load
     * 
     * @param url
     *            The URL to open a browser to
     * @param delay
     *            How long in seconds to wait for the page to load
     */
    public Browser(String url, int delay)
    {
        try
        {
            launch(url, delay);
            this.sync(delay, 1);
        }
        catch (Exception e)
        {
            RationalTestScript.logError("Error while launching browser.");
            RationalTestScript.logException(e);
        }
    }
    
    /**
     * Returns "[title]: [url]" as the string representation of this browser
     */
    @Override
    public String toString()
    {
        return StringFunctions.ifNull(getTitle()) + ": " + StringFunctions.ifNull(getURL());
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (allowInteractiveAsComplete ? 1231 : 1237);
        result = prime * result + (browser == null ? 0 : browser.hashCode());
        long temp;
        temp = Double.doubleToLongBits(sync_sleepBtwnTries);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + sync_tries;
        result = prime * result + 1237;
        return result;
    }
    
    /**
     * Compares two browsers based on their window handles (".window" property)
     */
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
        Browser other = (Browser) obj;
        try
        {
            return browser.getProperty(".window").equals(other.browser.getProperty(".window"));
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * Opens a new browser to the specified URL and waits a user specified number of seconds for the page to initially load
     * 
     * @param url
     *            The URL to open a browser to
     * @param secondsToWait
     *            How long in seconds to wait for the page to load
     * @return True if the browser launched, False if not
     */
    public boolean launch(String url, int secondsToWait)
    {
        ProcessTestObject browserProcess = RationalTestScript.startBrowser(url);
        RationalTestScript.sleep(2);
        long startTime = System.currentTimeMillis();
        boolean found = false;
        // We ID the browser by finding a Html DomainTestObject with the appropriate process ID.
        // On initial launch there should only be one of those. Later on, there may be multiple ones (one per window / tab / etc)
        while (!found && System.currentTimeMillis() < startTime + secondsToWait * 1000)
        {
            for (DomainTestObject dto : RootTestObject.getRootTestObject().getDomains())
            {
                if (dto.getProcess().getProcessId() == browserProcess.getProcessId() && dto.getName().equals("Html") && dto.getTopObjects().length > 0)
                {
                    browser = new BrowserTestObject(dto.getTopObjects()[0]);
                    found = true;
                }
            }
        }
        if (found)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Polls .readyState {@link #sync_tries} times and waits {@link #sync_sleepBtwnTries} seconds between each one.<br />
     * Waits for .readyState to be either "complete" or "interactive" (if allowInteractiveAsComplete is set to true)
     */
    public void sync()
    {
        this.sync(getSync_tries(), getSync_sleepBtwnTries());
    }
    
    /**
     * Polls .readyState a user specified number of times and waits a user specified number of seconds between each one.<br />
     * Waits for .readyState to be either "complete" or "interactive" (if allowInteractiveAsComplete is set to true)
     * 
     * @param tries
     *            The number of times to check .readyState to see if it's complete / interactive yet
     * @param sleepBtwnTries
     *            How long to wait between .readyState polling attempts
     */
    public void sync(int tries, double sleepBtwnTries)
    {
        int attempt = 0;
        TestObject[] documents = null;
        while (attempt < tries)
        {
            try
            {
                boolean allDone = true;
                documents = TO.findObject(10, .5, browser, Subitems.atDescendant, "ALL", false, ".class", "Html.HtmlDocument");
                for (int i = 0; i < documents.length; i++)
                {
                    try
                    {
                        String readyState = StringFunctions.ifNull(documents[i].getProperty("readyState"));
                        if (readyState.equalsIgnoreCase("complete") || allowInteractiveAsComplete == true && readyState.equalsIgnoreCase("interactive"))
                        {
                            continue;
                        }
                        allDone = false;
                        break;
                    }
                    catch (Exception e)
                    {
                        allDone = false;
                        break;
                    }
                }
                RationalTestScript.unregister(documents);
                documents = null;
                if (allDone == true)
                {
                    break;
                }
                else
                {
                    RationalTestScript.sleep(sleepBtwnTries);
                    attempt++;
                }
            }
            catch (ObjectNotFoundException e)
            {
                RationalTestScript.sleep(sleepBtwnTries);
                attempt++;
            }
            catch (Exception e)
            {
                RationalTestScript.logException(e);
                RationalTestScript.sleep(sleepBtwnTries);
                attempt++;
            }
        }
        if (attempt >= tries)
        {
            RationalTestScript.logInfo("Browser.sync() exceeded timeout", RootTestObject.getRootTestObject().getScreenSnapshot());
        }
    }
    
    /**
     * Attempts to close and unregister this browser window.
     */
    public void close()
    {
        try
        {
            browser.close();
            browser.unregister();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Kills the browser process
     */
    public void kill()
    {
        try
        {
            browser.getProcess().kill();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Checks to see if the browser is enabled or not. It might not be if some sort of dialog is blocking it.
     * 
     * @return True if the browser is enabled, False if it's disabled (e.g. because a dialog is blocking it)
     */
    public boolean isActive()
    {
        return new TopLevelWindow(Long.parseLong(browser.getProperty(".window").toString())).isEnabled();
    }
    
    /**
     * Attempts to bring the browser to the foreground
     */
    public void activate()
    {
        try
        {
            browser.activate();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Brings forward and maximizes the browser window.
     */
    public void maximize()
    {
        try
        {
            activate();
            browser.maximize();
            this.sync();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Brings forward and then minimizes the browser window.
     */
    public void minimize()
    {
        try
        {
            activate();
            browser.minimize();
            this.sync();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Brings forward and sends F5 to the browser window to refresh the page
     */
    public void refresh()
    {
        try
        {
            activate();
            inputKeys("{F5}");
            this.sync();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Browser back button
     */
    public void back()
    {
        try
        {
            activate();
            browser.back();
            this.sync();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Browser foward button
     */
    public void forward()
    {
        try
        {
            activate();
            browser.forward();
            this.sync();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Browser home button
     */
    public void home()
    {
        try
        {
            activate();
            browser.home();
            this.sync();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Equivalent to typing an address into the address bar and hitting enter / clicking Go to load the page
     * 
     * @param URL
     *            The URL to navigate to.
     */
    public void loadURL(String URL)
    {
        try
        {
            activate();
            browser.loadUrl(URL);
            this.sync();
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * This function presses Up, Down, Left, or Right a defined number of times and sleeps 0.3 seconds between keypresses.
     * 
     * @param direction
     *            "Up" (or "U"), "Down" (or "D"), "Left" (or "L"), or "Right" (or "R") [to determine which way to scroll]
     * @param numOfTimes
     *            Number of times to press the appropriate arrow key
     */
    public void scroll(String direction, int numOfTimes)
    {
        activate();
        direction = direction.toUpperCase();
        for (int i = 0; i < numOfTimes; i++)
        {
            if (direction.startsWith("L"))
            {
                inputKeys("{ExtLeft}");
            }
            else if (direction.startsWith("R"))
            {
                inputKeys("{ExtRight}");
            }
            else if (direction.startsWith("U"))
            {
                inputKeys("{ExtUp}");
            }
            else if (direction.startsWith("D"))
            {
                inputKeys("{ExtDown}");
            }
            RationalTestScript.sleep(0.3);
        }
    }
    
    /**
     * Sends raw keystrokes to the browser
     * 
     * @param keystrokes
     *            Keystrokes to send
     */
    public void inputKeys(String keystrokes)
    {
        browser.inputKeys(keystrokes);
    }
    
    /**
     * Sends raw characters to the browser
     * 
     * @param keystrokes
     *            Characters to send
     */
    public void inputChars(String keystrokes)
    {
        browser.inputChars(keystrokes);
    }
    
    /**
     * @return A screenshot of just the browser window.
     */
    public BufferedImage getScreenshot()
    {
        return Console.getScreenshot(browser.getScreenRectangle());
    }
    
    /**
     * Returns the process ID of the browser's process
     * 
     * @return The process ID of the browser's process
     */
    public long getProcessID()
    {
        return browser.getProcess().getProcessId();
    }
    
    /**
     * Returns the DocumentTestObject immediately under the browser (the current page in other words)
     * 
     * @return The current page the browser is on.
     */
    public DocumentTestObject getMainDocument()
    {
        return new DocumentTestObject(TO.findOneChildObject(1, 1, browser, false, ".class", "Html.HtmlDocument"));
    }
    
    /**
     * Returns the URL the browser is currently on
     * 
     * @return The URL the browser is currently on.
     */
    public String getURL()
    {
        return StringFunctions.ifNull(browser.getProperty(".documentName"));
    }
    
    /**
     * Returns the title of the browser window, minus any "Windows / Microsoft Internet Explorer" stuff. This may not always be the title of the main document
     * 
     * @return The title of the browser window.
     */
    public String getTitle()
    {
        // cast the browser into a top level window
        TopLevelWindow w = new TopLevelWindow(Long.parseLong(browser.getProperty(".window").toString()));
        // get the title bar
        String title = StringFunctions.ifNull(w.getWindowCaption());
        
        // We have to get the position of the beginning of the browser name in order to ensure we remove any branding after it too.
        // All versions of IE can be branded with a title similar to "Windows Internet Explorer provided by LPS"
        // Naturally we want to remove all of that
        
        // IE6-specific captions
        if (title.indexOf(" -- Web Page Dialog") != -1)
        {
            return title.substring(0, title.indexOf(" -- Web Page Dialog"));
        }
        if (title.indexOf(" - Microsoft Internet Explorer") != -1)
        {
            return title.substring(0, title.indexOf(" - Microsoft Internet Explorer"));
        }
        
        // IE7-specific captions
        if (title.indexOf(" -- Webpage Dialog") != -1)
        {
            return title.substring(0, title.indexOf(" -- Webpage Dialog"));
        }
        if (title.indexOf(" - Windows Internet Explorer") != -1)
        {
            return title.substring(0, title.indexOf(" - Windows Internet Explorer"));
        }
        return title;
    }
    
    /**
     * Executes a snippet of JavaScript inside the context of the main page (as opposed to inside the context of a frame)
     * 
     * @param javascriptSnippet
     *            The snippet of Javascript to run.
     */
    public void executeJavascript(String javascriptSnippet)
    {
        try
        {
            getMainDocument().invokeScript(javascriptSnippet);
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * Checks if page is displayed. <br>
     * Page is considered displayed if "Internet Explorer cannot display the webpage" is not found in browser title
     * 
     * @return true or false
     */
    public boolean isPageDisplayed()
    {
        boolean result = !Regex.isMatch(getTitle(), "contains=Internet Explorer cannot display the webpage");
        if (result)
        {
            result = !Regex.isMatch(browser.getProperty(".documentName").toString(), "contains=dnserror");
        }
        return result;
    }
    
    // ==========Browser Options==========//
    /**
     * @return The number of times to check .readyState to see if it's complete / interactive yet
     */
    public int getSync_tries()
    {
        return sync_tries;
    }
    
    /**
     * Sets the number of times to check .readyState to see if it's complete / interactive yet
     * 
     * @param sync_tries
     *            The number of times to check .readyState to see if it's complete / interactive yet
     */
    public void setSync_tries(int sync_tries)
    {
        this.sync_tries = sync_tries;
    }
    
    /**
     * @return How long to wait between .readyState polling attempts, in seconds.
     */
    public double getSync_sleepBtwnTries()
    {
        return sync_sleepBtwnTries;
    }
    
    /**
     * Sets how long to wait between .readyState polling attempts, in seconds.
     * 
     * @param sync_sleepBtwnTries
     *            How long to wait between .readyState polling attempts, in seconds.
     */
    public void setSync_sleepBtwnTries(double sync_sleepBtwnTries)
    {
        this.sync_sleepBtwnTries = sync_sleepBtwnTries;
    }
}
