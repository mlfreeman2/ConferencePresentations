package com.advancedrft.common.automation.rft.object.actions;

import java.util.Hashtable;

import com.advancedrft.common.lang.StringFunctions;

/**
 * This class is a library of wrappers around browser / webapp-specific RFT actions.<br />
 * The recommended usage is to simply create a new instance of this class and then call the actions for every click / type / etc.<br />
 * There is no harm in creating as many instances as you want. <br />
 * <br />
 * This class can be extended and individual methods overridden as needed to create action libraries for various particular web applications.
 */
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
