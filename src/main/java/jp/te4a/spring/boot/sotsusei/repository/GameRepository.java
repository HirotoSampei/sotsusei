package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.GameBean;

public interface GameRepository extends JpaRepository<GameBean, Integer>{
	@Query("SELECT X FROM GameBean X ORDER BY X.game_id")
	  List<GameBean> findAllOrderByGame_id();
	  
	@Query("SELECT X FROM GameBean X WHERE X.game_id = ?1 ORDER BY X.game_id")
	GameBean findByGame_id(Integer game_id);
}
