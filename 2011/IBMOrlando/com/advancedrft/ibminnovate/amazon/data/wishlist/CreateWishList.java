package com.advancedrft.ibminnovate.amazon.data.wishlist;

import java.io.Serializable;

import com.advancedrft.ibminnovate.amazon.data.WishList;

/**
 * A class representing a "wish list created" event.<br>
 * Gets sent out via Hazelcast to all interested scripts so that they learn that the customer has a new wishlist adeed
 */
public class CreateWishList implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private WishList          list             = new WishList();
    
    public CreateWishList(WishList wl)
    {
        setList(wl);
    }
    
    public WishList getList()
    {
        return list;
    }
    
    public void setList(WishList list)
    {
        this.list = list;
    }
}
