// $Id: RQM.java 3552 2012-07-11 14:54:22Z mlfreeman $
package org.mlfreeman.innovate2013.network.http.rqm;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mlfreeman.innovate2013.StringFunctions;
import org.mlfreeman.innovate2013.network.http.HTTPClient;
import org.mlfreeman.innovate2013.network.http.HTTPResponse;
import org.mlfreeman.innovate2013.parallel.Parallel;
import org.mlfreeman.innovate2013.parallel.Parallel.LoopBody;
import org.w3._2005.atom.Feed;
import org.w3._2005.atom.Feed.Entry;
import org.w3._2005.atom.Feed.Link;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.rqm.xml.bind.Project;
import com.ibm.rqm.xml.bind.Testcase;
import com.ibm.rqm.xml.bind.Testphase;
import com.ibm.rqm.xml.bind.Testplan;
import com.ibm.rqm.xml.bind.Testscript;

/**
 * A basic RQM API class. Uses {@link HTTPClient} and the RQM REST API to send/receive XML. <br>
 * You will need a jazz.net login for those two URLs below.<br>
 *
 * {@link https://jazz.net/wiki/bin/view/Main/RqmApi} <br>
 * {@link https://jazz.net/projects/rational-quality-manager/api-doc-3.0/} <br>
 */
public class RQM
{
    /**
     * A class representing an RQM login. Useful if you need a model to bind something against.
     */
    public static class RQMLogin
    {
        /**
         * The RQM server to log into (just the part that starts with "http" and ends with "/qm")
         */
        public String RQMServer;
        
        /**
         * The user name to log in as
         */
        public String user;
        
        /**
         * The password to log in with
         */
        public String password;
    }
    
    /**
     * This is the object returned by hitting (Server URL)/authenticated/identity in RQM.<br>
     * It is returned as JSON.<br>
     * It tells you who you are and what your roles are.
     */
    public static class RQMIdentity
    {
        /**
         * The user ID you logged in to RQM with
         */
        @JsonProperty("userId")
        public String       UserID;
        
        /**
         * The roles your user has
         */
        @JsonProperty("roles")
        public List<String> Roles;
    }
    
    /**
     * Templates used to build some common RQM URLs
     */
    public static class URLTemplates
    {
        /**
         * The format of URLs used to reach an artifact by its UI ID (a number)
         */
        public static final String ArtifactByWebID = "%1$s%2$sresources/%3$s/%4$s/urn:com.ibm.rqm:%4$s:%5$s";
        
        /**
         * The format of URLs used to reach an artifact by its internal ID (a string)
         */
        public static final String ArtifactByID    = "%1$s%2$sresources/%3$s/%4$s/%5$s";
        
        /**
         * The format of URLs used to download attachments (arbitrary files) from a RQM project
         */
        public static final String AttachmentByID  = "%1$s%2$sresources/%3$s/attachment/%4$s";
        
        /**
         * The format of URLs used to fetch RQM projects as XML files
         */
        public static final String ProjectByName   = "%1$s%2$s/projects/%3$s";
    }
    
    /**
     * The possible results of an effort to upload something to RQM.<br>
     * It either succeeded, failed, or ran afoul of RQM's efforts to detect "duplicate" artifacts. <br>
     * In the third scenario you received a redirection to the artifact that RQM thinks you're colliding with
     */
    public static enum UploadResultStatus
    {
        /**
         * Returned when the upload succeeded
         */
        Success, //
        /**
         * Returned when the upload failed
         */
        Failure, //
        /**
         * Returned when the upload failed because RQM decided the artifact was a duplicate of another
         */
        Duplicate
    }
    
    /**
     * The results of an effort to upload something to RQM
     */
    public static class UploadResult
    {
        /**
         * The status of the upload effort
         */
        public UploadResultStatus Status;
        
        /**
         * The URL of the artifact you've uploaded
         */
        public String             URL;
        
        /**
         * The URL returned by RQM when you run afoul of its duplicate detection code.<br>
         * This comes from the HTTP content-location header.
         */
        public String             ContentLocation;
    }
    
    private static final Logger log                  = LogManager.getLogger(RQM.class);
    
