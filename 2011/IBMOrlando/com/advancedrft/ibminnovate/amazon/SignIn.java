package com.advancedrft.ibminnovate.amazon;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.ibminnovate.amazon.data.Customer;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;

/**
 * This class represents the page wherein you sign in to an existing amazon account
 */
public class SignIn extends AmazonShell
{
    public SignIn()
    {
        super("Amazon.com Sign In Page");
        addItems();
    }
    
    public SignIn(Browser baseWindow)
    {
        super("Amazon.com Sign In Page", baseWindow);
        addItems();
    }
    
    private void addItems()
    {
        addItemToTable("My e-mail address is:", ".class", "Html.INPUT.text", ".id", "ap_email");
        addItemToTable("No, I am a new customer.", ".class", "Html.INPUT.radio", ".id", "ap_signin_create_radio");
        addItemToTable("Yes, I have a password:", ".class", "Html.INPUT.radio", ".id", "ap_signin_existing_radio");
        addItemToTable("Sign in using our secure server", ".class", "Html.INPUT.image", ".id", "signInSubmit");
        addItemToTable("Password", ".class", "Html.INPUT.password", ".id", "ap_password");
        addItemToTable("Error Message", ".class", "Html.DIV", ".id", "message_error");
    }
    
    /**
     * Creates a new Amazon account and leaves the session signed in as that customer
     * 
     * @param name
     *            The name of the customer
     * @param email
     *            The email address of the customer
     * @param password
     *            The desired password for the customer
     * @return The newly created customer object, and you're signed in as that customer too. Also populates {@link AmazonShell#signedInCustomer}.
     */
    public Customer create(String name, String email, String password)
    {
        type("My e-mail address is:", email, false);
        click("No, I am a new customer.");
        click("Sign in using our secure server");
        getTheBrowser().sync(30, 1);
        Registration reg = new Registration(getTheBrowser());
        AmazonShell.signedInCustomer = reg.completeForm(name, email, password);
        return AmazonShell.signedInCustomer;
    }
    
    /**
     * Logs in as the specified customer
     * 
     * @param email
     *            The email of the customer
     * @param password
     *            The customer's password
     * @return True if the login worked, False if not
     */
    public boolean login(String email, String password)
    {
        type("My e-mail address is:", email, false);
        click("Yes, I have a password:");
        type("Password", password, false);
        click("Sign in using our secure server");
        getTheBrowser().dismissDialog("contains=AutoComplete Passwords", "#32770", "No");
        
        // Search for DIV on page with error message, if not found assume login was successful
        TestObject to = null;
        int findTries = getNumberOfFindTries();
        try
        {
            setNumberOfFindTries(1);
            to = findDefinedItem("Error Message");
            String text = to.getProperty(".text").toString();
            if (text.contains("There was a problem with your request"))
            {
                return false;
            }
        }
        catch (Exception e)
        {
            
        }
        finally
        {
            RationalTestScript.unregister(new TestObject[] {to});
            setNumberOfFindTries(findTries);
        }
        Log.logInfo("Sign in successful", "email: " + email + " Password: " + password, getTheBrowser().getScreenshot());
        RationalTestScript.sleep(1);
        getTheBrowser().sync(30, 1);
        return true;
    }
    
}
