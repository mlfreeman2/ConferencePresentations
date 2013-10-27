'''
Created on May 26, 2012

@author: Jarga
'''
from com.rational.test.ft.script import RationalTestScript
import os

class Browser:
    ie_version = RationalTestScript.getOperatingSystem().getRegistryValue("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Internet Explorer\\Version")
    ie_folder = RationalTestScript.getOperatingSystem().getRegistryValue("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\IEXPLORE.EXE\\Path").replace(";", "")
    ie_name = "iexplore.exe"
    rootBrowserProcess = None
    
    def __init__(self, url):
        cmdLine = "\"" + self.ie_folder + os.sep + self.ie_name + "\" \"" + url.strip() + "\"";
        self.rootBrowserProcess = RationalTestScript.run(cmdLine, None);
        self.rootBrowserProcess.waitForExistence(120, 1)