    /**
     * The relative path from the server's main login page to the REST API
     */
    private static final String integrationPath      = "/service/com.ibm.rqm.integration.service.IIntegrationService/";
    
    /**
     * The header returned by RQM when there's an authentication problem.
     */
    private static final String RQMAuthMsgHeaderName = "X-com-ibm-team-repository-web-auth-msg";
    
    /**
     * The RQM server to log into (just the part that starts with "http" and ends with "/qm")
     */
    private final String        serverURL;
    
    /**
     * The user name to log in as
     */
    private final String        user;
    
    /**
     * The password to log in with
     */
    private final String        password;

    /**
     * The HTTPClient instance that does all the posting
     */
    private final HTTPClient    hc;
    
    /**
     * Constructor
     * 
     * @param serverURL
     *            The RQM server to log into (just the part that starts with "http" and ends with "/qm")
     * @param userName
     *            The user name to log in as
     * @param password
     *            The password to log in with
     */
    public RQM(String serverURL, String userName, String password)
    {
        if (StringFunctions.isNullOrWhitespace(serverURL))
        {
            log.error("The RQM Server URL can not be null, empty, or whitespace.");
            throw new IllegalArgumentException("The RQM Server URL can not be null, empty, or whitespace.");
        }
        if (StringFunctions.isNullOrWhitespace(userName))
        {
            log.error("The RQM user name can not be null, empty, or whitespace.");
            throw new IllegalArgumentException("The RQM user name can not be null, empty, or whitespace.");
        }
        if (StringFunctions.isNullOrWhitespace(password))
        {
            log.error("The RQM user's password can not be null, empty, or whitespace.");
            throw new IllegalArgumentException("The RQM user's password can not be null, empty, or whitespace.");
        }
        if (serverURL.trim().endsWith("/"))
        {
            serverURL = serverURL.trim().substring(0, serverURL.length() - 1);
        }
        
        this.serverURL = serverURL.trim();
        this.user = userName.trim();
        this.password = password.trim();
        
        hc = new HTTPClient(HTTPClient.ApacheUA, true, true, true);
    }

    /**
     * @return The password used to to log in with.
     */
    public String getPassword()
    {
        return password;
    }
    
    /**
     * @return The URL of the RQM installation
     */
    public String getServerURL()
    {
        return serverURL;
    }
    
    /**
     * @return The user name used to log into RQM
     */
    public String getUserName()
    {
        return user;
    }
    
    /**
     * Logs in to RQM by simulating what the browser posts from the RQM UI login page.
     * 
     * @return True if login worked, false if not.
     */
    public boolean logIn()
    {
        HTTPResponse login = hc.postForm(serverURL + "/j_security_check", null, null, "j_username", user, "j_password", password);
        if (login.containsError())
        {
            log.error("Received error during login.");
            log.error("HTTP Error " + login.getResponseCode());
            log.error("Response Body:");
            log.error(login.getResponseBodyAsString());
            return false;
        }
        if (login.getHeaderNames().contains(RQMAuthMsgHeaderName))
        {
            log.error("Login failed. '" + RQMAuthMsgHeaderName + "' was present in the response. Its value was " + login.getHeaders(RQMAuthMsgHeaderName).get(0) + ".");
            return false;
        }
        log.debug("Login succeeded.");
        return true;
    }
    
    /**
     * This method accesses (Server URL)/authenticated/identity.<br>
     * If you're logged in, you'll get a JSON object with your user name and RQM roles in it back.<br>
     * This method will parse that into an instance of the {@link RQMIdentity} class.
     * 
     * @return Either an instance of the {@link RQMIdentity} class or null if you're not logged or something goes wrong.
     */
    public RQMIdentity Identity()
    {
        HTTPResponse identity = hc.get(serverURL + "/authenticated/identity");
        if (identity.containsError())
        {
            log.error("Unable to fetch RQM user roles.");
            log.error("HTTP Error " + identity.getResponseCode());
            log.error("Response Body:");
            log.error(identity.getResponseBodyAsString());
            return null;
        }
        if (identity.getHeaderNames().contains(RQMAuthMsgHeaderName))
        {
            log.error("We are not logged in. '" + RQMAuthMsgHeaderName + "' was present in the response. Its value was " + identity.getHeaders(RQMAuthMsgHeaderName).get(0) + ".");
            return null;
        }
        if (identity.getContentType().contains("text/json"))
        {
            ObjectMapper om = new ObjectMapper();
            try
            {
                log.debug("The server returned JSON. Attempting to parse it as a RQMIdentity object.");
                return om.readValue(identity.getResponseBody(), RQMIdentity.class);
            }
            catch (Exception e)
            {
                log.error("Unable to parse the RQMIdentity object from JSON to our RQMIdentity class.", e);
                log.error("Response Body:");
                log.error(identity.getResponseBodyAsString());
                return null;
            }
        }
        return null;
    }
    
