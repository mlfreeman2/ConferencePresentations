// $Id: HTTPResponse.java 3359 2012-02-01 15:17:38Z mlfreeman $
package org.mlfreeman.innovate2013.network.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * A response from one of the HTTP action methods in {@link HTTPClient}.<br>
 * This class was created because according to Apache HttpClient docs you need to free their HTTP response object ASAP <br>
 * Our HTTPClient does so by copying essential information from it into an instance of this.
 */
public class HTTPResponse
{
    /**
     * The HTTP Content-Encoding header
     */
    private String              contentEncoding        = "";
    
    /**
     * The HTTP Content-Length header
     */
    private long                contentLength;
    
    /**
     * The HTTP Content-Type header
     */
    private String              contentType            = "";
    
    /**
     * A collection for the other header field names. HTTP allows multiple headers with the same name.<br>
     * Package visible so that only the HTTPClient class can access it to set fields in it.
     */
    Map<String, List<String>>   headers                = new LinkedHashMap<String, List<String>>();
    
    /**
     * True if the body of the response was stored in RAM, False if it was spooled out as a file
     */
    private boolean             isResponseBodyInMemory = true;
    
    /**
     * If there was a response body and it was kept in RAM this would hold it.<br>
     */
    private byte[]              responseBody           = new byte[0];
    
    /**
     * The HTTP status code that was returned
     */
    private int                 responseCode;
    
    /**
     * If there was a response body and it was spooled to disk this would be the path.<br>
     */
    private String              responseFileName       = "";
    
    /**
     * The message after the HTTP status code
     */
    private String              responseLine           = "";
    
    /**
     * How long the request took, in milliseconds
     */
    private long                duration;
    
    private static final Logger log                    = LogManager.getLogger(HTTPResponse.class);
    
    /**
     * @return the HTTP Content-Encoding header
     */
    public String getContentEncoding()
    {
        return contentEncoding;
    }
    
    /**
     * @return the HTTP Content-Length header
     */
    public long getContentLength()
    {
        return contentLength;
    }
    
    /**
     * @return the HTTP Content-Type header
     */
    public String getContentType()
    {
        return contentType;
    }
    
    /**
     * @return the names for all the headers in the response, except for Content-Encoding, Content-Type, and Content-Length.
     */
    public List<String> getHeaderNames()
    {
        return new ArrayList<String>(headers.keySet());
    }
    
    /**
     * HTTP allows multiple headers with the same name, so this function finds all headers matching the supplied name and returns their values<br>
     * This excludes Content-Encoding, Content-Type, and Content-Length.<br>
     * 
     * @param name
     *            The name of the header.
     * @return All values for the supplied header name
     */
    public ArrayList<String> getHeaders(String name)
    {
        if (headers.get(name) == null)
        {
            return new ArrayList<String>();
        }
        return new ArrayList<String>(headers.get(name));
    }
    
    /**
     * Regardless of whether the response body was saved to disk or stored in RAM this will load it.
     * 
     * @return the body of the response
     */
    public byte[] getResponseBody()
    {
        if (isResponseBodyInMemory)
        {
            return responseBody;
        }
        else
        {
            try
            {
                File f = new File(responseFileName);
                byte[] bytes = new byte[(int) f.length()];
                InputStream is = new FileInputStream(f);
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
                {
                    offset += numRead;
                }
                if (offset < bytes.length)
                {
                    throw new IOException("Could not completely read file " + f.getName());
                }
                is.close();
                return bytes;
            }
            catch (Exception e)
            {
                log.error("The file could not be read as a byte array.", e);
                return new byte[0];
            }
        }
    }
    
    /**
     * This function is a convenience wrapper for converting the raw bytes of the body into a string.<br>
     * Tries to use the default encoding (as returned by {@link #getContentEncoding()} first, then tries US-ASCII if that fails.<br>
     * Returns null if neither works.
     * 
     * @return The string form of the bytes making up the response body, or null if the bytes couldn't be converted into a Java string.
     */
    public String getResponseBodyAsString()
    {
        String s = getResponseBodyAsString(getContentEncoding());
        if (s == null)
        {
            s = getResponseBodyAsString("US-ASCII");
        }
        return s;
    }
    
    /**
     * This function is a convenience wrapper for converting the raw bytes of the body into a string.<br>
     * Tries to use a user specified encoding.<br>
     * Returns null if neither works.
     * 
     * @param encoding
     *            The encoding to try to use when turning the bytes into a string
     * @return The string form of the bytes making up the response body, or null if the bytes couldn't be converted into a Java string.
     */
    public String getResponseBodyAsString(String encoding)
    {
        String s = null;
        try
        {
            s = new String(getResponseBody(), encoding);
        }
        catch (Exception e)
        {
            log.error("Unable to read in the response body and encode it as a string with encoding " + encoding + ".", e);
        }
        return s;
    }
    
    /**
     * @return the HTTP response code
     */
    public int getResponseCode()
    {
        return responseCode;
    }
    
