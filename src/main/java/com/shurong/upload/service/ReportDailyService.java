package com.shurong.upload.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.shurong.upload.controller.UploadController;
import com.shurong.upload.entity.ReportDaily;
import com.shurong.upload.entity.matchup.DailyMatchup;
import com.shurong.upload.repository.ReportDailyRepository;
import com.shurong.upload.util.ExcelUtils;

import scala.math.BigDecimal;

@Service
public class ReportDailyService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportDailyService.class);
	
	@Autowired
	private ReportDailyRepository dailyRepository;
	/**
	 * 处理日报上传
	 * @param wb
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public String doDailyReport(Workbook wb) throws InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		Date  currentTime = new Date();
		List<ReportDaily> reportDailyList = new ArrayList<>();
		//遍历sheet
		for (int i = 0;i<wb.getNumberOfSheets();i++){
			Sheet sheet = wb.getSheetAt(i);
			if (sheet==null) continue;
			
			//获取数据的标题
			Map<String, Object> titleMap = ExcelUtils.getRowTitle(sheet);
			if (titleMap == null || (int)titleMap.get("contentStartIndex") < 0)
				continue;
			
//			List<String> titleList = (List<String>) titleMap.get("titleList");
			String[] titleList = (String[]) titleMap.get("titleList");
			int contentStartIndex = (int) titleMap.get("contentStartIndex");
			
			for (int k= 0 ; k < titleList.length; k++)
			{
				DailyMatchup matchup = DailyMatchup.queryByExcelName(titleList[k]);
				if (matchup != null)
					titleList[k] = matchup.getColumName();
				logger.info(titleList[k]);
			}
			
			for (int j = contentStartIndex; j <= sheet.getLastRowNum(); j++){
//				Object obj = clazz.newInstance();
				ReportDaily daily = new ReportDaily();
				
				Row row = sheet.getRow(j);
				for(int k=0; k< row.getLastCellNum(); k++)
				{
					Cell cell = row.getCell(k);
					if (cell == null) continue; //当为空时
					String title = titleList[cell.getColumnIndex()];
					if (StringUtils.isEmpty(title)) continue;//当标题为空时
					Class<?> clazz = daily.getClass();
					
					Method getMethod = null;
					Class<?> paramClazz = null;
					Method setMethod= null;
					try {
						getMethod = clazz.getMethod("get"+title);
						paramClazz = getMethod.getReturnType();
						setMethod = clazz.getMethod("set" + title, paramClazz);
					} catch (NoSuchMethodException e) {
						//没有找到次方法，不是所有的数据都需要导入数据库中
						continue;
					}
					
					if (paramClazz == String.class)
					{
						setMethod.invoke(daily, ExcelUtils.getCellStringValue(cell));
					}
					else if (paramClazz.getName().equals(BigDecimal.class.getName()))
					{
						setMethod.invoke(daily, BigDecimal.valueOf((Double)ExcelUtils.getCellValue(cell)));
					}
					else if (paramClazz.getName().equals(Float.class.getName()))
					{
						setMethod.invoke(daily, ExcelUtils.getCellFloatValue(cell));
					}
				}
				
				daily.setCreateTime(currentTime);
				daily.setIsRepeat(false);
				
				//查看是否有重复的记录
				List<ReportDaily> repetDailies = dailyRepository.findBy日期And机构ID(daily.get日期(), daily.get机构ID());
				if(!CollectionUtils.isEmpty(repetDailies))
				{
					for (ReportDaily repetDaily : repetDailies)
					{
						repetDaily.setIsRepeat(true);
						reportDailyList.add(repetDaily);
					}
				}
				
				reportDailyList.add(daily);
			}
		}
		dailyRepository.save(reportDailyList);
		return null;
	}
	
}
