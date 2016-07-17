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

@Service
public class ReportDailyService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportDailyService.class);
	
//	@Autowired
//	private ReportDailyRepository dailyRepository;
	
	@Value("${upload.monthorder}")
	private String monthorder;
	@Value("${upload.monthdatename}")
	private String monthdatename;
	@Value("${upload.monthdateindex}")
	private int monthdateindex;
	
	@Value("${upload.dailyorder}")
	private String dailyorder;
	@Value("${upload.dailydatename}")
	private String dailydatename;
	@Value("${upload.dailydateindex}")
	private int dailydateindex;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
//	/**
//	 * 处理日报上传
//	 * @param wb
//	 * @return
//	 * @throws IllegalAccessException 
//	 * @throws InstantiationException 
//	 * @throws SecurityException 
//	 * @throws NoSuchMethodException 
//	 * @throws InvocationTargetException 
//	 * @throws IllegalArgumentException 
//	 */
//	public String doDailyReport(Workbook wb) throws InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException
//	{
//		Date  currentTime = new Date();
//		List<ReportDaily> reportDailyList = new ArrayList<>();
//		//遍历sheet
//		for (int i = 0;i<wb.getNumberOfSheets();i++){
//			Sheet sheet = wb.getSheetAt(i);
//			if (sheet==null) continue;
//			
//			//获取数据的标题
//			Map<String, Object> titleMap = ExcelUtils.getRowTitle(sheet);
//			if (titleMap == null || (int)titleMap.get("contentStartIndex") < 0)
//				continue;
//			
////			List<String> titleList = (List<String>) titleMap.get("titleList");
//			String[] titleList = (String[]) titleMap.get("titleList");
//			int contentStartIndex = (int) titleMap.get("contentStartIndex");
//			
//			for (int k= 0 ; k < titleList.length; k++)
//			{
//				DailyMatchup matchup = DailyMatchup.queryByExcelName(titleList[k]);
//				if (matchup != null)
//					titleList[k] = matchup.getColumName();
//				logger.info(titleList[k]);
//			}
//			
//			for (int j = contentStartIndex; j <= sheet.getLastRowNum(); j++){
////				Object obj = clazz.newInstance();
//				ReportDaily daily = new ReportDaily();
//				
//				Row row = sheet.getRow(j);
//				for(int k=0; k< row.getLastCellNum(); k++)
//				{
//					Cell cell = row.getCell(k);
//					if (cell == null) continue; //当为空时
//					String title = titleList[cell.getColumnIndex()];
//					if (StringUtils.isEmpty(title)) continue;//当标题为空时
//					Class<?> clazz = daily.getClass();
//					
//					Method getMethod = null;
//					Class<?> paramClazz = null;
//					Method setMethod= null;
//					try {
//						getMethod = clazz.getMethod("get"+title);
//						paramClazz = getMethod.getReturnType();
//						setMethod = clazz.getMethod("set" + title, paramClazz);
//					} catch (NoSuchMethodException e) {
//						//没有找到次方法，不是所有的数据都需要导入数据库中
//						continue;
//					}
//					
//					if (paramClazz == String.class)
//					{
//						setMethod.invoke(daily, ExcelUtils.getCellStringValue(cell));
//					}
//					else if (paramClazz.getName().equals(BigDecimal.class.getName()))
//					{
//						setMethod.invoke(daily, BigDecimal.valueOf((Double)ExcelUtils.getCellValue(cell)));
//					}
//					else if (paramClazz.getName().equals(Float.class.getName()))
//					{
//						setMethod.invoke(daily, ExcelUtils.getCellFloatValue(cell));
//					}
//				}
//				
//				daily.setCreateTime(currentTime);
//				daily.setIsRepeat(false);
//				
//				//查看是否有重复的记录
//				List<ReportDaily> repetDailies = dailyRepository.findBy日期And机构ID(daily.get日期(), daily.get机构ID());
//				if(!CollectionUtils.isEmpty(repetDailies))
//				{
//					for (ReportDaily repetDaily : repetDailies)
//					{
//						repetDaily.setIsRepeat(true);
//						reportDailyList.add(repetDaily);
//					}
//				}
//				
//				reportDailyList.add(daily);
//			}
//		}
//		dailyRepository.save(reportDailyList);
//		return null;
//	}
	
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
	
	
}
