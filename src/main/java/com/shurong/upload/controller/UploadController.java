package com.shurong.upload.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shurong.upload.service.ReportDailyService;
import com.shurong.upload.util.ExcelUtils;
import com.shurong.upload.util.PropertiesUtils;

@Controller
public class UploadController {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	private ReportDailyService reportDailyService;
	
//	@ResponseBody
//	@RequestMapping(value="/uploadDayDaily")
//	public Map<String, Object> uploadDayDaily(@RequestParam("inputdaygly") MultipartFile inputdaygly)
//	{
//		Map<String, Object> returnMap = new HashMap<>();
//		if (inputdaygly != null)
//		{
//			logger.info("开始上传文件：" + inputdaygly.getOriginalFilename());
//			System.out.println(inputdaygly.getOriginalFilename());
//			try {
//				reportDailyService.doDailyReportBySize(ExcelUtils.initWorkbook(inputdaygly.getInputStream()));
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error(inputdaygly.getOriginalFilename() + "导入失败。" , e);
//				returnMap.put("result", "ko");
//				returnMap.put("msg", inputdaygly.getOriginalFilename() + "导入失败。" + e.getMessage());
//				return returnMap;
//			} 
//			returnMap.put("result", "ok");
//			returnMap.put("msg", inputdaygly.getOriginalFilename() +"导入成功");
//		}
//		else
//		{
//			returnMap.put("result", "ko");
//			returnMap.put("msg", "文件上传发生异常");
//		}
//		
//		return returnMap;
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="/uploadMonthDaily")
//	public Map<String, Object> uploadMonthDaily(@RequestParam("inputmonthgly") MultipartFile inputmonthgly)
//	{
//		Map<String, Object> returnMap = new HashMap<>();
//
//		if (inputmonthgly != null)
//		{
//			int recordSum = 0;
//			logger.info("开始上传文件：" + inputmonthgly.getOriginalFilename());
//			try {
//				recordSum = reportDailyService.doMonthReport(ExcelUtils.initWorkbook(inputmonthgly.getInputStream()));
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error(inputmonthgly.getOriginalFilename() + "导入失败。" , e);
//				returnMap.put("result", "ko");
//				returnMap.put("msg", inputmonthgly.getOriginalFilename() + "导入失败。" + e.getMessage());
//				return returnMap;
//			}
//			logger.info(inputmonthgly.getOriginalFilename() +"导入成功,共导入数据"+recordSum+"条");
//			returnMap.put("result", "ok");
//			returnMap.put("msg", inputmonthgly.getOriginalFilename() +"导入成功,共导入数据"+recordSum+"条");
//		}
//		else
//		{
//			returnMap.put("result", "ko");
//			returnMap.put("msg", "文件上传发生异常");
//		}
//		return returnMap;
//	}
	
	@ResponseBody
	@RequestMapping(value="/uploadReport")
	public Map<String, Object> uploadReport(@RequestParam("inputgly") MultipartFile inputgly,@RequestParam("type") String type){
		Map<String, Object> returnMap = new HashMap<>();

		if (inputgly != null)
		{
			int recordSum = 0;
			logger.info("开始上传文件：" + inputgly.getOriginalFilename());
			try {
				recordSum = reportDailyService.doReportUpload(ExcelUtils.initWorkbook(inputgly.getInputStream()), type);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(inputgly.getOriginalFilename() + "导入失败。" , e);
				returnMap.put("result", "ko");
				returnMap.put("msg", inputgly.getOriginalFilename() + "导入失败。" + e.getMessage());
				return returnMap;
			}
			logger.info(inputgly.getOriginalFilename() +"导入成功,共导入数据"+recordSum+"条");
			returnMap.put("result", "ok");
			returnMap.put("msg", inputgly.getOriginalFilename() +"导入成功,共导入数据"+recordSum+"条");
		}
		else
		{
			returnMap.put("result", "ko");
			returnMap.put("msg", "文件上传发生异常");
		}
		return returnMap;
	}
	
}
