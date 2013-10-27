package com.advancedrft.ibminnovate.google;

import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.automation.rft.object.management.WebObjectManager;

/**
 * A skeleton class containing widgets common to all google finance pages.
 */
public abstract class Finance extends WebObjectManager
{
    
    public Finance(String screenName)
    {
        super(screenName);
        addItems();
    }
    
    public Finance(String screenName, Browser theBrowser)
    {
        super(screenName, theBrowser);
        addItems();
    }
    
    private void addItems()
    {
        addItemToTable("Markets", ".class", "Html.A", ".id", "nav-m");
        addItemToTable("News", ".class", "Html.A", ".id", "nav-n");
        addItemToTable("Stock screener", ".class", "Html.A", ".id", "nav-s");
        addItemToTable("Google Domestic Trends", ".class", "Html.A", ".id", "nav-q");
        addItemToTable("Quote Search Input", ".class", "Html.INPUT.text", ".id", "searchbox");
        addItemToTable("Get quotes", ".class", "Html.INPUT.submit", ".value", "Get quotes");
    }
}
