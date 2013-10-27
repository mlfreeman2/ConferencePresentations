package demoScripts.googleDemo;

import java.util.LinkedHashMap;

import resources.demoScripts.googleDemo.FinanceDemoHelper;

import com.lps.rsdc2010.common.automation.rft.object.Browser;
import com.lps.rsdc2010.common.lang.StringFunctions;
import com.lps.rsdc2010.google.Environments;
import com.lps.rsdc2010.google.finance.CompanySummary;
import com.lps.rsdc2010.google.finance.MainPage;
import com.lps.rsdc2010.google.finance.MainPage.IndexName;
import com.lps.rsdc2010.google.finance.MainPage.IndexStatistic;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Functional Test Script
 * 
 * @author Administrator
 */
public class FinanceDemo extends FinanceDemoHelper
{
    /**
     * Script Name : <b>FinanceDemo</b><br>
     * Generated : <b>May 31, 2010 8:49:20 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 5.1 Build 2600 (S)
     * 
     * @since 2010/05/31
     * @author Administrator
     */
    public void testMain(Object[] args)
    {
        Environments env = Environments.Finance;
        if (args.length < 1)
        {
            logError("Must supply a company name.");
        }
        //
        // Open a new browser to the appropriate Google Finance URL
        Browser theBrowser = new Browser(env.getURL());
        //
        // Create a new instance of our Google Finance Main Page class
        MainPage mp = new MainPage(theBrowser);
        //
        // Scrape the current market index chart to the log - We're actually downloading the image file.
        RationalTestScript.logInfo("Today's market chart", mp.getMarketChart());
        //
        // Collect statistics on the indices.
        // In order to limit and control things better, we made the getMarketSummary() function take two enums.
        // Enums are nice because they let you control how
        for (IndexName n : IndexName.values())
        {
            String logMsg = "";
            for (IndexStatistic s : IndexStatistic.values())
            {
                logMsg += "<br>" + n.name + " today: " + mp.getMarketSummary(n, s) + " " + s.name();
            }
            RationalTestScript.logInfo("Market status for " + n.name + ":<br>" + logMsg);
        }
        //
        // Bring up the page for the ticker symbol "LPS"
        mp.findCompany(args[0].toString());
        //
        // Create a new instance of our company summary page class
        CompanySummary cs = new CompanySummary(theBrowser);
        //
        //
        RationalTestScript.logInfo("Current LPS Share Price: " + cs.getCurrentPrice() + "<br>Change so far today: " + cs.getChangeAmount() + "<br>% change so far today: " + cs.getChangePercent());
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
        RationalTestScript.logInfo(mktData);
        //
        RationalTestScript.logInfo("News about LPS:<br>" + StringFunctions.HTMLObjectDumper(cs.getNewsLinkTitles(), 0));
        //
        logInfo("Beginning validation of news links.");
        cs.validateNewsLinks();
        //
        theBrowser.kill();
    }
}
