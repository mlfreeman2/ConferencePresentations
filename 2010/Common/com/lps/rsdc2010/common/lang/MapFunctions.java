package com.lps.rsdc2010.common.lang;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * This class contains common java.util.Map manipulation functions.
 */
public class MapFunctions
{
    /**
     * Looks through a java.util.Map<String, ?> for all the keys that match the supplied pattern.<br>
     * 
     * @param pattern
     *            The regular expression
     * @param hash
     *            The Map&lt;String, ?&gt; to look inside
     * @return The keys that matched
     */
    public static Vector<String> getKeysMatchingPattern(String patternString, Map<String, ?> hash)
    {
        Vector<String> v = new Vector<String>();
        for (String key : hash.keySet())
        {
            if (Regex.stringToMatcher(patternString, key).find())
            {
                v.addElement(key);
            }
        }
        return v;
    }
    
    /**
     * Converts a java.util.Map<String, String> into a String array of (key,value,key,value,key,value...etc)<br>
     * If you supply values for exclusionPatterns, they will be treated as regular expressions and each one will be matched against every key.<br>
     * If any of the patterns match, the key (and its value) will be left out of the resulting array.<br>
     * Supplying an empty string ("") will cause everything to be excluded.<br>
     * If you want to include everything in the map, do not supply any exclusion patterns.<br>
     * 
     * @param properties
     *            The map of strings to strings to convert
     * @param exclusionPatterns
     *            A list of patterns or keys to ignore
     * @return The resulting string array. It will have an even length.
     */
    public static String[] getStringArray(Map<String, String> properties, String...exclusionPatterns)
    {
        Vector<String> items = new Vector<String>();
        if (properties == null)
        {
            return new String[0];
        }
        for (String key : properties.keySet())
        {
            boolean notIncluded = false;
            for (String p : exclusionPatterns)
            {
                if (Regex.stringToMatcher(p, key).find())
                {
                    notIncluded = true;
                }
            }
            if (notIncluded == false)
            {
                items.add(key);
                items.add(StringFunctions.ifNull(properties.get(key)));
            }
        }
        return items.toArray(new String[0]);
    }
    
    /**
     * Merges two java.util.Map<String, V>, primary and secondary. <br>
     * If secondary contains a key listed in exceptionList, that hash key will be completely ignored. <br>
     * If both primary and secondary contain the key "K" and overwrite is true, primary.put("K", secondary.get("K")) will be executed.
     * 
     * @param primary
     *            java.util.Map&lt;String, V&gt;
     * @param secondary
     *            java.util.Map&lt;String, V&gt;
     * @param overwrite
     *            If both primary and secondary contain the key "K" and overwrite is true, primary.put("K", secondary.get("K")) will be executed.
     * @param exceptionList
     *            A list of patterns or exact keys that will be treated as nonexistent.
     * @return LinkedHashMap
     */
    public static <V> LinkedHashMap<String, V> mergeMaps(Map<String, V> primary, Map<String, V> secondary, boolean overwrite, String...exceptionList)
    {
        LinkedHashMap<String, V> result = new LinkedHashMap<String, V>();
        result.putAll(primary);
        for (String key : secondary.keySet())
        {
            boolean includeInMergedHashtable = true;
            for (String p : exceptionList)
            {
                if (Regex.stringToMatcher(p, key).find())
                {
                    includeInMergedHashtable = false;
                }
            }
            if (includeInMergedHashtable == true && primary.containsKey(key))
            {
                includeInMergedHashtable = overwrite;
            }
            if (includeInMergedHashtable == true)
            {
                result.put(key, secondary.get(key));
            }
        }
        return result;
    }
    
    /**
     * This function is a generic way to convert two arrays into a LinkedHashMap<K, V> with keys => values.<br>
     * Extra elements in either array will be silently left behind.
     * 
     * @param <K>
     *            The type the keys in the LinkedHashMap will have.
     * @param <V>
     *            The type the values in the LinkedHashMap will have.
     * @param keys
     *            The array of items to use as keys.
     * @param values
     *            The array of item to use as values.
     * @return A LinkedHashMap with keys => values. Extra elements in either array will be silently left behind.
     */
    public static <K, V> LinkedHashMap<K, V> arraysToLinkedHashMap(K[] keys, V[] values)
    {
        LinkedHashMap<K, V> result = new LinkedHashMap<K, V>();
        int range = java.lang.Math.min(keys.length, values.length);
        for (int i = 0; i < range; i++)
        {
            result.put(keys[i], values[i]);
        }
        return result;
    }
    
    /**
     * This function will turn an array into a LinkedHashMap using the even numbered elements as keys and the odd numbered ones as values.
     * 
     * @param <X>
     *            The type that both the keys and values in the LinkedHashMap will have.
     * @param items
     *            The items to use in the LinkedHashMap. <br>
     *            If this array's length is not even, the last element will be silently discarded since there won't be a value to pair it with.
     * @return A LinkedHashMap where the keys and values are the same type.
     */
    public static <X> LinkedHashMap<X, X> arrayToLinkedHashMap(X...items)
    {
        int length = items.length;
        LinkedHashMap<X, X> result = new LinkedHashMap<X, X>((length / 2) + 1);
        if (length % 2 != 0)
        {
            length -= 1;
        }
        for (int i = 0; i < length; i += 2)
        {
            result.put(items[i], items[i + 1]);
        }
        return result;
    }
    
    /**
     * This function is a generic way to convert two arrays into a Hashtable<K, V> with keys => values.<br>
     * Extra elements in either array will be silently left behind.
     * 
     * @param <K>
     *            The type the keys in the Hashtable will have.
     * @param <V>
     *            The type the values in the Hashtable will have.
     * @param keys
     *            The array of items to use as keys.
     * @param values
     *            The array of item to use as values.
     * @return A Hashtable with keys => values. Extra elements in either array will be silently left behind.
     */
    public static <K, V> Hashtable<K, V> arraysToHashtable(K[] keys, V[] values)
    {
        Hashtable<K, V> result = new Hashtable<K, V>();
        int range = java.lang.Math.min(keys.length, values.length);
        for (int i = 0; i < range; i++)
        {
            result.put(keys[i], values[i]);
        }
        return result;
    }
    
    /**
     * This function will turn an array into a Hashtable using the even numbered elements as keys and the odd numbered ones as values.
     * 
     * @param <X>
     *            The type that both the keys and values in the Hashtable will have.
     * @param items
     *            The items to use in the Hashtable. <br>
     *            If this array's length is not even, the last element will be silently discarded since there won't be a value to pair it with.
     * @return A Hashtable where the keys and values are the same type.
     */
    public static <X> Hashtable<X, X> arrayToHashtable(X...items)
    {
        Hashtable<X, X> result = new Hashtable<X, X>();
        int length = items.length;
        if (length % 2 != 0)
        {
            length -= 1;
        }
        for (int i = 0; i < length; i += 2)
        {
            result.put(items[i], items[i + 1]);
        }
        return result;
    }
}
