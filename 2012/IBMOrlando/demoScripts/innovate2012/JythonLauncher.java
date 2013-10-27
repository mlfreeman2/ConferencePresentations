package demoScripts.innovate2012;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.DatatypeConverter;

import org.python.core.PyException;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTraceback;
import org.python.util.PythonInterpreter;

import resources.demoScripts.innovate2012.JythonLauncherHelper;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.automation.rft.Log.LogScrubber;
import com.advancedrft.common.lang.Reflection;
import com.advancedrft.common.lang.StringFunctions;
import com.ibm.rational.test.ft.sdk.RFTCore;
import com.rational.test.ft.script.IScriptDefinition;
import com.rational.test.ft.script.RationalTestScript;
import com.rational.test.ft.services.ILog;
import com.rational.test.ft.services.ILogMessage;
import com.rational.test.ft.services.IPlaybackMonitor;
import com.rational.test.ft.services.ISimpleLog;
import com.rational.test.util.ServiceBroker;

/**
 * Description : Functional Test Script
 * 
 * @author vmuser
 */
public class JythonLauncher extends JythonLauncherHelper
{
    static CountDownLatch     cb         = new CountDownLatch(1);
    
    private List<File>        scripts    = new ArrayList<File>();
    
    private Map<File, String> scriptArgs = new LinkedHashMap<File, String>();
    
    private Map<File, String> scriptLogs = new LinkedHashMap<File, String>();
    
    /**
     * Script Name : <b>JythonLauncher</b><br>
     * Generated : <b>Apr 25, 2012 9:49:31 PM</b><br>
     * Description : Functional Test Script<br>
     * Original Host : WinNT Version 6.1 Build 7601 (S)
     * 
     * @since 2012/04/25
     * @author Jarga
     * @param args
     *            The script arguments
     */
    public void testMain(Object[] args)
    {
        ILog originalLog = RFTCore.getScriptExecutionArgs().getScriptPlaybackLog();
        Log.rft = originalLog;
        if (Log.rft.getClass().getSimpleName().startsWith("HTML"))
        {
            Runtime.getRuntime().addShutdownHook(new LogScrubber(Log.rft.getLogDirectory() + "/rational_ft_logframe.html"));
        }
        
        if (args != null && args.length > 0)
        {
            Log.logWarning("Launch arguments were provided, but they will be ignored.");
        }
        
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            Log.logError("Internal error setting up launcher UI.");
            Log.logException(e);
            return;
        }
        
        File f = null;
        
        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
        
        if (isDebug)
        {
            try
            {
                Object o = Reflection.executeMethod(null, "org.eclipse.core.runtime.Platform", "getBundle", "org.python.pydev.debug");
                Object o1 = Reflection.executeMethod(o, "org.eclipse.osgi.framework.internal.core.AbstractBundle", "getEntry", "/pysrc/");
                URL url = (URL) Reflection.executeMethod(null, "org.eclipse.core.runtime.FileLocator", "toFileURL", o1);
                try
                {
                    f = new File(url.toURI());
                }
                catch (URISyntaxException e)
                {
                    try
                    {
                        f = new File(url.getPath());
                    }
                    catch (Exception ex)
                    {
                        Log.logError("Unable to locate PyDev debugger code. Error parsing the path into a File object.");
                        Log.logException(e);
                        return;
                    }
                }
            }
            catch (Exception e)
            {
                Log.logError("Unable to locate PyDev debugger code.");
                Log.logException(e);
                return;
            }
        }
        
        IPlaybackMonitor ipbm = (IPlaybackMonitor) ServiceBroker.getServiceBroker().findService(IPlaybackMonitor.class.getName());
        ipbm.setVisible(false);
        
        FileChooser fc = new FileChooser(getCurrentProject().getLocation());
        try
        {
            cb.await();
        }
        catch (InterruptedException e)
        {
            return;
        }
        scripts.addAll(fc.getScripts());
        scriptArgs.putAll(fc.getScriptArgs());
        scriptLogs.putAll(fc.getScriptLogs());
        if (scripts.size() == 0)
        {
            return;
        }
        ipbm.setVisible(true);
        