    /**
     * This method checks to see if the given test script is referenced by the given test case, and that they both exist in the supplied project name.
     * 
     * @param projectName
     *            The **API Name** of the project to look in
     * @param tsid
     *            The test script ID to check
     * @param tcid
     *            The test case ID to check
     * @return True if the given test script and test case exist in the supplied project and if the test case references the test script
     */
    public boolean DoesTestScriptBelongToTestCase(String projectName, int tsid, int tcid)
    {
        if (!DoesItemExist(Testcase.class, projectName, tcid))
        {
            return false;
        }
        
        if (!DoesItemExist(Testscript.class, projectName, tsid))
        {
            return false;
        }
        
        Testcase tc = GetArtifactByWebID(Testcase.class, projectName, tcid);
        Testscript ts = GetArtifactByWebID(Testscript.class, projectName, tsid);
        
        if (tc == null || ts == null || tc.getTestscript() == null || tc.getTestscript().size() == 0)
        {
            return false;
        }
        
        boolean result = false;
        
        for (Testcase.Testscript tss : tc.getTestscript())
        {
            Testscript t = GetURLAs(Testscript.class, tss.getHref());
            // i know this looks inefficient, but in RQM resources can appear at multiple URLs.
            // things can be referenced by both their internal and web IDs.
            // so therefore we have to fetch the URL and check the ID tag in the returned XML.
            if (t.getIdentifier().equals(ts.getIdentifier()))
            {
                result = true;
            }
        }
        
        return result;
    }
    
    /**
     * Gets all of the phases of a test plan. <br>
     * Useful in creating a TCER...Test Plan and Test Phase are two of the things you need to make one.
     * 
     * @param projectName
     *            The **API Name** of the project to look in
     * @param testPlanID
     *            The Web ID of the test plan
     * @return All of the phases of the test plan. Not ordered any particular way though.
     */
    public List<Testphase> GetPhasesForPlan(String projectName, int testPlanID)
    {
        Testplan tp = GetArtifactByWebID(Testplan.class, projectName, testPlanID);
        // i know this looks inefficient, but in RQM resources can appear at multiple URLs.
        // things can be referenced by both their internal and web IDs.
        // so therefore we have to fetch the URL and check the ID tag in the returned XML.
        //
        // get the target test plan, so we have its ID tag
        // get all the test phases in the project
        // get the distinct set of testplan URLs from the collection
        // hit all those URLs and grab the ID tag from each one, building a mapping collection
        // return all the test phases that reference a test plan with the right ID
        List<Testphase> collected = new ArrayList<Testphase>();
        Map<String, String> seen = new LinkedHashMap<String, String>();
        for (Entry e : GetArtifacts(Testphase.class, projectName))
        {
            Testphase tphase = GetURLAs(Testphase.class, e.getId());
            if (!seen.containsKey(tphase.getTestplan().getHref()))
            {
                Testplan tplan = GetURLAs(Testplan.class, tphase.getTestplan().getHref());
                seen.put(tphase.getTestplan().getHref(), tplan.getIdentifier());
            }
            collected.add(tphase);
        }
        List<Testphase> results = new ArrayList<Testphase>();
        for (Testphase tphase : collected)
        {
            if (tp.getIdentifier().equals(seen.get(tphase.getTestplan().getHref())))
            {
                results.add(tphase);
            }
        }
        return results;
    }
    
