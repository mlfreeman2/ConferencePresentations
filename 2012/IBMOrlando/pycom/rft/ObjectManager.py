'''
Created on May 25, 2012

@author: Jarga
'''
from com.advancedrft.common.automation.rft.object.management import ObjectManager
from pycom.rft.Actions import JythonActions
from com.rational.test.ft.object.interfaces import RootTestObject
from com.rational.test.ft.value import RegularExpression
from com.rational.test.ft.script import RationalTestScript
from java.lang.System import currentTimeMillis

class JythonWebObjectManager(ObjectManager):
    def __init__(self, screenName, baseObject = RootTestObject.getRootTestObject()):
        super(JythonWebObjectManager, self).__init__(screenName, JythonActions())
        #RationalTestScript.logInfo(str(dir(self)) + "\n" + str(self.__dict__.keys()))
        self.super__setBaseObject(baseObject)
        
    def findKnownItems(self, itemName, timeToLook=0.0):
        now = currentTimeMillis() / 1000.0
        stop = now + timeToLook
        value = []
        while now < stop and not value:
            #look through each property and if the property is marked as a regular expressing convert the property to a regular expression object
            obj_props = map(lambda x: RegularExpression(x[6:], True) if x.lower().startswith("regex=") else x, self.super__getItemPropertiesAsArray(itemName))
            value = self.super__getActionLib().findObjects(self.super__getBaseObject(), obj_props)
            now = currentTimeMillis() / 1000.0
            
        return value;
    
    def py_type(self, itemName, data, verify=True):
        tos = self.findKnownItems(itemName, 30)
        if(tos and len(tos) > 0):
            value = self.super__getActionLib().py_type(tos[0], itemName, data, verify)
        RationalTestScript.unregister(tos)
        return value
    
    def py_click(self, itemName):
        tos = self.findKnownItems(itemName, 30)
        if(tos and len(tos) > 0):
            value = self.super__getActionLib().py_click(tos[0], itemName)
        RationalTestScript.unregister(tos)
        return value
        
    def py_select(self, itemName, data):
        tos = self.findKnownItems(itemName, 30)
        if(tos and len(tos) > 0):
            value = self.super__getActionLib().py_select(tos[0], data, itemName)
        RationalTestScript.unregister(tos)
        return value
    
    def addObjectDefinition(self, itemName, properties):
        self.super__addItemToTable(itemName, properties)
        
        