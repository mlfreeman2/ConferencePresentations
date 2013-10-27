package demoScripts.innovate2011.hazelcast.queuedExecution;

import java.io.File;
import java.util.concurrent.BlockingQueue;

import resources.demoScripts.innovate2011.hazelcast.queuedExecution.DemoQueueLoaderHelper;

import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.common.hazelcast.ScriptExecutionRequest;
import com.rational.test.ft.script.RationalTestScript;

/**
 * In this demo we will access a Hazelcast queue and fill it with a series of {@link ScriptExecutionRequest} objects.<br>
 * Another driver script will run each one of the scripts specified in each {@link ScriptExecutionRequest}
 */
public class DemoQueueLoader extends DemoQueueLoaderHelper
{
    /**
     * Script Name : <b>DemoQueueLoader</b><br>
     * Generated : <b>May 8, 2011 3:18:23 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 6.1 Build 7601 (S)
     * 
     * @since 2011/05/08
     * @author vmuser
     * @param args
     *            The script arguments
     */
    public void testMain(Object[] args)
    {
        HazelcastAPI.start(RationalTestScript.getCurrentProject().getLocation() + File.separator + "rftConfigs\\automation.xml");
        //
        BlockingQueue<ScriptExecutionRequest> scriptQueue = HazelcastAPI.<ScriptExecutionRequest> getQueue("ScriptQueue");
        //
        // In this demo we'll run two scripts
        // The first one will be run 5 times with different arguments
        // The second one will be run 4 times
        scriptQueue.add(new ScriptExecutionRequest("Create Amazon Account", "demoScripts.innovate2011.amazon.registration.CreateAccount"));
        scriptQueue.add(new ScriptExecutionRequest("Scraper - Google News", "demoScripts.innovate2011.hazelcast.queuedExecution.URLFinder", "http://news.google.com", ".class", "Html.A", ".className", "contains=article"));
        scriptQueue.add(new ScriptExecutionRequest("Scraper - Google Finance", "demoScripts.innovate2011.hazelcast.queuedExecution.URLFinder", "http://www.google.com/finance", ".class", "Html.A", ".className", "contains=title"));
        scriptQueue.add(new ScriptExecutionRequest("Scraper - CNN", "demoScripts.innovate2011.hazelcast.queuedExecution.URLFinder", "http://www.cnn.com", ".class", "Html.A"));
        scriptQueue.add(new ScriptExecutionRequest("Scraper - Google News", "demoScripts.innovate2011.hazelcast.queuedExecution.URLFinder", "http://news.google.com", ".class", "Html.A", ".className", "contains=article"));
        scriptQueue.add(new ScriptExecutionRequest("Add Items To Wishlist", "demoScripts.innovate2011.amazon.wishlist.AddItems"));
        scriptQueue.add(new ScriptExecutionRequest("Scraper - Slashdot", "demoScripts.innovate2011.hazelcast.queuedExecution.URLFinder", "http://www.slashdot.org", ".class", "Html.A", ".className", "read-more"));
        scriptQueue.add(new ScriptExecutionRequest("Scraper - Ars Technica", "demoScripts.innovate2011.hazelcast.queuedExecution.URLFinder", "http://www.arstechnica.com", ".class", "Html.A", ".className", "article-title"));
        scriptQueue.add(new ScriptExecutionRequest("ScreenGrabber 1", "demoScripts.innovate2011.hazelcast.queuedExecution.URLScreenGrabber"));
        scriptQueue.add(new ScriptExecutionRequest("ScreenGrabber 2", "demoScripts.innovate2011.hazelcast.queuedExecution.URLScreenGrabber"));
        scriptQueue.add(new ScriptExecutionRequest("ScreenGrabber 3", "demoScripts.innovate2011.hazelcast.queuedExecution.URLScreenGrabber"));
        scriptQueue.add(new ScriptExecutionRequest("ScreenGrabber 4", "demoScripts.innovate2011.hazelcast.queuedExecution.URLScreenGrabber"));
        //
        HazelcastAPI.safeShutdown();
    }
}
