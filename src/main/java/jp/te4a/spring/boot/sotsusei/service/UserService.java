package jp.te4a.spring.boot.sotsusei.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.UserEditForm;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;
  @Autowired
  GameplayRepository gameplayRepository;

  public UserForm create(UserForm userForm, String[] game_id) {
    userForm.setPassword(new Pbkdf2PasswordEncoder().encode(userForm.getPassword()));
    GameplayBean gameplayBean = new GameplayBean();            
    UserBean userBean = new UserBean();
    BeanUtils.copyProperties(userForm, userBean);

    userRepository.save(userBean);
    UserBean user = userRepository.findByMail_address(userForm.getMail_address());
    for (int i = 0; i < game_id.length; i++){
      gameplayBean.setUser_id(user.getUser_id());
      gameplayBean.setGame_id(Integer.parseInt(game_id[i]));
      gameplayRepository.save(gameplayBean);
    }
    return userForm;
  }

  public List<UserForm> findAll() {
    List<UserBean> beanList = userRepository.findAll();
    List<UserForm> formList = new ArrayList<UserForm>();
    for(UserBean userBean: beanList) {
      UserForm userForm = new UserForm();
      BeanUtils.copyProperties(userBean, userForm);
      formList.add(userForm);
    }
    return formList;
  }

  public UserEditForm update(UserEditForm userEditForm, String[] game_id) {
    UserBean userBean = new UserBean();
    GameplayBean gameplayBean = new GameplayBean();
    BeanUtils.copyProperties(userEditForm, userBean);
    userRepository.save(userBean);
      
    UserBean user = userRepository.findByMail_address(userEditForm.getMail_address());
      for (int i = 0; i < game_id.length; i++){
        gameplayBean.setUser_id(user.getUser_id());
        gameplayBean.setGame_id(Integer.parseInt(game_id[i]));
        gameplayRepository.save(gameplayBean);
      }
    return userEditForm;
  }
  public void delete(Integer id) { userRepository.deleteById(id); }
  public UserForm findOne(Integer id) {
    Optional<UserBean> userBean = userRepository.findById(id);
    UserForm userForm = new UserForm();
    BeanUtils.copyProperties(userBean, userForm);
    return userForm;
  }
  public UserForm update(UserForm userForm) {
    UserBean userBean = new UserBean();
    BeanUtils.copyProperties(userForm, userBean);
    userRepository.save(userBean);
    return userForm;
  }
  public UserForm updatepass(UserForm userForm, String password, Integer user_id) {
    UserBean userBean = new UserBean();
    UserBean bean = userRepository.findByUser(user_id);
    userForm.setUser_id(user_id);
    userForm.setUser_name(bean.getUser_name());
    userForm.setPassword(new Pbkdf2PasswordEncoder().encode(password));
    userForm.setMail_address(bean.getMail_address());
    userForm.setNote(bean.getNote());
    BeanUtils.copyProperties(userForm, userBean);
    userRepository.save(userBean);
    return userForm;
  }
}