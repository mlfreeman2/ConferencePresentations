package com.lps.rsdc2010.google.finance;

import java.awt.image.BufferedImage;
import java.util.Vector;

import com.lps.rsdc2010.common.automation.rft.object.Browser;
import com.lps.rsdc2010.common.automation.rft.object.TestDataType;
import com.lps.rsdc2010.google.Finance;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;

public class MainPage extends Finance
{
    public MainPage(Browser b)
    {
        super("Google Finance", b);
        addItems();
    }

    public MainPage()
    {
        super("Google Finance");
        addItems();
    }

    private void addItems()
    {
        addItemToTable("Market Chart", ".class", "Html.IMG", ".id", "mkt-chart");
        addItemToTable("Market Summary", ".class", "Html.TABLE", ".id", "sfe-mktsumm");
    }

    public static enum IndexStatistic
    {
        Closing(2), PointChange(3), PercentChange(4);
        public final int column;

        private IndexStatistic(int position)
        {
            column = position;
        }
    }

    public static enum IndexName
    {
        Dow("Dow"), SP500("S&P 500"), Nasdaq("Nasdaq");
        public final String name;

        private IndexName(String n)
        {
            name = n;
        }
    }

    public BufferedImage getMarketChart()
    {
        return getImage("Market Chart");
    }

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
                if (row.get(0).equals(n.name))
                {
                    return row.get(s.column - 1);
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

    public void findCompany(String name)
    {
        type("Quote Search Input", name, false);
        click("Get quotes");
        theBrowser.sync();
    }

}
