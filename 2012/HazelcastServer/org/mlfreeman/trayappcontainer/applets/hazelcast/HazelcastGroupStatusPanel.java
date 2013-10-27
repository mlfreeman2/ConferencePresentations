package org.mlfreeman.trayappcontainer.applets.hazelcast;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.metal.MetalIconFactory;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

/**
 * A UI panel that displays information on a single Hazelcast instance. <br>
 */
public class HazelcastGroupStatusPanel extends JPanel
{
    private final String      groupName;
    
    private static final long serialVersionUID      = -634736612477722066L;
    
    private final JScrollPane descriptionScrollPane = new JScrollPane();
    
    private final JTextPane   descriptionText       = new JTextPane();
    
    private final Icon        stopButtonIcon        = new ImageIcon(new byte[] {71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -60, 0, 0, -72, -64, -24, -72, -64, -32, -80, -72, -48, -80, -64, -32, -72, -56, -24, 112, -112, -56, 120, -104, -56, -72, -56, -32, 64, 104, -96, 96, -120, -64, 104, -112, -56, -96, -72, -40, -88, -64, -32, 104, -112, -64, -112, -80, -40, 56, 104, -96, 88, -120, -64, 40, 96, -104, -104, -72, -40, -80, -56, -32, 16, 88, -104, 48, 104, -104, 0, 80, -112, 8, 88, -104, 24, 96, -104, -128, -96, -72, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, -7, 4, 1, 0, 0, 26, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 5, 91, -96, 38, -114, 100, 105, -98, 104, 106, 10, 72, -21, -70, 66, -119, 0, 68, 109, 19, 0, 34, 7, 70, -17, 27, 1, 29, -23, 49, -8, -7, 6, -113, 82, 101, 80, 104, 58, 11, -125, 74, 41, -78, 72, 88, -81, -119, 69, -92, -124, 113, 64, -66, 96, -120, 3, 83, -94, 72, -80, -41, 5, -91, 116, 97, 40, 26, -16, -122, 66, -63, -72, -108, 44, -109, -125, 126, 127, -104, 88, 74, 25, 22, -126, -125, -125, 25, 42, -121, -120, 26, 33, 0, 59});
    
    private final Icon        playButtonIcon        = new ImageIcon(new byte[] {71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -43, 0, 0, 37, 117, 80, 45, 124, 88, 54, -126, 94, 65, -98, 99, 88, -85, 113, 88, -86, 113, 77, -89, 102, 77, -89, 101, 77, -90, 101, 97, -81, 97, 97, -80, 96, 109, -75, 95, 109, -75, 94, -109, -59, -121, -109, -59, -122, 120, -70, 94, -94, -53, -117, -93, -53, -118, -84, -49, -114, -8, -8, -24, -8, -16, -80, -8, -24, -104, -72, -104, 40, -8, -32, -120, -16, -32, -88, -66, -100, 40, -48, -72, 104, -72, -112, 32, -8, -40, -128, -88, -128, 24, -96, 120, 24, -80, -120, 32, -64, -96, 96, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, -7, 4, 1, 0, 0, 33, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 6, 110, -64, -112, 112, 72, 44, 26, -113, -56, 36, 81, -109, -79, 88, 52, 33, -127, 0, -71, -103, 88, 55, 81, -119, -44, -72, -95, 120, -79, 2, 8, 36, -78, 29, 126, 40, 21, -54, 39, -22, 104, 55, 26, -127, -127, -16, -125, -31, 96, -42, 2, -126, 94, 95, 32, 4, 66, 116, 118, 107, 1, 6, -123, 7, 7, 8, 7, 127, 103, 23, 106, 33, 0, 9, -111, 10, 10, 0, 114, 33, 29, 94, 20, 29, -113, 11, 11, 12, 0, 0, 68, 29, 86, 19, -101, 0, 15, -96, 70, 32, 30, -84, 32, -113, -95, 74, -79, -78, -77, 72, 65, 0, 59});
    
    private final JButton     stopButton            = new JButton(stopButtonIcon);
    
    private final JButton     displayMembersButton  = new JButton(MetalIconFactory.getTreeComputerIcon());
    
    private final JToolBar    toolbar               = new JToolBar();
    
    private final Lock        inProgress            = new ReentrantLock();
    
