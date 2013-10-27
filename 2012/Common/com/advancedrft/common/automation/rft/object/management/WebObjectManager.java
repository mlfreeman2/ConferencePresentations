package com.advancedrft.common.automation.rft.object.management;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.object.Browser;
import com.advancedrft.common.automation.rft.object.actions.WebActions;
import com.advancedrft.common.lang.StringFunctions;
import com.rational.test.ft.object.interfaces.TestObject;

/**
 * A subclass of ObjectManager meant to deal with web domain applications.<br>
 * Right now it includes one web-specific additional action: the ability to download images.<br>
 * Typically you would create one of these for each page in your webapp and set the browser you're using as the base object.<br>
 */
public class WebObjectManager extends ObjectManager
{
    protected WebActions webSpecificActions = new WebActions();
    
    protected Browser    theBrowser;
    
    public WebObjectManager(String screenName)
    {
        super(screenName, new WebActions());
        webSpecificActions = (WebActions) actionLib;
    }
    
    public WebObjectManager(String screenName, Browser baseWindow)
    {
        this(screenName);
        setBaseObject(baseWindow.browser);
        theBrowser = baseWindow;
    }
    
    /**
     * This function can download an image as a BufferedImage for use in RFT logs.
     * 
     * @param friendlyName
     *            Name of the image object to download
     * @return The image as a BufferedImage, suitable for logging in RFT
     */
    public BufferedImage getImage(String friendlyName)
    {
        try
        {
            TestObject imageObject = findDefinedItem(friendlyName);
            String dotClass = StringFunctions.ifNull(imageObject.getProperty(".class"));
            String src = dotClass.equals("Html.IMG") == true || dotClass.equals("Html.INPUT.image") == true ? StringFunctions.ifNull(imageObject.getProperty(".src")) : "";
            if (src.equals(""))
            {
                Log.logError("No image URL provided for " + friendlyName + " in " + screenName);
                return null;
            }
            else
            {
                try
                {
                    BufferedImage b = ImageIO.read(new URL(src));
                    return b;
                }
                catch (Exception e)
                {
                    Log.logError("Error fetching image for " + friendlyName + " from " + src + " in " + screenName);
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
    
    public Browser getTheBrowser()
    {
        return theBrowser;
    }
    
}
