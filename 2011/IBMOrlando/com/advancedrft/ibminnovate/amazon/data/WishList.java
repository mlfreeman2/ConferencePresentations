package com.advancedrft.ibminnovate.amazon.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A data class representing an amazon.com wishlist
 */
public class WishList implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String            id               = new String();
    
    private boolean           isPrivate;
    
    private List<AmazonItem>  list             = new ArrayList<AmazonItem>();
    
    private String            name             = new String();
    
    public String getId()
    {
        return id;
    }
    
    public List<AmazonItem> getList()
    {
        return list;
    }
    
    public String getName()
    {
        return name;
    }
    
    public boolean isPrivate()
    {
        return isPrivate;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public void setList(List<AmazonItem> list)
    {
        this.list = list;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setPrivate(boolean isPrivate)
    {
        this.isPrivate = isPrivate;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        String string = "WishList <ul>[name=" + getName() + "</ul><ul>is private=" + isPrivate() + "</ul><ul>list=[";
        for (AmazonItem item : list)
        {
            string = string + "<li>" + item.toString() + "</li>";
        }
        string = string + "]</ul>]";
        return string;
    }
}