    public HazelcastGroupStatusPanel(String gName)
    {
        this.groupName = gName;
        //
        stopButton.setName("shut_down_instance");
        stopButton.setActionCommand("shut_down_instance");
        stopButton.setBorderPainted(false);
        stopButton.setPreferredSize(new Dimension(24, 24));
        stopButton.setToolTipText("Shut Down Instance");
        stopButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Spin off a separate thread to shut down the hazelcast instance. Keeps the UI responsive.
                Thread t = new Thread()
                {
                    public void run()
                    {
                        // We need to synchronize everything in case the user hits the button twice in quick succession
                        // If this lock is taken elsewhere that means either a shutdown or a startup of this group is in progress already so we don't want to do anything.
                        if (inProgress.tryLock())
                        {
                            // If this group is alive, we want to shut it down
                            if (GroupContainer.hazelcastInstances.containsKey(groupName))
                            {
                                stopButton.setIcon(playButtonIcon);
                                stopButton.setToolTipText("Start Up Instance");
                                GroupContainer.hazelcastInstances.get(groupName).getLifecycleService().shutdown();
                                GroupContainer.hazelcastInstances.remove(groupName);
                                descriptionText.setText("This machine is no longer a member of " + groupName + ".");
                            }
                            else
                            {
                                stopButton.setIcon(stopButtonIcon);
                                stopButton.setToolTipText("Shut Down Instance");
                                HazelcastInstance hci = Hazelcast.newHazelcastInstance(GroupContainer.groupConfigurations.get(groupName));
                                GroupContainer.hazelcastInstances.put(groupName, hci);
                                descriptionText.setText("This machine has rejoined " + groupName + ".");
                            }
                            inProgress.unlock();
                        }
                    }
                };
                t.start();
            }
        });
        //
        displayMembersButton.setName("display_cluster_members");
        displayMembersButton.setActionCommand("display_cluster_members");
        displayMembersButton.setBorderPainted(false);
        displayMembersButton.setPreferredSize(new Dimension(24, 24));
        displayMembersButton.setToolTipText("Display Cluster Members");
        displayMembersButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (GroupContainer.hazelcastInstances.containsKey(groupName))
                {
                    final Lock inProgress = new ReentrantLock();
                    Thread t = new Thread()
                    {
                        public void run()
                        {
                            if (inProgress.tryLock())
                            {
                                String membersLine = "Current Members of " + groupName + " (" + formatCalendar("EEE, dd MMM yyyy HH:mm:ss Z") + "):";
                                String membersNextLine = "";
                                for (int i = 0; i < membersLine.length(); i++)
                                {
                                    membersNextLine += "-";
                                }
                                String memberText = membersLine + System.getProperty("line.separator") + membersNextLine + System.getProperty("line.separator");
                                HazelcastInstance hi = GroupContainer.hazelcastInstances.get(groupName);
                                Set<Member> members = hi.getCluster().getMembers();
                                for (Member m : members)
                                {
                                    InetSocketAddress isa = m.getInetSocketAddress();
                                    if (m.localMember())
                                    {
                                        memberText += "(me) ";
                                    }
                                    memberText += isa.getAddress().getHostAddress() + ":" + isa.getPort() + System.getProperty("line.separator");
                                }
                                memberText += System.getProperty("line.separator");
                                descriptionText.setText("");
                                descriptionText.setText(memberText);
                                inProgress.unlock();
                            }
                        }
                    };
                    
                    t.start();
                }
                else
                {
                    descriptionText.setText("This machine is not connected to " + groupName + ".");
                }
            }
        });
        //
        toolbar.setFloatable(false);
        toolbar.setOrientation(0);
        toolbar.add(stopButton);
        toolbar.add(displayMembersButton);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.setMaximumSize(new Dimension(getWidth(), stopButton.getIcon().getIconHeight() + 5));
        //
        descriptionText.setEditable(false);
        descriptionText.setText("");
        descriptionText.setFont(new Font("Courier New", 0, 13));
        descriptionText.setToolTipText("Messages");
        descriptionText.setFocusable(false);
        //
        descriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        descriptionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScrollPane.setViewportView(descriptionText);
        //
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(Box.createVerticalStrut(3));
        add(descriptionScrollPane, BorderLayout.CENTER);
        //
        toolbar.setAlignmentX(0.0F);
        descriptionScrollPane.setAlignmentX(0.0F);
    }
    
    private String formatCalendar(String format)
    {
        Calendar c = new GregorianCalendar();
        SimpleDateFormat commonDateFormat = new SimpleDateFormat(format);
        GregorianCalendar twoDigitYears = new GregorianCalendar();
        twoDigitYears.set(Calendar.YEAR, 2000);
        twoDigitYears.set(Calendar.MONTH, Calendar.JANUARY);
        twoDigitYears.set(Calendar.DAY_OF_MONTH, 1);
        twoDigitYears.set(Calendar.HOUR_OF_DAY, 0);
        twoDigitYears.set(Calendar.MINUTE, 0);
        commonDateFormat.set2DigitYearStart(twoDigitYears.getTime());
        commonDateFormat.setTimeZone(c.getTimeZone());
        return commonDateFormat.format(c.getTime());
    }
    
    public String getGroupName()
    {
        return groupName;
    }
}