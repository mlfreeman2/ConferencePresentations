package org.mlfreeman.innovate2013.console;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Formatter;

/**
 * Virtual console layer that can automatically fall back to System.in/System.out if System.console returns null.<br>
 * System.console() returns null on Eclipse, but java.io.Console is a nice API.<br>
 */
public abstract class VirtualConsole
{
    private static VirtualConsole SystemIO      = new CharacterDevice(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out, true));
    private static VirtualConsole SystemConsole = new ConsoleDevice(System.console());
    private static VirtualConsole DEFAULT       = (System.console() == null) ? SystemIO : SystemConsole;
    
    /**
     * The default system text I/O device.
     * 
     * @return the default device
     */
    public static VirtualConsole getConsole()
    {
        return DEFAULT;
    }
    
    /**
     * Writes a formatted string to this console's output stream using the specified format string and arguments.
     * 
     * @param fmt
     *            A format string as described in {@link Formatter}
     * @param args
     *            Arguments referenced by the format specifiers in the format string.<br />
     *            If there are more arguments than format specifiers, the extra arguments are ignored.<br />
     *            The number of arguments is variable and may be zero.<br />
     *            The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the <a href="http://java.sun.com/docs/books/vmspec/">Java Virtual Machine Specification</a>.<br />
     *            The behavior on a <tt>null</tt> argument depends on the <a href="../util/Formatter.html#syntax">conversion</a>.
     * @throws IllegalFormatException
     *             If a format string contains an illegal syntax,<br />
     *             a format specifier that is incompatible with the given arguments,<br />
     *             insufficient arguments given the format string,<br />
     *             or other illegal conditions.<br />
     *             For specification of all possible formatting errors, see {@link Formatter}.
     * @return This console
     */
    public VirtualConsole format(String fmt, Object...args)
    {
        return printf(fmt, args);
    }
    
    /**
     * Flushes the console and forces any buffered output to be written immediately .
     */
    public abstract void flush();
    
    /**
     * A convenience method to write a formatted string to this console's output stream using the specified format string and arguments.
     * <p>
     * An invocation of this method of the form <tt>con.printf(format, args)</tt> behaves in exactly the same way as the invocation of
     * 
     * <pre>
     * con.format(format, args)
     * </pre>.
     * 
     * @param fmt
     *            A format string as described in {@link Formatter}
     * @param args
     *            Arguments referenced by the format specifiers in the format <br />
     *            string. If there are more arguments than format specifiers, the <br />
     *            extra arguments are ignored. The number of arguments is <br />
     *            variable and may be zero. The maximum number of arguments is <br />
     *            limited by the maximum dimension of a Java array as defined by <br />
     *            the <a href="http://java.sun.com/docs/books/vmspec/">Java <br />
     *            Virtual Machine Specification</a>. The behaviour on a <br />
     *            <tt>null</tt> argument depends on the <a href="../util/Formatter.html#syntax">conversion</a>.
     * @throws IllegalFormatException
     *             If a format string contains an illegal syntax, a format <br />
     *             specifier that is incompatible with the given arguments, <br />
     *             insufficient arguments given the format string, or other <br />
     *             illegal conditions. For specification of all possible <br />
     *             formatting errors, see {@link Formatter}..
     * @return This console
     */
    public abstract VirtualConsole printf(String fmt, Object...args);
    
    /**
     * Reads a single line of text from the console.
     * 
     * @throws IOError
     *             If an I/O error occurs.
     * @return A string containing the line read from the console, not including any line-termination characters, or <tt>null</tt> if an end of stream has been reached.
     */
    public abstract String readLine();
    
    /**
     * Provides a formatted prompt, then reads a single line of text from the console.
     * 
     * @param fmt
     *            A format string as described in {@link Formatter}
     * @param args
     *            Arguments referenced by the format specifiers in the format string.<br />
     *            If there are more arguments than format specifiers, the extra arguments are ignored.<br />
     *            The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the <a href="http://java.sun.com/docs/books/vmspec/">Java Virtual Machine Specification</a>.
     * @throws IllegalFormatException
     *             If a format string contains an illegal syntax,<br />
     *             a format specifier that is incompatible with the given arguments, <br />
     *             insufficient arguments given the format string, or other illegal conditions.<br />
     *             For specification of all possible formatting errors, see {@link Formatter}
     * @throws IOError
     *             If an I/O error occurs.
     * @return A string containing the line read from the console,<br />
     *         not including any line-termination characters, or <tt>null</tt> if an end of stream has been reached.
     */
    public abstract String readLine(String fmt, Object...args);
    
    /**
     * Reads a password or passphrase from the console with echoing disabled, unless this has to fall back on something other than java.io.Console
     * 
     * @throws IOError
     *             If an I/O error occurs.
     * @return A character array containing the password or passphrase read<br />
     *         from the console, not including any line-termination characters,<br />
     *         or <tt>null</tt> if an end of stream has been reached.
     */
    public abstract char[] readPassword();
    
    /**
     * Provides a formatted prompt, then reads a password or passphrase from the console with echoing disabled, unless this has to fall back on something other than java.io.Console
     * 
     * @param fmt
     *            A format string as described in {@link Formatter}
     * @param args
     *            Arguments referenced by the format specifiers in the format string.<br />
     *            If there are more arguments than format specifiers, the extra arguments are ignored.<br />
     *            The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the <a href="http://java.sun.com/docs/books/vmspec/">Java Virtual Machine Specification</a>.
     * @throws IllegalFormatException
     *             If a format string contains an illegal syntax,<br />
     *             a format specifier that is incompatible with the given arguments,<br />
     *             insufficient arguments given the format string, or other illegal conditions.<br />
     *             For specification of all possible formatting errors, see {@link Formatter}
     * @throws IOError
     *             If an I/O error occurs.
     * @return A character array containing the password or passphrase read from the console,<br />
     *         not including any line-termination characters, or <tt>null</tt> if an end of stream has been reached.
     */
    public abstract char[] readPassword(String fmt, Object...args);
    
    /**
     * Retrieves the unique {@link java.io.Reader Reader} object associated with this console.
     * <p>
     * This method is intended to be used by sophisticated applications, for<br />
     * example, a {@link java.util.Scanner} object which utilizes the rich<br />
     * parsing/scanning functionality provided by the <tt>Scanner</tt>: <blockquote>
     * 
     * <pre>
     * Console con = System.console();
     * if (con != null) {
     *     Scanner sc = new Scanner(con.reader());
     *     ...
     * }
     * </pre>
     * 
     * </blockquote>
     * <p>
     * For simple applications requiring only line-oriented reading, use<br />
     * <tt>{@link #readLine}</tt>.
     * <p>
     * The bulk read operations {@link java.io.Reader#read(char[]) read(char[]) },<br />
     * {@link java.io.Reader#read(char[], int, int) read(char[], int, int) } and<br />
     * {@link java.io.Reader#read(java.nio.CharBuffer) read(java.nio.CharBuffer)}<br />
     * on the returned object will not read in characters beyond the line<br />
     * bound for each invocation, even if the destination buffer has space for<br />
     * more characters. A line bound is considered to be any one of a line feed<br />
     * (<tt>'\n'</tt>), a carriage return (<tt>'\r'</tt>), a carriage return<br />
     * followed immediately by a linefeed, or an end of stream.<br />
     * 
     * @return The reader associated with this console
     */
    public abstract Reader reader();
    
    /**
     * Retrieves the unique {@link java.io.PrintWriter PrintWriter} object associated with this console.
     * 
     * @return The printwriter associated with this console
     */
    public abstract PrintWriter writer();
}

