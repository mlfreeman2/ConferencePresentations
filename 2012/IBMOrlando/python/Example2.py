'''
Created on May 27, 2012

@author: Jarga
'''

from python.screens.W3SchoolEditor import W3SchoolEditor
from com.advancedrft.common.automation.rft import Log
from com.advancedrft.common.io import Console

if __name__ == '__main__':
    w3s = W3SchoolEditor(url="http://www.w3schools.com/tags/tryit.asp?filename=tryhtml_option", launch=True)
    w3s.type("Input Text", "<html>\n"
                            "<body>\n"
                            "  <select id=\"exampleSelect\">\n"
                            "    <option>Volvo</option>\n"
                            "    <option>Saab</option>\n"
                            "    <option>Mercedes</option>\n"
                            "    <option>Audi</option>\n"
                            "  </select>\n"
                            "</body>\n"
                            "</html>\n", False)
    w3s.click("Edit and Click Me")
    selected = w3s.py_select("Example Drop Down", "Saab")
    Log.logTestResult("Selected 'Saab' from drop down list", None, Console.getScreenshot(), selected and selected[0] == "Saab")
    w3s.close()