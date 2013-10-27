package com.advancedrft.ibminnovate.amazon;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.ibminnovate.amazon.data.Customer;

/**
 * This class represents the page wherein you sign up for a new amazon account
 */
public class Registration extends AmazonShell
{
    public Registration()
    {
        super("Amazon.com Registration Page");
        addItems();
    }
    
    public Registration(Browser baseWindow)
    {
        super("Amazon.com Registration Page", baseWindow);
        addItems();
    }
    
    /**
     * Stores all the object definition for Amazon's Registration Page (no need for a map)
     */
    private void addItems()
    {
        addItemToTable("My name is:", ".class", "Html.INPUT.text", ".id", "ap_customer_name");
        addItemToTable("My e-mail address is:", ".class", "Html.INPUT.text", ".id", "ap_email");
        addItemToTable("Type it again: (email)", ".class", "Html.INPUT.text", ".id", "ap_email_check");
        addItemToTable("Enter a new password:", ".class", "Html.INPUT.password", ".id", "ap_password");
        addItemToTable("Type it again: (password)", ".class", "Html.INPUT.password", ".id", "ap_password_check");
        addItemToTable("Create account", ".class", "Html.INPUT.image", ".id", "continue");
    }
    
    /**
     * Completes the new customer registration form
     * 
     * @param name
     *            The new customer's full name
     * @param email
     *            The new customer's email address
     * @param password
     *            The new customer's desired password
     * @return A customer object representing the new customer
     */
    public Customer completeForm(String name, String email, String password)
    {
        Customer c = new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setPassword(password);
        type("My name is:", name, false);
        type("Type it again: (email)", email, false);
        type("Enter a new password:", password, false);
        type("Type it again: (password)", password, false);
        click("Create account");
        // TODO add checks for form errors
        getTheBrowser().sync(30, 1);
        Log.logInfo("New customer completed registration", c.toString(), getTheBrowser().getScreenshot());
        return c;
    }
}
