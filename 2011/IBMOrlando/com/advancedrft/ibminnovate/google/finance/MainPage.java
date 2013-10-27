package com.advancedrft.ibminnovate.google.finance;

import java.awt.image.BufferedImage;
import java.util.Vector;

import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.automation.rft.object.TestDataType;
import com.advancedrft.ibminnovate.google.Finance;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;

/**
 * This class represents the main page of google finance.
 */
public class MainPage extends Finance
{
    /**
     * This represents the possible indices shown on the main page
     */
    public static enum IndexName
    {
        /**
         * The possible indices shown on the main page
         */
        Dow("Dow"), Nasdaq("Nasdaq"), SP500("S&P 500");
        
        private final String name;
        
        private IndexName(String n)
        {
            name = n;
        }
        
        public String getName()
        {
            return name;
        }
    }
    
    /**
     * This represents the possible statistics that can be collected from the main page for the various indices shown there
     */
    public static enum IndexStatistic
    {
        /**
         * The possible statistics that can be collected from the main page for the various indices shown there
         */
        Closing(2), PercentChange(4), PointChange(3);
        
        private final int column;
        
        private IndexStatistic(int position)
        {
            column = position;
        }
        
        public int getColumn()
        {
            return column;
        }
    }
    
    public MainPage()
    {
        super("Google Finance");
        addItems();
    }
    
    public MainPage(Browser b)
    {
        super("Google Finance", b);
        addItems();
    }
    
    private void addItems()
    {
        addItemToTable("Market Chart", ".class", "Html.IMG", ".id", "mkt-chart");
        addItemToTable("Market Summary", ".class", "Html.TABLE", ".id", "sfe-mktsumm");
    }
    
    /**
     * Uses the search box to search for the specified company
     * 
     * @param name
     *            THe name of the company to search for
     */
    public void findCompany(String name)
    {
        type("Quote Search Input", name, false);
        click("Get quotes");
        theBrowser.sync();
    }
    
    /**
     * Gets the market chart image on the main page
     * 
     * @return The market chart image as a BufferedImage, suitable for logging with RFT
     */
    public BufferedImage getMarketChart()
    {
        return getImage("Market Chart");
    }
    
    /**
     * Gets the specified statistic for the specified index
     * 
     * @param n
     *            The index (e.g. Dow, S&P 500, etc)
     * @param s
     *            The statistic (point change, % change, etc)
     * @return The value for that statistic
     */
    public String getMarketSummary(IndexName n, IndexStatistic s)
    {
        TestObject marketSummaryTable = null;
        try
        {
            marketSummaryTable = findDefinedItem("Market Summary");
            Object tableData = marketSummaryTable.getTestData("visiblegrid");
            Vector<Vector<String>> table = TestDataType.iTestDataTableToTable(tableData);
            if (table == null)
            {
                return "";
            }
            for (Vector<String> row : table)
            {
                if (row.get(0).equals(n.getName()))
                {
                    return row.get(s.getColumn() - 1);
                }
            }
            return "";
        }
        catch (Exception e)
        {
            return "";
        }
        finally
        {
            RationalTestScript.unregister(new Object[] {marketSummaryTable});
        }
    }
    
}
