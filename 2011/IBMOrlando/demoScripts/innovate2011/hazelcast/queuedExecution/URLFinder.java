package demoScripts.innovate2011.hazelcast.queuedExecution;

import resources.demoScripts.innovate2011.hazelcast.queuedExecution.URLFinderHelper;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.automation.rft.object.TO;
import com.advancedrft.common.automation.rft.object.TO.Subitems;
import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.common.lang.StringFunctions;
import com.rational.test.ft.ObjectNotFoundException;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Functional Test Script
 * 
 * @author vmuser
 */
public class URLFinder extends URLFinderHelper
{
    /**
     * Script Name : <b>URLScraper</b><br>
     * Generated : <b>May 29, 2011 12:47:46 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 6.1 Build 7601 (S)
     * 
     * @since 2011/05/29
     * @author vmuser
     * @param args
     *            the script arguments
     */
    public void testMain(Object[] args)
    {
        if (args.length == 0)
        {
            Log.logError("No URL supplied.");
        }
        else
        {
            String url = StringFunctions.ifNull(args[0]);
            if (!url.equals(""))
            {
                Log.logInfo("Opening " + args[0].toString());
                Browser b = new Browser(url);
                RationalTestScript.sleep(10);
                Log.logInfo("Opened " + args[0].toString(), b.getScreenshot());
                if (args.length > 1)
                {
                    int length = args.length - 1;
                    if ((args.length - 1) % 2 == 1)
                    {
                        length -= 1;
                    }
                    String[] params = new String[length];
                    for (int i = 1; i < length + 1; i++)
                    {
                        params[i - 1] = StringFunctions.ifNull(args[i]);
                    }
                    TestObject[] links = null;
                    try
                    {
                        links = TO.findObject(10, 1, b.browser, Subitems.atDescendant, "ALL", false, params);
                        for (int i = 0; i < Math.min(links.length, 5); i++)
                        {
                            String foundURL = StringFunctions.ifNull(links[i].getProperty(".href"));
                            HazelcastAPI.<String> getQueue("URLs To Visit").add(foundURL);
                            Log.logInfo("Added \"" + foundURL + "\" to the \"URLs To Visit\" queue.");
                            HazelcastAPI.<String> getList("Master URL List").add(foundURL);
                        }
                    }
                    catch (ObjectNotFoundException e)
                    {
                        
                    }
                    finally
                    {
                        TO.free((Object) links);
                    }
                }
                RationalTestScript.sleep(10);
                b.kill();
            }
            else
            {
                Log.logError("Invalid URL supplied.");
            }
        }
    }
}
