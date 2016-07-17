package com.shurong.upload.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.shurong.upload.util.ExcelUtils;
import com.shurong.upload.util.PropertiesUtils;

@Service
public class ReportDailyService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportDailyService.class);
	
//	@Autowired
//	private ReportDailyRepository dailyRepository;
	
	@Value("${upload.month.order}")
	private String monthorder;
	@Value("${upload.month.datename}")
	private String monthdatename;
	@Value("${upload.month.dateindex}")
	private int monthdateindex;
	
	@Value("${upload.daily.order}")
	private String dailyorder;
	@Value("${upload.daily.datename}")
	private String dailydatename;
	@Value("${upload.daily.dateindex}")
	private int dailydateindex;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	private PropertiesUtils propertiesUtils;
	
	
	/**
	 * 根据excel位置处理日报上传
	 * @param wb
	 * @return
	 */
	public int doDailyReportBySize(Workbook wb){
		
		String sql = "insert into REPORT_DAILY values ("+dailyorder.replaceAll("\\d+", "?")+")";
		String deleteSql = "delete from REPORT_DAILY where " + dailydatename + " = ?";
		String[] excelOrder = dailyorder.split(",");
		logger.info("导入日报生成的sql：" + sql);
		
		List<Object[]> params = new ArrayList<>();
		Set<String> dateValueAray = new HashSet<>();
		for (int i = 0;i<wb.getNumberOfSheets();i++){
			//获取正文开始的行数
			Sheet sheet = wb.getSheetAt(i);
			if (sheet == null) continue;
			int firstRowNum = sheet.getFirstRowNum();
			while (!ExcelUtils.isStartContent(sheet.getRow(firstRowNum))) {
				firstRowNum++;
			}
			
			for (int j = firstRowNum; j <= sheet.getLastRowNum(); j++)
			{
				Row row = sheet.getRow(j);
				if (row == null) continue;
				
				//获取本行插入的日期
				String dateValue = row.getCell(dailydateindex -1 ).getStringCellValue();
				dateValueAray.add(dateValue);
				
				Object[] obj = new Object[excelOrder.length];
				for (int k = 0; k < excelOrder.length; k++)
				{
					//java中是从0开始的，数excel位置是从1开始的。故去位置时-1来算
					obj[k] = ExcelUtils.getCellValue(row.getCell(Integer.parseInt(excelOrder[k])-1));
				}
				params.add(obj);
//				jdbcTemplate.update(sql, obj);
			}
			
		}
		int deleteSum = 0;
		if (!CollectionUtils.isEmpty(dateValueAray))
		{
			 deleteSum = jdbcTemplate.update(deleteSql, dateValueAray.toArray());
			logger.info("导入日报时删除重复数据" +deleteSum+"条");
		}
		jdbcTemplate.batchUpdate(sql, params);
		return params.size();
		
	}
	
	/**
	 * 处理月报上传
	 * @param wb
	 * @return
	 */
	public int doMonthReport(Workbook wb)
	{
		String sql = "insert into REPORT_MONTH values ("+monthorder.replaceAll("\\d+", "?")+")";
		String deleteSql = "delete from REPORT_MONTH where " + monthdatename + " = ?";
		String[] excelOrder = monthorder.split(",");
		logger.info("导入月报生成的sql：" + sql);
		
		List<Object[]> params = new ArrayList<>();
		Set<String> dateValueAray = new HashSet<>();
		
		for (int i = 0;i<wb.getNumberOfSheets();i++){
			//获取正文开始的行数
			Sheet sheet = wb.getSheetAt(i);
			if (sheet == null) continue;
			int firstRowNum = sheet.getFirstRowNum();
			while (!ExcelUtils.isStartContent(sheet.getRow(firstRowNum))) {
				firstRowNum++;
			}
					
			for (int j = firstRowNum; j <= sheet.getLastRowNum(); j++)
			{
				Row row = sheet.getRow(j);
				if (row == null) continue;
				
				//获取本行插入的日期
				String dateValue = row.getCell(monthdateindex -1 ).getStringCellValue();
				dateValueAray.add(dateValue);
				
				Object[] obj = new Object[excelOrder.length];
				for (int k = 0; k < excelOrder.length; k++)
				{
					//java中是从0开始的，数excel位置是从1开始的。故去位置时-1来算
					obj[k] = ExcelUtils.getCellValue(row.getCell(Integer.parseInt(excelOrder[k])-1));
				}
				params.add(obj);
//				jdbcTemplate.update(sql, obj);
			}
			
		}
		
		int deleteSum = 0;
		if (!CollectionUtils.isEmpty(dateValueAray))
		{
			deleteSum = jdbcTemplate.update(deleteSql, dateValueAray.toArray());
			logger.info("导入月报是删除重复数据" +deleteSum+"条");
		}
		jdbcTemplate.batchUpdate(sql, params);
		return params.size();
	}
	
	/**
	 * 处理数据上传
	 * @param wb
	 * @param type
	 * @return
	 */
	public int doReportUpload(Workbook wb, String type){
		String order = propertiesUtils.getPropertiesValue("${upload."+type+".order}");
		String dateindex = propertiesUtils.getPropertiesValue("${upload."+type+".dateindex}");
		int dateIndexNum = Integer.parseInt(dateindex);
		String datename = propertiesUtils.getPropertiesValue("${upload."+type+".datename}");
		String tablename = propertiesUtils.getPropertiesValue("${upload."+type+".tablename}");
		
		String sql = "insert into " + tablename + " values (" + order.replaceAll("\\d+", "?") + ")";
		String deleteSql = "delete from " + tablename + " where " + datename + " = ?";
		String[] excelOrder = order.split(",");
		logger.info("导入" + type + "生成的sql：" + sql);
		
		List<Object[]> params = new ArrayList<>();
		Set<String> dateValueAray = new HashSet<>();
		
		for (int i = 0;i<wb.getNumberOfSheets();i++){
			//获取正文开始的行数
			Sheet sheet = wb.getSheetAt(i);
			if (sheet == null) continue;
			int firstRowNum = sheet.getFirstRowNum();
			while (!ExcelUtils.isStartContent(sheet.getRow(firstRowNum))) {
				firstRowNum++;
			}
					
			for (int j = firstRowNum; j <= sheet.getLastRowNum(); j++)
			{
				Row row = sheet.getRow(j);
				if (row == null) continue;
				
				//获取本行插入的日期
				//当dateIndexNum小于0时不删除重复的记录
				if (dateIndexNum > 0)
				{
					String dateValue = row.getCell(dateIndexNum -1 ).getStringCellValue();
					dateValueAray.add(dateValue);
				}
				
				Object[] obj = new Object[excelOrder.length];
				for (int k = 0; k < excelOrder.length; k++)
				{
					//java中是从0开始的，数excel位置是从1开始的。故去位置时-1来算
					obj[k] = ExcelUtils.getCellValue(row.getCell(Integer.parseInt(excelOrder[k])-1));
				}
				params.add(obj);
//				jdbcTemplate.update(sql, obj);
			}
			
		}
		
		int deleteSum = 0;
		if (!CollectionUtils.isEmpty(dateValueAray))
		{
			deleteSum = jdbcTemplate.update(deleteSql, dateValueAray.toArray());
			logger.info("导入" +type+"时删除重复数据" +deleteSum+"条");
		}
		jdbcTemplate.batchUpdate(sql, params);
		return params.size();
		
	}
	
	
}
