package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.CompBean;

public interface CompRepository extends JpaRepository<CompBean, Integer>{
	@Query("SELECT X FROM CompBean X ORDER BY X.comp_id")
	  List<CompBean> findAllOrderByComp_id();
	  
	@Query("SELECT X FROM CompBean X WHERE X.host_user_id = ?1 ORDER BY X.comp_id")
	  List<CompBean> findByHost_user_id(Integer host_user_id);
}
