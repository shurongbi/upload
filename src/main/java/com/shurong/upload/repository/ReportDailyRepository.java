package com.shurong.upload.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shurong.upload.entity.ReportDaily;

@Repository
public interface ReportDailyRepository extends CrudRepository<ReportDaily, Long>{

	List<ReportDaily> findBy日期(String dateTime);
	List<ReportDaily> findBy日期And机构ID(String dateTime,String orgid);
}
