package jp.te4a.spring.boot.sotsusei.security;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.te4a.spring.boot.sotsusei.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, String>{
}
