package com.advancedrft.ibminnovate.amazon.data.wishlist;

import java.io.Serializable;

import com.advancedrft.ibminnovate.amazon.data.AmazonItem;

/**
 * This data class is used by Hazelcast topic and serves as a message container. When a product is deleted from a wish list, it triggers an event which pushes the message to the AmazonShell.signedInCustomer.lists Anyone that calls Customer.listenForUpdates() for that customer will receive the update
 */
public class DeleteItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private AmazonItem        item;
    
    private String            wishListName     = new String();
    
    public DeleteItem()
    {
        
    }
    
    public DeleteItem(String wishListName, AmazonItem item)
    {
        super();
        this.wishListName = wishListName;
        this.item = item;
    }
    
    /**
     * Get the item that was deleted
     * 
     * @return The item that was removed
     */
    public AmazonItem getItem()
    {
        return item;
    }
    
    /**
     * Get a wish list name
     * 
     * @return The name of the wishlist that the item was removed from
     */
    public String getWishListName()
    {
        return wishListName;
    }
    
    /**
     * Set the item that was deleted
     * 
     * @param item
     *            The item that was deleted
     */
    public void setItem(AmazonItem item)
    {
        this.item = item;
    }
    
    /**
     * Set the wish list name
     * 
     * @param wishListName
     *            The name of the wishlist that the item was removed from
     */
    public void setWishListName(String wishListName)
    {
        this.wishListName = wishListName;
    }
}
