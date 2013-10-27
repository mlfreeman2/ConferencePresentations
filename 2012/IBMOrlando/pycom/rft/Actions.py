'''
Created on May 24, 2012

@author: Jarga
'''

import re
import sys
from com.rational.test.ft import UnregisteredObjectException, WindowActivateFailedException, ObjectIsDisposedException
from com.rational.test.ft.script import RationalTestScript, Property, SubitemFactory
from com.rational.test.ft.object.interfaces import GuiTestObject, TextGuiTestObject, SelectGuiSubitemTestObject

from com.advancedrft.common.automation.rft.object.actions import Actions
from com.advancedrft.common.automation.rft import Log
from com.advancedrft.common.io import Console
from java.lang import Exception

class JythonActions(Actions):
    def py_type(self, obj, friendlyName, data,  verify=False):
        '''
        Types the given data into the object requested
        
        Arguments:
            obj -- Object to type into
            friendlyName --  Friendly name for the object, used in logging
            data -- data to type into the object
            
        Keyword Arguments:
            verify -- true to retrieve the text after typing and retry to 
                type the value if it does not match the data given
        
        Returns:
            Resulting text contained by the given object
        '''
        try:
            tgto = (TextGuiTestObject(obj))
            tgto.setText(data)
            if verify:
                RationalTestScript.sleep(0.2)
                if ((tgto.getText() == data) == False):
                    tgto.setText(data)
            return tgto.getText()
        except UnregisteredObjectException:
            Log.logError("Failed to type " + data + " Into " + friendlyName + " because the object became unregistered.", Console.getScreenshot())
        except WindowActivateFailedException:
            Log.logError("Failed to type " + data + " Into " + friendlyName + " because it is disabled.", Console.getScreenshot())
        except ObjectIsDisposedException:
            Log.logError("Failed to type " + data + " Into " + friendlyName + " because the object was disposed.", Console.getScreenshot())
        except:
            exception = sys.exc_info()[1]
            if(isinstance(exception, Exception)):
                Log.logException(exception, "Failed to type " + data + " Into " + friendlyName)
            else:
                Log.logError("Failed to type " + data + " Into " + friendlyName, exception.__str__())
        
        return ""
    
    def py_click(self, obj, objName):
        try:
            gto = GuiTestObject(obj)
            gto.click()
            return 1
        except:
            Log.logError("Failed to click " + objName)
        return 0
    
    def py_select(self, obj, item, objName):
        try:
            listItems = self.py_getItemsInList(obj, objName)
            theObj = SelectGuiSubitemTestObject(obj)
            if listItems:
                if item.lower().startswith("index="):
                    index = item[6:]
                    theObj = SelectGuiSubitemTestObject(obj)
                    theObj.select(int(index))
                elif item.lower().startswith("regex="):
                    regex = item[6:]
                    for i in range(0, len(listItems)):
                        if listItems[i] and re.match(regex, listItems[i] , flags=0):
                            index = i
                    theObj.select(int(index))
                else:
                    theObj.select(item)
                return self.py_getSelectedItemsInList(obj, objName)
            else:
                Log.logError("No items available in list " + objName)
        except:
            exception = sys.exc_info()[1]
            if(isinstance(exception, Exception)):
                Log.logException(exception, "Failed to Select " + item + " in Object " + objName)
            else:
                Log.logError("Failed to Select " + item + " in Object " + objName, exception.__str__())
        return []
    
    def py_getItemsInList(self, obj, objName):
        try:
            nameList = obj.getTestData("list")
            nameListElements = nameList.getElements()
            items = []
            for i in range(nameList.getElementCount()):
                try:
                    option = nameListElements.getElement(i)
                    items.append(str(option.getElement()))
                except:
                    pass
            return items
        except:
            exception = sys.exc_info()[1]
            if(isinstance(exception, Exception)):
                Log.logException(exception, "Failed to retrieve items in list " + objName)
            else:
                Log.logError("Failed to retrieve items in list " + objName, exception.__str__())
            return None
        
    def py_getSelectedItemsInList(self, obj, objName):
        try:
            nameList = obj.getTestData("selected")
            nameListElements = nameList.getElements()
            items = []
            for i in range(nameList.getElementCount()):
                try:
                    option = nameListElements.getElement(i)
                    items.append(str(option.getElement()))
                except:
                    pass
            return items
        except:
            exception = sys.exc_info()[1]
            if(isinstance(exception, Exception)):
                Log.logException(exception, "Failed to retrieve selected items in list " + objName)
            else:
                Log.logError("Failed to retrieve selected items in list " + objName, exception.__str__())
            return None
        
    def findObjects(self, obj_start, properties):
        '''
        Finds objects with the given properties using a given starting point
        
        Arguments:
            obj_start -- TestObject to start looking from
            properties -- Array of properties representing the object(s) 
                you want to find in form of ["Property", "Value", "Property", "Value"]
                
        Returns:
            Array of TestObjects found
        '''
        if (len(properties) % 2) != 0 :
            Log.logError("Uneven number of properties given! Cannot continue.")
            return None
        prop_array = []
        for i in range(0, len(properties), 2):
            prop_array.append(Property(properties[i], properties[i + 1]))
        return obj_start.find(SubitemFactory.atDescendant(prop_array), False) if obj_start else None