package demoScripts.innovate2011.hazelcast.queuedExecution;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import resources.demoScripts.innovate2011.hazelcast.queuedExecution.URLScreenGrabberHelper;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.common.lang.StringFunctions;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Functional Test Script
 * 
 * @author vmuser
 */
public class URLScreenGrabber extends URLScreenGrabberHelper
{
    /**
     * Script Name : <b>URLScreenGrabber</b> Generated : <b>May 29, 2011 1:21:25 PM</b> Description : Functional Test Script Original Host : WinNT Version 6.1 Build 7601 (S)
     * 
     * @since 2011/05/29
     * @author vmuser
     * @param args
     *            the script arguments
     */
    public void testMain(Object[] args)
    {
        HazelcastAPI.start(RationalTestScript.getCurrentProject().getLocation() + File.separator + "rftConfigs\\automation.xml");
        BlockingQueue<String> urlq = HazelcastAPI.getQueue("URLs To Visit");
        Map<String, byte[]> screenshotMap = HazelcastAPI.getMap("URL Screenshots");
        if (urlq.isEmpty())
        {
            Log.logWarning("No URL supplied to open a page and take a screenshot.");
        }
        else
        {
            while (!urlq.isEmpty())
            {
                try
                {
                    String url = StringFunctions.ifNull(urlq.poll(1, TimeUnit.SECONDS));
                    if (!url.equals(""))
                    {
                        Log.logInfo("Opening " + url);
                        Browser b = new Browser(url);
                        RationalTestScript.sleep(10);
                        BufferedImage bi = b.getScreenshot();
                        Log.logInfo(url, bi);
                        //
                        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
                        try
                        {
                            ImageIO.write(bi, "jpeg", baos);
                            //
                            baos.flush();
                            byte[] resultImageAsRawBytes = baos.toByteArray();
                            //
                            baos.close();
                            screenshotMap.put(url, resultImageAsRawBytes);
                        }
                        catch (IOException e)
                        {
                            
                        }
                        RationalTestScript.sleep(10);
                        b.kill();
                    }
                    else
                    {
                        Log.logError("Invalid URL supplied.");
                        screenshotMap.put(url, null);
                    }
                }
                catch (InterruptedException e)
                {
                    break;
                }
            }
        }
    }
}
