package com.advancedrft.ibminnovate.amazon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.advancedrft.common.automation.rft.KeyboardAndMouse.MouseButtons;
import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.advancedrft.common.lang.StringFunctions;
import com.advancedrft.ibminnovate.amazon.data.AmazonItem;
import com.advancedrft.ibminnovate.amazon.data.WishList;
import com.advancedrft.ibminnovate.amazon.data.wishlist.CreateWishList;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;

/**
 * This class represents a basic amazon.com wish list
 */
public class WishListScreen extends AmazonShell
{
    private String              name        = new String();
    
    /**
     * A cached mapping of wish list names to ASIN-like wish list identifiers
     */
    private Map<String, String> wishlistIDs = new LinkedHashMap<String, String>();
    
    public WishListScreen(String wishListName)
    {
        super("Amazon Wish List \"" + wishListName + "\"");
        setName(wishListName);
        addItems();
    }
    
    public WishListScreen(String wishListName, Browser baseWindow)
    {
        super("Amazon Wish List \"" + wishListName + "\"", baseWindow);
        setName(wishListName);
        addItems();
    }
    
    /**
     * Stores all the object definition for Amazon's Wish List Page (no need for a map)
     */
    private void addItems()
    {
        addItemToTable("List Title", ".class", "Html.SPAN", ".id", "listTitleTxt");
        addItemToTable("Edit list name", ".class", "Html.SPAN", ".id", "listTitleHoverLink");
        addItemToTable("List Title Input", ".class", "Html.INPUT.text", ".id", "listTitleInput");
        addItemToTable("List Title Save", ".class", "Html.SPAN", ".id", "listTitleSave");
        
        addItemToTable("Create a new list", ".class", "Html.IMG", ".alt", "Create a list", ".src", "regex=.*btn-createanewlist._V192250037_.gif");
        
        addItemToTable("Create Another Wish List", ".class", "Html.BUTTON", ".id", "regListCreate");
        addItemToTable("Create Another Wish List -> List Name", ".class", "Html.INPUT.text", ".id", "createNewWLContent_t");
        addItemToTable("Create Another Wish List -> Make this list public", ".class", "Html.INPUT.radio", ".id", "createNewWLContent_r0");
        addItemToTable("Create Another Wish List -> Make this list private", ".class", "Html.INPUT.radio", ".id", "createNewWLContent_r1");
        addItemToTable("Create Another Wish List -> Save", ".class", "Html.INPUT.image", ".id", "createNewWLSave");
        addItemToTable("Create Another Wish List -> Cancel", ".class", "Html.INPUT.image", ".id", "createNewWLCancel");
        
        addItemToTable("Private Wish List Container", ".class", "Html.DIV", ".id", "regListprivateBlock");
        addItemToTable("Public Wish List Container", ".class", "Html.DIV", ".id", "regListpublicBlock");
        addItemToTable("Private Wish List", "nf==externalStartingPoint", "Private Wish List Container", ".class", "Html.DIV");
        addItemToTable("Public Wish List", "nf==externalStartingPoint", "Public Wish List Container", ".class", "Html.DIV");
        
        addItemToTable("Links to my Wish Lists", ".class", "Html.A", ".href", "contains=cm_wl_rlist_go");
        addItemToTable("Wish List Product Names", ".class", "Html.SPAN", ".className", "small productTitle");
        addItemToTable("Add to Cart", ".class", "Html.A", ".href", "contains=cm_wl_addtocart_o");
        addItemToTable("Wishlist Items", ".class", "Html.TBODY", ".id", "regex=item\\.\\d+\\..*");
        addItemToTable("Move to another list", ".class", "Html.DIV", ".id", "regex=moveItem_.*");
        addItemToTable("Destination Wishlist Link", ".class", "Html.DIV", ".id", "regex=wl\\-list\\-name\\-.*");
        addItemToTable("Delete item", ".class", "Html.INPUT.submit", ".value", "Delete item", ".id", "regex=submit\\.delete\\..*");
        addItemToTable("Wish List Info Div", ".class", "Html.DIV", ".id", "regex=regListsList.*");
        addItemToTable("Wish List Item Count", ".class", "Html.SPAN", ".className", "regListCount", "nf==externalStartingPoint", "Wish List Info Div");
        getAllWishListNames(true);
    }
    
