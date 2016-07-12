package com.shurong.upload.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
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
    
    
}
