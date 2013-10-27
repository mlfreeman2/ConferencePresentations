package org.mlfreeman.trayappcontainer;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalIconFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.mlfreeman.trayappcontainer.applets.hazelcast.GroupContainer;

/**
 * A basic container that can associate one or more modules with a tray icon.<br>
 * Modules should implement {@link ITrayAppComponent} so that they get handed the popup menu on launch and get notified when the exit option is chosen. <br>
 * There are no safeguards stopping a module from calling System.exit on its own or adding an exit menu item on its own. Just don't do it. This isn't resilient enough to handle that.
 */
public class GenericTrayIcon
{
    /**
     * This function returns a generic system tray icon for use with this class (if no icon is supplied.
     * 
     * @return A generic icon included in the JRE.
     */
    public static Image getDefaultIcon()
    {
        // Use a generic JRE-included computer icon for the system tray.
        // We have to convert it to a java.awt.Image though first, so we draw it on to a BufferedImage
        Icon defaultIcon = MetalIconFactory.getTreeComputerIcon();
        Image img = new BufferedImage(defaultIcon.getIconWidth(), defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
        return img;
    }
    
    /**
     * The tray app container's entry point. Currently, settings an only be tweaked by Java system properties. <br>
     * <i>log.pattern</i> - The pattern that Log4J should use when logging. <br>
     * <i>log.path</i> - The path that Log4J should log to. <br>
     * <i>log.backup.files</i> - The number of previous log files that Log4J should keep around <br>
     * <i>log.individual.file.size</i> - The maximum size that each log file should be allowed to grow to <br>
     * <i>log.levels.overall</i> - The level that Log4J should log at. See the Log4J docs for possible levels.
     * 
     * @param args
     *            None at the moment.
     */
    public static void main(String[] args)
    {
        GenericTrayIcon gti = new GenericTrayIcon("Tray Applet Container", GenericTrayIcon.getDefaultIcon());
        gti.addComponent(new GroupContainer());
    }
    
    private final List<ITrayAppComponent> hostedComponents = new ArrayList<ITrayAppComponent>();
    
    private TrayIcon                      icon;
    
    private MenuItem                      exit;
    
    public GenericTrayIcon(String tooltip, Image trayIcon, ITrayAppComponent...components)
    {
        // Setup the rolling log file appender. Note the last arg, false,
        // which tells this RollingFileAppender to overwrite what's already there
        // instead of appending to existing logs.
        RollingFileAppender rfp = null;
        try
        {
            rfp = new RollingFileAppender(new PatternLayout(System.getProperty("log.pattern", "%d{ISO8601} %5p %c{1}:%L - %m%n")), new File(System.getProperty("log.path", "logs/trayAppContainer.log")).getCanonicalPath(), false);
        }
        catch (IOException e2)
        {
            JOptionPane.showMessageDialog(null, "Unable to initialize logging.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        //
        // We want the logger to flush its output to the log file
        // stream immediately; if you don't have this set, then
        // Log4j will buffer the log file output which isn't ideal.
        rfp.setImmediateFlush(true);
        rfp.setBufferedIO(false);
        rfp.setBufferSize(1024);
        //
        // Set the Max number of files and max size of each log
        // file to keep around.
        rfp.setMaxBackupIndex(Integer.parseInt(System.getProperty("log.backup.files", "200")));
        rfp.setMaxFileSize(System.getProperty("log.individual.file.size", "1024KB"));
        Logger.getRootLogger().addAppender(rfp);
        Logger.getRootLogger().setLevel(Level.toLevel(System.getProperty("log.levels.overall", "WARNING")));
        //
        if (trayIcon == null)
        {
            trayIcon = getDefaultIcon();
        }
        // Create the actual tray icon
        icon = new TrayIcon(trayIcon, tooltip, new PopupMenu());
        // Add the icon to the system tray.
        if (SystemTray.isSupported())
        {
            try
            {
                SystemTray.getSystemTray().add(icon);
            }
            catch (AWTException e)
            {
                JOptionPane.showMessageDialog(null, "Unable to add system tray icon. Exiting.", "Fatal Error", JOptionPane.OK_OPTION);
                System.exit(2);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Unable to add system tray icon. Exiting.", "Fatal Error", JOptionPane.OK_OPTION);
            System.exit(2);
        }
        //
        for (ITrayAppComponent c : components)
        {
            addComponent(c);
        }
        //
        exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (ITrayAppComponent sd : hostedComponents)
                {
                    if (sd != null)
                    {
                        sd.onExit();
                    }
                }
                System.exit(0);
            }
        });
        icon.getPopupMenu().add(exit);
        
    }
    
    private void addComponent(ITrayAppComponent c)
    {
        hostedComponents.add(c);
        // remove the exit menu item
        icon.getPopupMenu().remove(exit);
        // create a sub menu and add it to the main menu and pass it to the plugin so that the plugin can add items to it as it deems fit
        Menu m = new Menu();
        icon.getPopupMenu().add(m);
        c.onLaunch(m);
        // re-add the exit menu item so that it will always be at the bottom.
        icon.getPopupMenu().add(exit);
    }
}