    /**
     * Gets all the testscripts referenced by a test case.<br>
     * First it gets the testcase.<br>
     * Then it hits every URL specified in the HREF property of each testscript tag under the testcase and collects the returned objects into a list.
     * 
     * @param projectName
     *            The **API Name** of the project to look in
     * @param testCaseID
     *            The Web ID of the test case
     * @return All of the test scripts associated with the testcase
     */
    public List<Testscript> GetScriptsForTestCase(String projectName, int testCaseID)
    {
        Testcase tc = GetArtifactByWebID(Testcase.class, projectName, testCaseID);
        
        List<Testscript> results = new ArrayList<Testscript>();
        for (Testcase.Testscript ts : tc.getTestscript())
        {
            results.add(GetURLAs(Testscript.class, ts.getHref()));
        }
        return results;
    }
    
    /**
     * Gets the specified project
     * 
     * @param projectName
     *            The API name of the project
     * @return the RQM project object
     */
    public Project GetProject(String projectName)
    {
        if (StringFunctions.isNullOrWhitespace(projectName))
        {
            throw new IllegalArgumentException("The argument \"projectName\" can not be null or whitespace.");
        }
        return GetURLAs(Project.class, String.format(URLTemplates.ProjectByName, serverURL, integrationPath, projectName));
    }
    
    /**
     * Gets the specified artifact by its web ID (number shown in the RQM UI)
     * 
     * @param <X>
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param type
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param projectName
     *            The API name of the project it's under
     * @param webID
     *            The web (UI) ID of the artifact you want
     * @return The artifact as a Java class
     */
    public <X> X GetArtifactByWebID(Class<X> type, String projectName, int webID)
    {
        if (StringFunctions.isNullOrWhitespace(projectName))
        {
            throw new IllegalArgumentException("The argument \"projectName\" can not be null or whitespace.");
        }
        if (webID <= 0)
        {
            throw new IndexOutOfBoundsException("The argument \"webID\" must be greater than 0.");
        }
        return GetURLAs(type, String.format(URLTemplates.ArtifactByWebID, serverURL, integrationPath, projectName, type.getSimpleName().toLowerCase(), webID));
    }
    
    /**
     * Gets the specified artifact by its internal ID (string shown in some places in the REST API)
     * 
     * @param <X>
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param type
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param projectName
     *            The API name of the project it's under
     * @param id
     *            The internal ID of the artifact you want
     * @return The artifact as a Java class
     */
    public <X> X GetArtifactByID(Class<X> type, String projectName, String id)
    {
        if (StringFunctions.isNullOrWhitespace(projectName))
        {
            throw new IllegalArgumentException("The argument \"projectName\" can not be null or whitespace.");
        }
        if (StringFunctions.isNullOrWhitespace(id))
        {
            throw new IllegalArgumentException("The argument \"id\" can not be null or whitespace.");
        }
        return GetURLAs(type, String.format(URLTemplates.ArtifactByID, serverURL, integrationPath, projectName, type.getSimpleName().toLowerCase(), id));
    }
    
    /**
     * Downloads the specified file from RQM.<br>
     * You can upload arbitrary files to a RQM project as "attachment" artifacts.
     * 
     * @param projectName
     *            The API name of the project it's under
     * @param id
     *            The internal ID of the attachment you want
     * @return The file as a byte array
     */
    public byte[] GetAttachment(String projectName, String id)
    {
        if (StringFunctions.isNullOrWhitespace(projectName))
        {
            throw new IllegalArgumentException("The argument \"projectName\" can not be null or whitespace.");
        }
        if (StringFunctions.isNullOrWhitespace(id))
        {
            throw new IllegalArgumentException("The argument \"id\" can not be null or whitespace.");
        }
        HTTPResponse attachment = hc.get(String.format(URLTemplates.AttachmentByID, serverURL, integrationPath, projectName, id));
        if (attachment.containsError())
        {
            return new byte[0];
        }
        return attachment.getResponseBody();
    }
    
