package demoScripts.innovate2011.hazelcast.queuedExecution;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import resources.demoScripts.innovate2011.hazelcast.queuedExecution.ResultsToLogHelper;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.common.lang.StringFunctions;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Functional Test Script
 * 
 * @author vmuser
 */
public class ResultsToLog extends ResultsToLogHelper
{
    /**
     * Script Name : <b>ResultsToLog</b><br>
     * Generated : <b>May 30, 2011 12:23:53 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 6.1 Build 7601 (S)
     * 
     * @since 2011/05/30
     * @author vmuser
     * @param args
     *            The script arguments
     */
    public void testMain(Object[] args)
    {
        HazelcastAPI.start(RationalTestScript.getCurrentProject().getLocation() + File.separator + "rftConfigs\\automation.xml");
        //
        // Finally we reach out to Hazelcast and collect results and write them to the log
        Map<String, byte[]> urlSnapshots = HazelcastAPI.<String, byte[]> getMap("URL Screenshots");
        Set<String> urls = urlSnapshots.keySet();
        int c = 0;
        for (String s : urls)
        {
            BufferedImage bi = null;
            try
            {
                bi = ImageIO.read(new ByteArrayInputStream(urlSnapshots.get(s)));
            }
            catch (IOException e)
            {
            }
            if (bi != null)
            {
                Log.logTestResult("URL #" + c + "\n\"" + s + "\"\n looked like this.", bi, true);
            }
            else
            {
                Log.logError("Unable to process \"" + s + "\".");
            }
            c++;
        }
        List<String> masterURLlist = HazelcastAPI.<String> getList("Master URL List");
        if (masterURLlist.size() != c)
        {
            Log.logError("Some URLs did not get screenshots - " + c + " screenshots found vs " + masterURLlist.size() + " urls found.");
        }
        else
        {
            Log.logInfo(c + " screenshots found vs " + masterURLlist.size() + " urls found.");
        }
        Log.logInfo("Master URL List", StringFunctions.HTMLObjectDumper(masterURLlist, 0));
        Log.logInfo("Screenshot URLs", StringFunctions.HTMLObjectDumper(urls, 0));
    }
}
