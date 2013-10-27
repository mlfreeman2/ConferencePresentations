package com.advancedrft.ibminnovate.amazon;

import java.util.ArrayList;

import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.common.lang.Regex;
import com.advancedrft.common.lang.StringFunctions;
import com.advancedrft.ibminnovate.amazon.data.AmazonItem;
import com.advancedrft.ibminnovate.amazon.data.AmazonItem.ItemStatus;
import com.advancedrft.ibminnovate.amazon.data.wishlist.AddItem;

/**
 * This class represents a generic product page on amazon.com.<br>
 * Could cover anything ranging from a CPU to a bag of chips to a book to a pair of shoes or anything amazon offers.
 */
public class ProductPage extends AmazonShell
{
    
    public ProductPage(String productName)
    {
        super(productName);
        addItems();
    }
    
    public ProductPage(String productName, Browser baseWindow)
    {
        super(productName, baseWindow);
        addItems();
    }
    
    /**
     * Stores all the object definition for Amazon's Product Page (no need for a map)
     */
    private void addItems()
    {
        addItemToTable("Product Photo", ".class", "Html.IMG", ".className", "prod_image_selector");
        addItemToTable("Item Name", ".class", "Html.SPAN", ".id", "btAsinTitle");
        addItemToTable("List Price", ".class", "Html.SPAN", ".className", "listprice");
        addItemToTable("Price Block DIV", ".class", "Html.DIV", ".id", "priceBlock");
        addItemToTable("You Save", ".class", "Html.TD", ".className", "price");
        addItemToTable("Item Name", ".class", "Html.SPAN", ".id", "btAsinTitle");
        addItemToTable("Temporarily out of stock.", ".class", "Html.SPAN", ".className", "availRed", ".text", "Temporarily out of stock.");
        addItemToTable("Usually ships within", ".class", "Html.SPAN", ".className", "availOrange", ".text", "startsWith=Usually ships within");
        addItemToTable("In Stock.", ".class", "Html.SPAN", ".className", "availGreen", ".text", "In Stock.");
        addItemToTable("In Stock.", ".class", "Html.SPAN", ".className", "availGreen", ".text", "In Stock.");
        addItemToTable("Add to Wish List", ".class", "Html.INPUT.image", ".title", "Add to Wish List");
        addItemToTable("Like", ".class", "Html.IMG", ".id", "startsWith=amazonLikeButton_");
        
        // This was a hard object to find on the page, using the URL and Regex instead
        // addItemToTable("ASIN", ".class", "Html.LI", ".text", "regex=(ASIN|ISBN");
        // addItemToTable("ISBN", ".class", "Html.LI", ".text", "");
        // addItemToTable("ASIN2", ".class", "Html.INPUT.hidden", ".id", "ASIN");
        
        addItemToTable("Price", "nf==externalStartingPoint", "Price Block DIV", ".class", "Html.B", ".className", "priceLarge");
    }
    
    /**
     * Add item to specified wish list and then publish the update to Hazelcast (via a topic)
     * 
     * @param wishListName
     *            The name of the wish list to add items to
     */
    public void addItemToWishList(String wishListName)
    {
        AmazonItem ai = getItemDetails();
        System.out.println(ai.toString());
        click("Add to Wish List");
        getTheBrowser().sync(30, 1);
        HazelcastAPI.<AddItem> getTopic(AmazonShell.signedInCustomer.getEmail() + " AddToWishList").publish(new AddItem(wishListName, ai));
    }
    
    /**
     * This collects important details about the currently opened item
     * 
     * @see AmazonItem
     * @return Important details on the currently opened item.
     */
    public AmazonItem getItemDetails()
    {
        String itemName = StringFunctions.ifNull(getItemRuntimeProperty("Item Name", ".text"));
        ArrayList<String> matches = Regex.matchString(getTheBrowser().getURL(), "regex=[product|dp]\\/(.*)\\/");
        String itemASIN = new String();
        if (!matches.isEmpty())
        {
            itemASIN = StringFunctions.ifNull(matches.get(0));
        }
        AmazonItem ai = new AmazonItem(itemName, itemASIN);
        ai.setPrice(StringFunctions.ifNull(getItemRuntimeProperty("Price", ".text")));
        if (itemExists("Temporarily out of stock"))
        {
            ai.setStatus(ItemStatus.TempOutOfStock);
        }
        else if (itemExists("Usually ships within"))
        {
            ai.setStatus(ItemStatus.UsuallyShipsWithin);
        }
        else if (itemExists("In Stock"))
        {
            ai.setStatus(ItemStatus.InStock);
        }
        return ai;
    }
}
