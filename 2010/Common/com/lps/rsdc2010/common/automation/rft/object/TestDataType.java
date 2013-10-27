package com.lps.rsdc2010.common.automation.rft.object;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import com.lps.rsdc2010.common.lang.StringFunctions;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.vp.ITestDataElement;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;
import com.rational.test.ft.vp.ITestDataTable;
import com.rational.test.ft.vp.ITestDataText;
import com.rational.test.ft.vp.ITestDataTree;
import com.rational.test.ft.vp.ITestDataTreeNode;
import com.rational.test.ft.vp.ITestDataTreeNodes;

public class TestDataType
{
    
    /**
     * Dumps all the test data type details on all objects found that match objectProperties
     * 
     * @param object
     *            The TestObject you want details on
     * @return A LinkedHashMap with the details on the given object.
     */
    public static LinkedHashMap<String, Object> dumpTestDataTypeDetails(TestObject object)
    {
        LinkedHashMap<String, Object> currentTODetails = new LinkedHashMap<String, Object>();
        for (Enumeration<?> availableTestDataTypes = object.getTestDataTypes().keys(); availableTestDataTypes.hasMoreElements();)
        {
            Object currentDataTypeName = availableTestDataTypes.nextElement();
            Object currentDataType = object.getTestData(currentDataTypeName.toString());
            LinkedHashMap<String, Object> extractedDetailsOfCurrentDataType = new LinkedHashMap<String, Object>();
            if (currentDataType == null)
            {
                currentTODetails.put(currentDataTypeName.toString(), "Null Test Data Type");
            }
            else if (currentDataType instanceof ITestDataText)
            {
                ITestDataText text = (ITestDataText) currentDataType;
                extractedDetailsOfCurrentDataType.put("Ignores Case", Boolean.toString(text.getIgnoreCase()));
                switch (text.getWhiteSpaceMode())
                {
                    case ITestDataText.IGNORE_ALL_SPACES:
                        extractedDetailsOfCurrentDataType.put("Whitespace Mode", "Ignores all whitespace characters");
                        break;
                    case ITestDataText.IGNORE_INTERNAL_SPACES:
                        extractedDetailsOfCurrentDataType.put("Whitespace Mode", "Ignores nested whitespace characters");
                        break;
                    case ITestDataText.IGNORE_LEADING_AND_TRAILING_SPACES:
                        extractedDetailsOfCurrentDataType.put("Whitespace Mode", "Ignores leading and trailing whitespace characters");
                        break;
                    case ITestDataText.IGNORE_LEADING_SPACES:
                        extractedDetailsOfCurrentDataType.put("Whitespace Mode", "Ignores leading whitespace characters");
                        break;
                    case ITestDataText.IGNORE_TRAILING_SPACES:
                        extractedDetailsOfCurrentDataType.put("Whitespace Mode", "Ignores trailing whitespace characters");
                        break;
                    default:
                    case ITestDataText.IGNORE_NONE:
                        extractedDetailsOfCurrentDataType.put("Whitespace Mode", "No characters ignored");
                        break;
                }
                extractedDetailsOfCurrentDataType.put("Text", StringFunctions.ifNull2(text.getText(), "null"));
            }
            else if (currentDataType instanceof ITestDataTable)
            {
                ITestDataTable table = (ITestDataTable) currentDataType;
                
                extractedDetailsOfCurrentDataType.put("Row Count", Integer.toString(table.getRowCount()));
                extractedDetailsOfCurrentDataType.put("Has Row Headers", Boolean.toString(table.hasRowHeaders()));
                extractedDetailsOfCurrentDataType.put("Column Count", Integer.toString(table.getColumnCount()));
                extractedDetailsOfCurrentDataType.put("Has Column Headers", Boolean.toString(table.hasColumnHeaders()));
                if (table.hasRowHeaders())
                {
                    Vector<String> rowHeaders = new Vector<String>();
                    for (int row = 0; row < table.getRowCount(); row++)
                    {
                        rowHeaders.add(StringFunctions.ifNull2(table.getRowHeader(row), "null"));
                    }
                    extractedDetailsOfCurrentDataType.put("Row Headers", rowHeaders);
                }
                if (table.hasColumnHeaders())
                {
                    Vector<String> colHeaders = new Vector<String>();
                    for (int column = 0; column < table.getColumnCount(); column++)
                    {
                        colHeaders.add(StringFunctions.ifNull2(table.getColumnHeader(column), "null"));
                    }
                    extractedDetailsOfCurrentDataType.put("Column Headers", colHeaders);
                }
                
                Vector<Vector<String>> rowsAndColumns = new Vector<Vector<String>>();
                for (int row = 0; row < table.getRowCount(); row++)
                {
                    Vector<String> rowVec = new Vector<String>();
                    for (int col = 0; col < table.getColumnCount(); col++)
                    {
                        String cell = StringFunctions.ifNull2(table.getCell(row, col), "null");
                        rowVec.add(cell.equals("") ? "empty" : cell);
                    }
                    rowsAndColumns.add(rowVec);
                }
                extractedDetailsOfCurrentDataType.put("Table Contents", rowsAndColumns);
            }
            else if (currentDataType instanceof ITestDataList)
            {
                ITestDataList list = (ITestDataList) currentDataType;
                extractedDetailsOfCurrentDataType.put("Number of Elements", list.getElementCount());
                ITestDataElementList elements = list.getElements();
                Vector<?> elementVec = elements.getElements();
                for (int i = 0; i < elementVec.size(); i++)
                {
                    extractedDetailsOfCurrentDataType.put(Integer.toString(i), ((ITestDataElement) elementVec.get(i)).getElement());
                }
            }
            else if (currentDataType instanceof ITestDataTree)
            {
                ITestDataTree tree = (ITestDataTree) currentDataType;
                extractedDetailsOfCurrentDataType.put("Is Ordered", tree.getOrdered());
                ITestDataTreeNodes nodes = tree.getTreeNodes();
                extractedDetailsOfCurrentDataType.put("Total Number Of Nodes", nodes.getNodeCount() + nodes.getRootNodeCount());
                ITestDataTreeNode[] rootNodes = nodes.getRootNodes();
                for (ITestDataTreeNode root : rootNodes)
                {
                    ITestDataTreeNode position = root;
                    LinkedHashMap<String, Object> rootPath = new LinkedHashMap<String, Object>();
                    LinkedHashMap<String, Object> dataPosition = rootPath;
                    int nodeIndex = 1, rootIndex = 1;
                    for (ITestDataTreeNode child : position.getChildren())
                    {
                        LinkedHashMap<String, Object> node = new LinkedHashMap<String, Object>();
                        node.put("Description", position.getNode());
                        position = child;
                        dataPosition.put("Node " + nodeIndex, node);
                        dataPosition = node;
                        nodeIndex++;
                    }
                    extractedDetailsOfCurrentDataType.put("Root Index " + rootIndex, rootPath);
                    rootIndex++;
                }
            }
            currentTODetails.put(currentDataTypeName.toString(), extractedDetailsOfCurrentDataType);
        }
        
        return currentTODetails;
    }
    