        for (File s : scripts)
        {
            String logName = s.getName();
            try
            {
                unregisterAll();
                PythonInterpreter interpreter = new PythonInterpreter();
                if (isDebug && f != null)
                {
                    try
                    {
                        ipbm.setDescription(IPlaybackMonitor.RUNNING, "Initializing PyDev Debug Connection. Please Wait.");
                        interpreter.getSystemState().path.append(new PyString(f.getAbsolutePath()));
                        interpreter.exec(new PyString("import pydevd; pydevd.settrace(suspend=False);"));
                    }
                    catch (PyException e)
                    {
                        Log.logError("Unable to initalize PyDev debugger connection.");
                        logPyException(Log.rft, e);
                        return;
                    }
                    catch (Exception e)
                    {
                        Log.logError("Unable to initalize PyDev debugger connection.");
                        Log.logException(e);
                        return;
                    }
                    ipbm.setDescription(IPlaybackMonitor.RUNNING, "");
                }
                
                interpreter.getSystemState().argv.clear();
                if (StringFunctions.ifNull(scriptArgs.get(s)).trim().length() > 0)
                {
                    PythonInterpreter shlex = new PythonInterpreter();
                    shlex.set("cmdLine", scriptArgs.get(s));
                    shlex.exec("import shlex; result = shlex.split(cmdLine);");
                    PyObject resultO = shlex.get("result");
                    PyList result = new PyList(resultO);
                    interpreter.getSystemState().argv.addAll(result);
                }
                
                if (scriptLogs.containsKey(s))
                {
                    logName = scriptLogs.get(s);
                    Object o = RFTCore.getScriptExecutionArgs().getScriptPlaybackLog().getClass().newInstance();
                    Reflection.foreignClassLoaders.add(o.getClass().getClassLoader());
                    try
                    {
                        for (Field fld : Reflection.getFields(o.getClass()))
                        {
                            if (fld.getType().equals(String.class) && fld.getName().equals("script"))
                            {
                                fld.set(o, logName);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        logException(e);
                    }
                    // Calculate the main directory that holds all logs so that we can put this new log there too
                    File scriptLogFolder = new File(originalLog.getLogDirectory()).getParentFile();
                    // Tell the HTMLLog code where to create its log folder.
                    Reflection.executeMethod(o, o.getClass().getSuperclass().getCanonicalName(), "setLogDirectory", new Object[] {scriptLogFolder.getAbsolutePath() + File.separator + logName});
                    // Tell the HTMLLog code what name to put at the top of the HTML file
                    Reflection.executeMethod(o, o.getClass().getSuperclass().getCanonicalName(), "setLogName", new Object[] {logName});
                    // Tell the HTMLLog code to actually create the directory and the initial rational_ft_logframe.html file
                    Reflection.executeMethod(o, o.getClass().getCanonicalName(), "initLog");
                    Log.rft = (ILog) o;
                    if (Log.rft.getClass().getSimpleName().startsWith("HTML"))
                    {
                        Runtime.getRuntime().addShutdownHook(new LogScrubber(Log.rft.getLogDirectory() + "/rational_ft_logframe.html"));
                    }
                }
                else
                {
                    Log.rft = originalLog;
                }
                
                try
                {
                    Method m = Log.rft.getClass().getMethod("scriptStart", String.class, IScriptDefinition.class);
                    m.invoke(Log.rft, logName, getScriptDefinition());
                }
                catch (NoSuchMethodException e)
                {
                    try
                    {
                        Method m = Log.rft.getClass().getMethod("scriptStart", String.class, String.class);
                        m.invoke(Log.rft, logName, "java");
                    }
                    catch (Exception ex)
                    {
                        logError("Unable to start new log.");
                        logException(e);
                        Log.rft = originalLog;
                    }
                }
                catch (Exception e)
                {
                    logError("Unable to start new log.");
                    logException(e);
                    Log.rft = originalLog;
                }
                
                interpreter.execfile(s.getAbsolutePath());
                
                if (Log.rft != originalLog)
                {
                    Log.rft.scriptEnd(logName, "java");
                    Log.rft.close();
                    Log.rft = originalLog;
                }
            }
            catch (PyException e)
            {
                if (Log.rft != originalLog)
                {
                    logPyException(Log.rft, e);
                    Log.rft.scriptEnd(logName, "java");
                    Log.rft.close();
                    Log.rft = originalLog;
                }
                logPyException(originalLog, e);
            }
            catch (Exception e)
            {
                if (Log.rft != originalLog)
                {
                    Log.logException(e);
                    Log.rft.scriptEnd(logName, "java");
                    Log.rft.close();
                    Log.rft = originalLog;
                }
                Log.logException(e);
            }
        }
    }
    
    /**
     * Logs an exception thrown by something
     * 
     * @param e
     *            The exception to log
     */
    public static void logException(Throwable e)
    {
        ILogMessage ilm = Log.rft.createMessage();
        ilm.setProperty(ILog.PROP_SCRIPT_NAME, RationalTestScript.getTopScriptName());
        ilm.setHeadline((e.getClass().getName() + " - " + e.getStackTrace()[0].toString()).replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;").replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>"));
        ilm.setEvent(ISimpleLog.EVENT_GENERAL);
        ilm.setResult(ISimpleLog.LOG_FAILURE);
        
        StackTraceElement[] ste = Log.getFilteredStackTrace();
        for (int i = 0; i < ste.length; i++)
        {
            ilm.setProperty("Stack_" + i, ste[i].toString().replace("<", "&lt;").replace(">", "&gt;"));
        }
        
        ilm.setProperty(ILog.PROP_EXCEPTION_NAME, e.getClass().getName());
        ilm.setProperty(ILog.PROP_EXCEPTION_MESSAGE, e.getMessage());
        Log.rft.write(ilm);
    }
    
    /**
     * Logs an exception thrown by something
     * 
     * @param l
     *            The log to log to
     * @param e
     *            The exception to log
     */
    public static void logPyException(ILog l, PyException e)
    {
        ILogMessage ilm = l.createMessage();
        
        ilm.setProperty(ILog.PROP_SCRIPT_NAME, RationalTestScript.getTopScriptName());
        ilm.setHeadline("Python Exception: " + ((org.python.core.PyType) e.type).getMro().get(0));
        ilm.setEvent(ISimpleLog.EVENT_GENERAL);
        ilm.setResult(ISimpleLog.LOG_FAILURE);
        ilm.setProperty(ILog.PROP_EXCEPTION_MESSAGE, e.value.toString());
        
        String traceback = "<br><ul>";
        PyTraceback current = e.traceback;
        while (current != null)
        {
            String locals = StringFunctions.ifNull(current.tb_frame.f_locals).equals("null") ? "" : "<br>" + StringFunctions.ifNull(current.tb_frame.f_locals);
            String name = StringFunctions.ifNull(current.tb_frame.f_code.co_filename).equals("") ? "(Unknown)" : StringFunctions.ifNull(current.tb_frame.f_code.co_filename);
            traceback += "<li>" + name + " " + current.tb_lineno + locals + "</li>";
            current = (PyTraceback) current.tb_next;
        }
        traceback += "</ul><br>";
        ilm.setProperty(ILog.PROP_EXCEPTION_STACK, traceback);
        l.write(ilm);
    }
    
}

class FileChooser extends JFrame
{
    private static final long serialVersionUID = 7743073590660034250L;
    
    private JList             scriptList       = new JList(new DefaultListModel());
    
    private Map<File, String> scriptArguments  = new LinkedHashMap<File, String>();
    
    private Map<File, String> scriptLogs       = new LinkedHashMap<File, String>();
    
    private boolean           isRemoving;
    
