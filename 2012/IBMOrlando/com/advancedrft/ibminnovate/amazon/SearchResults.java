package com.advancedrft.ibminnovate.amazon;

import com.advancedrft.common.automation.rft.object.Browser;

/**
 * This class represents the page returned when a search is peformed
 */
public class SearchResults extends AmazonShell
{
    
    public SearchResults(String searchString)
    {
        super("Search Result Page for " + searchString);
        
    }
    
    public SearchResults(String searchString, Browser baseWindow)
    {
        super("Search Result Page for " + searchString, baseWindow);
        addItems();
    }
    
    /**
     * Stores all the object definition for Amazon's Search Result Page (no need for a map)
     */
    private void addItems()
    {
        addItemToTable("Result Row", ".class", "Html.DIV", ".id", "result_(\\d+)");
        addItemToTable("Row Title", "nf==externalStartingPoint", "Row DIV", ".class", "Html.A");// "nf==externalStartingPoint", "Result Row",
        addItemToTable("Row DIV", ".class", "Html.DIV", ".className", "list results ", ".id", "atfResults");
        addItemToTable("Product Photo", "nf==externalStartingPoint", "Result Row", ".class", "Html.IMG", ".alt", "Product Details");
    }
    
    /**
     * Clicks the product image for the specified row in the search results page
     * 
     * @param row
     *            The row to click in
     */
    public void clickAtRow(int row)
    {
        String orgIDProperty = getItemProperty("Result Row", ".id");
        setItemProperty("Result Row", ".id", orgIDProperty.replace("(\\d+)", Integer.toString(row)));
        click("Product Photo");
        setItemProperty("Result Row", ".id", orgIDProperty);
        getTheBrowser().sync(30, 1);
    }
}
