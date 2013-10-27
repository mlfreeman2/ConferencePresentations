package org.mlfreeman.trayappcontainer;

import java.awt.Menu;

/**
 * The interface that all tray icon compatible components must conform to
 */
public interface ITrayAppComponent
{
    
    /**
     * When the tray icon's menu's exit option is clicked on this method is called in every component
     */
    void onExit();
    
    /**
     * When the icon is added, we call this method to allow the component to add to the popup menu. <br>
     * The exit menu option added by the host container will be reinserted below this to ensure it is always at the bottom.
     * 
     * @param menu
     *            The tray icon's popup menu
     */
    void onLaunch(Menu menu);
}