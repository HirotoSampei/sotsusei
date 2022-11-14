package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.TournamentBean;

public interface TournamentRepository extends JpaRepository<TournamentBean, Integer>{
	@Query("SELECT X FROM BookBean X ORDER BY X.title")
	  List<TournamentBean> findAllOrderByTitle();
}
