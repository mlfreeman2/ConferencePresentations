package com.lps.rsdc2010.common.lang;

import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * This class contains commonly-used String manipulation functions.
 */
public class StringFunctions extends StringUtils
{
    /**
     * If the given object's toString() form equals a common way of specifying true in other languages / realms then return true.<br>
     * Right now, only strings equalling "1", case insensitive "true", case insensitive "t", or case insensitive "Y" return true.
     * 
     * @param s
     *            The string
     * @return True if the string is a "true" value, false if it is something else.
     */
    public static boolean isTrue(Object o)
    {
        String objString = StringFunctions.ifNull(o);
        if (objString.equals("1") || objString.equalsIgnoreCase("true") || objString.equalsIgnoreCase("t") || objString.equalsIgnoreCase("Y"))
        {
            return true;
        }
        return false;
    }
    
    /**
     * Catches null Strings and replaces them with the empty string.
     * 
     * @param value
     *            Object or string that may be null
     * @return The empty string if <code>value == null</code> or <code>value.toString() == null</code>, or <code>value.toString()</code> otherwise.
     */
    public static String ifNull(Object value)
    {
        return StringFunctions.ifNull2(value, "");
    }
    
    /**
     * Catches null Strings and replaces them with a custom substitution.
     * 
     * @param value
     *            Object or string that may be null
     * @param substitution
     *            String to substitute in if value is null or <code>value.toString()</code> returns null;
     * @return The supplied string if <code>value == null</code> or <code>value.toString() == null</code>, or <code>value.toString()</code> otherwise.
     */
    public static String ifNull2(Object value, String substitution)
    {
        try
        {
            if (value == null || value.toString() == null)
            {
                return substitution;
            }
            return value.toString();
        }
        catch (NullPointerException e)
        {
            return substitution;
        }
    }
    
    /**
     * Catches null or empty strings and replaces them with a custom substitution
     * 
     * @param value
     *            Object or string that may be null or an empty string
     * @param substitution
     *            String to substitute in if value is null or an empty string or <code>value.toString()</code> returns null or the empty string;
     * @return The supplied string if <code>value == null</code> or <code>value.toString() == null</code> or <code>value.toString().equals("") == true</code>, or <code>value.toString()</code> otherwise.
     */
    public static String ifEmpty(Object value, String substitution)
    {
        if (StringFunctions.ifNull(value).length() == 0)
        {
            return substitution;
        }
        return value.toString();
    }
    
    /**
     * @param original
     *            String with HTML non-blank space (nbsp) characters in it.
     * @return String with regular spaces instead of HTML non-blank space characters in it.
     */
    public static String removeHTMLnbspChars(String original)
    {
        char HTMLnbsp = ' ' + 128;
        return original.replace(HTMLnbsp, ' ');
    }
    
    /**
     * @param original
     *            String with HTML mdash characters in it.
     * @return String with regular dashes instead of HTML mdash characters in it.
     */
    public static String removeHTMLmdashChars(String original)
    {
        char HTMLmdash = (char) 8212;
        return original.replace(HTMLmdash, '-');
    }
    
    /**
     * Concatenates an array of strings, and replaces null with "".<br>
     * 
     * @param prefix
     *            First string.
     * @return arg[0] + arg[1] + ... + arg[n], which could be a 0-length string if all entries in array were null.
     */
    public static String concat(Object...args)
    {
        String result = new String();
        for (int i = 0; i < args.length; i++)
        {
            result += StringFunctions.ifNull(args[i]);
        }
        return result;
    }
    
    /**
     * Concatenates 2 strings, and replaces null with "".<br>
     * If either one of them after the null replacement is the empty string, it returns just the other.
     * 
     * @param prefix
     *            First string.
     * @param suffix
     *            Second string.
     * @return prefix + suffix, which could be a 0-length string if both were null.
     */
    public static String concat(String prefix, String suffix)
    {
        return StringFunctions.concat(prefix, "", suffix);
    }
    
