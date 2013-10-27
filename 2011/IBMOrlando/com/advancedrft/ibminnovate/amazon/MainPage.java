package com.advancedrft.ibminnovate.amazon;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.automation.rft.object.TO;
import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.ibminnovate.amazon.data.Customer;
import com.advancedrft.ibminnovate.amazon.data.WishList;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;

/**
 * This class represents www.amazon.com itself. Here all we'll want to do is basically sign in / sign up for an account.
 */
/**
 * @author laguilar
 */
/**
 * @author laguilar
 */
public class MainPage extends AmazonShell
{
    
    /**
     * Opens a new browser and returns an instance of this class all ready to use.
     * 
     * @return An instance of this class, all ready to use
     */
    public static MainPage openAmazon()
    {
        Browser b = new Browser("http://www.amazon.com");
        RationalTestScript.sleep(3);
        return new MainPage(b);
    }
    
    public MainPage()
    {
        super("www.amazon.com");
        addItems();
        try
        {
            theBrowser = new Browser(TO.findOneObject(3, 3, RootTestObject.getRootTestObject(), false, ".class", "Html.HtmlBrowser", ".documentName", "endsWith=www.amazon.com/"));
        }
        catch (Exception e)
        {
            Log.logException(e);
        }
    }
    
    public MainPage(Browser b)
    {
        super("www.amazon.com");
        addItems();
        theBrowser = b;
        setBaseObject(b.browser);
    }
    
    /**
     * Stores all the object definition for Amazon's Main Page (no need for a map)
     */
    private void addItems()
    {
        addItemToTable("Start here", ".class", "Html.A", ".text", "Start here", ".href", "contains=signin");
        addItemToTable("Sign in", ".class", "Html.A", ".text", "Sign in", ".href", "contains=signin");
        addItemToTable("WelcomeMsg", ".class", "Html.DIV", ".id", "navidWelcomeMsg");
        addItemToTable("Nav Bar Message", ".class", "Html.SPAN", "nf==externalStartingPoint", "WelcomeMsg");
        addItemToTable("Logout", "nf==externalStartingPoint", "WelcomeMsg", ".class", "Html.A", ".text", "contains=Not", ".href", "contains=sign-out");
    }
    
    /**
     * Returns an active customer from Hazelcast
     * 
     * @return An active customer from Hazelcast, or null if none are present.
     */
    public Customer getActiveCustomer()
    {
        Collection<Customer> collectionCust = HazelcastAPI.<String, Customer> getMap("Customers.Active").values();
        for (Iterator<Customer> iterator = collectionCust.iterator(); iterator.hasNext();)
        {
            return iterator.next();
        }
        return null;
    }
    
    /**
     * Returns a random customer from Hazelcast with at least one wish list, or null if no such customers exist.
     * 
     * @return A random customer from Hazelcast with a wish list, or null if no such customers exist
     */
    public Customer getActiveCustomerWithWishList()
    {
        Customer c = null;
        Collection<Customer> collectionCust = HazelcastAPI.<String, Customer> getMap("Customers.Active").values();
        for (Iterator<Customer> iterator = collectionCust.iterator(); iterator.hasNext();)
        {
            Customer customer = iterator.next();
            List<WishList> wl = customer.getWishlists();
            if (!wl.isEmpty())
            {
                c = customer;
                Log.logInfo("Found an active customer with existing wish list in Customers.Active list", AmazonShell.signedInCustomer.toString());
                break;
            }
        }
        return c;
    }
    
    /**
     * Indicates if we're signed into Amazon
     * 
     * @return True if we're signed into Amazon, False otherwise
     */
    public boolean isSignedIn()
    {
        TestObject to = null;
        int orgNumFinds = getNumberOfFindTries();
        try
        {
            setNumberOfFindTries(2);
            to = findDefinedItem("WelcomeMsg");
            String status = to.getProperty(".text").toString();
            if (status.startsWith("Hello, "))
            {
                return true;
            }
        }
        catch (Exception e)
        {
            
        }
        finally
        {
            RationalTestScript.unregister(new TestObject[] {to});
            setNumberOfFindTries(orgNumFinds);
        }
        
        return false;
    }
    
