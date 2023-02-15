package jp.te4a.spring.boot.sotsusei.service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.te4a.spring.boot.sotsusei.bean.PrivateCommentBean;
import jp.te4a.spring.boot.sotsusei.bean.PublicCommentBean;
import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.CompPartBean;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.PrivateCommentForm;
import jp.te4a.spring.boot.sotsusei.form.PublicCommentForm;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.form.ParticipatedForm;
import jp.te4a.spring.boot.sotsusei.form.PopuserForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
import jp.te4a.spring.boot.sotsusei.repository.NGWordRepository;
import jp.te4a.spring.boot.sotsusei.repository.PrivateCommentRepository;
import jp.te4a.spring.boot.sotsusei.repository.PublicCommentRepository;
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
  @Autowired
  PrivateCommentRepository privateCommentRepository;
  @Autowired
  PublicCommentRepository publicCommentRepository;
  @Autowired
  NGWordRepository ngWordRepository;
  @Autowired
  ImageService imageService;


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

  public CompForm update(CompBean compBean, CompForm compForm, Integer game_id, Integer user_id) {
    GameBean gameBean = new GameBean();
    compForm.setComp_id(compBean.getComp_id());
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
    LocalDateTime now = (LocalDateTime.now()).plusHours(9);
    List<CompBean> compList = compRepository.findByGame_idLike(game_id, now);
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
    LocalDateTime now = (LocalDateTime.now()).plusHours(9);
    for (int i = 0; i < gameplay_idList.size(); i++){
      compList = compRepository.findByGame_idLike(gameplay_idList.get(i).getGame_id(),now);
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
      UserBean popuser = userRepository.findByPopUser(uid);
      BeanUtils.copyProperties(popuser, popuserForm);
      popuserForm.setLogin_id(user_id);
      popuserForm.setNickname(compPartRepository.findByNickname(comp_id, uid));
      popuserForm.setNote(popuserForm.getNote().replace(",", "<br>"));
      formList.add(popuserForm);
    }
      return formList;
      
    }

    public  List<PrivateCommentForm> privatecomment(Integer comp_id){
      List<PrivateCommentForm> formList = new ArrayList<PrivateCommentForm>();
      List<PrivateCommentBean> list = privateCommentRepository.findByComp_id(comp_id);
      for(PrivateCommentBean commentBean: list) { 
        PrivateCommentForm commentForm = new PrivateCommentForm();
        String comment_date = commentBean.getCommented_date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        BeanUtils.copyProperties(commentBean, commentForm);
        commentForm.setNickname(compPartRepository.findByNickname(commentForm.getComp_id(),commentForm.getUser_id()));
        commentForm.setComment_date(comment_date);
        if(commentForm.getNickname() == null){
          commentForm.setNickname("大会から抜けたユーザー");
        }
        formList.add(commentForm);
      }
      return formList;
    }

    public  List<PublicCommentForm> publiccomment(Integer comp_id){
      List<PublicCommentForm> formList = new ArrayList<PublicCommentForm>();
      List<PublicCommentBean> list = publicCommentRepository.findByComp_id(comp_id);
      for(PublicCommentBean commentBean: list) { 
        PublicCommentForm commentForm = new PublicCommentForm();
        String comment_date = commentBean.getCommented_date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        BeanUtils.copyProperties(commentBean, commentForm);
        commentForm.setUser_name(userRepository.findUser_name(commentForm.getUser_id()));
        commentForm.setComment_date(comment_date);
        formList.add(commentForm);
      }
      return formList;
    }

    public void delete_private_comment(Integer comp_id,LocalDateTime comment_date){
      privateCommentRepository.deleteComment(comp_id, comment_date);
    }

    public void delete_public_comment(Integer comp_id,LocalDateTime comment_date){
      publicCommentRepository.deleteComment(comp_id, comment_date);
    }

    public void overviewForParticipants(Model model, UserBean userBean, Integer comp_id){
      imageService.getImage(model);
      model.addAttribute("user_name", userBean.getUser_name());
      model.addAttribute("comppart", compPartRepository.findByComp_id(comp_id));
      model.addAttribute("comp", partoverview(comp_id));
      model.addAttribute("user", popuser(comp_id, userBean.getUser_id()));
      model.addAttribute("commentList",privatecomment(comp_id));
      model.addAttribute("comp_id", comp_id);
      model.addAttribute("user_id",userBean.getUser_id());
      model.addAttribute("NGWordList", ngWordRepository.findNGWords());
    }
   
}