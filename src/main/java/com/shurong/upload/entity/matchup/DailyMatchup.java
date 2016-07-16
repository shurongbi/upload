package com.shurong.upload.entity.matchup;

public enum DailyMatchup {
	ZJMZRC("其中：专家门诊人次", "专家门诊人次"),
	ZSJZ("中暑", "中暑急诊"),
	ZBDWDM("设置/主办单位代码", "主办单位代码"),
	EKJZ("其中：儿科", "儿科急诊"),
	CDJZ("肠道", "肠道急诊"),
	FRJZ("发热", "发热急诊");
	
	private String excelName;
	
	private String columName;

	private DailyMatchup(String excelName, String columName) {
		this.setExcelName(excelName);
		this.setColumName(columName);
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	public String getColumName() {
		return columName;
	}

	public void setColumName(String columName) {
		this.columName = columName;
	}
	
	public static DailyMatchup queryByExcelName(String excelName)
	{
		for (DailyMatchup dailyMatchup : DailyMatchup.values())
		{
			if (dailyMatchup.getExcelName().equals(excelName))
				return dailyMatchup;
		}
		return null;
	}
	
}