    /**
     * Executes a HTTP PUT of the specified artifact to the specified URL.<br>
     * HTTP PUTs are used to update an existing artifact.
     * 
     * @param item
     *            The artifact you're uploading
     * @param url
     *            The URL to HTTP PUT the artifact to.
     * @return The result of the update
     */
    @SuppressWarnings("rawtypes")
    public UploadResult Put(Object item, String url)
    {
        if (StringFunctions.isNullOrWhitespace(url))
        {
            throw new IllegalArgumentException("The argument \"url\" can not be null or whitespace.");
        }
        if (item == null)
        {
            throw new IllegalArgumentException("The argument \"item\" can not be null.");
        }
        
        UploadResult ur = new UploadResult();
        try
        {
            StringWriter sw = new StringWriter();
            if (item instanceof JAXBElement)
            {
                JAXBContext jc = JAXBContext.newInstance(((JAXBElement) item).getValue().getClass());
                Marshaller m = jc.createMarshaller();
                m.marshal(item, sw);
            }
            else
            {
                JAXBContext jc = JAXBContext.newInstance(item.getClass());
                Marshaller m = jc.createMarshaller();
                m.marshal(item, sw);
            }
            HTTPResponse r = hc.putBytes(url, null, sw.toString().getBytes("UTF-8"), "Content-Type", "application/xml");
            
            if (r.containsError())
            {
                ur.Status = UploadResultStatus.Failure;
                return ur;
            }
            if (r.getResponseCode() == 303)
            {
                ur.Status = UploadResultStatus.Duplicate;
                ur.URL = url;
                ur.ContentLocation = r.getHeaders("Content-Location").get(0);
                return ur;
            }
            ur.Status = UploadResultStatus.Success;
            ur.URL = url;
            ur.ContentLocation = r.getHeaders("Content-Location").get(0);
            return ur;
        }
        catch (JAXBException e)
        {
            log.error("Unable to convert the desired JAXB object to XML.", e);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("Unable to convert the desired JAXB object to a string.", e);
        }
        ur.Status = UploadResultStatus.Failure;
        return ur;
    }
    
    /**
     * Executes a HTTP POST of the specified artifact to the specified URL.<br>
     * HTTP POSTs are used to create a new artifact.
     * 
     * @param item
     *            The artifact you're uploading
     * @param projectName
     *            The RQM project to post the new artifact to
     * @return The result of the update
     */
    @SuppressWarnings("rawtypes")
    public UploadResult Post(Object item, String projectName)
    {
        if (StringFunctions.isNullOrWhitespace(projectName))
        {
            throw new IllegalArgumentException("The argument \"projectName\" can not be null or whitespace.");
        }
        if (item == null)
        {
            throw new IllegalArgumentException("The argument \"item\" can not be null.");
        }
        
        UploadResult ur = new UploadResult();
        try
        {
            StringWriter sw = new StringWriter();
            if (item instanceof JAXBElement)
            {
                JAXBContext jc = JAXBContext.newInstance(((JAXBElement) item).getValue().getClass());
                Marshaller m = jc.createMarshaller();
                m.marshal(item, sw);
            }
            else
            {
                JAXBContext jc = JAXBContext.newInstance(item.getClass());
                Marshaller m = jc.createMarshaller();
                m.marshal(item, sw);
            }
            HTTPResponse r = hc.postBytes(BuildFeedURL(item.getClass(), projectName), null, sw.toString().getBytes("UTF-8"), "Content-Type", "application/xml");
            
            if (r.containsError())
            {
                ur.Status = UploadResultStatus.Failure;
                return ur;
            }
            if (r.getResponseCode() == 303)
            {
                ur.Status = UploadResultStatus.Duplicate;
                ur.URL = BuildFeedURL(item.getClass(), projectName);
                ur.ContentLocation = r.getHeaders("Content-Location").get(0);
                return ur;
            }
            ur.Status = UploadResultStatus.Success;
            ur.URL = BuildFeedURL(item.getClass(), projectName);
            ur.ContentLocation = r.getHeaders("Content-Location").get(0);
            return ur;
        }
        catch (JAXBException e)
        {
            log.error("Unable to convert the desired JAXB object to XML.", e);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("Unable to convert the desired JAXB object to a string.", e);
        }
        ur.Status = UploadResultStatus.Failure;
        return ur;
    }
    