    /**
     * Concatenates 2 strings using the given separator, and replaces null with "".<br>
     * If either one of them after the null replacement is the empty string, it returns just the other.
     * 
     * @param prefix
     *            First string.
     * @param separator
     *            Separator of the strings.
     * @param suffix
     *            Second string.
     * @return prefix + separator + suffix, which could be a 0-length string if all 3 were null.
     */
    public static String concat(String prefix, String separator, String suffix)
    {
        String concatString = "";
        prefix = StringFunctions.ifNull(prefix);
        suffix = StringFunctions.ifNull(suffix);
        if (prefix.equals("") && suffix.equals(""))
        {
            
        }
        else if (prefix.equals(""))
        {
            concatString = suffix;
        }
        else if (suffix.equals(""))
        {
            concatString = prefix;
        }
        else
        {
            concatString = prefix + separator + suffix;
        }
        return concatString;
    }
    
    /**
     * Concatenates a series of strings together with a custom separator string.
     * 
     * @param separator
     *            The string to put between each element
     * @param elements
     *            The strings to join
     * @return element 1 + separator + element 2 + separator + ... + separator + element n
     */
    public static String join(String separator, List<?> elements)
    {
        String[] s = new String[elements.size()];
        for (int i = 0; i < elements.size(); i++)
        {
            s[i] = StringFunctions.ifNull(elements.get(i));
        }
        return StringFunctions.join(separator, s);
    }
    
    /**
     * Concatenates a series of strings together with a custom separator string.
     * 
     * @param separator
     *            The string to put between each element
     * @param elements
     *            The strings to join
     * @return element 1 + separator + element 2 + separator + ... + separator + element n
     */
    public static String join(String separator, String...elements)
    {
        separator = StringFunctions.ifNull(separator);
        if (elements.length == 0)
        {
            return "";
        }
        else
        {
            String concat = StringFunctions.ifNull(elements[0]);
            if (elements.length > 1)
            {
                for (int i = 1; i < elements.length; i++)
                {
                    concat += separator + StringFunctions.ifNull(elements[i]);
                }
            }
            return concat;
        }
    }
    
    /**
     * Recursively pretty-toString()s an object and all objects contained in it.<br>
     * This is similar to Perl's Data::Dumper.
     * 
     * @param o
     *            The starting object
     * @param spaceDepth
     *            The number of spaces to pad everything with. Typically this should be 0
     * @return A string dump of the objects, similar in format to Perl's Data::Dumper
     */
    public static String objectDumper(Object o, int spaceDepth)
    {
        return StringFunctions.objectDumper(o, spaceDepth, " ", "\r\n");
    }
    
    /**
     * Recursively pretty-toString()s an object and all objects contained in it.<br>
     * This is supposed to be compatible with Perl's Data::Dumper, for simple things like strings and integers.
     * 
     * @param o
     *            The starting object
     * @param spaceDepth
     *            The number of spaces to pad everything with. Typically this should be 0
     * @return A string dump of the objects, similar in format to Perl's Data::Dumper
     */
    public static String PerlCompatibleObjectDumper(Object o)
    {
        String start = "$VAR1 = ";
        return start + StringFunctions.objectDumper(o, 7, " ", "\r\n") + ";";
    }
    
    /**
     * Recursively pretty-toString()s an object and all objects contained in it.<br>
     * This is similar to Perl's Data::Dumper.
     * 
     * @param o
     *            The starting object
     * @param spaceDepth
     *            The number of spaces to pad everything with. Typically this should be 0
     * @return A string dump of the objects, similar in format to Perl's Data::Dumper
     */
    public static String HTMLObjectDumper(Object o, int spaceDepth)
    {
        return StringFunctions.objectDumper(o, spaceDepth, "&nbsp;", "<br>");
    }
    