    /**
     * @return the name of the file that the response body was stored in, if {@link #isResponseBodyInMemory()} is false
     */
    public String getResponseFileName()
    {
        return responseFileName;
    }
    
    /**
     * @return the message on the HTTP response code line
     */
    public String getResponseLine()
    {
        return responseLine;
    }
    
    /**
     * @return how long the request took, in milliseconds
     */
    public long getDuration()
    {
        return duration;
    }
    
    /**
     * @return True if the response body was stored in RAM. False if it was not.<br>
     *         See {@link #getResponseFileName()} to get the path if it was not stored in RAM.
     */
    public boolean isResponseBodyInMemory()
    {
        return isResponseBodyInMemory;
    }
    
    /**
     * @param contentEncoding
     *            the HTTP Content-Encoding header
     */
    public void setContentEncoding(String contentEncoding)
    {
        this.contentEncoding = contentEncoding;
    }
    
    /**
     * @param contentLength
     *            the HTTP Content-Length header
     */
    public void setContentLength(long contentLength)
    {
        this.contentLength = contentLength;
    }
    
    /**
     * @param contentType
     *            the HTTP Content-Type header
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }
    
    /**
     * @param responseBody
     *            the body of the response. This will set {@link HTTPResponse#isResponseBodyInMemory()} to true
     */
    public void setResponseBody(byte[] responseBody)
    {
        this.responseBody = responseBody;
        isResponseBodyInMemory = true;
    }
    
    /**
     * @param isResponseBodyInMemory
     *            True if this object is holding on to the response body as a byte array, False if the response body was saved to disk instead.
     */
    public void setResponseBodyInMemory(boolean isResponseBodyInMemory)
    {
        this.isResponseBodyInMemory = isResponseBodyInMemory;
    }
    
    /**
     * @param responseCode
     *            the HTTP response code
     */
    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }
    
    /**
     * @param responseFileName
     *            the name of the file that the response body was stored in, if {@link #isResponseBodyInMemory()} is false
     */
    public void setResponseFileName(String responseFileName)
    {
        this.responseFileName = responseFileName;
    }
    
    /**
     * @param responseLine
     *            the message on the HTTP response code line
     */
    public void setResponseLine(String responseLine)
    {
        this.responseLine = responseLine;
    }
    
    /**
     * @param timeElapsed
     *            How long the request took, in milliseconds
     */
    public void setDuration(long timeElapsed)
    {
        this.duration = timeElapsed;
    }
    
    /**
     * Indicates if this HTTP response contains some sort of error condition (e.g. TCP error, HTTP error, or server error)
     * 
     * @return True if this response contains some sort of error condition (e.g. TCP error, HTTP error, or server error)
     */
    public boolean containsError()
    {
        if (containsHTTPError() || containsServerError() || containsTCPError())
        {
            return true;
        }
        return false;
    }
    
    /**
     * Indicates if this HTTP response contains a HTTP 4xx response code because those are reserved for HTTP errors (e.g. 404 not found, 403 access denied, etc)
     * 
     * @return True if this HTTP response contains a HTTP 4xx response, False otherwise
     */
    public boolean containsHTTPError()
    {
        int rc = getResponseCode();
        if (rc >= 400 && rc < 500)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Indicates if this HTTP response contains a TCP error (which we allocated the response code range 0-99 since HTTP doesn't use that)
     * 
     * @return True if this HTTP response indicates a TCP error, False otherwise
     */
    public boolean containsTCPError()
    {
        if (getResponseCode() < 100)
        {
            return true;
        }
        if (getResponseLine() != null && getResponseLine().replace("<", "&lt;").replace(">", "&gt;").contains("Connection reset"))
        {
            return true;
        }
        return false;
    }
    
    /**
     * Indicates if this HTTP response contains a server error. HTTP spec says that should be response codes >= 500, so we check for that
     * 
     * @return True if this HTTP response contains a server error, False otherwise
     */
    public boolean containsServerError()
    {
        return getResponseCode() >= 500;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP Response");
        builder.append(System.getProperty("line.separator") + "isResponseBodyInMemory=");
        builder.append(isResponseBodyInMemory);
        builder.append(System.getProperty("line.separator") + "responseFileName=");
        builder.append(responseFileName);
        builder.append(System.getProperty("line.separator"));
        builder.append(System.getProperty("line.separator"));
        builder.append(responseLine);
        builder.append(System.getProperty("line.separator") + "Content-Length: ");
        builder.append(contentLength);
        builder.append(System.getProperty("line.separator") + "Content-Type: ");
        builder.append(contentType);
        for (String h : headers.keySet())
        {
            builder.append(System.getProperty("line.separator") + "" + h + ": ");
            for (String v : headers.get(h))
            {
                builder.append(System.getProperty("line.separator") + "\t" + v);
            }
        }
        builder.append(System.getProperty("line.separator"));
        builder.append(getResponseBodyAsString());
        return builder.toString();
    }
}
