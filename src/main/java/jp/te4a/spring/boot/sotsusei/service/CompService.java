package jp.te4a.spring.boot.sotsusei.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.CompPartBean;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.form.ParticipatedForm;
import jp.te4a.spring.boot.sotsusei.form.PopuserForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompPartRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
@Service
public class CompService {
  @Autowired
  CompRepository compRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GameRepository gameRepository;

  @Autowired
  CompPartRepository compPartRepository;

  @Autowired
  GameplayRepository gameplayRepository;

  public CompForm create(CompForm compForm, Integer game_id, String user_pass, boolean radio_button) {
	  CompBean compBean = new CompBean();
    GameBean gameBean = new GameBean();
    gameBean.setGame_id(game_id);
    UserBean userBean = userRepository.findByMail_address(user_pass);
	  BeanUtils.copyProperties(compForm, compBean);
    compBean.setHost_user_id(userBean.getUser_id());
    compBean.setGameBean(gameBean);
	  compRepository.save(compBean);
    if(radio_button){
      CompPartBean compPartBean = new CompPartBean();
      compPartBean.setComp_id(compBean.getComp_id());
      compPartBean.setUser_id(userBean.getUser_id());
      compPartBean.setNickname(compBean.getHost_nickname());
      compPartRepository.save(compPartBean);
    }
	  return compForm;
	}

  public CompForm update(CompForm compForm, Integer game_id, Integer user_id) {
	  CompBean compBean = new CompBean();
    GameBean gameBean = new GameBean();
    gameBean.setGame_id(game_id);
	  BeanUtils.copyProperties(compForm, compBean);
    compBean.setHost_user_id(user_id);
    compBean.setGameBean(gameBean);
	  compRepository.save(compBean);
	  return compForm;
	}
  public void delete(Integer id) { 
    compRepository.deleteById(id); 
    compPartRepository.deleteBycomp_id(id);
  }
  public List<ParticipatedForm> findAll() {
    List<CompBean> beanList = compRepository.findAll();
    //List<Integer> comp_idList = compRepository.findAllByComp_id();
    List<ParticipatedForm> formList = new ArrayList<ParticipatedForm>();
    for(CompBean compBean: beanList) { 
      ParticipatedForm participatedForm = new ParticipatedForm();
      BeanUtils.copyProperties(compBean, participatedForm);
      participatedForm.setCount(compPartRepository.countByComp_id(participatedForm.getComp_id()));
      formList.add(participatedForm); 
    }
    
    return formList;
    }
  public CompForm findOne(Integer id) {
    Optional<CompBean> compBean = compRepository.findById(id);
    CompForm compForm = new CompForm();
    BeanUtils.copyProperties(compBean, compForm);
    return compForm;
	  }

    public List<ParticipatedForm> participated(Integer user_id){
      List<Integer> list = compPartRepository.findByComp_idToUser_id(user_id);
      List<ParticipatedForm> formList = new ArrayList<ParticipatedForm>();
      for(Integer comp_id: list) { 
        ParticipatedForm participatedForm = new ParticipatedForm();
        CompBean participated = compRepository.findByComp_id(comp_id);
        BeanUtils.copyProperties(participated, participatedForm);
        participatedForm.setNickname(compPartRepository.findByNickname(comp_id, user_id)); 
        formList.add(participatedForm);
      }
      return formList;
    }

   public List<ParticipatedForm> hostoverview(Integer user_id){
    List<ParticipatedForm> formList = new ArrayList<ParticipatedForm>();
    ParticipatedForm participatedForm = new ParticipatedForm();
    CompBean overview = compRepository.findBeanByHost_user_id(user_id);
    //CompBean ovcomp_id = compRepository.findByComp_idToHost_user_id(user_id);
    BeanUtils.copyProperties(overview, participatedForm);
    participatedForm.setCount(compPartRepository.countByComp_id(participatedForm.getComp_id()));
    formList.add(participatedForm); 
    return formList;
   }

   public ParticipatedForm partoverview(Integer comp_id){
    ParticipatedForm participatedForm = new ParticipatedForm();
    CompBean partov =  compRepository.findByComp_id(comp_id);
    BeanUtils.copyProperties(partov, participatedForm);
    participatedForm.setCount(compPartRepository.countByComp_id(comp_id));

    return participatedForm;

   }

   public List<ParticipatedForm> compgamesearch(Integer game_id){
    List<ParticipatedForm> formList = new ArrayList<ParticipatedForm>();
    List<CompBean> compList = compRepository.findByGame_idLike(game_id);
    for(CompBean compBean: compList) { 
      ParticipatedForm participatedForm = new ParticipatedForm();
      BeanUtils.copyProperties(compBean, participatedForm);
      participatedForm.setCount(compPartRepository.countByComp_id(participatedForm.getComp_id()));
      formList.add(participatedForm); 
    }
    return formList;
   }

   public List<ParticipatedForm> compAllgamesearch(Integer user_id){
    List<ParticipatedForm> formList = new ArrayList<ParticipatedForm>();
    List<GameplayBean> gameplay_idList = gameplayRepository.findAllByGame_id(user_id);
    List<CompBean> compList;
    Integer count = 0;
    for (int i = 0; i < gameplay_idList.size(); i++){
      compList = compRepository.findByGame_idLike(gameplay_idList.get(i).getGame_id());
      for(CompBean compBean: compList) { 
        ParticipatedForm participatedForm = new ParticipatedForm();
        BeanUtils.copyProperties(compBean, participatedForm);
        participatedForm.setCount(compPartRepository.countByComp_id(participatedForm.getComp_id()));
        formList.add(participatedForm);
        count++; 
      }
    }
    if(count == 0){
      List<CompBean> beanList = compRepository.findAll();
      for(CompBean compBean: beanList) { 
        ParticipatedForm participatedForm = new ParticipatedForm();
        BeanUtils.copyProperties(compBean, participatedForm);
        participatedForm.setCount(compPartRepository.countByComp_id(participatedForm.getComp_id()));
        formList.add(participatedForm); 
      }
    }
    
    return formList;
   }

   public List<ParticipatedForm> compnamesearch(String comp_name){
    List<ParticipatedForm> formList = new ArrayList<ParticipatedForm>();
    List<CompBean> compList = compRepository.findByComp_nameLike(comp_name);
    for(CompBean compBean: compList) { 
      ParticipatedForm participatedForm = new ParticipatedForm();
      BeanUtils.copyProperties(compBean, participatedForm);
      participatedForm.setCount(compPartRepository.countByComp_id(participatedForm.getComp_id()));
      formList.add(participatedForm); 
    }
    return formList;
  }

  public List<PopuserForm> popuser(Integer comp_id, Integer user_id){
      List<PopuserForm> formList = new ArrayList<PopuserForm>();
      List<Integer> userList = compPartRepository.findByUser_id(comp_id);
    for(Integer uid:userList){
      PopuserForm popuserForm = new PopuserForm();
      UserBean popuser = userRepository.findByUser(uid);
      BeanUtils.copyProperties(popuser, popuserForm);
      popuserForm.setLogin_id(user_id);
      popuserForm.setNickname(compPartRepository.findByNickname(comp_id, uid));
      formList.add(popuserForm);
    }
      return formList;
      
    }
   
}