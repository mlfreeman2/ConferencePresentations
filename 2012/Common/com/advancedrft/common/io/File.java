package com.advancedrft.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.lang.StringFunctions;

/**
 * Common helper functions for working with files. Fills gaps in java.io.File, including things like copy()
 */
public class File extends java.io.File
{
    private static final long serialVersionUID = 80363073750168464L;
    
    /**
     * This function uses Java NIO to copy a file from one path to another in 64 MByte chunks.<br>
     * 64 MByte chunks is the largest we could use here because of Windows
     * 
     * @param src
     *            Source path to copy from
     * @param dest
     *            Destination path to copy to. If destination is a directory, the file portion of src will be appended to it.<br>
     *            (e.g. copy("c:\\boot.ini", "c:\\foo\\") will result in c:\foo\boot.ini being created.
     * @return True if copy succeeded, False if not.
     */
    public static boolean copy(String src, String dest)
    {
        boolean success = false;
        File source = new File(src);
        File destination = new File(dest);
        // If the destination is a directory, we need to append the file portion of src to it
        if (destination.isDirectory())
        {
            dest += java.io.File.separator + source.getName();
            destination = new File(dest);
        }
        // If we can read the file, start the copy
        if (source.isFile() && source.canRead())
        {
            FileChannel inChannel = null, outChannel = null;
            try
            {
                inChannel = new FileInputStream(source).getChannel();
                outChannel = new FileOutputStream(destination).getChannel();
                // Windows can't copy more than 64 MB at a time, so we'll copy 64MB - 32KB at a time
                int maxCount = 64 * 1024 * 1024 - 32 * 1024;
                long size = inChannel.size();
                long position = 0;
                while (position < size)
                {
                    position += inChannel.transferTo(position, maxCount, outChannel);
                }
                success = true;
            }
            catch (Exception e)
            {
                Log.logException(e);
                success = false;
            }
            if (success == true)
            {
                try
                {
                    if (inChannel != null)
                    {
                        inChannel.close();
                    }
                    if (outChannel != null)
                    {
                        outChannel.close();
                    }
                    success = true;
                }
                catch (Exception e)
                {
                    Log.logException(e);
                    success = false;
                }
            }
        }
        return success;
    }
    
    /**
     * Creates a new <code>File</code> instance by converting the given pathname string into an abstract pathname. If the given string is the empty string, then the result is the empty abstract pathname.
     * 
     * @param pathname
     *            A pathname string
     * @throws NullPointerException
     *             If the <code>pathname</code> argument is <code>null</code>
     */
    public File(String fileName)
    {
        super(fileName);
    }
    
    /**
     * Creates a new <code>File</code> instance from a parent abstract pathname and a child pathname string.
     * <p>
     * If <code>parent</code> is <code>null</code> then the new <code>File</code> instance is created as if by invoking the single-argument <code>File</code> constructor on the given <code>child</code> pathname string.
     * <p>
     * Otherwise the <code>parent</code> abstract pathname is taken to denote a directory, and the <code>child</code> pathname string is taken to denote either a directory or a file. If the <code>child</code> pathname string is absolute then it is converted into a relative pathname in a system-dependent way. If <code>parent</code> is the empty abstract pathname then the new <code>File</code> instance is created by converting <code>child</code> into an abstract pathname and resolving the result against a system-dependent default directory. Otherwise each pathname string is converted into an abstract pathname and the child abstract pathname is resolved against the parent.
     * 
     * @param parent
     *            The parent abstract pathname
     * @param child
     *            The child pathname string
     * @throws NullPointerException
     *             If <code>child</code> is <code>null</code>
     */
    public File(File parent, String child)
    {
        super(parent, child);
    }
    
    /**
     * Creates a new <code>File</code> instance from a parent pathname string and a child pathname string.
     * <p>
     * If <code>parent</code> is <code>null</code> then the new <code>File</code> instance is created as if by invoking the single-argument <code>File</code> constructor on the given <code>child</code> pathname string.
     * <p>
     * Otherwise the <code>parent</code> pathname string is taken to denote a directory, and the <code>child</code> pathname string is taken to denote either a directory or a file. If the <code>child</code> pathname string is absolute then it is converted into a relative pathname in a system-dependent way. If <code>parent</code> is the empty string then the new <code>File</code> instance is created by converting <code>child</code> into an abstract pathname and resolving the result against a system-dependent default directory. Otherwise each pathname string is converted into an abstract pathname and the child abstract pathname is resolved against the parent.
     * 
     * @param parent
     *            The parent pathname string
     * @param child
     *            The child pathname string
     * @throws NullPointerException
     *             If <code>child</code> is <code>null</code>
     */
    public File(String parent, String child)
    {
        super(parent, child);
    }
    
