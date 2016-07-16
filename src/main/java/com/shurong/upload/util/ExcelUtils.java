package com.shurong.upload.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;


public class ExcelUtils {
    public static Workbook initWorkbook(InputStream is) throws IOException{
        if(!is.markSupported()){
            is = new PushbackInputStream(is, 8);
        }
        Workbook wb = null;
        if(POIFSFileSystem.hasPOIFSHeader(is)){
            wb = new HSSFWorkbook(is);
        }else if(POIXMLDocument.hasOOXMLHeader(is)){
            try {
                wb = new XSSFWorkbook(OPCPackage.open(is));
            } catch (InvalidFormatException e) {
                throw new IllegalArgumentException("非法的格式!");
            }
        }else{
            throw new IllegalArgumentException("非法的格式!");
        }
        return wb;
    }
    
    public static Object getCellValue(Cell cell) {
        int cellType = cell.getCellType();
        if (cellType == Cell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue();
        }else if(cellType == Cell.CELL_TYPE_STRING){
            return cell.getStringCellValue();
        }else if (cellType == Cell.CELL_TYPE_NUMERIC) {
            if(DateUtil.isCellDateFormatted(cell)){
                Date date = cell.getDateCellValue();
                return CalendarUtils.getFormatDate(CalendarUtils.Y_M_D_LONG,date);
            }else{
                return cell.getNumericCellValue();
            }
        }else  {
            return cell.toString();
        }
    }
    
    public static String getCellStringValue(Cell cell) {    
        String cellValue = "";    
        DecimalFormat df = new DecimalFormat("0");
        switch (cell.getCellType()) {    
        case HSSFCell.CELL_TYPE_STRING://字符串类型
            cellValue = cell.getStringCellValue();    
            if(cellValue.trim().equals("")||cellValue.trim().length()<=0)    
                cellValue="";    
            break;    
        case HSSFCell.CELL_TYPE_NUMERIC: //数值类型
            cellValue = df.format(cell.getNumericCellValue());   
            break;    
        case HSSFCell.CELL_TYPE_FORMULA: //公式
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);    
            cellValue = String.valueOf(cell.getNumericCellValue());    
            break;    
        case HSSFCell.CELL_TYPE_BLANK:    
            break;    
        case HSSFCell.CELL_TYPE_BOOLEAN:    
            break;    
        case HSSFCell.CELL_TYPE_ERROR:    
            break;    
        default:    
            break;    
        }    
        return cellValue;    
    }
    
    public static Float getCellFloatValue(Cell cell) {    
    	Float cellValue = null;    
        switch (cell.getCellType()) {    
        case HSSFCell.CELL_TYPE_STRING://字符串类型
            String cellString = cell.getStringCellValue();    
            if(StringUtils.isEmpty(cellString))    
            	break;  
            try {
				cellValue = Float.valueOf(cellString);
			} catch (NumberFormatException e) {
				break;
			}
            
        case HSSFCell.CELL_TYPE_NUMERIC: //数值类型
            cellValue =  (float)cell.getNumericCellValue();
            break;    
        case HSSFCell.CELL_TYPE_FORMULA: //公式
//            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);    
//            cellValue = String.valueOf(cell.getNumericCellValue());    
            break;    
        case HSSFCell.CELL_TYPE_BLANK:    
            break;    
        case HSSFCell.CELL_TYPE_BOOLEAN:  
        	boolean cellBoolean = cell.getBooleanCellValue();
        	cellValue = cellBoolean? 1f : 0f;
            break;    
        case HSSFCell.CELL_TYPE_ERROR:    
            break;    
        default:    
            break;    
        }    
        return cellValue;    
    }
    
    /**
     * 获取一夜的表头
     * 判断依据，到第一行是出现数字为止的，之前最近的数据
     * @param sheet
     * @return
     */
    public static Map<String, Object> getRowTitle(Sheet sheet)
    {
    	Map<String, Object> resultMap = new HashMap<>();
    	int contentStartIndex = -1;
//    	List<String> titleList = new LinkedList<>();
    	String[] titleList = new String[30];
    	if (sheet == null) return null;
    	for(int i=0; i<sheet.getLastRowNum(); i++)
    	{
    		Row row = sheet.getRow(i);
    		//判断是否为正文
    		if (isStartContent(row))
    		{
    			contentStartIndex = row.getRowNum();
    			break;
    		}
    		
    		for (int j = 0; j < row.getLastCellNum(); j++)
			{
				Cell cell = row.getCell(j);
				if (cell == null)
					continue;
				
				if (Cell.CELL_TYPE_BLANK == cell.getCellType())
					continue;
				
				String value = cell.getStringCellValue();
				if (StringUtils.isEmpty(value))
					continue;
				if (j >= titleList.length)
					titleList = (String[]) extendArrays(titleList, 10, j);
				titleList[j] = value;
//				setIndexObj(titleList, j, value);
			}
    		
    	}
    	resultMap.put("contentStartIndex", contentStartIndex);
    	resultMap.put("titleList", titleList);
    		
    	return resultMap;
    }
    
    /**
     * 判断是否到表的中文了
     * 当第一格是数字就认为开始正文了
     * @param row
     * @return
     */
    public static boolean isStartContent(Row row)
    {
    	Cell cellStart = row.getCell(0);
    	if (cellStart == null || Cell.CELL_TYPE_BLANK == cellStart.getCellType())
    		return false;
    	
    	if (Cell.CELL_TYPE_NUMERIC == cellStart.getCellType())
    		return true;
    	if (Cell.CELL_TYPE_STRING == cellStart.getCellType() && isNum(cellStart.getStringCellValue()))
    		return true;
    	
    	return false;
    }
    
    /**
     * 判断String 是否是数字
     * @param str
     * @return
     */
    public static boolean isNum(String str) {
    	  
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static <E> void setIndexObj(List<E> list,int index,  E element)
    {
    	if (list == null) return;
    	
    	if (list.size() < index)
    	{
    		for (int i = list.size(); i < index; i++)
    			list.set(i, null);
    		list.set(index, element);
    	}else
    	{
    		list.set(index, element);
    	}
    }
    
    public static Object[] extendArrays(Object[] array,int extendNum, int index)
    {
    	if (index > array.length + extendNum)
    		return Arrays.copyOf(array, index + extendNum);
    	return Arrays.copyOf(array, array.length + extendNum);
    }
    
}
