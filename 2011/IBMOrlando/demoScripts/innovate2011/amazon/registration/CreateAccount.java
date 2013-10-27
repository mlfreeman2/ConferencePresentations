package demoScripts.innovate2011.amazon.registration;

import java.io.File;

import resources.demoScripts.innovate2011.amazon.registration.CreateAccountHelper;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.ibminnovate.amazon.MainPage;
import com.advancedrft.ibminnovate.amazon.SignIn;
import com.advancedrft.ibminnovate.amazon.data.Customer;
import com.rational.test.ft.script.RationalTestScript;

/**
 * Description : Functional Test Script<br>
 * 1. Launch browser and open Amazon home page<br>
 * 2. Complete registration to create a new customer account<br>
 * 3. Login to new account
 * 
 * @author vmuser
 */
public class CreateAccount extends CreateAccountHelper
{
    /**
     * Script Name : <b>CreateAccount</b><br>
     * Generated : <b>May 29, 2011 3:37:45 PM</b><br>
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
        // Launch browser, load Amazon home page
        MainPage amazon = MainPage.openAmazon();
        
        // Navigate Sign In page via New Customer? Start here link
        if (amazon.isSignedIn())
        {
            amazon.logout();
        }
        else
        {
            amazon.click("Start here");
            RationalTestScript.sleep(2);
        }
        
        // Enter email address and indicate new customer
        // At the Registration page, complete form to create account
        SignIn signInPage = new SignIn(amazon.getTheBrowser());
        Customer customer = signInPage.create("Jane Doe", "newAutoUser" + System.currentTimeMillis() / 1000 + "@qa.com", "securePassword");
        
        // start Hazel cast
        HazelcastAPI.start(RationalTestScript.getCurrentProject().getLocation() + File.separator + "rftConfigs\\automation.xml");
        
        // If registration successful, we are at the main amazon page signed in as new customer we just created
        // Sign out and verify login works
        amazon.logout();
        boolean result = signInPage.login(customer.getEmail(), customer.getPassword());
        Log.logTestResult("New customer login was successful", result);
        amazon.logout();
        amazon.getTheBrowser().kill();
        
        // Add customer to Hazelcast
        HazelcastAPI.getMap("Customers.Active").put(customer.getEmail(), customer);
    }
}