    /**
     * Creates a new <tt>File</tt> instance by converting the given <tt>file:</tt> URI into an abstract pathname.
     * <p>
     * The exact form of a <tt>file:</tt> URI is system-dependent, hence the transformation performed by this constructor is also system-dependent.
     * <p>
     * For a given abstract pathname <i>f</i> it is guaranteed that <blockquote><tt>
     * new File(</tt><i>&nbsp;f</i><tt>.{@link #toURI() toURI}()).equals(</tt><i>&nbsp;f</i><tt>.{@link #getAbsoluteFile() getAbsoluteFile}())
     * </tt></blockquote> so long as the original abstract pathname, the URI, and the new abstract pathname are all created in (possibly different invocations of) the same Java virtual machine. This relationship typically does not hold, however, when a <tt>file:</tt> URI that is created in a virtual machine on one operating system is converted into an abstract pathname in a virtual machine on a different operating system.
     * 
     * @param uri
     *            An absolute, hierarchical URI with a scheme equal to <tt>"file"</tt>, a non-empty path component, and undefined authority, query, and fragment components
     * @throws NullPointerException
     *             If <tt>uri</tt> is <tt>null</tt>
     * @throws IllegalArgumentException
     *             If the preconditions on the parameter do not hold
     * @see #toURI()
     * @see java.net.URI
     * @since 1.4
     */
    public File(URI uri)
    {
        super(uri);
    }
    
    /**
     * Saves an object into a plain text file encoded using US-ASCII. The object is converted into a String by use of toString();
     * 
     * @param text
     *            The text to save to this File object.
     * @return True if successful, False if not
     */
    public boolean saveTextToFile(Object text)
    {
        return saveTextToFile(text, "US-ASCII");
    }
    
    /**
     * Saves an object into a plain text file encoded using a user specified encoding. The object is converted into a String by use of toString();
     * 
     * @param text
     *            The text to save to this File object.
     * @param encoding
     *            The name of the encoding to use
     * @return True if successful, False if not
     */
    public boolean saveTextToFile(Object text, String encoding)
    {
        OutputStreamWriter p = null;
        boolean result = false;
        getParentFile().mkdirs();
        if (isFile())
        {
            if (!canWrite())
            {
                Log.logError("Unable to write the supplied string to " + getAbsolutePath() + " because the file is not writable.");
                return false;
            }
            delete();
            if (isFile())
            {
                Log.logError("Unable to write the supplied string to " + getAbsolutePath() + " because the file was not erased.");
                return false;
            }
        }
        try
        {
            p = new OutputStreamWriter(new FileOutputStream(this), encoding);
            p.write(StringFunctions.ifNull(text));
            result = true;
        }
        catch (Exception e)
        {
            Log.logException(e);
        }
        finally
        {
            try
            {
                if (p != null)
                {
                    p.flush();
                    p.close();
                }
            }
            catch (Exception e)
            {
            }
        }
        return result;
    }
    
    /**
     * Gets each line of a file and places it into a list. Line breaks are left out.
     * 
     * @return List of strings that represent each line in the file
     */
    public ArrayList<String> readFileLineByLine()
    {
        ArrayList<String> list = new ArrayList<String>();
        try
        {
            // use buffering, reading one line at a time
            // FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(this));
            try
            {
                String line = null; // not declared within while loop
                // readLine is a bit quirky : it returns the content of a line MINUS the newline.
                // it returns null only for the END of the stream.
                // it returns an empty String if two newlines appear in a row.
                while ((line = input.readLine()) != null)
                {
                    list.add(line);
                }
            }
            finally
            {
                input.close();
            }
        }
        catch (IOException ex)
        {
            Log.logException(ex);
        }
        return list;
    }
    
    /**
     * Takes a list of string lines and places them into a file. Each item in the list will be a new line in the file.
     * 
     * @param lines
     *            Lines to place into the file
     * @return True if the lines were successfully written to the file, false if not
     */
    public boolean saveLinesToFile(ArrayList<String> lines)
    {
        try
        {
            // use buffering, writing one line at a time
            // FileWriter always assumes default encoding is OK!
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(this)));
            try
            {
                for (String s : lines)
                {
                    output.println(s);
                }
            }
            finally
            {
                output.close();
            }
        }
        catch (IOException ex)
        {
            Log.logException(ex);
            return false;
        }
        return true;
    }
}
