package com.advancedrft.ibminnovate.amazon.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.ibminnovate.amazon.data.wishlist.AddItem;
import com.advancedrft.ibminnovate.amazon.data.wishlist.CreateWishList;
import com.advancedrft.ibminnovate.amazon.data.wishlist.DeleteItem;
import com.advancedrft.ibminnovate.amazon.data.wishlist.MoveItem;
import com.hazelcast.core.MessageListener;

/**
 * Customer represents any signed in user. The wish lists instance member uses a Hazel cast topic to keep it synchronized since it has the potential for frequent updates
 * 
 * @author laguilar
 */
public class Customer implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private String            email            = new String();
    
    private String            name             = new String();
    
    private String            password         = new String();
    
    private List<WishList>    wishlists        = new ArrayList<WishList>();
    
    public Customer()
    {
        super();
    }
    
    public Customer(String name, String email, String password)
    {
        super();
        setName(name);
        setEmail(email);
        setPassword(password);
    }
    
    /**
     * Get the email of the customer
     * 
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }
    
    /**
     * Get the name of the customer (FirstName LastName format)
     * 
     * @return the name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Get the password of the customer
     * 
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }
    
    /**
     * Iterates through the this.wishlists instance member and compares the specified name against each wishlist name. Matches must be exact, and the first match is returned
     * 
     * @param name
     *            the name of the wishlist
     * @return return a WishList that matches the specified name
     */
    public WishList getWishList(String name)
    {
        for (WishList list : wishlists)
        {
            if (list.getName().equals(name))
            {
                return list;
            }
        }
        return null;
    }
    
    /**
     * Get all the wish lists associated with customer
     * 
     * @return the wishlists
     */
    public List<WishList> getWishlists()
    {
        return wishlists;
    }
    
    /**
     * Keeps the wish list synchronized, call if you care to receive the updates
     */
    public void listenForUpdates()
    {
        HazelcastAPI.<AddItem> getTopic(email + " AddToWishList").addMessageListener(new MessageListener<AddItem>()
        {
            @Override
            public void onMessage(AddItem message)
            {
                getWishList(message.getWishListName()).getList().add(message.getItem());
            }
        });
        // repeat for deletes
        HazelcastAPI.<DeleteItem> getTopic(email + " DeleteFromWishList").addMessageListener(new MessageListener<DeleteItem>()
        {
            @Override
            public void onMessage(DeleteItem message)
            {
                getWishList(message.getWishListName()).getList().remove(message.getItem());
            }
        });
        // repeat for moves
        HazelcastAPI.<MoveItem> getTopic(email + " MoveFromWishList").addMessageListener(new MessageListener<MoveItem>()
        {
            @Override
            public void onMessage(MoveItem message)
            {
                getWishList(message.getDestinationWishListName()).getList().add(message.getItem());
                getWishList(message.getSourceWishListName()).getList().remove(message.getItem());
            }
        });
        
        // repeat for create list
        HazelcastAPI.<CreateWishList> getTopic(email + " CreateWishList").addMessageListener(new MessageListener<CreateWishList>()
        {
            
            @Override
            public void onMessage(CreateWishList message)
            {
                getWishlists().add(message.getList());
            }
            
        });
        
        //
        // to send,
        // HazelcastAPI.<AddItem> getTopic(email + " AddToWishList").publish(new AddItem("foo", AmazonItemFoo));
        
    }
    
    /**
     * Set the customer's email
     * 
     * @param email
     *            the email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    /**
     * Set the customer's name (FirstName LastName format)
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Set the customer's password
     * 
     * @param password
     *            the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * Set the wishlists
     * 
     * @param wishlists
     *            the wishlists to set
     */
    public void setWishlists(List<WishList> wishlists)
    {
        this.wishlists = wishlists;
    }
    
    @Override
    public String toString()
    {
        return "Customer [name=" + getName() + ", email=" + getEmail() + ", password=" + getPassword() + "]";
    }
}
