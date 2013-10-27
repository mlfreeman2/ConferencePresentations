'''
Created on May 29, 2012

@author: vmuser
'''
from pycom.rft.ObjectManager import JythonWebObjectManager
from com.advancedrft.common.automation.rft.object import Browser
from com.rational.test.ft.script import RationalTestScript

class W3SchoolEditor(JythonWebObjectManager):
    '''
    classdocs
    '''
    browser = None

    def __init__(self, url="http://www.w3schools.com/tags/tryit.asp", launch=False):
        '''
        Constructor
        '''
        if launch:
            self.launch(url)
        if self.browser:
            super(W3SchoolEditor, self).__init__("W3School Html Editor", baseObject = self.browser.browser)
        else:
            super(W3SchoolEditor, self).__init__("W3School Html Editor")
        self.add_items()
        
    def add_items(self):
        self.addObjectDefinition("Input Text", [".class", "Html.TEXTAREA", ".id", "pre_code"])
        self.addObjectDefinition("Edit and Click Me", [".class", "Html.INPUT.submit", ".value", "regex=Edit and Click Me.*"])
        self.addObjectDefinition("Example Drop Down", [".class", "Html.SELECT", ".id", "exampleSelect"])
   
    def launch(self, url):
        self.browser = Browser(url)
        RationalTestScript.sleep(2)
        
    def close(self):
        self.browser.close()