    /**
     * Recursively pretty-toString()s an object and all objects contained in it.<br>
     * This is similar to Perl's Data::Dumper.
     * 
     * @param o
     *            The starting object
     * @param spaceDepth
     *            The number of spaces to pad everything with. Typically this should be 0
     * @param space
     *            The String to use as the space separator (" " for text and "&nbsp;" for HTML)
     * @param lineBreak
     *            The String to use as the newline separator ("\r\n" for text and "&lt;br&gt;" for HTML)
     * @return A string dump of the objects, similar in format to Perl's Data::Dumper
     */
    public static String objectDumper(Object o, int spaceDepth, String space, String lineBreak)
    {
        String paddingString = "";
        StringBuffer resultString = new StringBuffer();
        for (int i = 0; i < spaceDepth; i++)
        {
            paddingString += space;
        }
        if (o == null)
        {
            resultString.append("'null'," + lineBreak);
        }
        else if (o instanceof Map<?, ?>)
        {
            Map<?, ?> results = (Map<?, ?>) o;
            StringBuffer mapAsString = new StringBuffer();
            Set<?> mapSet = results.keySet();
            mapAsString.append("{" + lineBreak);
            Iterator<?> mapIterator = mapSet.iterator();
            while (mapIterator.hasNext())
            {
                Object key = mapIterator.next();
                int nextSpaceDepth = (space + "'" + key.toString() + "'" + space + "=>" + space).length();
                mapAsString.append(space + "'" + key.toString() + "'" + space + "=>" + space + StringFunctions.objectDumper(results.get(key), nextSpaceDepth, space, lineBreak));
            }
            mapAsString.append("},");
            resultString.append(mapAsString.toString().replace(lineBreak, lineBreak + paddingString) + lineBreak);
        }
        else if (o instanceof Dictionary<?, ?>)
        {
            Dictionary<?, ?> results = (Dictionary<?, ?>) o;
            Enumeration<?> keys = results.keys();
            StringBuffer dictionaryAsString = new StringBuffer();
            dictionaryAsString.append("{" + lineBreak);
            while (keys.hasMoreElements())
            {
                Object key = keys.nextElement();
                int nextSpaceDepth = (space + "'" + key.toString() + "'" + space + "=>" + space).length();
                dictionaryAsString.append(space + "'" + key.toString() + space + "=>" + space + StringFunctions.objectDumper(results.get(key), nextSpaceDepth, space, lineBreak));
            }
            dictionaryAsString.append("},");
            resultString.append(dictionaryAsString.toString().replace(lineBreak, lineBreak + paddingString) + lineBreak);
        }
        else if (o instanceof Collection<?>)
        {
            StringBuffer collectionAsString = new StringBuffer();
            collectionAsString.append("[" + lineBreak);
            Collection<?> c = (Collection<?>) o;
            Iterator<?> it = c.iterator();
            while (it.hasNext())
            {
                collectionAsString.append(space + StringFunctions.objectDumper(it.next(), 1, space, lineBreak));
            }
            collectionAsString.append("]," + lineBreak);
            resultString.append(collectionAsString.toString().replace(lineBreak, lineBreak + paddingString) + lineBreak);
        }
        else if (o instanceof Enumeration<?>)
        {
            StringBuffer enumerationAsString = new StringBuffer();
            enumerationAsString.append("[" + lineBreak);
            Enumeration<?> e = (Enumeration<?>) o;
            while (e.hasMoreElements())
            {
                enumerationAsString.append(space + StringFunctions.objectDumper(e.nextElement(), 1, space, lineBreak));
            }
            enumerationAsString.append("]," + lineBreak);
            resultString.append(enumerationAsString.toString().replace(lineBreak, lineBreak + paddingString) + lineBreak);
        }
        else if (o.getClass().isArray())
        {
            StringBuffer arrayAsString = new StringBuffer();
            arrayAsString.append("[" + lineBreak);
            for (int i = 0; i < Array.getLength(o); i++)
            {
                arrayAsString.append(space + StringFunctions.objectDumper(Array.get(o, i), 1, space, lineBreak));
            }
            arrayAsString.append("]," + lineBreak);
            resultString.append(arrayAsString.toString().replace(lineBreak, lineBreak + paddingString) + lineBreak);
        }
        else if (o instanceof Calendar)
        {
            resultString.append("'" + DateTime.formatCalendar((Calendar) o, "hh:mm:ss aa zzzz 'on' EEEE, MMMM dd, yyyy") + "'," + lineBreak);
        }
        else if (o instanceof Date)
        {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime((Date) o);
            resultString.append("'" + DateTime.formatCalendar(gc, "hh:mm:ss aa zzzz 'on' EEEE, MMMM dd, yyyy") + "'," + lineBreak);
        }
        else if (o instanceof StackTraceElement)
        {
            StackTraceElement s = (StackTraceElement) o;
            resultString.append("'" + s.getFileName() + "-" + s.getLineNumber() + ": " + s.getClassName() + "$" + s.getMethodName() + "'," + lineBreak);
        }
        else if (o instanceof java.awt.Rectangle)
        {
            Rectangle r = (Rectangle) o;
            resultString.append("'[Upper Left Corner: (" + r.x + "," + r.y + "), Width: " + r.width + ", Height: " + r.height + "]'," + lineBreak);
        }
        else if (o.getClass().getCanonicalName().startsWith("com.rational.test.ft.object.interfaces"))
        {
            try
            {
                Object result = Reflection.executeMethod(o, "com.rational.test.ft.object.interfaces.TestObject", "getProperties", (Object[]) null);
                resultString.append(StringFunctions.objectDumper(result, 1, space, lineBreak));
            }
            catch (InvocationTargetException e)
            {
                resultString.append(e.getCause().getClass().getSimpleName() + " -- " + StringFunctions.ifNull(e.getCause().getMessage()));
            }
            catch (Exception e)
            {
                resultString.append(e.getClass().getSimpleName() + " -- " + StringFunctions.ifNull(e.getMessage()));
            }
        }
        else if (o.getClass().getCanonicalName().equals("com.rational.test.ft.sys.SpyMap"))
        {
            LinkedHashMap<Object, Object> converted = new LinkedHashMap<Object, Object>();
            try
            {
                Object result = Reflection.executeMethod(o, o.getClass().getCanonicalName(), "keys", (Object[]) null);
                for (int i = 0; i < Array.getLength(result); i++)
                {
                    Object key = Array.get(result, i);
                    Object value = Reflection.executeMethod(o, o.getClass().getCanonicalName(), "get", key.toString());
                    converted.put(key, value);
                }
                resultString.append(StringFunctions.objectDumper(converted, 1, space, lineBreak));
            }
            catch (InvocationTargetException e)
            {
                resultString.append(e.getCause().getClass().getSimpleName() + " -- " + StringFunctions.ifNull(e.getCause().getMessage()));
            }
            catch (Exception e)
            {
                resultString.append(e.getClass().getSimpleName() + " -- " + StringFunctions.ifNull(e.getMessage()));
            }
        }
        else if (o.getClass().getCanonicalName().equals("com.rational.test.ft.sys.SpyVector"))
        {
            try
            {
                Object result = Reflection.executeMethod(o, o.getClass().getCanonicalName(), "toJavaVector", (Object[]) null);
                resultString.append(StringFunctions.objectDumper(result, 1, space, lineBreak));
            }
            catch (InvocationTargetException e)
            {
                resultString.append(e.getCause().getClass().getSimpleName() + " -- " + StringFunctions.ifNull(e.getCause().getMessage()));
            }
            catch (Exception e)
            {
                resultString.append(e.getClass().getSimpleName() + " -- " + StringFunctions.ifNull(e.getMessage()));
            }
        }
        else
        {
            resultString.append("'" + o.toString() + "'," + lineBreak);
        }
        return resultString.toString();
    }
    
