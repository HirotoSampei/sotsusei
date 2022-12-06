package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.CompBean;

public interface CompSearchRepository extends JpaRepository<CompBean, Integer>,JpaSpecificationExecutor<CompBean>{
	@Query("SELECT X FROM CompBean X WHERE X.game_id = ?1")
	  List<CompBean> findByGame_idLike(Integer game_id);
}
