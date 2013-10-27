package org.mlfreeman.trayappcontainer.applets.hazelcast;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.metal.MetalIconFactory;

import org.apache.log4j.Logger;
import org.mlfreeman.trayappcontainer.ITrayAppComponent;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * The main GUI for the Hazelcast instance host container
 */
public class GroupContainer extends JFrame implements ITrayAppComponent
{
    
    private static final long                            serialVersionUID    = -1664139487565364578L;
    
    /**
     * A structure that tracks configurations and group names. Eventually functionality may be added to update this on the fly
     */
    public static Map<String, Config>                    groupConfigurations = new LinkedHashMap<String, Config>();
    
    /**
     * A structure that tracks instances and group names.
     */
    public static Map<String, HazelcastInstance>         hazelcastInstances  = new LinkedHashMap<String, HazelcastInstance>();
    
    /**
     * A structure that maps UI tabs to Hazelcast groups
     */
    private final Map<String, HazelcastGroupStatusPanel> groupPanels         = new LinkedHashMap<String, HazelcastGroupStatusPanel>();
    
    private final JTabbedPane                            tabbedPane          = new JTabbedPane();
    
    public GroupContainer()
    {
        System.setProperty("hazelcast.logging.type", "log4j");
        
        setVisible(false);
        setTitle("Hazelcast Instance Container");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //
        // Add content to the window.
        JPanel panel = new JPanel(new GridLayout(1, 1));
        add(panel, BorderLayout.CENTER);
        //
        // Add the tab control to the window
        panel.add(tabbedPane);
        //
        pack();
        setBounds(0, 0, 800, 600);
        //
        // The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        //
        try
        {
            // We look in the directory that contains the JAR for a "configs" subdirectory.
            // In there we should find one hazelcast XML config file for each hazelcast group to join.
            // In the future we may add some kind of monitor to watch for changed or new config files and react automatically.
            // Right now you have to restart the app to get changes or new configs to be detected
            java.io.File dir = new java.io.File(System.getProperty("hzc.configs", "configs"));
            if (!dir.exists() || !dir.isDirectory())
            {
                JOptionPane.showMessageDialog(null, "No config files found to use. Please select a directory to load config files from.", "", JOptionPane.ERROR_MESSAGE);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showOpenDialog(null);
                dir = fileChooser.getSelectedFile();
            }
            
            java.io.File[] files = dir.listFiles();
            if (files != null)
            {
                for (java.io.File directoryEntry : files)
                {
                    if (directoryEntry.getName().endsWith(".xml"))
                    {
                        // Attempt to load this configuration unless we've already seen a hazelcast group with that name already.
                        XmlConfigBuilder xcb = new XmlConfigBuilder(directoryEntry.getAbsolutePath());
                        Config c = xcb.build();
                        if (!groupConfigurations.containsKey(c.getGroupConfig().getName()))
                        {
                            // Create another hazelcast instance for each group
                            HazelcastInstance hci = Hazelcast.newHazelcastInstance(c);
                            hazelcastInstances.put(c.getGroupConfig().getName(), hci);
                            groupConfigurations.put(c.getGroupConfig().getName(), c);
                            //
                            // Add another tab to the UI
                            HazelcastGroupStatusPanel panel1 = new HazelcastGroupStatusPanel(c.getGroupConfig().getName());
                            groupPanels.put(c.getGroupConfig().getName(), panel1);
                            tabbedPane.addTab(c.getGroupConfig().getName(), MetalIconFactory.getTreeComputerIcon(), panel1, "");
                        }
                        else
                        {
                            Logger.getLogger(getClass()).error("Could not join " + directoryEntry.getAbsolutePath() + " (" + c.getGroupConfig().getName() + "). A group with that name already exists.");
                        }
                    }
                }
            }
            
        }
        catch (Exception e)
        {
            
        }
        setVisible(true);
    }
    
    @Override
    public void onExit()
    {
        setVisible(false);
        dispose();
        Hazelcast.shutdownAll();
    }
    
    @Override
    public void onLaunch(Menu menu)
    {
        menu.setLabel("Hazelcast Groups");
        for (String s : groupPanels.keySet())
        {
            final MenuItem mi = new MenuItem(s);
            mi.addActionListener(new ActionListener()
            {
                
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    tabbedPane.setSelectedComponent(groupPanels.get(mi.getLabel()));
                    setVisible(true);
                }
            });
            menu.add(mi);
        }
    }
}
