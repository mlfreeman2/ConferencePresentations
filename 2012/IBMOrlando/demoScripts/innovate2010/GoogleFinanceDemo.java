package demoScripts.innovate2010;

import java.util.LinkedHashMap;

import resources.demoScripts.innovate2010.GoogleFinanceDemoHelper;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.lang.StringFunctions;
import com.advancedrft.ibminnovate.google.finance.CompanySummary;
import com.advancedrft.ibminnovate.google.finance.MainPage;
import com.advancedrft.ibminnovate.google.finance.MainPage.IndexName;
import com.advancedrft.ibminnovate.google.finance.MainPage.IndexStatistic;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Functional Test Script
 * 
 * @author Administrator
 */
public class GoogleFinanceDemo extends GoogleFinanceDemoHelper
{
    /**
     * Script Name : <b>FinanceDemo</b><br>
     * Generated : <b>May 31, 2010 8:49:20 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 5.1 Build 2600 (S)
     * 
     * @since 2010/05/31
     * @author Administrator
     * @param args
     *            The script arguments
     */
    public void testMain(Object[] args)
    {
        if (args.length < 1)
        {
            RationalTestScript.logError("Must supply a company name.");
        }
        //
        // Open a new browser to the appropriate Google Finance URL
        Browser theBrowser = new Browser("http://finance.google.com");
        //
        // Create a new instance of our Google Finance Main Page class
        MainPage mp = new MainPage(theBrowser);
        //
        // Scrape the current market index chart to the log - We're actually downloading the image file.
        Log.logInfo("Today's market chart", mp.getMarketChart());
        //
        // Collect statistics on the indices.
        // In order to limit and control things better, we made the getMarketSummary() function take two enums.
        // Enums are nice because they let you control how
        for (IndexName n : IndexName.values())
        {
            String logMsg = "";
            for (IndexStatistic s : IndexStatistic.values())
            {
                logMsg += "<br>" + n.getName() + " today: " + mp.getMarketSummary(n, s) + " " + s.name();
            }
            Log.logInfo("Market status for " + n.getName() + ":<br>" + logMsg);
        }
        //
        // Bring up the page for the ticker symbol "LPS"
        mp.findCompany(args[0].toString());
        //
        // Create a new instance of our company summary page class
        CompanySummary cs = new CompanySummary(theBrowser);
        //
        //
        Log.logInfo("Current LPS Share Price: " + cs.getCurrentPrice() + "<br>Change so far today: " + cs.getChangeAmount() + "<br>% change so far today: " + cs.getChangePercent());
        //
        // Scrape things like 52 week high/low, p/e, etc
        // Then build a HTML Table for the RFT log so that it looks nice when dumped out.
        LinkedHashMap<String, String> marketData = cs.getMarketData();
        String mktData = "LPS Market Data:<br><table>";
        for (String name : marketData.keySet())
        {
            mktData += "<tr><td>" + name + "</td><td>" + marketData.get(name) + "</td></tr>";
        }
        mktData += "</table>";
        Log.logInfo(mktData);
        //
        Log.logInfo("News about LPS:<br>" + StringFunctions.HTMLObjectDumper(cs.getNewsLinkTitles(), 0));
        //
        RationalTestScript.logInfo("Beginning validation of news links.");
        cs.validateNewsLinks();
        //
        theBrowser.kill();
    }
}
