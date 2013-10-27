package com.lps.rsdc2010.google.finance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.lps.rsdc2010.common.automation.rft.KeyboardAndMouse.MouseButtons;
import com.lps.rsdc2010.common.automation.rft.object.Browser;
import com.lps.rsdc2010.common.automation.rft.object.TO;
import com.lps.rsdc2010.common.automation.rft.object.TO.Subitems;
import com.lps.rsdc2010.common.io.Console;
import com.lps.rsdc2010.common.lang.StringFunctions;
import com.lps.rsdc2010.google.Finance;
import com.rational.test.ft.ObjectNotFoundException;
import com.rational.test.ft.UnableToPerformActionException;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;

/**
 * This class represents the screen you see when you go to Google Finance and search for a specific company.
 */
public class CompanySummary extends Finance
{
    /**
     * This constructor attempts to find a browser open to Google Finance
     */
    public CompanySummary()
    {
        super("Google Finance Company Summary");
        addItems();
        try
        {
            theBrowser = new Browser(TO.findOneObject(3, 3, RootTestObject.getRootTestObject(), false, ".class", "Html.HtmlBrowser", ".documentName", "contains=www.google.com/finance\\?q="));
        }
        catch (Exception e)
        {
            RationalTestScript.logException(e);
        }
    }
    
    /**
     * This constructor creates a new instance of this class and points it at the supplied browser.<br>
     * No checking is done to make sure you're actually at Google Finance...if you wanted to check that you'd do it here
     * 
     * @param theBrowser
     *            Browser you want to manipulate
     */
    public CompanySummary(Browser theBrowser)
    {
        super("Google Finance Company Summary", theBrowser);
        addItems();
    }
    
    private void addItems()
    {
        addItemToTable("Company -> News", ".class", "Html.A", ".id", "nav-cn");
        addItemToTable("Company -> Related companies", ".class", "Html.A", ".id", "nav-cr");
        addItemToTable("Company -> Historical prices", ".class", "Html.A", ".id", "nav-ch");
        addItemToTable("Company -> Financials", ".class", "Html.A", ".id", "nav-cf");
        addItemToTable("Current Price", ".class", "Html.SPAN", ".id", "regex=ref_\\d+_l");
        addItemToTable("Price Change", ".class", "Html.SPAN", ".id", "regex=ref_\\d+_c");
        addItemToTable("Price Change Percent", ".class", "Html.SPAN", ".id", "regex=ref_\\d+_cp");
        addItemToTable("Market Data", ".class", "Html.OL", ".id", "snap-data");
        addItemToTable("Market Data Key", ".class", "Html.SPAN", ".className", "goog-inline-block key");
        addItemToTable("Market Data Value", ".class", "Html.SPAN", ".className", "goog-inline-block val");
        addItemToTable("Officers and Directors", ".class", "Html.TABLE", ".id", "mgmt-table");
        addItemToTable("News Links", ".class", "Html.A", ".id", "regex=n-c-\\d+");
        addItemToTable("Scroll News Down", ".class", "Html.DIV", ".className", "goog-inline-block goog-custom-button chartclient-button-down");
        addItemToTable("Scroll News Up", ".class", "Html.DIV", ".className", "goog-inline-block goog-custom-button chartclient-button-up");
    }
    
