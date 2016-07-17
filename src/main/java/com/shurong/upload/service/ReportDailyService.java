package com.shurong.upload.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PropertiesUtils propertiesUtils;
	
	/**
	 * 处理数据上传
	 * @param wb
	 * @param type
	 * @return
	 * @throws ParseException 
	 */
	public int doReportUpload(Workbook wb, String type) throws ParseException{
		String order = propertiesUtils.getPropertiesValue("${upload."+type+".order}");
		String dateindex = propertiesUtils.getPropertiesValue("${upload."+type+".exceldateindex}");
		int dateIndexNum = Integer.parseInt(dateindex);
		String datename = propertiesUtils.getPropertiesValue("${upload."+type+".columndatename}");
		String tablename = propertiesUtils.getPropertiesValue("${upload."+type+".tablename}");
		String dateformat = propertiesUtils.getPropertiesValue("${upload."+type+".dateformat}");
		
		String[] dateformatArray = dateformat.split(",");
//		boolean isFormatDate = (dateformatArray[0].equals(dateformatArray[1]))?false:true;
		SimpleDateFormat dateformatold = new SimpleDateFormat(dateformatArray[0]);
		SimpleDateFormat dateformatnew = new SimpleDateFormat(dateformatArray[1]);
		
		String sql = "insert into " + tablename + " values (" + order.replaceAll("\\d+", "?") + ")";
		String deleteSql = "delete from " + tablename + " where " + datename + " in ({params})";
		String[] excelOrder = order.split(",");
		logger.info("导入" + type + "生成的sql：" + sql);
		
		List<Object[]> params = new ArrayList<>();
		Set<String> dateValueAray = new HashSet<>();
		
		for (int i = 0;i<wb.getNumberOfSheets();i++){
			//获取正文开始的行数
			Sheet sheet = wb.getSheetAt(i);
			if (sheet == null) continue;
			int firstRowNum = sheet.getFirstRowNum();
			//当前100行还是没有数据的话默认没有数据不循环了，换到下一个sheet页面
			while (!ExcelUtils.isStartContent(sheet.getRow(firstRowNum)) && firstRowNum < 100) {
				firstRowNum++;
			}
			if (firstRowNum > 100) continue;
					
			for (int j = firstRowNum; j <= sheet.getLastRowNum(); j++)
			{
				Row row = sheet.getRow(j);
				if (row == null) continue;
				
				//获取本行插入的日期
				//当dateIndexNum小于0时不删除重复的记录
				if (dateIndexNum > 0)
				{
					try {
						String dateValue = dateformatnew.format(dateformatold.parse(ExcelUtils.getCellStringValue(row.getCell(dateIndexNum -1 ))));
						dateValueAray.add(dateValue);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Object[] obj = new Object[excelOrder.length];
				for (int k = 0; k < excelOrder.length; k++)
				{
					//java中是从0开始的，数excel位置是从1开始的。故去位置时-1来算
					Object value =  ExcelUtils.getCellValue(row.getCell(Integer.parseInt(excelOrder[k])-1));
					//格式化日期
					if (dateIndexNum == Integer.parseInt(excelOrder[k]))
					{
						String saveDate = dateformatnew.format(dateformatold.parse((String)value));
						obj[k] = saveDate;
					}else
					{
						obj[k] = value;
					}
				}
				params.add(obj);
//				jdbcTemplate.update(sql, obj);
			}
			
		}
		
		int deleteSum = 0;
		if (!CollectionUtils.isEmpty(dateValueAray))
		{
			String paramStr = "";
			for (String key : dateValueAray)
			{
				paramStr += "?,";
			}
			paramStr = paramStr.substring(0,paramStr.length() -1 );
			deleteSql = deleteSql.replace("{params}", paramStr);
			deleteSum = jdbcTemplate.update(deleteSql, dateValueAray.toArray());
			logger.info("导入" +type+"时删除重复数据" +deleteSum+"条");
		}
		
		//TODO 由于协议限制无法一次性插入太多数据故拆开执行.后续可以根据参数来动态选择每次执行的条数
		for (Object[] param : params)
		{
			jdbcTemplate.update(sql, param);
		}
//		jdbcTemplate.batchUpdate(sql, params);
		return params.size();
		
	}
	
}
