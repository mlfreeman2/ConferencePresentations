package com.advancedrft.ibminnovate.amazon.data;

import java.io.Serializable;

/**
 * A generic data class representing a single item on Amazon.com
 */
public class AmazonItem implements Serializable
{
    /**
     * The possible states the item could be in. <br>
     * There could be more states I didn't catch initially
     */
    public enum ItemStatus
    {
        /**
         * In stock. Green text on the page
         */
        InStock,
        /**
         * Red out of stock message
         */
        TempOutOfStock,
        /**
         * Orange-yellow usually ships within message
         */
        UsuallyShipsWithin;
    }
    
    private static final long serialVersionUID = -3391525121500111869L;
    
    /**
     * Amazon.com ASIN - their way of identifying a product uniquely (see http://en.wikipedia.org/wiki/Amazon_Standard_Identification_Number)
     */
    protected String          itemASIN;
    
    protected String          itemName;
    
    protected double          price;
    
    protected ItemStatus      status;
    
    /**
     * Each wishlist entry has an ID similar in format to an ASIN. <br>
     * Wishlists themselves have such IDs too.
     */
    protected String          wishlistItemID;
    
    public AmazonItem(String itemName, String itemASIN)
    {
        super();
        this.itemName = itemName;
        this.itemASIN = itemASIN;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        AmazonItem other = (AmazonItem) obj;
        if (itemASIN == null)
        {
            if (other.itemASIN != null)
            {
                return false;
            }
        }
        else if (!itemASIN.equals(other.itemASIN))
        {
            return false;
        }
        if (itemName == null)
        {
            if (other.itemName != null)
            {
                return false;
            }
        }
        else if (!itemName.equals(other.itemName))
        {
            return false;
        }
        if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
        {
            return false;
        }
        if (status != other.status)
        {
            return false;
        }
        return true;
    }
    
    public String getItemASIN()
    {
        return itemASIN;
    }
    
    public String getItemName()
    {
        return itemName;
    }
    
    public double getPrice()
    {
        return price;
    }
    
    public ItemStatus getStatus()
    {
        return status;
    }
    
    public String getWishlistItemID()
    {
        return wishlistItemID;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (itemASIN == null ? 0 : itemASIN.hashCode());
        result = prime * result + (itemName == null ? 0 : itemName.hashCode());
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (status == null ? 0 : status.hashCode());
        return result;
    }
    
    public void setItemASIN(String itemASIN)
    {
        this.itemASIN = itemASIN;
    }
    
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }
    
    public void setPrice(double price)
    {
        this.price = price;
    }
    
    /**
     * Converts a price into a Java double. Sets the price to -1 if an error occured parsing it.
     * 
     * @param price
     *            A string price amount scraped from Amazon
     */
    public void setPrice(String price)
    {
        try
        {
            this.price = Double.parseDouble(price.trim().replace("$", "").replace(",", ""));
        }
        catch (NumberFormatException e)
        {
            this.price = -1.00;
        }
    }
    
    public void setStatus(ItemStatus status)
    {
        this.status = status;
    }
    
    /**
     * Sets the wishlist entry's ID. The ID is similar in format to an ASIN
     * 
     * @param wishlistItemID
     *            The wishlist entry's ID
     */
    public void setWishlistItemID(String wishlistItemID)
    {
        this.wishlistItemID = wishlistItemID;
    }
    
    @Override
    public String toString()
    {
        return "AmazonItem [itemName=" + itemName + ", itemASIN=" + itemASIN + ", status=" + status + ", price=" + price + "]";
    }
    
}