    /**
     * Extracts an ITestDataTable object to a vector of vectors of strings
     * 
     * @param currentDataType
     *            The ITestDataTable object
     * @return A vector of vectors of strings, or null if the object isn't an ITestDataTable object
     */
    public static Vector<Vector<String>> iTestDataTableToTable(Object currentDataType)
    {
        if (!(currentDataType instanceof ITestDataTable))
        {
            return null;
        }
        ITestDataTable table = (ITestDataTable) currentDataType;
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        if (table.hasRowHeaders())
        {
            columnCount += 1;
        }
        if (table.hasColumnHeaders())
        {
            rowCount += 1;
        }
        Vector<Vector<String>> finalTable = new Vector<Vector<String>>();
        for (int i = 0; i < rowCount; i++)
        {
            finalTable.add(new Vector<String>());
            finalTable.get(i).setSize(columnCount);
        }
        if (table.hasColumnHeaders())
        {
            int start = table.hasRowHeaders() ? 1 : 0;
            for (int column = 0; column < table.getColumnCount(); column++)
            {
                finalTable.get(0).insertElementAt(StringFunctions.ifNull(table.getColumnHeader(column)), start + column);
            }
        }
        if (table.hasRowHeaders())
        {
            int start = table.hasColumnHeaders() ? 1 : 0;
            for (int row = 0; row < table.getRowCount(); row++)
            {
                finalTable.get(start + row).set(0, StringFunctions.ifNull(table.getRowHeader(row)));
            }
        }
        
        for (int row = 0; row < table.getRowCount(); row++)
        {
            for (int col = 0; col < table.getColumnCount(); col++)
            {
                String cell = StringFunctions.ifNull(table.getCell(row, col));
                int r = table.hasColumnHeaders() ? row + 1 : row;
                int c = table.hasRowHeaders() ? col + 1 : col;
                finalTable.get(r).set(c, cell);
            }
        }
        return finalTable;
    }
    
    /**
     * Extracts an ITestDataList object to a list of strings
     * 
     * @param currentDataType
     *            The ITestDataList object
     * @return A list of strings, or null if the object isn't an ITestDataList object
     */
    public static List<String> iTestDataListToList(Object currentDataType)
    {
        List<String> result = new ArrayList<String>();
        if (!(currentDataType instanceof ITestDataList))
        {
            return null;
        }
        ITestDataList list = (ITestDataList) currentDataType;
        ITestDataElementList elements = list.getElements();
        Vector<?> elementVec = elements.getElements();
        for (int i = 0; i < elementVec.size(); i++)
        {
            result.add(StringFunctions.ifNull(((ITestDataElement) elementVec.get(i)).getElement()));
        }
        return result;
    }
    
    /**
     * Extracts an ITestDataText object to a string
     * 
     * @param currentDataType
     *            The ITestDataText object
     * @return A string, or null if the object isn't an ITestDataText object
     */
    public static String iTestDataTextToString(Object currentDataType)
    {
        if (!(currentDataType instanceof ITestDataText))
        {
            return null;
        }
        ITestDataText text = (ITestDataText) currentDataType;
        return StringFunctions.ifNull(text.getText());
    }
    
}
