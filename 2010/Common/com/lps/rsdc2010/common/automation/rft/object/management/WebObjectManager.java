package com.lps.rsdc2010.common.automation.rft.object.management;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import com.lps.rsdc2010.common.automation.rft.object.Browser;
import com.lps.rsdc2010.common.automation.rft.object.actions.WebActions;
import com.lps.rsdc2010.common.lang.StringFunctions;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.RationalTestScript;

/**
 * A simple subclass of ObjectManager. Right now it doesn't have any web specific actions, but later on it might
 */
public class WebObjectManager extends ObjectManager
{
    protected WebActions webSpecificActions = new WebActions();
    protected Browser    theBrowser         = null;
    
    public WebObjectManager(String screenName, Browser baseWindow)
    {
        this(screenName);
        setBaseObject(baseWindow.browser);
        theBrowser = baseWindow;
    }
    
    public WebObjectManager(String screenName)
    {
        super(screenName, new WebActions());
        webSpecificActions = (WebActions) actionLib;
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
                RationalTestScript.logError("No image URL provided for " + friendlyName + " in " + screenName);
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
                    RationalTestScript.logError("Error fetching image for " + friendlyName + " from " + src + " in " + screenName, null);
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
    
}
