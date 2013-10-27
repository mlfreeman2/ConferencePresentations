package com.advancedrft.common.lang;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regular Expression helper functions
 */
public class Regex
{
    /**
     * A generic regex to match email addresses
     */
    public static final String EmailAddress = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9._-]+)";
    
    /**
     * A generic regex to match both integer and floating point numbers (positive ones only)
     */
    public static final String Numbers      = "(\\d+\\.?+\\d*+)";
    
    /**
     * This function turns a string into something that will be treated as a literal word pattern by Java regexes,
     * 
     * @param regex
     *            The regex string (e.g. "Your search returned 23 items (showing 1-10).")
     * @return A String "\QYour search returned 23 items (showing 1-10).\E".
     */
    public static String escapeRegex(String regex)
    {
        return Pattern.quote(regex);
    }
    
    /**
     * This function returns a hashtable of predefined regular expressions.<br>
     * The regex's name is the key and the actual pattern is the value.<br>
     * If the regex pattern begins with "(?i)" then it is supposed to be a case insensitive regex.<br>
     * You will need to replace "====PLACEHOLDER====" with your string to use these.<br>
     * Currently the following predefined regexes are offered: <br>
     * <ul>
     * <li><b>equals</b> -- the text must equal originalText</li>
     * <li><b>contains</b> -- the text must contain originalText</li>
     * <li><b>startsWith</b> -- the text must start with originalText</li>
     * <li><b>endsWith</b> -- the text must end with originalText</li>
     * </ul>
     * You can add functionality by appending one of the following to the regex name:<br>
     * <ul>
     * <li><b>IgnoreCase</b> -- Make the regex case insensitive</li>
     * <li><b>Literal</b> -- Escape out the text you supply (used if your string contains regex special chars but you want them ignored)</li>
     * <li><b>IgnoreCaseLiteral</b> -- Make the regex case insensitive and escape out the text you supply (used if your string contains regex special chars but you want them ignored)</li>
     * </ul>
     * 
     * @return Hashtable of predefined regular expressions
     */
    public static Hashtable<String, String> getRegexPatterns()
    {
        Hashtable<String, String> patterns = new Hashtable<String, String>();
        // Equals Regexes
        patterns.put("equals", "^====PLACEHOLDER====$");
        patterns.put("equalsIgnoreCase", "(?i)^====PLACEHOLDER====$");
        patterns.put("equalsLiteral", "^\\Q====PLACEHOLDER====\\E$");
        patterns.put("equalsIgnoreCaseLiteral", "(?i)^\\Q====PLACEHOLDER====\\E$");
        // Contains Regexes
        patterns.put("contains", ".*====PLACEHOLDER====.*");
        patterns.put("doesNotContain", "^(?!.*====PLACEHOLDER====).*$");
        patterns.put("containsIgnoreCase", "(?i).*====PLACEHOLDER====.*");
        patterns.put("containsLiteral", ".*\\Q====PLACEHOLDER====\\E.*");
        patterns.put("containsIgnoreCaseLiteral", "(?i).*\\Q====PLACEHOLDER====\\E.*");
        // Starts With Regexes
        patterns.put("startsWith", "^====PLACEHOLDER====.*");
        patterns.put("startsWithIgnoreCase", "(?i)^====PLACEHOLDER====.*");
        patterns.put("startsWithLiteral", "^\\Q====PLACEHOLDER====\\E.*");
        patterns.put("startsWithIgnoreCaseLiteral", "(?i)^\\Q====PLACEHOLDER====\\E.*");
        // Ends With Regexes
        patterns.put("endsWith", ".*====PLACEHOLDER====$");
        patterns.put("endsWithIgnoreCase", "(?i).*====PLACEHOLDER====$");
        patterns.put("endsWithLiteral", ".*\\Q====PLACEHOLDER====\\E$");
        patterns.put("endsWithIgnoreCaseLiteral", "(?i).*\\Q====PLACEHOLDER====\\E$");
        // Default
        patterns.put("regex", "====PLACEHOLDER====");
        return patterns;
    }
    
    /**
     * Tests to see if a string is either a stringToPattern regex match or an exact equals() match with another string.
     * 
     * @param original
     *            The string to compare
     * @param regex
     *            The regex (or literal string) to compare against.
     * @return True if regex is a stringToPattern regex that matches original, or if original.equals(regex) is true. False otherwise.
     */
    public static boolean isMatch2(String original, String regex)
    {
        if (Regex.isStringToPattern(regex))
        {
            if (!Regex.isMatch(original, regex))
            {
                return false;
            }
        }
        else
        {
            if (!original.equals(regex))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * This function determines if a string matches a regex.
     * 
     * @param originalString
     *            The candidate string
     * @param regex
     *            The regex to match against. This gets routed through {@link #stringToMatcher(String, String)}, so see {@link #getRegexPatterns()} for a list of supported predefined patterns.
     * @return True if it matches, False if it doesn't.
     */
    public static boolean isMatch(String originalString, String regex)
    {
        return Regex.stringToMatcher(regex, originalString).find();
    }
    
    /**
     * Tests for one of the following:
     * <ol>
     * <li>One string matching at least one regex out of a supplied set</li>
     * <li>One string matching all regexes in a supplied set</li>
     * <li>A set of strings in which all matching a supplied regex</li>
     * <li>A set of strings in which at least one matches a supplied regex</li>
     * </ol>
     * 
     * @param arg1
     *            The one string to use either as a regex or a candidate
     * @param allEntriesMustMatch
     *            True if the candidate(s) must match all supplied regexes
     * @param treatArg1AsRegex
     *            True if arg1 is the regex and argN are the candidate strings, False for the opposite
     * @param argN
     *            A collection of values to use either as regexes or candidates
     * @return True if the appropriate scenario (one of the four above) comes out to be true, False if not.
     */
    public static boolean isMatch(String arg1, boolean allEntriesMustMatch, boolean treatArg1AsRegex, String...argN)
    {
        boolean isMatchFound = false;
        for (int i = 0; i < argN.length; i++)
        {
            String regex = new String();
            String originalString = new String();
            if (treatArg1AsRegex == true)
            {
                regex = arg1;
                originalString = argN[i];
            }
            else
            {
                originalString = arg1;
                regex = argN[i];
            }
            if (Regex.isMatch(originalString, regex) == false)
            {
                if (allEntriesMustMatch == true)
                {
                    return false;
                }
            }
            else
            {
                isMatchFound = true;
                if (allEntriesMustMatch == false)
                {
                    return true;
                }
            }
        }
        return isMatchFound;
    }
    
    /**
     * This function looks at a List of <code>Map&lt;String, String&gt;</code> objects.<br />
     * For each <code>Map&lt;String, String&gt;</code>, it executes <code>Regex.isMatch(StringFunctions.ifNull(map.get(key)), regex)</code>.<br />
     * If allEntriesMustMatch is false and one of these comparisons is true, it immediately returns true.<br />
     * If allEntriesMustMatch is true and one of these comparisons is false, it immediately returns false.<br />
     * Otherwise it compares the given element in all the maps
     * 
     * @param regex
     *            The regex to match against.
     * @param key
     *            The key to grab in each map to compare with
     * @param allEntriesMustMatch
     *            True if <code>Regex.isMatch(StringFunctions.ifNull(map.get(key)), regex)</code> must return true for every map in <code>dbResults</code>, False if only one comparison has to pass
     * @param dbResults
     *            The List of Map&lt;String, String&gt; to examine
     * @return True if either all entries matched or at least one did (depending on allEntriesMustMatch), or False if the wrong condition occurred
     */
    public static boolean isMatch(String regex, String key, boolean allEntriesMustMatch, List<Map<String, String>> dbResults)
    {
        for (ListIterator<Map<String, String>> iterator = dbResults.listIterator(); iterator.hasNext();)
        {
            Map<String, String> row = iterator.next();
            String originalString = StringFunctions.ifNull(row.get(key));
            if (Regex.isMatch(originalString, regex) == false)
            {
                if (allEntriesMustMatch == true)
                {
                    return false;
                }
            }
            else
            {
                if (allEntriesMustMatch == false)
                {
                    return true;
                }
            }
        }
        return true;
    }
    
    /**
     * This function tests to see if a given string would be treated as a regex by stringToPattern()<br>
     * See getRegexPatterns() for the list of supported regexes<br>
     * 
     * @param regex
     *            The string you want to check for a regex
     * @return True if the string is one of our predefined regexes, False if it isn't.
     */
    public static boolean isStringToPattern(String regex)
    {
        regex = StringFunctions.ifNull(regex);
        if (!regex.startsWith("regex="))
        {
            if (regex.indexOf("=") < 0)
            {
                return false;
            }
            return Regex.getRegexPatterns().containsKey(regex.substring(0, regex.indexOf("=")));
        }
        return true;
    }
    
    /**
     * This function runs originalString against regex and returns all (if any) capture groups from the regex
     * 
     * @param string
     *            The main string
     * @param p
     *            The regex, as a compiled pattern
     * @return All the captured groups from the regex
     */
    public static ArrayList<String> matchPattern(String string, Pattern p)
    {
        Matcher m = p.matcher(string);
        ArrayList<String> matcher = new ArrayList<String>();
        while (m.find())
        {
            for (int i = 1; i <= m.groupCount(); i++)
            {
                matcher.add(m.group(i));
            }
        }
        return matcher;
    }
    
    /**
     * This function runs originalString against regex and returns all (if any) capture groups from the regex
     * 
     * @param originalString
     *            The main string
     * @param regex
     *            The regex, as a string (passed through stringToPattern(regex))
     * @return All the captured groups from the regex
     */
    public static ArrayList<String> matchString(String originalString, String regex)
    {
        return Regex.matchPattern(originalString, Regex.stringToPattern(regex));
    }
    
    /**
     * This function returns a Java matcher object for the given regex and candidate string
     * 
     * @param regex
     *            A regex such as "regex=^.*blah.*$". This will be fed through stringToPattern() to be turned into a Pattern object
     * @param candidate
     *            The candidate string to test the regex against. This will be fed into the matcher object.
     * @return A Java Matcher object
     */
    public static Matcher stringToMatcher(String regex, String candidate)
    {
        return Regex.stringToPattern(regex).matcher(candidate);
    }
    
    /**
     * This takes text strings and makes them into common regular expressions.<br>
     * You must specify the regex name at the beginning of the string, followed by an equals sign, followed by your text.<br>
     * For a generic regular expression, begin the string with "regex=".<br>
     * See {@link #getRegexPatterns()} for the list of supported regexes<br>
     * 
     * @param originalText
     *            The regex string
     * @return A Java Pattern object
     */
    public static Pattern stringToPattern(String originalText)
    {
        String regexPattern = originalText;
        if (Regex.isStringToPattern(originalText))
        {
            String regexName = originalText.substring(0, originalText.indexOf("="));
            regexPattern = Regex.getRegexPatterns().get(regexName).replace("====PLACEHOLDER====", originalText.replace(regexName + "=", ""));
        }
        return Pattern.compile(regexPattern);
    }
    
    /**
     * This function splits a string into key-value pairs. Semicolons separate each pair and equals separates keys from values.<br>
     * (e.g. for the string "a=b;c=d;e=f" this function would create a Hashtable with 3 keys and 3 values (a => b, c => d, e => f))
     * 
     * @param arg
     *            The string to split up into key-value pairs.
     * @return The Map of key-value pairs
     */
    public static Map<String, String> getNameValuePairs(String arg)
    {
        Map<String, String> pairs = new Hashtable<String, String>();
        
        // Split on semi-colon
        String[] semiColonMatches = arg.split(";");
        for (int i = 0; i < semiColonMatches.length; i++)
        {
            String pair = semiColonMatches[i];
            
            // Split on =
            String[] equalMatches = pair.split("=");
            if (equalMatches.length == 2)
            {
                pairs.put(equalMatches[0], equalMatches[1]);
            }
        }
        return pairs;
    }
}