    /**
     * Creates a new wish list. Handles if there are no wish list or if some exists it will add another wish lists
     * 
     * @param name
     *            name of the wish list
     * @param isPrivate
     *            a list can be public or private
     * @return WishList
     */
    public WishList createNewList(String name, boolean isPrivate)
    {
        WishList wl = new WishList();
        int orgFindTries = getNumberOfFindTries();
        setNumberOfFindTries(1);
        if (countAppearances("Create a new list", false) > 0)
        {
            click("Create a new list");
            setNumberOfFindTries(orgFindTries);
            editListNameByHover(name);
            wl.setName(name);
            wl.setId("");// TODO find a way to get the ID
            wl.setPrivate(isPrivate); // By default amazon make it private
        }
        else if (countAppearances("Create Another Wish List", false) > 0)
        {
            click("Create Another Wish List");
            setNumberOfFindTries(orgFindTries);
            wl.setName(type("Create Another Wish List -> List Name", name, true));
            wl.setPrivate(isPrivate);
            if (isPrivate)
            {
                click("Create Another Wish List -> Make this list private");
            }
            else
            {
                click("Create Another Wish List -> Make this list public");
            }
            click("Create Another Wish List -> Save");
            Log.logInfo("Created Another Wish List", wl.toString());
        }
        else
        {
            setNumberOfFindTries(orgFindTries);
        }
        
        // Add to hazel cast
        HazelcastAPI.<CreateWishList> getTopic(AmazonShell.signedInCustomer.getEmail() + " CreateWishList").publish(new CreateWishList(wl));
        return wl;
    }
    
    /**
     * Clicks on the specified item's "Delete item" link to remove it from the wish list.
     * 
     * @param ai
     *            The item to remove from the wish list
     * @return True if the item was removed, False otherwise (e.g. never there to remove in the first place)
     */
    public boolean deleteFromWishlist(AmazonItem ai)
    {
        String id = getItemProperty("Delete item", ".id");
        String newID = id + ai.getWishlistItemID();
        setItemProperty("Delete item", ".id", newID);
        boolean result = click("Delete item", 1, MouseButtons.Left, false, false, false, 5, 5);
        setItemProperty("Delete item", ".id", id);
        return result;
    }
    
    /**
     * Edit the list name by hovering over the list name
     * 
     * @param name
     *            desired name of the wish list
     */
    public void editListNameByHover(String name)
    {
        hover("List Title", 3, 0, 0);
        click("Edit list name");
        type("List Title Input", name, false);
        click("List Title Save");
        RationalTestScript.sleep(2);
        Log.logInfo("Changed wish list name to: " + name);
    }
    
