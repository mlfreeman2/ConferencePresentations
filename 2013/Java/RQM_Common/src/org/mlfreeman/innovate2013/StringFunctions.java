package org.mlfreeman.innovate2013;

/**
 * Basic helpful string related functions
 */
public class StringFunctions
{

    /**
     * A port of System.String.IsNullOrWhiteSpace(string str) from C# to Java
     * 
     * @param str
     *            The string in question
     * @return True if the string is null or whitespace
     */
    public static boolean isNullOrWhitespace(String str)
    {
        return str == null || str.trim().length() == 0;
    }
    
}
