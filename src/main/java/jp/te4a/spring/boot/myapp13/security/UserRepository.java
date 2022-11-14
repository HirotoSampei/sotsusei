package jp.te4a.spring.boot.myapp13.security;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.te4a.spring.boot.myapp13.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, String>{
}
