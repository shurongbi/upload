package com.shurong.upload.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class ReportDaily {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String 日期;
	private String 机构ID;
	private String 机构名称;
	private String 组织机构代码;
	private String 经济类型代码;
	private String 卫生机构类别代码;
	private String 机构分类管理代码;
	private String 行政区划代码;
	private String 乡镇街道代码;
	private String 主办单位代码;
	private String 政府办卫生机构隶属关系代码;
	private String 级别;
	private String 等次;
	private String 是否分支机构;
	private Float 门急诊人次;
	private Float 门诊人次;
	private Float 专家门诊人次;
	private Float 特需门诊人次;
	private Float 夜门诊;
	private Float 急诊人次;
	private Float 儿科急诊;
	private Float 肠道急诊;
	private Float 中暑急诊;
	private Float 发热急诊;
	private Float 住院病区空余床位数;
	private Float 急诊留观室空余床位数;
	private String 医疗机构采取的措施;
	private String 单位负责人;
	private String 统计负责人;
	private String 填表人;
	private String 联系电话;
	private String 报出日期;
	private String 填报说明;
	
	private Date createTime;
	private Boolean isRepeat=false;
	
	public String get日期() {
		return 日期;
	}
	public void set日期(String 日期) {
		this.日期 = 日期;
	}
	public String get机构ID() {
		return 机构ID;
	}
	public void set机构ID(String 机构id) {
		机构ID = 机构id;
	}
	public String get机构名称() {
		return 机构名称;
	}
	public void set机构名称(String 机构名称) {
		this.机构名称 = 机构名称;
	}
	public String get组织机构代码() {
		return 组织机构代码;
	}
	public void set组织机构代码(String 组织机构代码) {
		this.组织机构代码 = 组织机构代码;
	}
	public String get经济类型代码() {
		return 经济类型代码;
	}
	public void set经济类型代码(String 经济类型代码) {
		this.经济类型代码 = 经济类型代码;
	}
	public String get卫生机构类别代码() {
		return 卫生机构类别代码;
	}
	public void set卫生机构类别代码(String 卫生机构类别代码) {
		this.卫生机构类别代码 = 卫生机构类别代码;
	}
	public String get机构分类管理代码() {
		return 机构分类管理代码;
	}
	public void set机构分类管理代码(String 机构分类管理代码) {
		this.机构分类管理代码 = 机构分类管理代码;
	}
	public String get行政区划代码() {
		return 行政区划代码;
	}
	public void set行政区划代码(String 行政区划代码) {
		this.行政区划代码 = 行政区划代码;
	}
	public String get乡镇街道代码() {
		return 乡镇街道代码;
	}
	public void set乡镇街道代码(String 乡镇街道代码) {
		this.乡镇街道代码 = 乡镇街道代码;
	}
	public String get主办单位代码() {
		return 主办单位代码;
	}
	public void set主办单位代码(String 主办单位代码) {
		this.主办单位代码 = 主办单位代码;
	}
	public String get政府办卫生机构隶属关系代码() {
		return 政府办卫生机构隶属关系代码;
	}
	public void set政府办卫生机构隶属关系代码(String 政府办卫生机构隶属关系代码) {
		this.政府办卫生机构隶属关系代码 = 政府办卫生机构隶属关系代码;
	}
	public String get级别() {
		return 级别;
	}
	public void set级别(String 级别) {
		this.级别 = 级别;
	}
	public String get等次() {
		return 等次;
	}
	public void set等次(String 等次) {
		this.等次 = 等次;
	}
	public String get是否分支机构() {
		return 是否分支机构;
	}
	public void set是否分支机构(String 是否分支机构) {
		this.是否分支机构 = 是否分支机构;
	}
	public Float get门急诊人次() {
		return 门急诊人次;
	}
	public void set门急诊人次(Float 门急诊人次) {
		this.门急诊人次 = 门急诊人次;
	}
	public Float get门诊人次() {
		return 门诊人次;
	}
	public void set门诊人次(Float 门诊人次) {
		this.门诊人次 = 门诊人次;
	}
	public Float get专家门诊人次() {
		return 专家门诊人次;
	}
	public void set专家门诊人次(Float 专家门诊人次) {
		this.专家门诊人次 = 专家门诊人次;
	}
	public Float get特需门诊人次() {
		return 特需门诊人次;
	}
	public void set特需门诊人次(Float 特需门诊人次) {
		this.特需门诊人次 = 特需门诊人次;
	}
	public Float get夜门诊() {
		return 夜门诊;
	}
	public void set夜门诊(Float 夜门诊) {
		this.夜门诊 = 夜门诊;
	}
	public Float get急诊人次() {
		return 急诊人次;
	}
	public void set急诊人次(Float 急诊人次) {
		this.急诊人次 = 急诊人次;
	}
	public Float get儿科急诊() {
		return 儿科急诊;
	}
	public void set儿科急诊(Float 儿科急诊) {
		this.儿科急诊 = 儿科急诊;
	}
	public Float get肠道急诊() {
		return 肠道急诊;
	}
	public void set肠道急诊(Float 肠道急诊) {
		this.肠道急诊 = 肠道急诊;
	}
	public Float get中暑急诊() {
		return 中暑急诊;
	}
	public void set中暑急诊(Float 中暑急诊) {
		this.中暑急诊 = 中暑急诊;
	}
	public Float get发热急诊() {
		return 发热急诊;
	}
	public void set发热急诊(Float 发热急诊) {
		this.发热急诊 = 发热急诊;
	}
	public Float get住院病区空余床位数() {
		return 住院病区空余床位数;
	}
	public void set住院病区空余床位数(Float 住院病区空余床位数) {
		this.住院病区空余床位数 = 住院病区空余床位数;
	}
	public Float get急诊留观室空余床位数() {
		return 急诊留观室空余床位数;
	}
	public void set急诊留观室空余床位数(Float 急诊留观室空余床位数) {
		this.急诊留观室空余床位数 = 急诊留观室空余床位数;
	}
	public String get医疗机构采取的措施() {
		return 医疗机构采取的措施;
	}
	public void set医疗机构采取的措施(String 医疗机构采取的措施) {
		this.医疗机构采取的措施 = 医疗机构采取的措施;
	}
	public String get单位负责人() {
		return 单位负责人;
	}
	public void set单位负责人(String 单位负责人) {
		this.单位负责人 = 单位负责人;
	}
	public String get统计负责人() {
		return 统计负责人;
	}
	public void set统计负责人(String 统计负责人) {
		this.统计负责人 = 统计负责人;
	}
	public String get填表人() {
		return 填表人;
	}
	public void set填表人(String 填表人) {
		this.填表人 = 填表人;
	}
	public String get联系电话() {
		return 联系电话;
	}
	public void set联系电话(String 联系电话) {
		this.联系电话 = 联系电话;
	}
	public String get报出日期() {
		return 报出日期;
	}
	public void set报出日期(String 报出日期) {
		this.报出日期 = 报出日期;
	}
	public String get填报说明() {
		return 填报说明;
	}
	public void set填报说明(String 填报说明) {
		this.填报说明 = 填报说明;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Boolean getIsRepeat() {
		return isRepeat;
	}
	public void setIsRepeat(Boolean isRepeat) {
		this.isRepeat = isRepeat;
	}
	
}
