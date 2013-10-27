'''
Created on May 27, 2012

@author: Jarga
'''
import sys
from com.advancedrft.common.automation.rft import Log
from com.advancedrft.ibminnovate.google.finance import MainPage, CompanySummary
from com.advancedrft.ibminnovate.google.finance.MainPage import IndexName, IndexStatistic
from com.advancedrft.common.automation.rft.object import Browser
from com.advancedrft.common.lang import StringFunctions

if __name__ == '__main__':
    if not sys.argv: Log.logError("Must supply a company name.")
    
    # Open a new browser to the appropriate Google Finance URL
    theBrowser = Browser("http://finance.google.com")
    
    # Create a new instance of our Google Finance Main Page class
    mp = MainPage(theBrowser)
    
    # Scrape the current market index chart to the log - We're actually downloading the image file.
    Log.logInfo("Today's market chart", mp.getMarketChart())
    
    # Collect statistics on the indices.
    # In order to limit and control things better, we made the getMarketSummary() function take two enums.
    # Enums are nice because they let you control how (?)
    for n in IndexName.values(): #@UndefinedVariable - The Java enum values() method is only created at runtime
        logMsg = "".join(["<br>" + n.getName() + " today: " + mp.getMarketSummary(n, s) + " " + s.name() for s in IndexStatistic.values()]) #@UndefinedVariable
        Log.logInfo("Market status for " + n.getName() + ":<br>" + logMsg)
    
    # Bring up the page for the ticker symbol for the given argument (which should be a company)
    mp.findCompany(sys.argv[0])
    
    # Create a new instance of our company summary page class
    cs = CompanySummary(theBrowser)
    
    Log.logInfo("Current LPS Share Price: " + str(cs.getCurrentPrice()) + "<br>Change so far today: " + str(cs.getChangeAmount()) + "<br>% change so far today: " + str(cs.getChangePercent()))
    
    # Scrape things like 52 week high/low, p/e, etc
    # Then build a HTML Table for the RFT log so that it looks nice when dumped out.
    marketData = cs.getMarketData()
    
    Log.logInfo("LPS Market Data:<br><table>" + "".join(["<tr><td>" + name + "</td><td>" + marketData.get(name) + "</td></tr>" for name in marketData.keySet()]) + "</table>")
    
    Log.logInfo("News about LPS:<br>" + StringFunctions.HTMLObjectDumper(cs.getNewsLinkTitles(), 0))
      
    Log.logInfo("Beginning validation of news links.")
    cs.validateNewsLinks()
    
    theBrowser.kill()