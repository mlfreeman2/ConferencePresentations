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
        addItemToTable("Markets", ".class", "Html.A", ".text", "Markets");
        addItemToTable("News", ".class", "Html.A", ".text", "News");
        addItemToTable("Stock screener", ".class", "Html.A", ".text", "Stock screener");
        addItemToTable("Google Domestic Trends", ".class", "Html.A", ".text", "Google Domestic Trends");
        addItemToTable("Quote Search Input", ".class", "Html.INPUT.text", ".id", "gbqfq");
        addItemToTable("Get quotes", ".class", "Html.BUTTON", ".id", "gbqfb");
    }
}
