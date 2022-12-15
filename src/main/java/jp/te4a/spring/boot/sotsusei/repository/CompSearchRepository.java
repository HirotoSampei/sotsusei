package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.CompsearchBean;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
public interface CompSearchRepository extends JpaRepository<CompsearchBean, Integer>,JpaSpecificationExecutor<CompsearchBean>{
	@Query(value="SELECT X.comp_id as comp_id,X.comp_name as comp_name,X.host_user_id as host_user_id"
		+" FROM competitions X LEFT JOIN games Y ON Y.game_id = X.game_id WHERE Y.game_id = ?1",nativeQuery=true)
	  List<CompsearchBean> findByGame_idLike(Integer game_id); //game_idの参照の仕方がわからない　JoinColumn

	@Query(value="SELECT X.comp_id,X.comp_name,X.host_user_id FROM competitions X WHERE X.host_user_id = ?1",nativeQuery=true)
	  List<CompsearchBean> findByUser_idLike(Integer host_user_id);
}
	
