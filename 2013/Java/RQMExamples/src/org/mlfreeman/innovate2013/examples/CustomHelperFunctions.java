package org.mlfreeman.innovate2013.examples;

import java.util.List;

import org.mlfreeman.innovate2013.Log4J_Helpers;
import org.mlfreeman.innovate2013.console.VirtualConsole;
import org.mlfreeman.innovate2013.network.http.rqm.RQM;
import org.w3._2005.atom.Feed.Entry;

import com.ibm.rqm.xml.bind.Project;
import com.ibm.rqm.xml.bind.Testcase;
import com.ibm.rqm.xml.bind.Testphase;
import com.ibm.rqm.xml.bind.Testplan;
import com.ibm.rqm.xml.bind.Testscript;

public class CustomHelperFunctions
{
    
    /**
     * @param args
     *            none
     */
    public static void main(String[] args)
    {
        Log4J_Helpers.addRollingFileAppender();
        
        VirtualConsole console = VirtualConsole.getConsole();
        console.printf("The log4j logging output is sent to a file in this example.%n");
        
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
        
        // 1. show the user a project list so they can pick a project for use with the later parts of this example.
        console.printf("Available Projects:%n");
        List<Entry> projects = rqmSession.GetProjects();
        for (int i = 0; i < projects.size(); i++)
        {
            Entry e = projects.get(i);
            Project project = e.getContent().getProject();
            console.printf("%d: %s%n(API Name: %s)%n%n", i, project.getTitle(), project.getAlias().get(0).getValue());
        }
        
        int projectChoice = Integer.parseInt(console.readLine("Please enter the index of the project you want to work with (0-%d):", projects.size() - 1).trim());
        Project project = projects.get(projectChoice).getContent().getProject();
        String projectAPIName = project.getAlias().get(0).getValue();
        String projectUIName = project.getTitle();
        console.printf("Chosen Project: #%d: %s (API Name: %s)%n", projectChoice, projectUIName, projectAPIName);
        
        List<Entry> testPlans = rqmSession.GetArtifacts(Testplan.class, projectAPIName);
        console.printf("Test Plans:%n");
        for (int i = 0; i < Math.min(testPlans.size(), 3); i++)
        {
            Testplan testPlan = rqmSession.GetURLAs(Testplan.class, testPlans.get(i).getId());                    
            console.printf("%d: %s%n", i, testPlan.getTitle());
            console.printf("Test Phases:%n");
            List<Testphase> phases = rqmSession.GetPhasesForPlan(projectAPIName, testPlan.getWebId());
            for (Testphase phase : phases)
            {
                console.printf("\t%s%n", phase.getTitle());
            }
            console.printf("%n");
        }
        
        List<Entry> testcases = rqmSession.GetArtifacts(Testcase.class, projectAPIName);
        for (int i = 0; i < Math.min(testcases.size(), 3); i++)
        {
            Testcase testcase = rqmSession.GetURLAs(Testcase.class, testcases.get(i).getId());                    
            console.printf("%d: %s", i, testcase.getTitle());
            console.printf("Test Phases:%n");
            List<Testscript> scripts = rqmSession.GetScriptsForTestCase(projectAPIName, testcase.getWebId());
            for (Testscript script : scripts)
            {
                console.printf("\t%s", script.getTitle());
            }
            console.printf("%n");
        }
        
        System.out.println();
        System.out.println("Done");
        console.readLine();
        
    }
    
}
