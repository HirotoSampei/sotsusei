package jp.te4a.spring.boot.sotsusei.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserForm create(UserForm userForm) {
          userForm.setPassword(new Pbkdf2PasswordEncoder().encode(userForm.getPassword()));
  
        UserBean userBean = new UserBean();
        BeanUtils.copyProperties(userForm, userBean);

        userRepository.save(userBean);
        return userForm;
    }
}