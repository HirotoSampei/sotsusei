package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.ReportBean;

public interface ReportRepository extends JpaRepository<ReportBean, Integer>, JpaSpecificationExecutor<ReportBean>{
	@Query("SELECT X FROM ReportBean X ORDER BY X.report_id")
	List<ReportBean> findAllOrderByReport_id();

	@Query("SELECT X FROM ReportBean X WHERE X.report_id = ?1") 
	  List<ReportBean> findByReport_id(Integer report_id);
}
