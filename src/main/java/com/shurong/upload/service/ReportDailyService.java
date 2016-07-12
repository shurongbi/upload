package com.shurong.upload.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.shurong.upload.entity.ReportDaily;
import com.shurong.upload.repository.ReportDailyRepository;
import com.shurong.upload.util.ExcelUtils;

import scala.math.BigDecimal;

@Service
public class ReportDailyService {
	
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
	public String doDailyReport(Workbook wb) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		Date  currentTime = new Date();
		List<ReportDaily> reportDailyList = new ArrayList<>();
		//遍历sheet
		for (int i = 0;i<wb.getNumberOfSheets();i++){
			Sheet sheet = wb.getSheetAt(i);
			if (sheet==null) continue;
			
			//第一排默认为字段名称
			Row rowtitle = sheet.getRow(0);
			for (int j = 1; j < sheet.getLastRowNum(); j++){
//				Object obj = clazz.newInstance();
				ReportDaily daily = new ReportDaily();
				
				Row row = sheet.getRow(j);
				for(int k=0; k< row.getLastCellNum(); k++)
				{
					Cell cell = row.getCell(k);
					if (cell == null) continue; //当为空时
					Cell celltitle = rowtitle.getCell(cell.getColumnIndex());
					String title = celltitle.getStringCellValue().toString();
					if (StringUtils.isEmpty(title)) continue;//当标题为空时
					Class<?> clazz = daily.getClass();
					Method getMethod = clazz.getMethod("get"+title);
					Class<?> paramClazz = getMethod.getReturnType();
					Method setMethod = clazz.getMethod("set" + title, paramClazz);
					
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
