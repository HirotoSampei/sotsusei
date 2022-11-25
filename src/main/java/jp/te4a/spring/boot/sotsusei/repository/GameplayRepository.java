package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;

public interface GameplayRepository extends JpaRepository<GameplayBean, Integer>{
	@Query("SELECT X FROM GameplayBean X ORDER BY X.user_id")
	  List<GameplayBean> findAllOrderByGame_id();
	  
}
