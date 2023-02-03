package jp.te4a.spring.boot.sotsusei.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import jp.te4a.spring.boot.sotsusei.bean.AuthenticationBean;

public interface AuthenticationRepository extends JpaRepository<AuthenticationBean, Integer>{
  @Query("SELECT X FROM AuthenticationBean X WHERE X.authentication_pass = ?1")
  AuthenticationBean findByAuthentication_pass(Integer authentication_pass);

  @Query("SELECT X FROM AuthenticationBean X WHERE X.user_id = ?1")
  AuthenticationBean findByuser_id(Integer user_id);
}