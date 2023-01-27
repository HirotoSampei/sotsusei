package jp.te4a.spring.boot.sotsusei.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jp.te4a.spring.boot.sotsusei.bean.PopBean;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, Integer>, JpaSpecificationExecutor<UserBean>{
	@Query("SELECT X FROM UserBean X ORDER BY X.user_id")
	List<UserBean> findAllOrderByUser_id();

	@Query("SELECT X FROM UserBean X WHERE X.mail_address = ?1")
    UserBean findByMail_address(String mail_address);
	//Integer findByUser_id(Integer user_id);

	@Query("SELECT X FROM UserBean X WHERE X.user_name = ?1")
	List<UserBean> findByUser_nameLike(String user_name);

	@Query("SELECT X.user_id FROM UserBean X WHERE X.user_name = ?1")
	Integer findByUser_name(String user_name);

	@Query("SELECT X.user_name FROM UserBean X WHERE X.user_id = ?1")
	String findUser_name(Integer user_id);

	@Query("SELECT X FROM UserBean X WHERE X.user_id = ?1")
	List<UserBean> findByUser_id(Integer user_id);

	@Query("SELECT X.user_name FROM UserBean X WHERE X.user_id = ?1")
	String findUser_nameByUser_id(Integer user_id);

	@Query("SELECT X.is_banned FROM UserBean X WHERE X.user_id = ?1")
	Boolean findIs_bannedByUser_id(Integer user_id);

	@Query("SELECT X.user_id FROM UserBean X WHERE X.mail_address = ?1")
    Integer findByIdMail_address(String mail_address);

	@Transactional
	@Modifying
	@Query("UPDATE UserBean X set X.is_banned = true WHERE X.user_id = ?1")
	void updateByUser_id(Integer user_id);
}

