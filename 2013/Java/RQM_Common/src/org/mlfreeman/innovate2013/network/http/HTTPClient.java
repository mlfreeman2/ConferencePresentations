// $Id: HTTPClient.java 3805 2013-04-08 16:57:19Z mlfreeman $
package org.mlfreeman.innovate2013.network.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mlfreeman.innovate2013.StringFunctions;

/**
 * A simple HTTP client, built on top of Apache HttpClient 4.0
 */
public class HTTPClient
{
    private static final Logger log       = LogManager.getLogger(HTTPClient.class);
    
    /**
     * The User Agent for IE 7 on Win XP.
     */
    public static final String  IE7_WinXP = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)";
    
    /**
     * The User Agent for Apache HTTPClient (with version number removed)
     */
    public static final String  ApacheUA  = "Apache-HttpClient";
    
    /**
     * The actual Apache HTTPClient object
     */
    private DefaultHttpClient   theClient;
    
    /**
     * Set to true to trust any SSL cert, including self-signed ones. <br>
     * Set to false to make it work like a browser and throw an error.
     */
    private boolean             trustingAllSSL;
    
    /**
     * Construct a new instance with "Apache-HttpClient" as the default user agent.<br>
     * Will trust all SSL certificates.
     * 
     * @param keyManagers
     *            The SSL crypto key managers to use to pull custom client certificates from, if you want to do client authentication using SSL certificates
     */
    public HTTPClient(KeyManager...keyManagers)
    {
        this(HTTPClient.ApacheUA, true, false, false, keyManagers);
    }
    
    /**
     * Construct a new instance with "Apache-HttpClient" as the user agent string
     * 
     * @param trustEverySSLCert
     *            True to ignore SSL errors, False to block on them.
     * @param saveCookies
     *            True if you want to save cookies received by the target
     * @param followRedirects
     *            True if you want to follow all HTTP 301 or 302 redirects found by this client
     * @param keyManagers
     *            The SSL crypto key managers to use to pull custom client certificates from, if you want to do client authentication using SSL certificates
     */
    public HTTPClient(boolean trustEverySSLCert, boolean saveCookies, boolean followRedirects, KeyManager...keyManagers)
    {
        this(ApacheUA, trustEverySSLCert, saveCookies, followRedirects, keyManagers);
    }
    
    /**
     * Construct a new instance with a custom user agent string
     * 
     * @param userAgent
     *            The custom user agent string.
     * @param trustEverySSLCert
     *            True to ignore SSL errors, False to block on them.
     * @param saveCookies
     *            True if you want to save cookies received by the target
     * @param followRedirects
     *            True if you want to follow all HTTP 301 or 302 redirects found by this client
     * @param keyManagers
     *            The SSL crypto key managers to use to pull custom client certificates from, if you want to do client authentication using SSL certificates
     */
    public HTTPClient(String userAgent, boolean trustEverySSLCert, boolean saveCookies, boolean followRedirects, KeyManager...keyManagers)
    {
        this(null, null, userAgent, trustEverySSLCert, saveCookies, followRedirects, keyManagers);
    }
    
    /**
     * Construct a new instance with a custom user agent string & a user name and password for use with HTTP auth
     * 
     * @param userName
     *            A user name to supply in HTTP basic, digest, or NTLM authentication
     * @param password
     *            A password to supply in HTTP basic, digest, or NTLM authentication
     * @param userAgent
     *            The custom user agent string.
     * @param trustEverySSLCert
     *            True to ignore SSL errors, False to block on them.
     * @param saveCookies
     *            True if you want to save cookies received by the target
     * @param followRedirects
     *            True if you want to follow all HTTP 301 or 302 redirects found by this client
     * @param keyManagers
     *            The SSL crypto key managers to use to pull custom client certificates from, if you want to do client authentication using SSL certificates
     */
    public HTTPClient(String userName, String password, String userAgent, boolean trustEverySSLCert, boolean saveCookies, boolean followRedirects, KeyManager...keyManagers)
    {
        trustingAllSSL = trustEverySSLCert;
        
        if (keyManagers.length == 0)
        {
            keyManagers = null;
        }
        
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(params, userAgent);
        
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        // In order to handle things like self signed SSL certificates, create a trust manager that won't care.
        X509TrustManager trustManager = new X509TrustManager()
        {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
                // Don't do anything.
            }
            
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
                // Don't do anything.
            }
            
            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                // Don't do anything.
                return null;
            }
        };
        
        try
        {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            
            sslContext.init(keyManagers, trustEverySSLCert ? new TrustManager[] {trustManager} : null, null);
            
            // Now put the trust manager into an SSLContext.
            SSLSocketFactory sf = new SSLSocketFactory(sslContext);
            
            if (trustEverySSLCert)
            {
                // Now add a hostname verifier that doesn't really check hostnames against certificates
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            }
            
            Scheme httpsScheme = new Scheme("https", sf, 443);
            schemeRegistry.register(httpsScheme);
        }
        catch (Exception e)
        {
            log.error("Unable to enable support for self-signed certificates.", e);
        }
        
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        
        theClient = new DefaultHttpClient(cm, params);
        
        if (!StringFunctions.isNullOrWhitespace(userName) && !StringFunctions.isNullOrWhitespace(password))
        {
            // if a user name and password were supplied, pass them to Apache HttpClient to be used if we hit HTTP Authentication
            theClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), (Credentials) new UsernamePasswordCredentials(userName, password));
        }
        
        if (followRedirects)
        {
            HttpClientParams.setRedirecting(params, true);
            theClient.setRedirectHandler(new DefaultRedirectHandler()
            {
                @Override
                public boolean isRedirectRequested(HttpResponse response, HttpContext context)
                {
                    if (!super.isRedirectRequested(response, context))
                    {
                        int responseCode = response.getStatusLine().getStatusCode();
                        if (responseCode == 301 || responseCode == 302)
                        {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        
        if (saveCookies)
        {
            theClient.setCookieStore(new BasicCookieStore());
            theClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        }
    }
    
    /**
     * Closes out / cleans up Apache HttpClient resources.<br>
     * Call this when you're done with an instance of this class.
     */
    public void cleanup()
    {
        theClient.getConnectionManager().closeExpiredConnections();
    }
    
    /**
     * Extracts all the important details from an Apache HttpResponse object, so it can be freed.<br>
     * 
     * @param response
     *            The Apache HttpResponse object
     * @param bodyPath
     *            The on-disk path to store the body of the response.<br>
     *            Null or empty string means to try to store it in memory instead of on disk.
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    private HTTPResponse extractResponseContents(HttpResponse response, String bodyPath)
    {
        Pattern p = Pattern.compile("charset\\s*=\\s*\"?((?:\"[^\"]*\")|(?:[^\"\\s]*))\"?");
        HTTPResponse r = new HTTPResponse();
        if (response == null)
        {
            return r;
        }
        r.setResponseCode(response.getStatusLine().getStatusCode());
        r.setResponseLine(response.getStatusLine().toString());
        for (Header h : response.getAllHeaders())
        {
            if (h.getName().equals("Content-Type"))
            {
                r.setContentType(h.getValue());
                Matcher m = p.matcher(h.getValue());
                if (m.find())
                {
                    r.setContentEncoding(m.group(1));
                }
            }
            else if (h.getName().equals("Content-Length"))
            {
                r.setContentLength(Long.parseLong(h.getValue()));
            }
            else if (h.getName().equals("Content-Encoding"))
            {
                r.setContentEncoding(h.getValue());
            }
            else
            {
                if (!r.headers.containsKey(h.getName()))
                {
                    r.headers.put(h.getName(), new ArrayList<String>());
                }
                r.headers.get(h.getName()).add(h.getValue());
            }
        }
        if (StringFunctions.isNullOrWhitespace(r.getContentEncoding()))
        {
            r.setContentEncoding("US-ASCII");
        }
        if (StringFunctions.isNullOrWhitespace(bodyPath))
        {
            r.setResponseBodyInMemory(true);
            r.setResponseFileName("");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try
            {
                response.getEntity().writeTo(baos);
            }
            catch (Exception e)
            {
                log.error("Unable to write the body of the HTTP response to memory.", e);
            }
            r.setResponseBody(baos.toByteArray());
        }
        else
        {
            r.setResponseBodyInMemory(false);
            r.setResponseFileName(bodyPath);
            try
            {
                FileOutputStream fos = new FileOutputStream(new File(bodyPath));
                response.getEntity().writeTo(fos);
                fos.flush();
                fos.close();
            }
            catch (Exception e)
            {
                log.error("Unable to write the body of the HTTP response to a file.", e);
            }
        }
        return r;
    }
    
    /**
     * Attempts a generic HTTP GET request against the specified URL. <br>
     * The response will be stashed entirely in memory (inside the byte array reachable by {@link HTTPResponse#getResponseBody()}).<br>
     * ALWAYS CHECK THE RESPONSE CODE.<br>
     * A response code of 000 means no connection could be made (see {@link HTTPResponse#getResponseCode()}).<br>
     * In that case, check {@link HTTPResponse#getResponseLine()} for a more detailed error message (usually a Java exception message).<br>
     * 
     * @param URL
     *            The URL to execute a HTTP GET against.
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse get(String URL)
    {
        return get(URL, null);
    }
    
    /**
     * Attempts to repeatedly HTTP GET the specified URL until a 2xx response code is returned and the response body has something in it.<br>
     * It will try up to a supplied number of times, waiting a supplied delay between them.<br>
     * ALWAYS CHECK THE RESPONSE CODE.<br>
     * A response code of 000 means no connection could be made (see {@link HTTPResponse#getResponseCode()}).<br>
     * In that case, check {@link HTTPResponse#getResponseLine()} for a more detailed error message (usually a Java exception message).<br>
     * 
     * @param URL
     *            The URL to HTTP GET repeatedly
     * @param path
     *            The path on disk to stash a valid response (if one comes back). Null or empty string means to try to stash the response in memory.
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse get(String URL, String path, String...additionalHeaderNamesAndValues)
    {
        HttpGet httpget = new HttpGet(URL);
        addHeaders(httpget, additionalHeaderNamesAndValues);
        HTTPResponse r = doHttpThing(httpget, path);
        cleanup();
        return r;
    }
    
    /**
     * Attempts a HTTP PUT request to the specified URL
     * 
     * @param URL
     *            The URL to HTTP PUT data to
     * @param responsePath
     *            The path on disk to stash a valid response (if one comes back). Null or empty string means to try to stash the response in memory.
     * @param requestFile
     *            The path to the file to include with this HTTP PUT request
     * @param mimeType
     *            The MIME type of the file being sent up via a HTTP PUT
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse putFile(String URL, String responsePath, File requestFile, String mimeType, String...additionalHeaderNamesAndValues)
    {
        HttpPut httpput = new HttpPut(URL);
        addHeaders(httpput, additionalHeaderNamesAndValues);
        httpput.setEntity(new FileEntity(requestFile, mimeType));
        HTTPResponse r = doHttpThing(httpput, responsePath);
        cleanup();
        return r;
    }
    
    /**
     * Attempts a HTTP PUT request to the specified URL
     * 
     * @param URL
     *            The URL to HTTP PUT data to
     * @param responsePath
     *            The path on disk to stash a valid response (if one comes back). Null or empty string means to try to stash the response in memory.
     * @param putPayload
     *            A byte array to include in the PUT request
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse putBytes(String URL, String responsePath, byte[] putPayload, String...additionalHeaderNamesAndValues)
    {
        HttpPut httpput = new HttpPut(URL);
        addHeaders(httpput, additionalHeaderNamesAndValues);
        httpput.setEntity(new ByteArrayEntity(putPayload));
        HTTPResponse r = doHttpThing(httpput, responsePath);
        cleanup();
        return r;
    }
    
    /**
     * Attempts a HTTP PUT request to the specified URL
     * 
     * @param URL
     *            The URL to HTTP PUT data to
     * @param responsePath
     *            The path on disk to stash a valid response (if one comes back). Null or empty string means to try to stash the response in memory.
     * @param putPayload
     *            A string to include in the PUT request
     * @param charset
     *            The character set of the string included in the PUT request
     * @param mimeType
     *            The MIME type that the raw string represents
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse putString(String URL, String responsePath, String putPayload, String charset, String mimeType, String...additionalHeaderNamesAndValues)
    {
        HttpPut httpput = new HttpPut(URL);
        try
        {
            addHeaders(httpput, additionalHeaderNamesAndValues);
            httpput.setEntity(new StringEntity(putPayload, charset, mimeType));
        }
        catch (UnsupportedEncodingException e)
        {
            HTTPResponse r = new HTTPResponse();
            r.setResponseCode(0);
            r.setResponseLine("Unable to set the HTTP PUT body from [" + putPayload + "]: " + e.getMessage());
            log.error("Unable to set the HTTP PUT body from the supplied payload.", e);
            return r;
        }
        HTTPResponse r = doHttpThing(httpput, responsePath);
        cleanup();
        return r;
    }
    
    /**
     * Attempts a HTTP DELETE request against the specified URL
     * 
     * @param URL
     *            The URL to HTTP PUT data to
     * @param responsePath
     *            The path on disk to stash a valid response (if one comes back). Null or empty string means to try to stash the response in memory.
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse delete(String URL, String responsePath, String...additionalHeaderNamesAndValues)
    {
        HttpDelete httpdelete = new HttpDelete(URL);
        addHeaders(httpdelete, additionalHeaderNamesAndValues);
        HTTPResponse r = doHttpThing(httpdelete, responsePath);
        cleanup();
        return r;
    }
    
    /**
     * Attempts to send the specified file located at the specified path to the specified URL, using a HTTP POST command.
     * 
     * @param URL
     *            The URL to try to send the file to.
     * @param responsePath
     *            The path to store the response in, if necessary (null or empty string means store it in memory instead of on disk)
     * @param mimeType
     *            The MIME type to send the file as.
     * @param path
     *            The path of the file to send.
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse postFile(String URL, String responsePath, String mimeType, File path, String...additionalHeaderNamesAndValues)
    {
        HttpPost httppost = new HttpPost(URL);
        httppost.setEntity(new FileEntity(path, mimeType));
        addHeaders(httppost, additionalHeaderNamesAndValues);
        HTTPResponse r = doHttpThing(httppost, responsePath);
        cleanup();
        return r;
    }
    
    /**
     * Attempts to send the specified file located at the specified path to the specified URL, using a HTTP POST command.
     * 
     * @param URL
     *            The URL to try to send the file to.
     * @param responsePath
     *            The path to store the response in, if necessary (null or empty string means store it in memory instead of on disk)
     * @param mimeType
     *            The MIME type to send the file as.
     * @param fileName
     *            The name to assign the file when you upload it
     * @param path
     *            The file to send
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse postFileAsMultipart(String URL, String responsePath, String fileName, String mimeType, File path, String...additionalHeaderNamesAndValues)
    {
        HttpPost httppost = new HttpPost(URL);
        
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(path, mimeType);
        entity.addPart(fileName, fileBody);
        
        addHeaders(httppost, additionalHeaderNamesAndValues);
        HTTPResponse r = doHttpThing(httppost, responsePath);
        cleanup();
        return r;
    }
    
    /**
     * Attempts to send the specified file located at the specified path to the specified URL, using a HTTP POST command.
     * 
     * @param URL
     *            The URL to try to send the file to.
     * @param responsePath
     *            The path to store the response in, if necessary (null or empty string means store it in memory instead of on disk)
     * @param payload
     *            The bytes of the file to send.
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse postBytes(String URL, String responsePath, byte[] payload, String...additionalHeaderNamesAndValues)
    {
        HttpPost httppost = new HttpPost(URL);
        httppost.setEntity(new ByteArrayEntity(payload));
        addHeaders(httppost, additionalHeaderNamesAndValues);
        HTTPResponse r = doHttpThing(httppost, responsePath);
        cleanup();
        return r;
    }
    
    /**
     * Attempts to send the specified file located at the specified path to the specified URL, using a HTTP POST command.
     * 
     * @param URL
     *            The URL to try to send the file to.
     * @param responsePath
     *            The path to store the response in, if necessary (null or empty string means store it in memory instead of on disk)
     * @param mimeType
     *            The MIME type to send the file as.
     * @param fileName
     *            The name to assign to the file on upload
     * @param payload
     *            The bytes of the file to send
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse postBytesAsMultipart(String URL, String responsePath, String fileName, String mimeType, byte[] payload, String...additionalHeaderNamesAndValues)
    {
        HttpPost httppost = new HttpPost(URL);
        
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        InputStreamBody isb = new InputStreamBody(new ByteArrayInputStream(payload), fileName);
        entity.addPart(fileName, isb);
        
        addHeaders(httppost, additionalHeaderNamesAndValues);
        HTTPResponse r = doHttpThing(httppost, responsePath);
        cleanup();
        return r;
    }
    
    /**
     * Attempts to send the specified name-value pairs to the specified URL, using a HTTP POST command and treating them as if they were form names and values.
     * 
     * @param URL
     *            The URL to try to send the form fields to
     * @param responsePath
     *            The path to store the response in, if necessary (null or empty string means store it in memory instead of on disk)
     * @param additionalHeaderNamesAndValues
     *            Additional HTTP header names and values to be included in the request's HTTP header
     * @param elements
     *            A name-value alternating series of "form fields"
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse postForm(String URL, String responsePath, Map<String, List<String>> additionalHeaderNamesAndValues, String...elements)
    {
        HTTPResponse r = new HTTPResponse();
        
        int arrayLen = elements.length;
        if (elements.length % 2 != 0)
        {
            System.out.println("Odd number of form parameters supplied. Ignoring the last element.");
            arrayLen -= 1;
        }
        
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (int i = 0; i < arrayLen; i += 2)
        {
            formparams.add(new BasicNameValuePair(elements[i], elements[i + 1]));
        }
        HttpPost httppost = new HttpPost(URL);
        
        try
        {
            httppost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
            addHeaders(httppost, additionalHeaderNamesAndValues);
        }
        catch (UnsupportedEncodingException e)
        {
            r.setResponseCode(0);
            r.setResponseLine("Unable to set the HTTP POST body from the supplied form values.");
            System.err.println("Unable to set the HTTP POST body from the supplied form values.");
            e.printStackTrace();
            return r;
        }
        return doHttpThing(httppost, responsePath);
    }
    
    /**
     * Attempts to send the specified string to the specified URL, using a HTTP POST command.
     * 
     * @param URL
     *            The URL to try to send the string to.
     * @param responsePath
     *            The path to store the response in, if necessary (null or empty string means store it in memory instead of on disk)
     * @param postBody
     *            The raw string to post to the server.
     * @param charSet
     *            The character set to encode the String in.
     * @param contentTypeHeader
     *            The value for the HTTP Content-Type header (specifying the type of content being posted)
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse post(String URL, String responsePath, String postBody, String charSet, String contentTypeHeader, Map<String, List<String>> additionalHeaderNamesAndValues)
    {
        HTTPResponse r = new HTTPResponse();
        HttpPost httppost = new HttpPost(URL);
        try
        {
            httppost.setEntity(new StringEntity(postBody, charSet));
            addHeaders(httppost, additionalHeaderNamesAndValues);
        }
        catch (UnsupportedEncodingException e)
        {
            r.setResponseCode(0);
            r.setResponseLine("Unable to set the HTTP POST body from " + postBody + ": " + e.getMessage());
            System.err.println("Unable to set the HTTP POST body from the supplied payload.");
            e.printStackTrace();
            return r;
        }
        httppost.setHeader("Content-Type", contentTypeHeader);
        return doHttpThing(httppost, responsePath);
    }
    
    /**
     * Attempts to send the specified string to the specified URL, using a HTTP POST command.
     * 
     * @param URL
     *            The URL to try to send the string to.
     * @param responsePath
     *            The path to store the response in, if necessary (null or empty string means store it in memory instead of on disk)
     * @param postBody
     *            The raw string to post to the server.
     * @param charSet
     *            The character set to encode the String in.
     * @param contentTypeHeader
     *            The value for the HTTP Content-Type header (specifying the type of content being posted)
     * @param additionalHeaderNamesAndValues
     *            Alternating HTTP header names and values to be included in the request's HTTP header
     * @return A {@link HTTPResponse} object describing what was returned from the server.
     */
    public HTTPResponse post(String URL, String responsePath, String postBody, String charSet, String contentTypeHeader, String...additionalHeaderNamesAndValues)
    {
        HTTPResponse r = new HTTPResponse();
        HttpPost httppost = new HttpPost(URL);
        try
        {
            httppost.setEntity(new StringEntity(postBody, charSet));
            addHeaders(httppost, additionalHeaderNamesAndValues);
        }
        catch (UnsupportedEncodingException e)
        {
            r.setResponseCode(0);
            r.setResponseLine("Unable to set the HTTP POST body from " + postBody + ": " + e.getMessage());
            System.err.println("Unable to set the HTTP POST body from the supplied payload.");
            e.printStackTrace();
            return r;
        }
        httppost.setHeader("Content-Type", contentTypeHeader);
        return doHttpThing(httppost, responsePath);
    }
    
    public boolean isTrustingAllSSL()
    {
        return trustingAllSSL;
    }
    
    private void addHeaders(HttpRequestBase httpRequest, String...additionalHeaderNamesAndValues)
    {
        if (additionalHeaderNamesAndValues != null && additionalHeaderNamesAndValues.length > 0)
        {
            int highestIndex = additionalHeaderNamesAndValues.length - 1;
            if (highestIndex % 2 != 0)
            {
                // since this is name/value pairs we have to only process an even number of strings
                // if an odd number of them are supplied we ignore the last one
                highestIndex -= 1;
            }
            for (int i = 0; i <= highestIndex; i += 2)
            {
                httpRequest.setHeader(additionalHeaderNamesAndValues[i], additionalHeaderNamesAndValues[i + 1]);
            }
        }
    }
    
    private void addHeaders(HttpRequestBase httpRequest, Map<String, List<String>> additionalHeaderNamesAndValues)
    {
        if (additionalHeaderNamesAndValues != null && additionalHeaderNamesAndValues.size() > 0)
        {
            for (String name : additionalHeaderNamesAndValues.keySet())
            {
                for (String value : additionalHeaderNamesAndValues.get(name))
                {
                    httpRequest.setHeader(name, value);
                }
            }
        }
    }
    
    private HTTPResponse doHttpThing(HttpUriRequest hur, String responsePath)
    {
        HTTPResponse r = new HTTPResponse();
        
        try
        {
            long startTime = System.currentTimeMillis();
            HttpResponse response = theClient.execute(hur);
            long endTime = System.currentTimeMillis();
            
            r = extractResponseContents(response, responsePath);
            r.setDuration(endTime - startTime);
        }
        catch (Exception e)
        {
            r.setResponseCode(0);
            r.setResponseLine(e.getMessage());
        }
        
        return r;
    }
    
    /**
     * This function attempts to parse the query string part of a URL back into a map of keys and values.<br>
     * Technically, keys can appear more than once, so we have to represent that as a Map of Strings with Lists of Strings as the values.
     * 
     * @param URL
     *            The URL to parse
     * @return The parsed query string from said URL
     */
    public static Map<String, List<String>> parseURLQueryString(String URL)
    {
        if (StringFunctions.isNullOrWhitespace(URL))
        {
            return new LinkedHashMap<String, List<String>>();
        }
        String target = URL;
        if (URL.contains("?"))
        {
            target = URL.substring(URL.indexOf("?"));
        }
        Map<String, List<String>> parts = new LinkedHashMap<String, List<String>>();
        
        for (String section : target.split("&"))
        {
            try
            {
                String[] split = section.split("=");
                String name = java.net.URLDecoder.decode(split[0], "US-ASCII");
                if (!parts.containsKey(name))
                {
                    parts.put(name, new ArrayList<String>());
                }
                if (split.length > 1)
                {
                    parts.get(name).add(java.net.URLDecoder.decode(split[1], "US-ASCII"));
                }
            }
            catch (Exception e)
            {
                log.error("Unable to parse part of query string", e);
            }
        }
        return parts;
    }
}
