package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.UserBean;

public interface UserSearchRepository extends JpaRepository<UserBean, Integer>, JpaSpecificationExecutor<UserBean>{
	@Query("SELECT X FROM UserBean X WHERE X.user_name = ?1")
	List<UserBean> findByUser_nameLike(String user_name);
	@Query("SELECT X FROM UserBean X WHERE X.user_id = ?1")
	List<UserBean> findByUser_idLike(Integer user_id);
}