    /**
     * Uploads a file to a RQM project's "attachments" artifact collection.<br>
     * You can send any arbitrary file.<br>
     * This sends the file as "multipart/form-data", which is how browsers upload files when you use an [input type="file"] html tag
     * 
     * @param projectName
     *            The API name of the project
     * @param id
     *            The ID to assign to the attachment
     * @param fileName
     *            The file name to assign to the attachment
     * @param contentMIMEType
     *            The MIME type of the file you're uploading (e.g. text/plan, application/pdf, etc).<br>
     *            Use "application/octet-stream" if you don't know what to put here.
     * @param file
     *            The actual bytes of the file you're uploading
     * @return The result of the upload.
     */
    public UploadResult PostAttachment(String projectName, String id, String fileName, String contentMIMEType, byte[] file)
    {
        if (StringFunctions.isNullOrWhitespace(projectName))
        {
            throw new IllegalArgumentException("The argument \"projectName\" can not be null or whitespace.");
        }
        if (file == null || file.length == 0)
        {
            throw new IllegalArgumentException("The argument \"file\" can not be null or a 0-length byte array.");
        }
        
        String url = String.format(URLTemplates.AttachmentByID, serverURL, integrationPath, projectName, id);
        HTTPResponse r = hc.postBytesAsMultipart(url, null, fileName, contentMIMEType, file);
        
        UploadResult ur = new UploadResult();
        if (r.containsError())
        {
            ur.Status = UploadResultStatus.Failure;
            return ur;
        }
        if (r.getResponseCode() == 303)
        {
            ur.Status = UploadResultStatus.Duplicate;
            ur.URL = url;
            ur.ContentLocation = r.getHeaders("Content-Location").get(0);
            return ur;
        }
        ur.Status = UploadResultStatus.Success;
        ur.URL = url;
        ur.ContentLocation = r.getHeaders("Content-Location").get(0);
        return ur;
    }
    
    /**
     * Gets all the RQM projects on this RQM server.<br>
     * Once you have this list you can convert a project's UI name into its API name by looking at Project.Title (the UI name) and Project.Aliases[0].Value (the API name)
     * 
     * @return All the projects on this RQM server
     */
    public List<Entry> GetProjects()
    {
        return GetFeedOf(String.format("%1$s%2$sprojects/", serverURL, integrationPath));
    }
    
    /**
     * Gets all pages of the given artifact feed. <br>
     * (e.g. gets all the pages of the testcase feed or testscript feed, etc) <br>
     * <br>
     * The artifact types with feeds are: <br>
     * executionresult <br>
     * testplan <br>
     * testphase <br>
     * testcase <br>
     * testscript <br>
     * executionworkitem <br>
     * requirement <br>
     * datapool <br>
     * tooladapter <br>
     * adaptertask <br>
     * template <br>
     * testsuite <br>
     * keyword <br>
     * testsuitelog <br>
     * suiteexecutionrecord <br>
     * configuration <br>
     * remotescript <br>
     * request <br>
     * reservation <br>
     * resourcecollection <br>
     * resourcegroup <br>
     * labresource <br>
     * job <br>
     * jobresult <br>
     * teamarea <br>
     * contributor <br>
     * workitem <br>
     * buildrecord <br>
     * builddefinition <br>
     * testcell <br>
     * objective <br>
     * project <br>
     * attachment <br>
     * category <br>
     * categoryType
     * 
     * @param <X>
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param type
     *            The artifact type you want (the Java class represnting one of the items above)
     * @param projectName
     *            The RQM project to pull the feed from. If an empty string, null, or whitespace it will try to hit a RQM 4.0 global feed
     * @param filteringOptions
     *            Additional options to pass to the RQM server for filtering. See the wiki on jazz.net for details.<br>
     *            They must alternate between option name and value
     * @return All the pages of the feed
     */
    public <X> List<Entry> GetArtifacts(Class<X> type, String projectName, String...filteringOptions)
    {
        return GetFeedOf(BuildFeedURL(type, projectName), filteringOptions);
    }
    