    /**
     * This function decomposes the company market details list into a map.<br>
     * Each element in the list is composed of two span tags: a key tag and a value tag
     * 
     * @return The market info (e.g. 52 wk high / low, market cap, etc) for the current stock
     */
    public LinkedHashMap<String, String> getMarketData()
    {
        TestObject marketData = null;
        TestObject[] dataItemKeys = null, dataItemValues = null;
        LinkedHashMap<String, String> results = new LinkedHashMap<String, String>();
        try
        {
            marketData = findDefinedItem("Market Data");
            dataItemKeys = TO.findObject(getNumberOfFindTries(), getDelayBetweenTries(), marketData, Subitems.atDescendant, "ALL", false, getItemPropertiesAsArray("Market Data Key", "nf=="));
            dataItemValues = TO.findObject(getNumberOfFindTries(), getDelayBetweenTries(), marketData, Subitems.atDescendant, "ALL", false, getItemPropertiesAsArray("Market Data Value", "nf=="));
            for (int i = 0; i < Math.min(dataItemKeys.length, dataItemValues.length); i++)
            {
                if (!StringFunctions.ifNull(dataItemKeys[i].getProperty(".text")).equals(""))
                {
                    results.put(StringFunctions.ifNull(dataItemKeys[i].getProperty(".text")), StringFunctions.ifNull(dataItemValues[i].getProperty(".text")));
                }
            }
        }
        catch (Exception e)
        {
            RationalTestScript.logException(e);
        }
        finally
        {
            RationalTestScript.unregister(new Object[] {marketData});
            RationalTestScript.unregister(dataItemKeys);
            RationalTestScript.unregister(dataItemValues);
        }
        return results;
    }
    
    /**
     * @return The current price of the stock.
     */
    public double getCurrentPrice()
    {
        double d = 0;
        try
        {
            d = Double.parseDouble(StringFunctions.ifNull(getItemRuntimeProperty("Current Price", ".text")));
        }
        catch (Exception e)
        {
            
        }
        return d;
    }
    
    /**
     * @return How much the stock has changed relative to its opening price today, percentage-wise
     */
    public double getChangePercent()
    {
        double d = 0;
        try
        {
            d = Double.parseDouble(StringFunctions.ifNull(getItemRuntimeProperty("Price Change Percent", ".text")).replace("(", "").replace(")", "").replace("%", ""));
        }
        catch (Exception e)
        {
            
        }
        return d;
    }
    
    /**
     * @return How much the stock has changed relative to its opening price today, in actual dollars and cents
     */
    public double getChangeAmount()
    {
        double d = 0;
        try
        {
            return Double.parseDouble(StringFunctions.ifNull(getItemRuntimeProperty("Price Change", ".text")));
        }
        catch (Exception e)
        {
            
        }
        return d;
    }
    
    public void validateNewsLinks()
    {
        for (String title : getNewsLinkTitles())
        {
            TestObject link = null;
            try
            {
                link = TO.findOneObject(getNumberOfFindTries(), getDelayBetweenTries(), getBaseObject(), false, ".class", "Html.A", ".text", title);
                int attempts = 0;
                boolean clicked = false;
                do
                {
                    clicked = webSpecificActions.click(link, title, 1, 1, MouseButtons.Left, false, false, false, 0, 0, false);
                    if (!clicked)
                    {
                        click("Scroll News Down");
                        RationalTestScript.sleep(2);
                    }
                } while (attempts < 10 && clicked == false);
                if (!clicked)
                {
                    RationalTestScript.logError("Unable to click on " + title + " after 10 tries.", Console.getScreenshot());
                }
                else
                {
                    theBrowser.sync();
                    RationalTestScript.sleep(2);
                    String url = theBrowser.getURL();
                    RationalTestScript.logTestResult("Did " + title + " load a news site?", !url.contains("www.google.com/finance"));
                    if (!url.contains("www.google.com/finance"))
                    {
                        RationalTestScript.logInfo("Page Loaded", Console.getScreenshot());
                        theBrowser.back();
                        RationalTestScript.sleep(2);
                        theBrowser.sync();
                    }
                }
            }
            catch (Exception e)
            {
                continue;
            }
            finally
            {
                TO.free(link);
            }
        }
    }
    
    public List<String> getNewsLinkTitles()
    {
        List<String> titles = new ArrayList<String>();
        TestObject[] items = null;
        try
        {
            items = findDefinedItems("News Links");
            for (TestObject t : items)
            {
                titles.add(StringFunctions.ifNull(t.getProperty(".text")));
            }
        }
        catch (UnableToPerformActionException e)
        {
            RationalTestScript.logException(e);
        }
        catch (ObjectNotFoundException e)
        {
            RationalTestScript.logException(e);
        }
        finally
        {
            TO.free((Object[]) items);
        }
        return titles;
    }
}
