'''
Created on Dec 19, 2011

@author: smcadams
'''

from com.rational.test.ft.script import RationalTestScript
from python.screens.GoogleSearch import GoogleSearch

if __name__ == '__main__':
    #Open Google Search Page
    search = GoogleSearch(launch=True)
    
    #Type Into the Search Box
    search.type("Google Search Box", "Java Testing", True)
    
    RationalTestScript.sleep(3) #For Demo purposes
    
    #Click the Images link on the left hand side
    search.click("Images")
    
    #wait for the browser to finish loading
    search.browser.sync(20, 1)
    
    #Using Jython click evertyhing link on left hand side
    search.py_click("Everything")
    
    RationalTestScript.sleep(3) #For Demo purposes
    
    #waid for browser to finish loading
    search.browser.sync(20, 1)
    
    #Using Jython type into the search box
    search.py_type("Google Search Box", "Jython Testing", True)
    RationalTestScript.sleep(5) #For Demo purposes
    
    #Close the explorer window
    search.close()