    /**
     * Find the Levenstein distance between two Strings. <br>
     * This is the number of changes needed to change one String into another, where each change is a single character modification (deletion, insertion or substitution).<br>
     * The previous implementation of the Levenshtein distance algorithm was from http://www.merriampark.com/ld.htm<br>
     * Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError which can occur when my Java implementation is used with very large strings.<br>
     * This implementation of the Levenshtein distance algorithm is from http://www.merriampark.com/ldjava.htm<br>
     * <br>
     * StringUtils.getLevenshteinDistance(null, *) = -1<br>
     * StringUtils.getLevenshteinDistance(*, null) = -1<br>
     * StringUtils.getLevenshteinDistance("","") = 0<br>
     * StringUtils.getLevenshteinDistance("Harry","harry") = 1<br>
     * StringUtils.getLevenshteinDistance("","a") = 1<br>
     * StringUtils.getLevenshteinDistance("aaapppp", "") = 7<br>
     * StringUtils.getLevenshteinDistance("frog", "fog") = 1<br>
     * StringUtils.getLevenshteinDistance("fly", "ant") = 3<br>
     * StringUtils.getLevenshteinDistance("elephant", "hippo") = 7<br>
     * StringUtils.getLevenshteinDistance("hippo", "elephant") = 7<br>
     * StringUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8<br>
     * StringUtils.getLevenshteinDistance("hello", "hallo") = 1<br>
     * 
     * @param source
     *            the first String, if null -1 returned
     * @param target
     *            the second String, if null -1 returned
     * @return result distance
     */
    public static int getLevenshteinDistance(String source, String target)
    {
        try
        {
            return StringUtils.getLevenshteinDistance(source, target);
        }
        catch (Exception e)
        {
            return -1;
        }
    }
    
