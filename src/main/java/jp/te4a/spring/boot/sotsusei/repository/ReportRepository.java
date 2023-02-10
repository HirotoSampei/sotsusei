package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.ReportBean;

public interface ReportRepository extends JpaRepository<ReportBean, Integer>, JpaSpecificationExecutor<ReportBean>{
	@Query("SELECT X FROM ReportBean X ORDER BY X.report_id")
	List<ReportBean> findAllOrderByReport_id();

	@Query("SELECT X FROM ReportBean X WHERE X.report_id = ?1") 
	  List<ReportBean> findByReport_id(Integer report_id);

	@Query("SELECT X.suspicious_user_id FROM ReportBean X WHERE X.reporter_user_id = ?1") 
	  List<Integer> findByreporter_user_id(Integer reporter_user_id);

	@Query("SELECT X FROM ReportBean X WHERE X.reporter_user_id = ?1 and X.suspicious_user_id = ?2")
	  ReportBean findBeanByuser_id(Integer reporter_user_id, Integer suspicious_user_id);

	@Transactional
    @Modifying
    @Query("DELETE FROM ReportBean X WHERE X.compBean.comp_id = ?1")
    void deleteByComp_id(Integer comp_id);

	@Transactional
    @Modifying
    @Query("DELETE FROM ReportBean X WHERE X.report_id = ?1")
    void deleteByReport_id(Integer report_id);

	@Transactional
    @Modifying
    @Query("DELETE FROM ReportBean X WHERE X.reporter_user_id = ?1 and X.suspicious_user_id = ?2")
    void deleteByuser_id(Integer reporter_user_id, Integer suspicious_user_id);
}
