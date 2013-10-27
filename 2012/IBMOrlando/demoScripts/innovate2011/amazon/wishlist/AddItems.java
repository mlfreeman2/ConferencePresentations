package demoScripts.innovate2011.amazon.wishlist;

import java.io.File;

import resources.demoScripts.innovate2011.amazon.wishlist.AddItemsHelper;

import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.ibminnovate.amazon.AmazonShell;
import com.advancedrft.ibminnovate.amazon.MainPage;
import com.advancedrft.ibminnovate.amazon.ProductPage;
import com.advancedrft.ibminnovate.amazon.SearchResults;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Demonstrates the usage of Hazelcast topic and an example of potentially reducing redundant steps by reusing a active customer from Hazelcast
 */
public class AddItems extends AddItemsHelper
{
    /**
     * Script Name : <b>AddItems</b><br>
     * Generated : <b>May 29, 2011 7:09:55 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 6.1 Build 7601 (S)
     * 
     * @since 2011/05/29
     * @author vmuser
     * @param args
     *            The script args
     */
    public void testMain(Object[] args)
    {
        // start Hazelcast
        HazelcastAPI.start(RationalTestScript.getCurrentProject().getLocation() + File.separator + "rftConfigs\\automation.xml");
        
        // Launch browser, load Amazon home page
        MainPage amazon = MainPage.openAmazon();
        
        // Check if hazel cast has any customers with wish lists
        // sign in, load any wish list
        String wishListName = amazon.signInAsAnyCustomerWithWishlist();
        AmazonShell.signedInCustomer.listenForUpdates();
        
        // Search and add first item returned in the search results
        SearchResults search = amazon.search("IBM Rational", "All Departments");
        search.getTheBrowser().sync();
        search.clickAtRow(0);
        search.getTheBrowser().sync();
        
        ProductPage pp = new ProductPage("Product Page", search.getTheBrowser());
        pp.addItemToWishList(wishListName);
        
        // Search and add first item returned in the search results
        search = amazon.search("Test Automation", "All Departments");
        search.clickAtRow(0);
        search.getTheBrowser().sync();
        pp = new ProductPage("Product Page", search.getTheBrowser());
        pp.addItemToWishList(wishListName);
        
        // Search and add first item returned in the search results
        search = amazon.search("Java", "All Departments");
        search.clickAtRow(0);
        search.getTheBrowser().sync();
        pp = new ProductPage("Product Page", search.getTheBrowser());
        pp.addItemToWishList(wishListName);
        
        // Search and add first item returned in the search results
        search = amazon.search("Quality Assurance", "All Departments");
        search.clickAtRow(0);
        search.getTheBrowser().sync();
        pp = new ProductPage("Product Page", search.getTheBrowser());
        pp.addItemToWishList(wishListName);
        
        amazon.logout();
        amazon.getTheBrowser().kill();
    }
}
