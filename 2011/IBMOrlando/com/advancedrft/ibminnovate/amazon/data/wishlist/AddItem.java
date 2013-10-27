package com.advancedrft.ibminnovate.amazon.data.wishlist;

import java.io.Serializable;

import com.advancedrft.ibminnovate.amazon.data.AmazonItem;

/** 
 * This data class is used by Hazelcast topic and serves as a message container.
 * When a product is added to a wish list, it triggers an event which pushes the message to the Amazon.SignedInUser.lists
 * Anyone that calls Customer.listenForUpdates() for that customer will receive the update
 */
public class AddItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String            wishListName     = new String();
    
    private AmazonItem        item;
    
    public AddItem()
    {
        
    }
    
    public AddItem(String wishListName, AmazonItem item)
    {
        super();
        this.wishListName = wishListName;
        this.item = item;
    }
    
    /**
     * Get the item added
     * 
     * @return The item that was added to a wishlist
     */
    public AmazonItem getItem()
    {
        return item;
    }
    
    /**
     * Get the wish list name
     * 
     * @return The name of the wishlist that this item was added to
     */
    public String getWishListName()
    {
        return wishListName;
    }
    
    public void setItem(AmazonItem item)
    {
        this.item = item;
    }
    
    /**
     * Set the wish list name that the item was added to
     * 
     * @param wishListName
     *            The name of the wishlist that the item was added to
     */
    public void setWishListName(String wishListName)
    {
        this.wishListName = wishListName;
    }
}
