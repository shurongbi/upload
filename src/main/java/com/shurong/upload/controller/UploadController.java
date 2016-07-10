package com.shurong.upload.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

	@ResponseBody
	@RequestMapping(value="/uploadDayDaily")
	public Map<String, Object> uploadDayDaily(@RequestParam("inputdaygly") MultipartFile inputdaygly)
	{
		Map<String, Object> returnMap = new HashMap<>();
		if (inputdaygly != null)
		{
			System.out.println(inputdaygly.getOriginalFilename());
		}
		returnMap.put("err.code", "上传成功");
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