    /**
     * Logs this browser out of Amazon
     */
    public void logout()
    {
        click("Logout");
        AmazonShell.signedInCustomer = null;
    }
    
    /**
     * Logs into Amazon as the specified email and password
     * 
     * @param email
     *            The email address to log in as
     * @param password
     *            The password of that email address
     * @return True if the login worked, False otherwise
     */
    public boolean signIn(String email, String password)
    {
        click("Sign in");
        getTheBrowser().sync(30, 1);
        SignIn signInPage = new SignIn(getTheBrowser());
        return signInPage.login(email, password);
    }
    
    /**
     * Logs out (if necessary), selects a random customer from Hazelcast, and signs in as that customer.<br>
     * If no users are found in Hazelcast, then create a new one and add to Hazelcast See {@link AmazonShell#signedInCustomer} for the details on who we signed in as.
     */
    public void signInAsAnyCustomer()
    {
        if (isSignedIn())
        {
            // TODO explore the idea of adding a way to get a customer's data once logged in
            // for now we will always log out.
            logout();
        }
        
        // Check Hazelcast for new active customers
        Customer c = getActiveCustomer();
        
        // If no users are found in Hazelcast, then create a new one and add to Hazelcast
        if (c == null)
        {
            Log.logInfo("No active customers available in Customers.Active, creating a new user...");
            click("Sign in");
            getTheBrowser().sync(30, 1);
            SignIn signInPage = new SignIn(getTheBrowser());
            AmazonShell.signedInCustomer = signInPage.create("Jane Doe", "newAutoUser" + System.currentTimeMillis() / 1000 + "@qa.com", "securePassword");
            Log.logInfo("Added new customer to Customers.Active list", AmazonShell.signedInCustomer.toString());
            HazelcastAPI.getMap("Customers.Active").put(AmazonShell.signedInCustomer.getEmail(), AmazonShell.signedInCustomer);
        }
        else
        {
            Log.logInfo("Using customer from Customers.Active list", c.toString());
            signIn(c.getEmail(), c.getPassword());
            AmazonShell.signedInCustomer = c;
        }
        return;
    }
    
    /**
     * Logs out (if necessary), selects a random customer from Hazelcast with at least 1 wish list, and signs in as that customer.<br>
     * If no active customer exists, we create a new user and add it to Hazelcast. Also, if no wish list exists for any active customer we will create one and add it to Hazelcast<br>
     * See {@link AmazonShell#signedInCustomer} for the details on who we signed in as.
     * 
     * @return The ASIN of the customer's first wish list
     */
    public String signInAsAnyCustomerWithWishlist()
    {
        String wishListAISN = new String();
        if (isSignedIn())
        {
            logout();
        }
        
        // Check hazel cast for new active customers with wish list
        Customer c = getActiveCustomerWithWishList();
        
        // If no matching users are found in Hazelcast, then try any active customer and create a wish list called "Birthday"
        if (c == null)
        {
            signInAsAnyCustomer();
            // Wish list
            click("Wish List");
            getTheBrowser().sync(15, 1);
            WishListScreen wls = new WishListScreen("Birthday", getTheBrowser());
            AmazonShell.signedInCustomer.getWishlists().add(wls.createNewList(wls.getWishListName(), true));
            wishListAISN = wls.getWishListName();
        }
        else
        {
            // Sign in as the customer with a wish list
            signIn(c.getEmail(), c.getPassword());
            AmazonShell.signedInCustomer = c;
            
            // Load Wish list
            click("Wish List");
            getTheBrowser().sync(15, 1);
            WishListScreen wls = new WishListScreen(c.getWishlists().get(0).getName(), getTheBrowser());
            wls.loadWishList(wls.getWishListName());
            wishListAISN = wls.getWishListName();
        }
        return wishListAISN;
    }
}
