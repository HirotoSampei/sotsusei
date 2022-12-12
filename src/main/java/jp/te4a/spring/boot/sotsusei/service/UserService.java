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

  public UserForm update(UserForm userForm, String[] game_id) {
    UserBean userBean = new UserBean();
    GameplayBean gameplayBean = new GameplayBean();
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
}