package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, Integer>{
	@Query("SELECT X FROM UserBean X ORDER BY X.user_id")
	List<UserBean> findAllOrderByUser_id();

	//Integer findByUser_id(Integer user_id);

}
