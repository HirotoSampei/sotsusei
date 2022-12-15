package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.CompBean;

public interface CompRepository extends JpaRepository<CompBean, Integer>{
	@Query("SELECT X FROM CompBean X ORDER BY X.comp_id")
	  List<CompBean> findAllOrderByComp_id();

	@Query("SELECT X.comp_id FROM CompBean X")
	  List<Integer> findAllByComp_id();
	  
	@Query("SELECT X FROM CompBean X WHERE X.host_user_id = ?1 ORDER BY X.comp_id")
	  CompBean findByHost_user_id(Integer host_user_id);

	@Query("SELECT X.comp_id FROM CompBean X WHERE X.host_user_id = ?1")
	CompBean findByComp_idToHost_user_id(Integer host_user_id);
	 
	@Query("SELECT X FROM CompBean X WHERE X.comp_id = ?1") 
	  CompBean findByComp_id(Integer comp_id);

	@Query(value="SELECT * FROM competitions X LEFT JOIN games Y ON Y.game_id = X.game_id WHERE Y.game_id = ?1",nativeQuery=true)
	  List<CompBean> findByGame_idLike(Integer game_id); //game_idの参照の仕方がわからない　JoinColumn

	@Query("SELECT X FROM CompBean X WHERE X.comp_name LIKE %:comp_name% ORDER BY X.comp_id")
	  List<CompBean> findByComp_nameLike(String comp_name);
}
