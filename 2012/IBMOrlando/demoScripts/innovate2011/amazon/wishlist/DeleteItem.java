package demoScripts.innovate2011.amazon.wishlist;

import resources.demoScripts.innovate2011.amazon.wishlist.DeleteItemHelper;

import com.advancedrft.ibminnovate.amazon.AmazonShell;
import com.advancedrft.ibminnovate.amazon.MainPage;
import com.advancedrft.ibminnovate.amazon.ProductPage;

/**
 * Description : Functional Test Script
 * 
 * @author laguilar
 */
public class DeleteItem extends DeleteItemHelper
{
    /**
     * Script Name : <b>DeleteItem</b> Generated : <b>Jun 7, 2011 11:56:30 AM</b> Description : Functional Test Script Original Host : WinNT Version 5.1 Build 2600 (S)
     * 
     * @since 2011/06/07
     * @author laguilar
     * @param args
     *            The script args
     */
    public void testMain(Object[] args)
    {
        // start Hazel cast
        // HazelcastAPI.start(RationalTestScript.getCurrentProject().getLocation() + File.separator + "rftConfigs\\automation.xml");
        
        // Launch browser, load Amazon home page
        MainPage amazon = MainPage.openAmazon();
        ProductPage pp = new ProductPage("Product Page", amazon.getTheBrowser());
        pp.getItemDetails();
        
        // Check if hazel cast has any customers with wish lists
        // sign in, load any wish list
        amazon.signInAsAnyCustomerWithWishlist();
        AmazonShell.signedInCustomer.listenForUpdates();
    }
}
