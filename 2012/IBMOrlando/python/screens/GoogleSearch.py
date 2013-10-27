'''
Created on May 27, 2012

@author: Jarga
'''
from pycom.rft.ObjectManager import JythonWebObjectManager
from com.rational.test.ft.script import RationalTestScript
from com.advancedrft.common.automation.rft.object import Browser

class GoogleSearch(JythonWebObjectManager):
    '''
    classdocs
    '''
    browser = None

    def __init__(self, launch = False):
        '''
        Constructor
        '''
        if launch:
            self.launch()
        if self.browser:
            super(GoogleSearch, self).__init__("Google Search", baseObject = self.browser.browser)
        else:
            super(GoogleSearch, self).__init__("Google Search")
        self.add_items()
    
    def add_items(self):
        self.addObjectDefinition("Google Search Box", [".class", "regex=(^Html\\.INPUT\\.text|^Html\\.INPUT\\.textarea)", ".name", "q"])
        self.addObjectDefinition("Everything", [".class", "Html.A", ".text", "regex=.*(Everything|Web).*", ".className", "kl"])
        self.addObjectDefinition("Images", [".class", "Html.TextNode", ".text", "regex=.*Images.*"])
        self.addObjectDefinition("Google Home", [".class", "Html.A", ".id", "logo", ".title", "Go to Google Home"])
        
    def launch(self):
        self.browser = Browser("www.google.com")
        RationalTestScript.sleep(2)
        
    def close(self):
        self.browser.close()