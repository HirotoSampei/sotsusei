package jp.te4a.spring.boot.sotsusei.service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.bean.GameplayPrimaryKey;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.CompsearchBean;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.form.CompsearchForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserSearchRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompSearchRepository;
import lombok.ToString;
@Service
public class AdminService { //現在は直接AdminControllerからRepositoryを参照しているため、使用していない。
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserSearchRepository userSearchRepository;
    @Autowired
    CompSearchRepository compSearchRepository;
    @Autowired
    GameplayRepository gameplayRepository;
    @Autowired
    CompRepository compRepository;  
    @Autowired
    GameRepository gameRepository;

    public List<UserForm> findByUser_name(String user_name) {
      List<UserBean> userSearch = userSearchRepository.findByUser_nameLike(user_name);
      List<UserForm> formList = new ArrayList<UserForm>();
      for(UserBean userBean: userSearch) {
        UserForm userForm = new UserForm();
        BeanUtils.copyProperties(userBean, userForm);
        formList.add(userForm);
      }
      return formList;
    }

    public List<CompsearchForm> findByGame_id(Integer game_id) {
        List<CompsearchBean> compSearch = compSearchRepository.findByGame_idLike(game_id);
        List<CompsearchForm> formList = new ArrayList<CompsearchForm>();
        for(CompsearchBean compsearchBean: compSearch) {
          CompsearchForm compsearchForm = new CompsearchForm();
          BeanUtils.copyProperties(compsearchBean, compsearchForm);
          formList.add(compsearchForm);
        }
        return formList;
      }
}