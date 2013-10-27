// $Id: WebActions.java 2153 2010-05-29 16:10:42Z mlfreeman $
package com.lps.rsdc2010.common.automation.rft.object.actions;

import java.util.Hashtable;

import com.lps.rsdc2010.common.lang.StringFunctions;

public class WebActions extends Actions
{
    
    /*
     * (non-Javadoc)
     * @see com.lps.common.automation.rft.object.action.Actions#getDomainSpecificPropertyName(java.lang.String, java.lang.String)
     */
    @Override
    public String getDomainSpecificPropertyName(String property)
    {
        Hashtable<String, String> propertyMappings = new Hashtable<String, String>();
        propertyMappings.put("Disabled", ".disabled");
        propertyMappings.put("Index", ".index");
        propertyMappings.put("Text", ".text");
        propertyMappings.put("Value", ".value");
        if (propertyMappings.containsKey(property))
        {
            return propertyMappings.get(property);
        }
        return "No mapping of " + property + " for the Html domain.";
    }
    
    /*
     * (non-Javadoc)
     * @see com.lps.common.automation.rft.object.action.Actions#getDomainSpecificTestDataTypeName(java.lang.String, java.lang.String)
     */
    @Override
    public String getDomainSpecificTestDataTypeName(String control, String property)
    {
        Hashtable<String, String> propertyMappings = new Hashtable<String, String>();
        if (control.equals("Table"))
        {
            propertyMappings.put("Table Contents", "contents");
            propertyMappings.put("Default", "contents");
        }
        else if (control.equals("List"))
        {
            propertyMappings.put("List Items", "list");
            propertyMappings.put("Selected List Items", "selected");
            propertyMappings.put("Default", "list");
        }
        else
        {
            return "No mapping of the test data type " + property + " for the Html domain.";
        }
        return propertyMappings.get(property);
    }
    
    @Override
    public String getGenericControlType(String className)
    {
        Hashtable<String, String> control = new Hashtable<String, String>();
        control.put("Html.SELECT", "List");
        control.put("Html.TABLE", "Table");
        return StringFunctions.ifNull(control.get(className));
    }
    
}
