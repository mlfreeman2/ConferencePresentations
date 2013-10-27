package com.lps.rsdc2010.common.lang;

import java.util.Vector;

/**
 * A collection of functions used for array manipulation
 */
public class ArrayFunctions
{
    /**
     * If you supply a string with ranges in it (starting from 0, and of the form "1", "1-3", "1-3,5", "1,2,4,6", "ALL", "1,3-END", etc), this function will expand them into a list of indexes.<br>
     * (e.g. "1-3, 5, 7-9" on a 10 item array would expand to a list with "1", "2", "3", "5", "7", "8", "9").<br>
     * (e.g. "ALL" on a 6 item array would expand to a list with "0", "1", "2", "3", "4", "5").<br>
     * (e.g. "1,3-END" on a 6 item array would expand to a list with "1", "3", "4", "5").<br>
     * The overall list is a mix of comma-separated numbers or ranges.<br>
     * Ranges are indicated by starting and ending values separated by a dash. The starting and ending values are INCLUDED.<br>
     * 
     * @param arrayLen
     *            The maximum length of the array is necessary for use with "ALL" or "END"<br>
     * @param range
     *            The range string (e.g. "1", "1-3", "1-3,5", "1,2,4,6", "ALL", "1,3-END", etc).<br>
     *            The string is converted to uppercase and if the string contains "ALL", everything else in it will be ignored.<br>
     * @return A vector of indexes
     */
    public static Vector<Integer> calculateIndexesToInclude(int arrayLen, String range)
    {
        range = range.trim().toUpperCase().replace("END", Integer.toString(arrayLen - 1));
        Vector<Integer> indexes = new Vector<Integer>();
        if (range.contains("ALL"))
        {
            for (int i = 0; i < arrayLen; i++)
            {
                indexes.add(i);
            }
            return indexes;
        }
        
        for (String dataset : range.split(","))
        {
            if (dataset.indexOf("-") == -1)
            {// if there's only one value in this spot, add it and go on
                indexes.add(Integer.parseInt(dataset));
                continue;
            }
            // otherwise split the string into starting and ending values
            String[] rangeBoundaries = dataset.split("-");
            // if the range would start outside the array's boundaries, ignore it.
            if (Integer.parseInt(rangeBoundaries[0]) < arrayLen)
            {
                // if the range exceeds the array's size, use the array's size instead.
                int len = Integer.parseInt(rangeBoundaries[1]) < arrayLen ? Integer.parseInt(rangeBoundaries[1]) : arrayLen - 1;
                for (int i = Integer.parseInt(rangeBoundaries[0]); i <= len; i++)
                {
                    indexes.add(i);
                }
            }
        }
        return indexes;
    }
}