    public FileChooser(final String path)
    {
        setLayout(null);
        
        final JLabel logNameLabel = new JLabel("Log Name:");
        add(logNameLabel);
        logNameLabel.setSize(150, 20);
        logNameLabel.setLocation(5, 400);
        logNameLabel.setVisible(false);
        
        final JTextField logNameLine = new JTextField();
        add(logNameLine);
        logNameLine.setSize(540, 20);
        logNameLine.setLocation(160, 400);
        logNameLine.setEnabled(false);
        logNameLine.setVisible(false);
        
        final JButton saveLogNameButton = new JButton("Save");
        add(saveLogNameButton);
        saveLogNameButton.setSize(80, 20);
        saveLogNameButton.setLocation(710, 400);
        saveLogNameButton.setEnabled(false);
        saveLogNameButton.setVisible(false);
        saveLogNameButton.setFocusable(false);
        saveLogNameButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                int selected = scriptList.getSelectedIndex();
                if (selected < 0)
                {
                    return;
                }
                DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
                if (selected > (dlm.size() - 1))
                {
                    return;
                }
                if (StringFunctions.ifNull(logNameLine.getText()).trim().length() > 0)
                {
                    scriptLogs.put(new File(dlm.get(selected).toString()), StringFunctions.ifNull(logNameLine.getText()).trim());
                }
            }
        });
        
        final JCheckBox useDedicatedLog = new JCheckBox("Use Dedicated Log");
        add(useDedicatedLog);
        useDedicatedLog.setBorder(null);
        useDedicatedLog.setSize(150, 20);
        useDedicatedLog.setLocation(5, 375);
        useDedicatedLog.setVisible(false);
        useDedicatedLog.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                JCheckBox jcb = (JCheckBox) arg0.getSource();
                if (jcb.isSelected())
                {
                    JOptionPane.showMessageDialog(FileChooser.this, "In order for dedicated logs to work, you need to use the logging functions in " + Log.class.getCanonicalName() + " rather than RationalTestScript's logging functions.", "Reminder", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        useDedicatedLog.addChangeListener(new ChangeListener()
        {
            
            @Override
            public void stateChanged(ChangeEvent changeEvent)
            {
                int selected = scriptList.getSelectedIndex();
                if (selected < 0)
                {
                    return;
                }
                DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
                if (selected > (dlm.size() - 1))
                {
                    return;
                }
                File f = new File(dlm.get(selected).toString());
                
                AbstractButton abstractButton = (AbstractButton) changeEvent.getSource();
                ButtonModel buttonModel = abstractButton.getModel();
                if (buttonModel.isSelected())
                {
                    logNameLabel.setVisible(true);
                    logNameLine.setVisible(true);
                    saveLogNameButton.setVisible(true);
                    
                    saveLogNameButton.setEnabled(true);
                    logNameLine.setEnabled(true);
                    
                    if (scriptLogs.get(f) != null)
                    {
                        logNameLine.setText(scriptLogs.get(f));
                    }
                    else
                    {
                        logNameLine.setText("");
                    }
                }
                else
                {
                    logNameLabel.setVisible(false);
                    logNameLine.setVisible(false);
                    saveLogNameButton.setVisible(false);
                    
                    saveLogNameButton.setEnabled(false);
                    logNameLine.setEnabled(false);
                    
                    scriptLogs.remove(f);
                }
            }
        });
        
        final JLabel scriptArgsLabel = new JLabel("Script Arguments:");
        add(scriptArgsLabel);
        scriptArgsLabel.setSize(100, 20);
        scriptArgsLabel.setLocation(5, 350);
        scriptArgsLabel.setVisible(false);
        
        final JTextField scriptArgsLine = new JTextField();
        add(scriptArgsLine);
        scriptArgsLine.setSize(540, 20);
        scriptArgsLine.setLocation(160, 350);
        scriptArgsLine.setEnabled(false);
        scriptArgsLine.setVisible(false);
        
        final JButton saveArgsButton = new JButton("Save");
        add(saveArgsButton);
        saveArgsButton.setSize(80, 20);
        saveArgsButton.setLocation(710, 350);
        saveArgsButton.setEnabled(false);
        saveArgsButton.setVisible(false);
        saveArgsButton.setFocusable(false);
        saveArgsButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                int selected = scriptList.getSelectedIndex();
                if (selected < 0)
                {
                    return;
                }
                DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
                if (selected > (dlm.size() - 1))
                {
                    return;
                }
                
                scriptArguments.put(new File(dlm.get(selected).toString()), StringFunctions.ifNull(scriptArgsLine.getText()).trim());
            }
        });
        
        final JButton upButton = new JButton(getUpArrow());
        add(upButton);
        upButton.setSize(40, 40);
        upButton.setLocation(5, 20);
        upButton.setEnabled(false);
        upButton.setFocusable(false);
        upButton.setToolTipText("Move Script Up");
        upButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                int selected = scriptList.getSelectedIndex();
                if (selected <= 0)
                {
                    return;
                }
                DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
                Object moving = dlm.get(selected);
                Object to = dlm.get(selected - 1);
                dlm.setElementAt(moving, selected - 1);
                dlm.setElementAt(to, selected);
                scriptList.setSelectedIndex(selected - 1);
            }
        });
        
        final JButton downButton = new JButton(getDownArrow());
        add(downButton);
        downButton.setSize(40, 40);
        downButton.setLocation(5, 105);
        downButton.setEnabled(false);
        downButton.setFocusable(false);
        downButton.setToolTipText("Move Script Down");
        downButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int selected = scriptList.getSelectedIndex();
                if (selected < 0)
                {
                    return;
                }
                DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
                if (selected > (dlm.size() - 1))
                {
                    return;
                }
                Object moving = dlm.get(selected);
                Object to = dlm.get(selected + 1);
                dlm.setElementAt(moving, selected + 1);
                dlm.setElementAt(to, selected);
                if ((selected + 1) <= (dlm.size() - 1))
                {
                    scriptList.setSelectedIndex(selected + 1);
                }
                else
                {
                    scriptList.setSelectedIndex(dlm.size() - 1);
                }
            }
        });
        
        final JButton playButton = new JButton(getPlayButton());
        add(playButton);
        playButton.setSize(40, 40);
        playButton.setLocation(5, 280);
        playButton.setEnabled(false);
        playButton.setFocusable(false);
        playButton.setToolTipText("Run Scripts");
        playButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                FileChooser.this.setVisible(false);
                JythonLauncher.cb.countDown();
            }
        });
        
        final JButton removeButton = new JButton(getRemoveIcon());
        add(removeButton);
        removeButton.setSize(40, 40);
        removeButton.setLocation(5, 185);
        removeButton.setEnabled(false);
        removeButton.setFocusable(false);
        removeButton.setToolTipText("Remove Selected Script");
        removeButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int selected = scriptList.getSelectedIndex();
                if (selected < 0)
                {
                    return;
                }
                DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
                if (selected > (dlm.size() - 1))
                {
                    return;
                }
                isRemoving = true;
                String sel = dlm.remove(selected).toString();
                if (scriptArguments.containsKey(new File(sel)))
                {
                    scriptArguments.remove(new File(sel));
                }
                if (scriptLogs.containsKey(new File(sel)))
                {
                    scriptLogs.remove(new File(sel));
                }
                isRemoving = false;
                if (dlm.size() == 0)
                {
                    scriptList.clearSelection();
                    saveArgsButton.setEnabled(false);
                    scriptArgsLine.setEnabled(false);
                    
                    saveArgsButton.setVisible(false);
                    scriptArgsLine.setVisible(false);
                    scriptArgsLabel.setVisible(false);
                }
                else if (dlm.size() >= selected)
                {
                    scriptList.setSelectedIndex(dlm.size() - 1);
                }
                else
                {
                    scriptList.setSelectedIndex(selected);
                }
                if (dlm.size() == 1)
                {
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);
                }
                scriptList.repaint();
            }
        });
        
        JScrollPane scriptListScroller = new JScrollPane(scriptList);
        add(scriptListScroller);
        scriptListScroller.setSize(740, 300);
        scriptListScroller.setLocation(50, 20);
        scriptList.getModel().addListDataListener(new ListDataListener()
        {
            @Override
            public void contentsChanged(ListDataEvent arg0)
            {
                toggleButtons();
            }
            
            @Override
            public void intervalAdded(ListDataEvent arg0)
            {
                toggleButtons();
            }
            
            @Override
            public void intervalRemoved(ListDataEvent arg0)
            {
                toggleButtons();
            }
            
            private void toggleButtons()
            {
                if (scriptList.getModel().getSize() > 0)
                {
                    playButton.setEnabled(true);
                    if (scriptList.getModel().getSize() > 1)
                    {
                        upButton.setEnabled(true);
                        downButton.setEnabled(true);
                    }
                    removeButton.setEnabled(true);
                }
                else
                {
                    playButton.setEnabled(false);
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            }
            
        });
        scriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scriptList.addListSelectionListener(new ListSelectionListener()
        {
            
            @Override
            public void valueChanged(ListSelectionEvent arg0)
            {
                if (isRemoving)
                {
                    return;
                }
                if (!arg0.getValueIsAdjusting())
                {
                    int selected = scriptList.getSelectedIndex();
                    if (selected < 0)
                    {
                        saveArgsButton.setEnabled(false);
                        scriptArgsLine.setEnabled(false);
                        useDedicatedLog.setEnabled(false);
                        
                        saveArgsButton.setVisible(false);
                        scriptArgsLine.setVisible(false);
                        scriptArgsLabel.setVisible(false);
                        useDedicatedLog.setVisible(false);
                        return;
                    }
                    
                    DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
                    if (selected == 0)
                    {
                        upButton.setEnabled(false);
                    }
                    else if (selected == dlm.size() - 1)
                    {
                        downButton.setEnabled(false);
                    }
                    else
                    {
                        upButton.setEnabled(true);
                        downButton.setEnabled(true);
                    }
                    
                    File f = new File(dlm.get(selected).toString());
                    if (scriptArguments.get(f) == null)
                    {
                        scriptArgsLine.setText("");
                    }
                    else
                    {
                        scriptArgsLine.setText(scriptArguments.get(f));
                    }
                    saveArgsButton.setEnabled(true);
                    scriptArgsLine.setEnabled(true);
                    useDedicatedLog.setEnabled(true);
                    
                    saveArgsButton.setVisible(true);
                    scriptArgsLine.setVisible(true);
                    scriptArgsLabel.setVisible(true);
                    useDedicatedLog.setVisible(true);
                    
                    selected = scriptList.getSelectedIndex();
                    if (selected < 0)
                    {
                        return;
                    }
                    if (selected > (dlm.size() - 1))
                    {
                        return;
                    }
                    
                    if (scriptLogs.containsKey(f))
                    {
                        useDedicatedLog.setSelected(true);
                        saveLogNameButton.setVisible(true);
                        logNameLabel.setVisible(true);
                        logNameLine.setVisible(true);
                        
                        saveLogNameButton.setEnabled(true);
                        logNameLine.setEnabled(true);
                        logNameLine.setText(scriptLogs.get(f));
                    }
                    else
                    {
                        useDedicatedLog.setSelected(false);
                        saveLogNameButton.setVisible(false);
                        logNameLabel.setVisible(false);
                        logNameLine.setVisible(false);
                        
                        saveLogNameButton.setEnabled(false);
                        logNameLine.setEnabled(false);
                    }
                }
            }
        });
        
        addWindowListener(new WindowListener()
        {
            
            @Override
            public void windowActivated(WindowEvent arg0)
            {
            }
            
            @Override
            public void windowClosed(WindowEvent arg0)
            {
                cancel();
            }
            
            @Override
            public void windowClosing(WindowEvent arg0)
            {
                cancel();
            }
            
            @Override
            public void windowDeactivated(WindowEvent arg0)
            {
            }
            
            @Override
            public void windowDeiconified(WindowEvent arg0)
            {
            }
            
            @Override
            public void windowIconified(WindowEvent arg0)
            {
            }
            
            @Override
            public void windowOpened(WindowEvent arg0)
            {
            }
        });
        
        setJMenuBar(new JMenuBar());
        
        JMenu file = new JMenu("File");
        getJMenuBar().add(file);
        
        JMenuItem selectScriptsMenuItem = new JMenuItem("Select Scripts To Run");
        selectScriptsMenuItem.addActionListener(chooseFiles(path));
        file.add(selectScriptsMenuItem);
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        file.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cancel();
            }
        });
        
        setLocation(100, 100);
        
        setTitle("Jython Launcher");
        
        setSize(800, 500);
        setMinimumSize(getSize());
        setResizable(false);
        pack();
        setVisible(true);
    }
    
    private void cancel()
    {
        DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
        dlm.clear();
        scriptArguments.clear();
        JythonLauncher.cb.countDown();
    }
    
    private ActionListener chooseFiles(final String path)
    {
        return new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Select Python Scripts...");
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setCurrentDirectory(new File(path));
                jfc.setMultiSelectionEnabled(true);
                int result = jfc.showOpenDialog(FileChooser.this);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
                    for (File f : jfc.getSelectedFiles())
                    {
                        if (!dlm.contains(f.getAbsolutePath()))
                        {
                            dlm.addElement(f.getAbsolutePath());
                        }
                    }
                    if (dlm.size() > 0)
                    {
                        scriptList.setSelectedIndex(0);
                    }
                }
            }
        };
    }
    
    private Icon getDownArrow()
    {
        String downArrow = "";
        downArrow += "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAK8AAACvABQqw0mAAAABh0RVh0U29mdHdhcmUAQWRv";
        downArrow += "YmUgRmlyZXdvcmtzT7MfTgAABGdJREFUWIXFVm9MlVUcfn7vRbkvF6oPTcGRAopkK6QluJCAMVymQrqGmV/Klh9gptZsoR/C2Jpiq1FtTaGNPjfmH+TPDVOE";
        downArrow += "mE6RiOtmaepYRWF5LwLvhXvh3vP04b0XiiXwsu4623nfnffP8zzn/J7z7AhJ/J8taj4/iYhj3763CvLycp8R0eTSxUs9VUePnCM5YhmMpKUOYOmFC+31nNY6";
        downArrow += "OzsbNNFSLONZJI9pa2urJ8kW59csK9vN0rLdPHOmkSTZ0fHtGQBxEROw89WdGyYCAbae/YaZmWuZn1/A/PwCZq5Zy6amZgYCAZaWlhVbwdSslOu5vNyMKJsN";
        downArrow += "TY2N0HU7dF2HruuIccSgqakZNpsN2dnZT1vBtGRCXbc7AMDn80PTbCAVAEATgd/vhyIRvXChHjEBwaAiAIgIlFJQ5hBKmUJAQIUfRkIACIYJwz08Nk1KhC8R";
        downArrow += "EUBSQndwkhRQYVOZ7BIxAQjPkwqKUytAFd5S5jBiAqYoOEUKQnFqNQBr2W41ioUkFBnyQDBkvKDpAxBkJEsQKrQZImpy2UOJNvlR5ASIkDCXXynzDoQ8YCqB";
        downArrow += "QCwJmHMSikhUf3//AhGZNOD0LqJh4PeBBSIy54nNKkBEHq2r+/JgV9fVhpKSkm2amCUO1/3vXrDZNBRvefGlK1e6Gmprv3hHRB6ZDX9GpSISdeLEyYpNmzft";
        downArrow += "vj94H16vgdu372BsbAwAzCwAAAI+nw8/3boFm2ZbnpSUtDw9Pf0FR2xsvIiUk5x4IMdMJ6KH4x5KudPXd+nmzZuLykpLISLQNA2aCCDTzE6YpQkqiKbheM0x";
        downArrow += "pCxfMbrj5e25zlZn94M4ZiuBZouyyeDgINz33NBEQ3gbUinTfEqBygwmKkLTNPz5x1309/8GXbfrcXGxjhkJZno5bIz84up1XSxcX4hns7MxMjwcCqGp2itl";
        downArrow += "ZgKVAkB4vQZWZ2SgsLAQrl5Xe1NLy7V5CyDpr3z/0MeGYRgHDh4ABAgEAtPcP2XGYDCI8fFxvFteDkX6q44crhod9Q7OWwAAnDt/vqP+q/rPMzJW45UdO+Dx";
        downArrow += "eEI58M8tSEV4PB5s2boVOTnr0HD6dN3JU6daZ8Of0YSTH4kkXr/+w9n4hPjHNz6/AW63G/boaDPzQl6cGB9HjMOBZqcT3tHRvrTU1PWBYODWbNhzCiKSv372";
        downArrow += "yacfxDpi8fb+/TBGDHPmoUAiiaGhIby5Zy8WLV6EmmPHq+ZCHgaf64nY3tLiPEmS27dt55LF8Vy1Mo2rVqYxMWEJizcXMRAIsq2tvRUWTsaWTsXpTz6VNTBw";
        downArrow += "1+NyXWPy0mVckZzC1OQUJiYs4eXLV3jP7TZy1uXkRuxYThLV1dWVJFl5qJKOaDtj7XZWvFdBkqypqf0IIV9FTACAhO7velzDIwZff20n9+7ZyzGfj11Xu3sA";
        downArrow += "JFrGs/oDSRRtKsr78caNPv/EBH3+cfZ839ubtSZrzXyw5rQN/60lPbbsicMfHi32Gsborl1vnCL583xw5i3gv2p/AbiM6qmt3d2MAAAAAElFTkSuQmCC";
        
        return new ImageIcon(DatatypeConverter.parseBase64Binary(downArrow));
    }
    
    private Icon getPlayButton()
    {
        String playButton = "";
        playButton += "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAGuUlEQVR42u2Wa3ATVRSAzz6y2SSblD5opaRvSIUWeSgUKmBtS6VDURBwEBmE4gjF98irgFA";
        playButton += "RKIKOjoqoI1AZQGYqggoDFoGqvFoHReUlAq209Eko0CTd7Ca7nt0abMymVP/oD2/n5G7Pvfec755z9t4lZFmGf7MR/wP8pwCK9+luN78nyniUMSjdUQb8oT";
        playButton += "+J0oyyB2UnypXOjBSOFrUBVu4NCnA3SoFJHzYzISId4sKGgEEXAhFcojp41XEJ2sQb8Nu1Sqi6ehSc7msbUL0e5YSWscW5QQBW7GkHmDR45i1d6Xcb1pj0o";
        playButton += "fMGx0+BXlHDwOO9CiKKR2oFWXa3GyH0QJNm0FERQKNcaDwG31VvQ5CWtWhrfgdbar9kTBCAl79g1H5yWj5sr9io/PNmfPjAgsy+T4JXagLB2wiSxKM6WN0Q";
        playButton += "QJIsMFQUUGQkHDzzAVTbf1Ai8TzaFNCmOmvZWEEbYNmudoAp6fmw7ejGd1OtGQVpSXng9tSCKN3oxHEgiI4MAT1thYqLu+FUbfl6tDkHbbZvdFwQgCWfMr7";
        playButton += "HNbHhfeZlp04FwVOPu2/tomP/RmFaGLoHfHVqC1y2n12LKjUdKx4OArCoVK90Q4x6c8WEtNlAEjzm+/o/cu5rOqobSDILOyreA5e7NQ1VlasmubUB5m9XAT";
        playButton += "YNv3PUdFuPfpjzZr+wY4ax4CgsPi9qpS6ng6G6w/n6n+Hwuf0lqJixZnIQgBe36hM41nJpwtCpWEwChr7Nz5RSYKsXnoXxU2MgOZVDEI8qt0+FAYuXgR3Ht";
        playButton += "4CDv5n4+mPuKk2A5zezC1NjU4qH9L4Xw+aAvxYdTYXAjAePqM+9+1pgwtRYsKVwONeNIGInCBg7goPKX4/AqcunC9+cxq/WBHhmE3sge8DIzPjIJCRuCzDD";
        playButton += "0BHwxMNH/HRJyWYYPyUOI2JSIybJbk0EEqNQ3XQRvjr5zcG3Z/BZmgAFH7K1E0eM6RlqCsMdCQFGWKzo2Y8c0XSQaDPDmIkx0Kc/B17ZGZA+gmCgxXkNPvl";
        playButton += "2z5X1T/BWTYAn3ze4p2WNY1iGweAHvvNGJg6efvRwp/mOS0KQSbGQlCL6QRD4xwsCbD6wS/hgVpteEyB/ncE9PechBlT3gQAWQzIsyK/oFKDvgFAYNc4KIV";
        playButton += "FNCOD2A1B+S8o+EzY+FQTg8beMtVOyRvdkGR3m0htgPNTYDxbPqtR0bEsJgeyHrBBp9eLJeQ3PD//Di8TXlxdE2HZg35WPnnVpp+CxN4xlecPSR0WHh+NlE";
        playButton += "1jVEdw9sHSOfwQSky2QNTYGouK8wIvNeHJqH1w0qYM6ux12Hzu6f+sLrhxNgMmvGRcOstmKh/S5Ew3xAUaizOmw/Ll2gNhEM2Si4zviJGgTGoD32DtNDUOz";
        playButton += "UHn2HHx//nzh9rku7ddw0qumnpyBrZ2em4MhFDAK/odMBDcItq2rgYzcGIhOkMEp1OF3QCPcrtEkjUcyAyV7y8DRxltLFzivaAKMX2VSuncz7+5fkJoQizt";
        playButton += "zQcfDyMREA6e3gsNdqzrvWiPAwBjhVNVlOHjiR+VqnrNzkRM0AR58RQXoZ2LZn/LzsnEUC0rku+hIu+l1LO6Bgk17DuDu2+5C1c+fvxQEIK+I8z0WJ0ZHLZ";
        playButton += "xw/1AMsQvrwf23nPoaQ+vx080IOw4dh0t1jUreCxX97iKHNkDuUq7j+rcH9E54evSw/moqlEjIXfwgUd55ZedK6Pcd+xFO/lr1Dqqf8Y3vXR4E4IHFHJStc";
        playButton += "hJFRUVEXV0dVR3+8Vu9YiJnT8wcCl4QEcSJxSl26lxH6dCxCSjQwScHj8OFmqb3qZPDn+M4zlNaWirlLDLJX64MApBdaIYR7FzSbrfrGhoalIIIuRH7zRKG";
        playButton += "E/Nz0u6Cgclx+FEqqNEQvMp13f5NQJEk3vmMumsa+x9++Q3KKn4CwcmUhNbcVyxJ0k2WZVtramrc5eXlXrmDUz+ArAVmCK/KpXAyK4piGI7dQRBEJM/VprR";
        playButton += "x1Q/ou/GZfROsoIjZyEJ4iFldZ7/RCq0uHs5U1arivs4eMjji97MO6zl0bkcbDdhfxakOjIIYFOD++WbIMPlHgCRJC/YG7GmebYx0mapHepib6TIhhUmE2E";
        playButton += "NZR8q6ekKmWmjBcszkiv+acXVvQqceFMHr9bZ2OQIKQPlax60aqK+v11ksFhrnUBgRgqZp0uPx0GiUZhiGwJ7wraUoSjGEKq+IcySDwSDhGiVHXofDIfpqI";
        playButton += "GMeJx9a8+c9EQDgawqI0iswPt3p06eJ5uZmwmazqbqWlpZbY6GhoXJ0dLSMc+SUlJRbRnG9+qw49uk6AvwOwtc97mBAOPgAAAAASUVORK5CYII=";
        
        return new ImageIcon(DatatypeConverter.parseBase64Binary(playButton));
    }
    
    private Icon getRemoveIcon()
    {
        String recycleBin = "";
        recycleBin += "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAIiUlEQVR42pWXf2wT9xXAv9+789l3Z59/JXEch/xw4qahIbCWskGyMtp1akvFKraOVhX7odI";
        recycleBin += "VtP5Y1TGqSWRlon+s3ZpKbVCbMhVUQF3pWIXajYFgUSkM1jYQCiTxwEmc37Zj++78686+u70zYWIIiPOU54t9977fz/e99/3eexjNIYcPH7ZjjD3wbxmoQ9";
        recycleBin += "M0RlEUOpfLkaBIlmW1UCjI8EwG7iVBoyRJTm/cuDE119iG4Gu/HDx40AGXNTDgPTDgYoIgAqA8DAhfMYI/BPdQJp1BSSGJ4vEEisVicJ1BgiAaMIimaVReX";
        recycleBin += "q7X1dXFvV5v0O12n2FZtoeiqE9XrlyZuSnA/v37fTDYOUEQnBVVNTrv8uiMzYFoxqqTJgsiKBphkgIIEhVJwFTXdaRpKirk80jOZVE2LaGUJKBcRsJ6HmBI";
        recycleBin += "HTMmHVNINaDGYBGBVatWyTcE2L59+5LGQKDX17BIdVTWqgVEaYpqzIF1jFHJos9+EGBGk8Cs5wlFmKJMWpYYGBxw/ejRR5M3BNj2yu9N1fWBo1VOtt3jqUA";
        recycleBin += "23o5YjjNWr+sEpWmI0DWAUTUd5fMFXVaUostz2SyEJIVTkohEIYElIYlVJUuwDI3dLjf2VlYgymRGM2K2+4F72zbeMgd6+hMdd3jplyHJ8pmcouZhNgKWbz";
        recycleBin += "aRpInEBEkgrBUKhASTRSLTeGJiAo2PT+jT0xE9nhR0A8zCMLqvuho1396MAoFGXOX1EKJCqpNC4aFv3+46ekuAk6HsEzW8tstpY1Qxp2USWT0jynrOeMiI9";
        recycleBin += "zVqJMDsCBhBoupGkpopTLE0NjEmTBtqoTBtphAVms7k4jliyYoG9vJcAO2VnHbI42AIAEglc7ok5PRMqSlgJpEJPG9mTdgMAGYLKOQB3T+RTqQLZM0KPyvP";
        recycleBin += "BVDrtKhf1JSxrJTThGRWTwJASfu5CEAhGia3wOSMoQDAAIC5L5zuu9tvW3Yjm+sBaI5SLwYq2XLwQAIAZgBAmAeAsXoOwsACAAch4ACAOR2SPmy/zb5+TgB";
        recycleBin += "D/h2SjrRUc0slWY8ms1pUyOoJfXZ3zSUWCllgcitA2ADABjlhxUhjvxjOvLKyid9eEsDxoPDW0jrb4wAwNZHMx17bN1SfVTSUEBXOxlLKisWukVV3lQ/dBI";
        recycleBin += "DlaMwDhB3cz4MH+HRWZoIx7am2Bu6DkgB6BsVn76xhOmSVGNtzZNLe9ddwPfxcqCqzJMLT2XKSwOqWHwcOB2qsEdZCFq61ZUyIAwA7eMAJAE5wP39+VJwqI";
        recycleBin += "PrZtkbuREkAJy5nVvtd+B2CNE2+2DXYerpfoJe3ur6+9+7yCy93D6wzbLxlltjrL7TshTPi/0LDmhDP0YSDpZGbprB9UtT6x2MSxVrMv2lv5C6VBACJ+A3Y";
        recycleBin += "insKGqkMjGZn/vDBUOvWDU27E2Kee//vo/cFR1I16+73/XN1e+VX19tyNLIDgBtWboumtRBFYFYvyH5JIX4IZ0CyVIAFZYz2/ud9wrLTA6L8kzV1nSRJ/G/";
        recycleBin += "/FlSN+PqS6D99PtHy87V1HxOzLwrDFTYaOykCmeHwikIYPPVucnVwKvNVtkA8ttzP6qUCWHlafe94b/KRY2cT5IPt3h13NPD9V+/v/iS85tMTU/cbtr9eH9";
        recycleBin += "ixdKHz4lUAE4HogoYU3ox9PgdxD01i77nR9L5lfttWdBO5EQC2kGrndET+/rnhdOxbi8t2W8yUdPX+yFSmcsubF7bDSUw11Vov/u7p5levtectuLLCSrTCF";
        recycleBin += "vSBN3xfDqd+2x7g95UMYMipy9IzZoTXd34UXohJog8yvq99ifuk2UTI6Zxq2Xdo7PFjX0YfgkfV9zruXA+7QTHsrBACB4Nr4DiuNpO4RtVUz7kx+en2gPXk";
        recycleBin += "vAA+C4rfW1zNbvjV25fu++xcwiXnNdSxoemFwZFUc1zMl9VXscGPjk6sq/Uy/RCGNwniyjAuFnsAogoAasH9tVJG4UJx7WcQ/8vzAjj+n9TC5kp68+mBVH7";
        recycleBin += "b7tAj0/Fc+fOPNWx59+Phl7Ky5izGHIqhrU82/aKlwXbhql25lfBaIf4Mhevg9e2PinJmXELPLK9novMCgDyorLGjzSYTfXFCVPvPD6UW8AyynjgTfeLDnt";
        recycleBin += "gqI+GWN5mETQ87j0DpkMvJShx0rMJlT3grnLzbYVvAWui68YQcGotnt36n2Z2ZF8C/hrI2D6dvhDN1ZjomtCZjEw9LQtwvpnJ4NKajNGzK27yQ9cXysFgPI";
        recycleBin += "IoikdXKaRzLhu08f8np4JXzQ9HPd3bveH1v9xvyfAFol0V7UhUnO8729VWKknSlzoOJCAIKPYg5njUtVsvwOwlqs1mR3c4jl9NhaGTvX/627aXnnvoEjI0Q";
        recycleBin += "ZEvfBaEMplXpteELp14cCY8iBapes9mMnA4n4qycbrFYdAoqZMNag7Itn89jWZENX2CWZVB5WRmy8zb06htdm/60o9Mow/Kg47PXuQEMOXzizIbJ0IV3oT5";
        recycleBin += "EUC3Dipy3ePqK0CbaSE8EeYEmJ6f0557/5XeD/efH0JVzKgaaKBlg5+5936yqcJ7y+/1IVaH2B52rNIPmA/oErQgQDo9E1679QbumFl+YBsAMaLxkgLfe3u";
        recycleBin += "nyuO2xQCCAnbB6E3Q8GkCAu1G+kIcOCaAKRkMiI0EUUGQ6ghKJJHJC/BsbG9GhQ/841tGxddPscIXZEMglAxiya9euYEtLS8DlcsGLnivmgZF06XQajY2No";
        recycleBin += "WAwWNRQKFRs0aANQ21tbWjRokWos7PzjwcOHHgHhlFBjSSUbjTHLQG6urqcMPnmqqqqn9bX13uhx0PQJxYBwuEwGhwcRAMDA2hoaAj6w7gBoPt8vuDZs2f3";
        recycleBin += "9PT0/BnCIc3GfX7b8Hrp7u4mYPWtAHMX7IDGVCrliUQi3Pj4OOrt7c2Mjo5GkslkCPrKXtBh6BtyYGZofq6x59H13VzsdjuGiUsqXK+X/wJL2O9OGsj8wQA";
        recycleBin += "AAABJRU5ErkJggg==";
        
        return new ImageIcon(DatatypeConverter.parseBase64Binary(recycleBin));
    }
    
    private Icon getUpArrow()
    {
        String upArrow = "";
        upArrow += "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAK8AAACvABQqw0mAAAABh0RVh0U29mdHdhcmUAQWRvYm";
        upArrow += "UgRmlyZXdvcmtzT7MfTgAABIdJREFUWIXFV1toVFcUXWceN1EykQRHTY11klS0D9tKJlHStIiaREMaisYqNYZSpFXoTz8URNtKpRChEo0pLTSxWh/xLzavkofG";
        upArrow += "oj/GFjRSdMbGtkYoGBAyeUwy95yz+jE3IU1lXhp64HAv3HPWXnuvvfc5V5DE/zlsiW4UQnjOnT33SVPTxQOvrXx1ZcIMSMY9CwreWO3337tNazx4MPBg8zub1y";
        upArrow += "WCFf8G4Pm7d319JFldfYQHDn7KUMikz+f/DUDmrBIAYLvQeOEESdbW1tHrzWdubh5P1H1Nkqz/ruHIrBIoKy0rCo6PB/v6brOw8C1uLNnEkpJNXLt2HX1+P4cC";
        upArrow += "gcCa1WsK48GMOQmFEGlffHn4s+SkpOSammOw2QQgBIQQUFLi+LFapLpcrq+OHj0ohEiJFdd+6NChmBbOSZ77cUXFll3nGy+guflHuFJTp7xwOp3o7++H2+1GcX";
        upArrow += "HRC6GJ0MD6Det/icmxWPrA4ozFK2/23exSSi2srKyC1hp2u/1fa6RpYs7cuTh77gxGR0b/yvJ4Nkglf4+GHVUCIYRRf7Jhv9vtXlhTcxzDgQDsdhtIPU1LDYfT";
        upArrow += "gcHBR6g7UYclSzKXNjY27hVCRMWPumDP7j1biouLtvVc+RndXd1ISUmBVhpahw1rHX5XSsPlcqGlpRW9vTdQVv72+9u3bS+N6mAkCYQQ6Xfu3O3IeC7DW7WzCo";
        upArrow += "ODg0hKSgYwc4+YegTHgvAsXYrvT52Ez+e/umrV62UkAwlFoGBNQU52TvYrba1t8Pv8cDqcUFJaXs+cCkpKJCUZuHXrFlpaWrFixfLcfG/eskg2HJE+jowOj4cm";
        upArrow += "JmRm5mLMm5cKqSSEELBpW9hpWs5bUSQJRQWXy4W0tDRIpYTWWkWyEU0Co62t/ZuSkpIPHj9+jGAwCNM0sW/vPjx8+BCGYQAAJiYm4MnyoLq6Gna7HclzkpGeno";
        upArrow += "4rPVfOFxcX7SIZTCgCJENCiP0/nD7z54svv1Tgnj9/eU5OdpbdbodSClprAIBSEk6HA9nZWbh//4/+gYGBe91d3b2VlTu+jWQciKEKSD7aWVV5OM+bW/5Te3uL";
        upArrow += "aUporUBLe1KDVhWYUqGlubkpz+st37Hjvc9J/h0NP+ZWTNJcsGCBOT3x+IREXLQowyRpxoobUYInkBAkw8YYniCgaUWCAKnjuuTERQAACIKW97RygBah8NfJpj";
        upArrow += "BLBABAT0bBIqA1QUXEcKw8NYGwBErPIKDCiUn+t0k+SwIEBS3N9bQyDOcEp1bNGgFAhE8/PTMCVk4koEF8BKa1XK05lYR6yvhsEwBAakgZbkZKhdu8VgpSyskI";
        upArrow += "xFUFcdUsSdPpcMIwDJih0NSFxAyFYBgGnE4DQoiIh89TEbh+vfdXAqjYuhVDQ0MIWHN4ZBgVW9+FlBJXr127EQ9mvP8F8y5d7ukwpWJDfQNLN25i6cZSnjp1mq";
        upArrow += "ZU7OjsagbgmtU/o9QU17LLl3vaRkbHOBYc51hwnMMjo+zo7GoC4IkXL6Zb8cwhhEj96MPdb67bsD5fa+pLXZ3X6xvqr5IcjRsrEQLPcvwD8+Zwv2yAZTgAAAAASUVORK5CYII=";
        
        return new ImageIcon(DatatypeConverter.parseBase64Binary(upArrow));
    }
    
    public Map<File, String> getScriptArgs()
    {
        return Collections.unmodifiableMap(scriptArguments);
    }
    
    public Map<File, String> getScriptLogs()
    {
        return Collections.unmodifiableMap(scriptLogs);
    }
    
    public List<File> getScripts()
    {
        DefaultListModel dlm = (DefaultListModel) scriptList.getModel();
        List<File> scripts = new ArrayList<File>();
        for (int i = 0; i < dlm.size(); i++)
        {
            scripts.add(new File(dlm.get(i).toString()));
        }
        return Collections.unmodifiableList(scripts);
    }
}