    /**
     * Scrapes all the wish list names from the list of them on the left hand side
     * 
     * @return All the available wish lists.
     */
    public List<WishList> getAllWishList()
    {
        ArrayList<WishList> list = new ArrayList<WishList>();
        int orgFindTries = getNumberOfFindTries();
        try
        {
            setNumberOfFindTries(1);
            if (countAppearances("Create a new list", false) > 0)
            {
                return new ArrayList<WishList>();
            }
        }
        catch (Exception e)
        {
            
        }
        finally
        {
            setNumberOfFindTries(orgFindTries);
        }
        
        // Get all private wish lists
        TestObject[] lists = null;
        try
        {
            TestObject privateDIV = findDefinedItem("Private Wish List Container");
            lists = privateDIV.find(RationalTestScript.atDescendant(".class", "Html.DIV"));
            
            for (TestObject t : lists)
            {
                WishList wl = new WishList();
                String name = StringFunctions.ifNull(t.find(RationalTestScript.atDescendant(".class", "Html.A"))[0].getProperty(".text"));
                wl.setId(StringFunctions.ifNull(t.getProperty(".id")));
                wl.setName(name);
                wl.setPrivate(true);
                wishlistIDs.put(name, wl.getId()); // TODO do we even need this anymore???
                list.add(wl);
                t.unregister();
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            RationalTestScript.unregister(lists);
        }
        
        // Get all public wish list
        try
        {
            TestObject publicDIV = findDefinedItem("Public Wish List Container");
            lists = publicDIV.find(RationalTestScript.atDescendant(".class", "Html.DIV"));
            
            // TODO fix memory leaks
            for (TestObject t : lists)
            {
                WishList wl = new WishList();
                String name = StringFunctions.ifNull(t.find(RationalTestScript.atDescendant(".class", "Html.A"))[0].getProperty(".text"));
                wl.setId(StringFunctions.ifNull(t.getProperty(".id")));
                wl.setName(name);
                wl.setPrivate(false);
                wishlistIDs.put(name, wl.getId()); // TODO do we even need this anymore???
                list.add(wl);
                t.unregister();
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            RationalTestScript.unregister(lists);
        }
        return list;
    }
    
    /**
     * Returns all the wish list names we already found and cached
     * 
     * @return All the wish list names we already found and cached
     */
    public List<String> getAllWishListNames()
    {
        return getAllWishListNames(false);
    }
    
    /**
     * Scrapes all the wish list names from the list of them on the left hand side, or returns the keys in {@link #wishlistIDs}
     * 
     * @param refresh
     *            True to scrape the page for wish list names and IDs and then to update {@link #wishlistIDs}, False to return the keys from {@link #wishlistIDs}.
     * @return All the available wish lists.
     */
    public List<String> getAllWishListNames(boolean refresh)
    {
        if (refresh)
        {
            int orgFindTries = getNumberOfFindTries();
            try
            {
                setNumberOfFindTries(1);
                if (countAppearances("Create a new list", false) > 0)
                {
                    return new ArrayList<String>();
                }
            }
            catch (Exception e)
            {
                
            }
            finally
            {
                setNumberOfFindTries(orgFindTries);
            }
            TestObject[] lists = null;
            try
            {
                lists = findDefinedItems("Links to my Wish Lists");
                for (TestObject t : lists)
                {
                    String name = StringFunctions.ifNull(t.getProperty(".text"));
                    String ID = StringFunctions.ifNull(t.getProperty(".href")).replace("http://www.amazon.com/wishlist/", "").replace("/ref=cm_wl_rlist_go", "");
                    wishlistIDs.put(name, ID);
                }
            }
            catch (Exception e)
            {
                
            }
            RationalTestScript.unregister(lists);
        }
        return new ArrayList<String>(wishlistIDs.keySet());
    }
    
    /**
     * Get the ID from the left side of the page.<br>
     * Since Amazon allows duplicate wish list names, it assigned a ID to each however its a hidden value
     * 
     * @param name
     *            The ID of the wishlist
     */
    public void getID(String name)
    {
        // TODO implement
    }
    
    /**
     * Gets the number of items in the given wish list. Uses the counts below each wish list on the left side
     * 
     * @param name
     *            The name of the wish list
     * @return The number of items in it, or -1 if an error occurred
     */
    public int getWishlistItemCount(String name)
    {
        String id = getItemProperty("Wish List Info Div", ".id");
        String newID = id + wishlistIDs.get(name);
        setItemProperty("Wish List Info Div", ".id", newID);
        try
        {
            int count = Integer.parseInt(StringFunctions.ifNull(getItemRuntimeProperty("Wish List Item Count", ".text")));
            return count;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }
    
    /**
     * Get the wish list name
     * 
     * @return The name of the wishlist we have open
     */
    public String getWishListName()
    {
        return name;
    }
    
    /**
     * Checks to see if the supplied item is present on the current page of the current wish list.
     * 
     * @param ai
     *            The item to look for on this wish list page
     * @return True if it is, False if it isn't. If it is, the {@link AmazonItem#wishlistItemID} field will be updated with its ID
     */
    public boolean isItemPresentOnPage(AmazonItem ai)
    {
        String id = getItemProperty("Wishlist Items", ".id");
        String newID = id + ai.getItemASIN();
        setItemProperty("Wishlist Items", ".id", newID);
        if (itemExists("Wishlist Items"))
        {
            String wishlistID = StringFunctions.ifNull(getItemRuntimeProperty("Wishlist Items", ".id")).replaceAll("item\\.\\d+\\.", "").replace(ai.getItemASIN(), "");
            ai.setWishlistItemID(wishlistID);
            setItemProperty("Wishlist Items", ".id", id);
            return true;
        }
        else
        {
            setItemProperty("Wishlist Items", ".id", id);
            return false;
        }
    }
    
    /**
     * Switches to the specified wish list by clicking on the link on the left hand side
     * 
     * @param name
     *            Name of the wish list to switch to
     * @return True if we switched to it, False otherwise
     */
    public boolean loadWishList(String name)
    {
        setItemProperty("Links to my Wish Lists", ".text", name);
        boolean result = click("Links to my Wish Lists", 1, MouseButtons.Left, false, false, false, 5, 5);
        deleteItemProperty("Links to my Wish Lists", ".text");
        theBrowser.sync(120, 0.5);
        Log.logInfo("Loaded wish list : " + name);
        return result;
    }
    
    /**
     * Clicks on the specified item's "Move to wish list" link and then selects to move it to the supplied wish list
     * 
     * @param ai
     *            The item to move from this wish list
     * @param newWishlistName
     *            The name of the wish list to move the item to
     * @return True if the item was moved, False otherwise (e.g. item wasn't actually on this page of the list)
     */
    public boolean moveToWishlist(AmazonItem ai, String newWishlistName)
    {
        String id = getItemProperty("Move to another list", ".id");
        String newID = id + ai.getWishlistItemID();
        setItemProperty("Move to another list", ".id", newID);
        boolean result = click("Move to another list", 1, MouseButtons.Left, false, false, false, 5, 5);
        setItemProperty("Move to another list", ".id", id);
        //
        if (!result)
        {
            return false;
        }
        setItemProperty("Destination Wishlist Link", ".text", newWishlistName);
        result = click("Destination Wishlist Link", 1, MouseButtons.Left, false, false, false, 5, 5);
        deleteItemProperty("Destination Wishlist Link", ".text");
        theBrowser.sync(120, 0.5);
        return result;
    }
    
    /**
     * Set the wish list name
     * 
     * @param wishListName
     *            name of the wish list
     */
    public void setName(String wishListName)
    {
        name = wishListName;
    }
    
}