    /**
     * Gets all pages of the given RQM 4.0 global artifact feed. <br>
     * (e.g. gets all the pages of the testcase feed or testscript feed, etc) <br>
     * <br>
     * The artifact types with feeds are: <br>
     * executionresult <br>
     * testplan <br>
     * testphase <br>
     * testcase <br>
     * testscript <br>
     * executionworkitem <br>
     * requirement <br>
     * datapool <br>
     * tooladapter <br>
     * adaptertask <br>
     * template <br>
     * testsuite <br>
     * keyword <br>
     * testsuitelog <br>
     * suiteexecutionrecord <br>
     * configuration <br>
     * remotescript <br>
     * request <br>
     * reservation <br>
     * resourcecollection <br>
     * resourcegroup <br>
     * labresource <br>
     * job <br>
     * jobresult <br>
     * teamarea <br>
     * contributor <br>
     * workitem <br>
     * buildrecord <br>
     * builddefinition <br>
     * testcell <br>
     * objective <br>
     * project <br>
     * attachment <br>
     * category <br>
     * categoryType
     * 
     * @param <X>
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param type
     *            The artifact type you want (the Java class represnting one of the items above)
     * @return All the pages of the RQM 4.0 global feed
     */
    public <X> List<Entry> GetArtifacts(Class<X> type)
    {
        return GetFeedOf(BuildFeedURL(type, ""));
    }
    
    /**
     * Helper method used to build feed URLs for a given artifact type and optionally project
     * 
     * @param <X>
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param type
     *            An artifact type with a feed. See {@link #GetArtifacts(Class, String, String...)}.
     * @param project
     *            Optional: the API name of the project that has the feed you want.<br>
     *            Use null, whitespace, or empty string to hit a RQM 4.0 global feed.
     * @return The full URL to the feed
     */
    private <X> String BuildFeedURL(Class<X> type, String project)
    {
        String artifact = type.getSimpleName().toLowerCase();
        return String.format("%1$s%2$sresources/%3$s/", serverURL, integrationPath, !StringFunctions.isNullOrWhitespace(project) ? String.format("%1$s/%2$s", project, artifact) : artifact);
    }
    
    /**
     * This function downloads all pages of the given RQM feed.<br>
     * It attempts to do so in parallel using threads, to make up for individual feed pages loading slowly
     * 
     * @param baseURL
     *            The URL of the feed
     * @param queryParams
     *            Additional parameters to append to the URL (alternate between names and values)
     * @return All the items on all the pages of this feed.
     */
    private List<Entry> GetFeedOf(String baseURL, String...queryParams)
    {
        String url = baseURL;
        if (queryParams != null && queryParams.length > 0)
        {
            try
            {
                StringBuilder postData = new StringBuilder();
                int lim = queryParams.length % 2 == 0 ? queryParams.length : queryParams.length - 1;
                for (int i = 0; i < lim; i += 2)
                {
                    if (StringFunctions.isNullOrWhitespace(queryParams[i]) || StringFunctions.isNullOrWhitespace(queryParams[i + 1]))
                    {
                        continue;
                    }
                    postData.append(URLEncoder.encode(queryParams[i], "ASCII"));
                    postData.append("=");
                    postData.append(URLEncoder.encode(queryParams[i + 1], "ASCII"));
                    postData.append("&");
                }
                String finalS = postData.toString();
                if (finalS.endsWith("&"))
                {
                    finalS = finalS.substring(0, finalS.length() - 1);
                }
                url += "?" + finalS;
            }
            catch (UnsupportedEncodingException e)
            {
                log.error("Unable to append query parameters", e);
                url = baseURL;
            }
        }
        
        // determine last page by downloading first page and parsing links
        Feed f = GetURLAs(Feed.class, url);
        int lastPage = 0;
        for (Link l : f.getLink())
        {
            if ("last".equals(l.getRel()))
            {
                Map<String, List<String>> parsed = HTTPClient.parseURLQueryString(l.getHref());
                lastPage = Integer.parseInt(parsed.get("page").get(0));
                break;
            }
        }
        
        List<String> urls = new ArrayList<String>();
        for (int i = 0; i <= lastPage; i++)
        {
            urls.add(url.replaceAll("page=\\d+", "page=" + i));
        }
        
        final Vector<Entry> items = new Vector<Entry>();
        try
        {
            Parallel.ForEach(urls, new LoopBody<String>()
            {
                
                @Override
                public void run(String currentURL)
                {
                    try
                    {
                        Feed currentPage = GetURLAs(Feed.class, currentURL);
                        for (Entry e : currentPage.getEntry())
                        {
                            items.add(e);
                        }
                    }
                    catch (Exception e)
                    {
                        log.error("Unable to download all pages of the requested feed.", e);
                    }
                }
                
            });
        }
        catch (Exception e)
        {
            log.error("Unable to download all pages of the requested feed.", e);
        }
        Collections.sort(items, new Comparator<Entry>()
        {
            
            @Override
            public int compare(Entry o1, Entry o2)
            {
                if (o1 == null || o1.getUpdated() == null)
                {
                    return -1;
                }
                if (o2 == null || o2.getUpdated() == null)
                {
                    return 1;
                }
                return o2.getUpdated().compare(o1.getUpdated());
            }
        });
        return items;
        
    }
    