/**
 * {@link VirtualConsole} implementation wrapping character streams.
 */
class CharacterDevice extends VirtualConsole
{
    private final BufferedReader reader;
    private final PrintWriter    writer;
    
    public CharacterDevice(BufferedReader reader, PrintWriter writer)
    {
        this.reader = reader;
        this.writer = writer;
    }
    
    @Override
    public CharacterDevice printf(String fmt, Object...params)
    {
        writer.printf(fmt, params);
        return this;
    }
    
    @Override
    public String readLine()
    {
        try
        {
            return reader.readLine();
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
    }
    
    @Override
    public String readLine(String fmt, Object...params)
    {
        writer.printf(fmt, params);
        return readLine();
    }
    
    @Override
    public char[] readPassword()
    {
        writer.printf("%nWARNING: Passwords are not masked.%n");
        return readLine().toCharArray();
    }
    
    @Override
    public char[] readPassword(String fmt, Object...params)
    {
        writer.printf("%nWARNING: Passwords are not masked.%n");
        writer.printf(fmt, params);
        return readLine().toCharArray();
    }
    
    @Override
    public Reader reader()
    {
        return reader;
    }
    
    @Override
    public PrintWriter writer()
    {
        return writer;
    }
    
    @Override
    public void flush()
    {
        writer.flush();
    }
}

/**
 * {@link VirtualConsole} implementation wrapping a {@link Console}.
 */
class ConsoleDevice extends VirtualConsole
{
    private final Console console;
    
    public ConsoleDevice(Console console)
    {
        this.console = console;
    }
    
    @Override
    public VirtualConsole printf(String fmt, Object...params)
    {
        console.format(fmt, params);
        return this;
    }
    
    @Override
    public Reader reader()
    {
        return console.reader();
    }
    
    @Override
    public String readLine()
    {
        return console.readLine();
    }
    
    @Override
    public String readLine(String fmt, Object...params)
    {
        return console.readLine(fmt, params);
    }
    
    @Override
    public char[] readPassword()
    {
        return console.readPassword();
    }
    
    @Override
    public char[] readPassword(String fmt, Object...params)
    {
        return console.readPassword(fmt, params);
    }
    
    @Override
    public PrintWriter writer()
    {
        return console.writer();
    }
    
    @Override
    public void flush()
    {
        console.flush();
    }
}