    /**
     * Gets the index of the item in the string array with the shortest Levenshtein distance.<br>
     * The greater the Levenstein distance, the more different the strings are.
     * 
     * @param source
     *            source string that all targets must transform into
     * @param targets
     *            an array of strings that should be compared to the source string
     * @return index of the item in the array that has the shortest Levenshtein distance (best match)
     */
    public static int getIndexOfBestMatch(String source, String...targets)
    {
        return StringFunctions.getIndexOfBestMatch(source, 99999, targets);
    }
    
    /**
     * Gets the index of the item in the string array with the shortest Levenshtein distance aka the best match.<br>
     * The greater the Levenstein distance, the more different the strings are.
     * 
     * @param source
     *            source string that all targets must transform into
     * @param threshold
     *            sets the maximum acceptable distance or differences allowed
     * @param targets
     *            an array of strings that should be compared to the source string
     * @return index of the item in the array that has the shortest Levenshtein distance (best match).<br>
     *         If no items in the array have a smaller Levenshtein distance than the threshold, -1 is returned.
     */
    public static int getIndexOfBestMatch(String source, int threshold, String...targets)
    {
        int distanceOfBestMatch = threshold;
        int indexOfBestMatch = -1;
        for (int i = 0; i < targets.length; i++)
        {
            String currentTarget = targets[i];
            int currentTargetDistance = StringFunctions.getLevenshteinDistance(source, currentTarget);
            if (currentTargetDistance <= distanceOfBestMatch && currentTargetDistance != -1)
            {
                distanceOfBestMatch = currentTargetDistance;
                indexOfBestMatch = i;
            }
        }
        return indexOfBestMatch;
    }
    
    /**
     * Gets the string in the array with the shortest Levenshtein distance.<br>
     * The greater the Levenshtein distance, the more different the strings are.<br>
     * 
     * @param source
     *            source string that all targets must transform into
     * @param targets
     *            an array of strings that should be compared to the source string
     * @return a string of the item in the array that has the shortest Levenshtein distance (best match).<br>
     *         If no items in the array have a smaller Levenshtein distance than 99999, null is returned.
     */
    public static String getBestMatch(String source, String...targets)
    {
        return targets[StringFunctions.getIndexOfBestMatch(source, targets)];
    }
    
