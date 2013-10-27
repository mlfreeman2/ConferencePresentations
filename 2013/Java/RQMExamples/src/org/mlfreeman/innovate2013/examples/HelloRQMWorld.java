package org.mlfreeman.innovate2013.examples;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.mlfreeman.innovate2013.Log4J_Helpers;
import org.mlfreeman.innovate2013.console.VirtualConsole;
import org.mlfreeman.innovate2013.network.http.rqm.RQM;
import org.w3._2005.atom.Feed.Entry;

import com.ibm.rqm.xml.bind.Project;

/**
 * A basic login demo in Java. <br>
 * Logs in and prints your username and roles by hitting the /authenticated/identity relative URL on the server.<br>
 * If run under Eclipse, this will not be able to mask your password due to limits in Eclipse's "console"
 */
public class HelloRQMWorld
{
    /**
     * @param args
     *            none
     */
    public static void main(String[] args)
    {
        // set up log4j
        Log4J_Helpers.addConsoleAppender();
        
        // decrease the amount of log4j output sent to the console
        LogManager.getLogger("org.apache.http").setLevel(Level.ERROR);
        LogManager.getLogger("org.mlfreeman.innovate2013.network.http.HTTPClient").setLevel(Level.ERROR);
        
        
        VirtualConsole console = VirtualConsole.getConsole();
        String user = console.readLine("User: ");
        String password = new String(console.readPassword("Password: "));
        String serverURL = console.readLine("Server: ");
        
        RQM rqmSession = new RQM(serverURL, user, password);
        
        if (!rqmSession.logIn())
        {
            System.out.printf("Unable to log in to RQM at %s as \"%s\".", serverURL, user);
            console.readLine();
            return;
        }
        
        RQM.RQMIdentity rqmIdentity = rqmSession.Identity();
        
        System.out.printf("Logged in as \"%s\".%n", rqmIdentity.UserID);
        System.out.println("Roles:");
        for (String role : rqmIdentity.Roles)
        {
            System.out.printf("\t%s%n", role);
        }
        
        // show the user a project list too
        console.printf("%n");
        List<Entry> projects = rqmSession.GetProjects();
        
        console.printf("Available Projects:%n");        
        for (int i = 0; i < projects.size(); i++)
        {
            Entry e = projects.get(i);
            Project project = e.getContent().getProject();
            console.printf("%d: %s%n(API Name: %s)%n%n", i, project.getTitle(), project.getAlias().get(0).getValue());
        }
        
        System.out.println();
        System.out.println("Done");
        console.readLine();
    }
    
}
