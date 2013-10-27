package com.advancedrft.ibminnovate.amazon;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.automation.rft.object.management.WebObjectManager;
import com.advancedrft.ibminnovate.amazon.data.Customer;

/**
 * The base class for all Amazon.com pages.<br>
 * The pages all have a common header and footer, so if we need any functionality in those we put it here so it appears on every page class.
 */
public abstract class AmazonShell extends WebObjectManager
{
    /**
     * The currently signed in Amazon customer
     */
    public static Customer signedInCustomer;
    
    public AmazonShell(String screenName)
    {
        super(screenName);
        addGlobalItems();
    }
    
    public AmazonShell(String screenName, Browser baseWindow)
    {
        super(screenName, baseWindow);
        addGlobalItems();
    }
    
    /**
     * Stores all the global object definition (no need for a map)
     */
    private void addGlobalItems()
    {
        addItemToTable("Logo", ".class", "Html.SPAN", ".className", "navSprite", ".id", "navLogo");
        addItemToTable("Your Amazon.com", ".class", "Html.A", ".text", "Your Amazon.com");
        addItemToTable("Gifts & Wish Lists", ".class", "Html.A", ".text", "Gifts & Wish Lists");
        addItemToTable("Search Department", ".class", "Html.SELECT", ".id", "searchDropdownBox");
        addItemToTable("Search", ".class", "Html.INPUT.text", ".id", "twotabsearchtextbox");
        addItemToTable("Cart", ".class", "Html.A", ".className", "destination", ".text", "Cart");
        addItemToTable("Wish List", ".class", "Html.A", ".className", "destination", ".text", "Wish List");
        
        addItemToTable("Go", ".class", "Html.DIV", ".id", "navGoButton");
    }
    
    /**
     * Search Amazon for the specified text for the given department
     * 
     * @param text
     *            Criteria to search for
     * @param department
     *            Department to search against
     * @return SearchResults
     */
    public SearchResults search(String text, String department)
    {
        type("Search", text, true);
        select("Search Department", department);
        click("Go");
        Log.logInfo("Searched for " + text + " with " + department, getTheBrowser().getScreenshot());
        getTheBrowser().sync(30, 1);
        return new SearchResults(text, getTheBrowser());
    }
    
}
