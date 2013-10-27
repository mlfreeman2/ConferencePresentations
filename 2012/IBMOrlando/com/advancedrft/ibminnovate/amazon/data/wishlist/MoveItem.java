package com.advancedrft.ibminnovate.amazon.data.wishlist;

import java.io.Serializable;

import com.advancedrft.ibminnovate.amazon.data.AmazonItem;

/**
 * A class that represents a "item moved between wishlists" event
 */
public class MoveItem implements Serializable
{
    private static final long serialVersionUID        = 1L;
    
    private String            destinationWishListName = new String();
    
    private AmazonItem        item;
    
    private String            sourceWishListName      = new String();
    
    public MoveItem()
    {
        
    }
    
    public MoveItem(String fromWishListName, String toWishListName, AmazonItem item)
    {
        super();
        setSourceWishListName(fromWishListName);
        setDestinationWishListName(toWishListName);
        this.item = item;
    }
    
    /**
     * @return the destinationWishListName
     */
    public String getDestinationWishListName()
    {
        return destinationWishListName;
    }
    
    public AmazonItem getItem()
    {
        return item;
    }
    
    /**
     * @return the sourceWishListName
     */
    public String getSourceWishListName()
    {
        return sourceWishListName;
    }
    
    /**
     * @param destinationWishListName
     *            the destinationWishListName to set
     */
    public void setDestinationWishListName(String destinationWishListName)
    {
        this.destinationWishListName = destinationWishListName;
    }
    
    public void setItem(AmazonItem item)
    {
        this.item = item;
    }
    
    /**
     * @param sourceWishListName
     *            the sourceWishListName to set
     */
    public void setSourceWishListName(String sourceWishListName)
    {
        this.sourceWishListName = sourceWishListName;
    }
}