    /**
     * Downloads the specified URL and attempts to convert it into a Java object of the specified type.<br>
     * The download had better return XML.<br>
     * This is the main function used when downloading assets from RQM.
     * 
     * @param <X>
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param type
     *            The type you want to serialize the download into
     * @param url
     *            The URL to download
     * @return Either the XML returned from that URL serialized into a Java object, or null if something went wrong.
     */
    @SuppressWarnings("unchecked")
    public <X> X GetURLAs(Class<X> type, String url)
    {
        HTTPResponse obj = hc.get(url);
        if (obj.containsError())
        {
            log.error("Received error trying to get the URL " + url + " as an instance of " + type.getSimpleName().toLowerCase());
            log.error("HTTP Error " + obj.getResponseCode());
            log.error("Response Body:");
            log.error(obj.getResponseBodyAsString());
            return null;
        }
        
        Object o = null;
        try
        {
            JAXBContext jc = JAXBContext.newInstance(type);
            Unmarshaller um = jc.createUnmarshaller();
            o = um.unmarshal(new StringReader(obj.getResponseBodyAsString()));
        }
        catch (JAXBException e)
        {
            log.error("Unable to convert the desired document to a JAXB XML object.", e);
        }
        return (X) o;
    }
    
    /**
     * Checks to see if the specified item exists in the specified project
     * 
     * @param <X>
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param type
     *            The type of the artifact (testplan, testcase, etc)
     * @param projectName
     *            The API name of the RQM project you're polling
     * @param webID
     *            The UI ID of the artifact you're looking for
     * @return True if the artifact exists, False if not
     */
    public <X> boolean DoesItemExist(Class<X> type, String projectName, int webID)
    {
        if (StringFunctions.isNullOrWhitespace(projectName))
        {
            throw new IllegalArgumentException("The argument \"projectName\" can not be null or whitespace.");
        }
        if (webID <= 0)
        {
            throw new IndexOutOfBoundsException("The argument \"webID\" must be greater than 0.");
        }
        try
        {
            return GetURLAs(type, String.format(URLTemplates.ArtifactByWebID, serverURL, integrationPath, projectName, type.getSimpleName().toLowerCase(), webID)) != null;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * Checks to see if the specified item exists in the specified project
     * 
     * @param <X>
     *            The type of the artifact you want (testcase, testscript, testplan, etc)
     * @param type
     *            The type of the artifact (testplan, testcase, etc)
     * @param projectName
     *            The API name of the RQM project you're polling
     * @param id
     *            The internal ID of the artifact you're looking for
     * @return True if the artifact exists, False if not
     */
    public <X> boolean DoesItemExist(Class<X> type, String projectName, String id)
    {
        if (StringFunctions.isNullOrWhitespace(projectName))
        {
            throw new IllegalArgumentException("The argument \"projectName\" can not be null or whitespace.");
        }
        if (StringFunctions.isNullOrWhitespace(id))
        {
            throw new IllegalArgumentException("The argument \"id\" can not be null or whitespace.");
        }
        try
        {
            return GetURLAs(type, String.format(URLTemplates.ArtifactByWebID, serverURL, integrationPath, projectName, type.getSimpleName().toLowerCase(), id)) != null;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