    /**
     * Gets the string in the array with a smaller Levenshtein distance than the threshold specified.<br>
     * The greater the Levenshtein distance, the more different the strings are. <br>
     * 
     * @param source
     *            source string that all targets must transform into
     * @param threshold
     *            sets the maximum acceptable distance or differences allowed
     * @param targets
     *            an array of strings that should be compared to the source string
     * @return a string of the item in the array that has the shortest Levenshtein distance (best match).<br>
     *         If no items in the array have a smaller Levenshtein distance than the threshold, null is returned.
     */
    public static String getBestMatch(String source, int threshold, String...targets)
    {
        try
        {
            return targets[StringFunctions.getIndexOfBestMatch(source, threshold, targets)];
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Gets the substring after the first occurrence of a separator.<br>
     * The separator is not returned. <br>
     * <br>
     * StringUtils.substringAfter(null, *) = ""<br>
     * StringUtils.substringAfter("", *) = ""<br>
     * StringUtils.substringAfter(*, null) = ""<br>
     * StringUtils.substringAfter("abc", "a") = "bc"<br>
     * StringUtils.substringAfter("abcba", "b") = "cba"<br>
     * StringUtils.substringAfter("abc", "c") = ""<br>
     * StringUtils.substringAfter("abc", "d") = ""<br>
     * StringUtils.substringAfter("abc", "") = "abc"<br>
     * 
     * @param str
     *            the String to get a substring from, may be null (routes through ifNull)
     * @param separator
     *            the String to search for, may be null
     * @return the substring after the first occurrence of the separator<br>
     *         If nothing is found, the empty string is returned.<br>
     */
    public static String substringAfter(String str, String separator)
    {
        return StringFunctions.ifNull(StringUtils.substringAfter(str, separator));
    }
    
    /**
     * Gets the substring before the first occurrence of a separator.<br>
     * The separator is not returned. A null string input will return null.<br>
     * An empty ("") string input will return the empty string.<br>
     * A null separator will return the input string.<br>
     * <br>
     * StringUtils.substringBefore(null, *) = null<br>
     * StringUtils.substringBefore("", *) = ""<br>
     * StringUtils.substringBefore("abc", "a") = ""<br>
     * StringUtils.substringBefore("abcba", "b") = "a"<br>
     * StringUtils.substringBefore("abc", "c") = "ab"<br>
     * StringUtils.substringBefore("abc", "d") = "abc"<br>
     * StringUtils.substringBefore("abc", "") = ""<br>
     * 
     * @param str
     *            the String to get a substring from, may be null (routes through ifNull)
     * @param separator
     *            the String to search for, may be null
     * @return the substring before the first occurrence of the separator.<br>
     *         If nothing is found, the empty string is returned.<br>
     */
    public static String substringBefore(String str, String separator)
    {
        return StringFunctions.ifNull(StringUtils.substringBefore(str, separator));
    }
    
    /**
     * This function goes through a given string and makes sure the numbers in it have a minimum number of digits before and after the decimal point.
     * 
     * @param number
     *            The string with numbers in it.
     * @param minDigitsBeforeDecimal
     *            The minimum number of digits to have before the decimal point
     * @param minDigitsAfterDecimal
     *            The minimum number of digits to have after the decimal point
     * @return The revised string
     */
    public static String padNumbersInString(String number, int minDigitsBeforeDecimal, int minDigitsAfterDecimal)
    {
        Matcher m = Pattern.compile("(\\d+\\.?+\\d*+)").matcher(number);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(minDigitsBeforeDecimal);
        nf.setMinimumFractionDigits(minDigitsAfterDecimal);
        StringBuffer sb = new StringBuffer();
        while (m.find())
        {
            String foundNumber = m.group(1);
            m.appendReplacement(sb, nf.format(Double.parseDouble(foundNumber)));
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * This function goes through a group of strings and returns the length of the shortest one.<br>
     * Useful if you're trying to iterate through a group of them for comparisons.
     * 
     * @param items
     *            The strings to measure
     * @return The length of the shortest one. Loop through it using 0 to length-1.
     */
    public static int getLengthOfShortestString(String...items)
    {
        int length = Integer.MAX_VALUE;
        for (String o : items)
        {
            if (o.length() < length)
            {
                length = o.length();
            }
        }
        return length;
    }
    
    /**
     * This function goes through a group of strings and returns the length of the longest one.<br>
     * 
     * @param items
     *            The strings to measure
     * @return The length of the longest one. Loop through it using 0 to length-1.
     */
    public static int getLengthOfLongestString(String...items)
    {
        int length = -1;
        for (String o : items)
        {
            if (o.length() > length)
            {
                length = o.length();
            }
        }
        return length;
    }
}
