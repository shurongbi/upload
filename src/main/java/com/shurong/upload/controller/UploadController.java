package com.shurong.upload.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shurong.upload.service.ReportDailyService;
import com.shurong.upload.util.ExcelUtils;

@Controller
public class UploadController {
	
	@Autowired
	private ReportDailyService reportDailyService;
 
	@ResponseBody
	@RequestMapping(value="/uploadDayDaily")
	public Map<String, Object> uploadDayDaily(@RequestParam("inputdaygly") MultipartFile inputdaygly)
	{
		Map<String, Object> returnMap = new HashMap<>();
		if (inputdaygly != null)
		{
			System.out.println(inputdaygly.getOriginalFilename());
			try {
				reportDailyService.doDailyReport(ExcelUtils.initWorkbook(inputdaygly.getInputStream()));
			} catch (Exception e) {
				returnMap.put("result", "ko");
				returnMap.put("msg", inputdaygly.getOriginalFilename() + "导入失败。" + e.getMessage());
				return returnMap;
			} 
			returnMap.put("result", "ok");
			returnMap.put("msg", inputdaygly.getOriginalFilename() +"导入成功");
		}
		else
		{
			returnMap.put("result", "ko");
			returnMap.put("msg", "文件上传发生异常");
		}
		
		return returnMap;
	}
	
	@RequestMapping(value="/uploadMonthDaily")
	public Map<String, Object> uploadMonthDaily(@RequestParam("inputmonthgly") MultipartFile inputmonthgly)
	{
		Map<String, Object> returnMap = new HashMap<>();
		System.out.println(inputmonthgly.getOriginalFilename());
		return returnMap;
	}
}
