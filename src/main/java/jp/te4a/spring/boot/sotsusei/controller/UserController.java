package jp.te4a.spring.boot.sotsusei.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.te4a.spring.boot.sotsusei.bean.AuthenticationBean;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.CompPartBean;
import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.UserEditForm;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.AuthenticationRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompPartRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.GameplayService;
import jp.te4a.spring.boot.sotsusei.service.ImageService;
import jp.te4a.spring.boot.sotsusei.service.UserService;

@Component
@Controller
@RequestMapping("users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    GameplayService gameplayService;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GameplayRepository gameplayRepository;
    @Autowired
    CompRepository compRepository;
    @Autowired
    CompPartRepository compPartRepository;
    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
	  ImageService imageService;
    private final MailSender mailSender;

    public UserController(MailSender mailSender) { 
        this.mailSender = mailSender;
    }
    @ModelAttribute 
    UserForm setUpForm() {
        return new UserForm();
    }
    @GetMapping
    String list(Model model, HttpServletRequest httpServletRequest) { //新規登録画面遷移
      imageService.getImage(model);
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      return "users/CreateUser";
    }
    @PostMapping(path="create")
      String create(@Validated UserForm form, BindingResult result, Model model, String[] game_id, HttpServletRequest httpServletRequest) {
        if(result.hasErrors() || game_id == null) {
          List<String> errorList = new ArrayList<String>();
          for (ObjectError error : result.getAllErrors()) {
            errorList.add(error.getDefaultMessage());
          }
          if(form.getPassword() != "" && form.getPassword().length() < 8){
            errorList.add("パスワードは8文字以上で入力してください。");
          }
          if(game_id == null){
            errorList.add("プレイ中のゲームを選択してください。");
          }
          model.addAttribute("validationError", errorList);
          return list(model, httpServletRequest);
        }
        if(form.getPassword().length() < 8){
          List<String> errorList = new ArrayList<String>();
          errorList.add("パスワードは8文字以上で入力してください。");
          model.addAttribute("validationError", errorList);
          return list(model, httpServletRequest);
        }
        userService.create(form, game_id);
        return "redirect:/login";
    }
    @GetMapping(path = "profile") //プロフィール画面遷移
    String profile_list(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
      String user_pass = httpServletRequest.getRemoteUser();
      UserBean userBean = userRepository.findByMail_address(user_pass);
      userBean.setNote(userBean.getNote().replace(",", " "));
      List<GameBean> game_List = new ArrayList<GameBean>();
      List<GameplayBean> gameplay_idList = gameplayRepository.findAllByGame_id(userBean.getUser_id());
        for (int i = 0; i < gameplay_idList.size(); i++){
            game_List.add(gameRepository.getById(gameplay_idList.get(i).getGame_id()));
        }
      imageService.getImage(model);
      model.addAttribute("user_name", userBean.getUser_name());
      model.addAttribute("game_List",game_List);
      model.addAttribute("profile",userBean);
      return "users/Userprofile";
    }
    @PostMapping(path = "edit", params = "form") //編集画面遷移
    String editForm(UserEditForm form, Model model, HttpServletRequest httpServletRequest) {
      String user_pass = httpServletRequest.getRemoteUser();
      UserBean userBean = userRepository.findByMail_address(user_pass);
      UserForm userForm = userService.findOne(userBean.getUser_id());
      BeanUtils.copyProperties(userForm,  form);
      imageService.getImage(model);
      model.addAttribute("user_name", userBean.getUser_name());
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      userBean.setNote(userBean.getNote().replace(",", "\r\n"));
      model.addAttribute("edit",userBean);
      return "users/Edituser2";
    }
    @PostMapping(path = "edit") //編集内容登録機能
    String edit(@Validated UserEditForm form, BindingResult result, String[] game_id ,Model model, HttpServletRequest httpServletRequest) {
      if(result.hasErrors() || game_id == null) {
        List<String> errorList = new ArrayList<String>();
        for (ObjectError error : result.getAllErrors()) {
          errorList.add(error.getDefaultMessage());
        }
        if(game_id == null){
          errorList.add("プレイ中のゲームを選択してください。");
        }
        model.addAttribute("validationError", errorList);
        return editForm(form, model, httpServletRequest);
      }
      String user_pass = httpServletRequest.getRemoteUser();
      UserBean userBean = userRepository.findByMail_address(user_pass);
      Integer user_id = userBean.getUser_id();
      gameplayRepository.deleteByuser_id(user_id);
      userService.update(userBean, form, game_id);
      return "redirect:/users/profile";
    }
    @PostMapping(path = "delete") //削除
    String delete(HttpServletRequest httpServletRequest) {
      String user_pass = httpServletRequest.getRemoteUser();
      UserBean userBean = userRepository.findByMail_address(user_pass);
      Integer user_id = userBean.getUser_id();
      userService.delete(user_id);
      gameplayRepository.deleteByuser_id(user_id);
      compPartRepository.deleteByuser_id(user_id);
      return "redirect:/login";
    }
    @GetMapping(path = "Password")
    String password(Model model){
      imageService.getImage(model);
      return "users/Password";
    }
    @PostMapping(path = "authentication")//新しいパスワード
    String authentication(@RequestParam String mail_address, Model model){
      List<String> errorList = new ArrayList<String>();
      UserBean userBean = userRepository.findByMail_address(mail_address);
      if(userBean == null){
        errorList.add("一致するメールアドレスがありません。");
        model.addAttribute("validationError", errorList);
        return password(model);
      }
      if(userBean.is_banned()){
        errorList.add("そのメールアドレスは現在使用することは出来ません。");
        model.addAttribute("validationError", errorList);
        return password(model);
      }

      if(authenticationRepository.findByuser_id(userBean.getUser_id()) != null){
        authenticationRepository.deleteById(userBean.getUser_id());
      }
      
      Random random = new Random();
      Integer authentication_pass = Integer.valueOf(String.format("%08d", random.nextInt(99999999)));
      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setFrom("onlinetaikai605@gmail.com"); // 送信元メールアドレス
      msg.setTo(mail_address); // 送信先メールアドレス
      //        msg.setCc(); //Cc用
      //        msg.setBcc(); //Bcc用
      msg.setSubject("認証コード送信"); // タイトル               
      msg.setText("パスワード変更要請がありました。\r\n認証コードは" + authentication_pass + "です。\r\n認証画面で入力してください。"); //本文

      try {
          mailSender.send(msg);
      } catch (MailException e) {
          e.printStackTrace();
      }
      imageService.getImage(model);
      AuthenticationBean authenticationBean = new AuthenticationBean();
      authenticationBean.setUser_id(userBean.getUser_id());
      authenticationBean.setAuthentication_pass(authentication_pass);
      authenticationRepository.save(authenticationBean);
      model.addAttribute("mail_address", mail_address);
      return "users/Authentication";
    }
    @PostMapping(path = "new_password")//認証処理
    String new_password(@RequestParam Integer input_pass, String mail_address, Model model){
      if(authenticationRepository.findByAuthentication_pass(input_pass) != null){
        imageService.getImage(model);
        model.addAttribute("mail_address", mail_address);
        return "users/NewPassword";
      }
      List<String> errorList = new ArrayList<String>();
      errorList.add("認証コードが違います。");
      model.addAttribute("validationError", errorList);
      imageService.getImage(model);
      return "users/Authentication";
    }
    @PostMapping(path = "updatepass")//パスワード更新処理
    String updatepass(@RequestParam String password, String mail_address, Model model){
      UserBean userBean = userRepository.findByMail_address(mail_address);
      Integer user_id = userBean.getUser_id();
      if(user_id == null || password == null){
        return password(model);
      }
      password = password.substring(0, password.length()-1);
      UserForm form = userService.findOne(user_id);
      userService.updatepass(form, password, user_id);
      authenticationRepository.deleteById(user_id);
      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setFrom("onlinetaikai605@gmail.com"); // 送信元メールアドレス
      msg.setTo(form.getMail_address()); // 送信先メールアドレス
      //        msg.setCc(); //Cc用
      //        msg.setBcc(); //Bcc用
      msg.setSubject("パスワード変更"); // タイトル               
      msg.setText("パスワードが正しく変更されました。"); //本文

      try {
          mailSender.send(msg);
      } catch (MailException e) {
          e.printStackTrace();
      }
      return "redirect:/login";
    }
    @GetMapping(path = "User_Password")
    String User_password(Model model, HttpServletRequest httpServletRequest){
      String user_pass = httpServletRequest.getRemoteUser();
      UserBean userBean = userRepository.findByMail_address(user_pass);
      imageService.getImage(model);
      model.addAttribute("user_name", userBean.getUser_name());
      return "users/UpdatePassword";
    }
    @PostMapping(path = "up_password")//パスワード変更
    String up_password(@RequestParam String mail_address, String password, Model model, HttpServletRequest httpServletRequest, Pbkdf2PasswordEncoder passwordEncoder){
      String user_pass = httpServletRequest.getRemoteUser();
      UserBean userBean = userRepository.findByMail_address(user_pass);
      password = password.substring(0, password.length()-1);
      String pass_word = userBean.getPassword();

      if(mail_address.equals(user_pass) && passwordEncoder.matches(password,pass_word)){
        imageService.getImage(model);
        model.addAttribute("user_id", userBean.getUser_id());
        model.addAttribute("user_name", userBean.getUser_name());
        return "users/NewPassword";
      }
      List<String> errorList = new ArrayList<String>();
      errorList.add("メールアドレス又はパスワードが違います。");
      model.addAttribute("validationError", errorList);
      return User_password(model, httpServletRequest);
    }
    @GetMapping(path = "bifurcation")
    String bifurcation(Model model,ModelMap modelMap ,HttpServletRequest httpServletRequest){
      String user_pass = httpServletRequest.getRemoteUser();
      UserBean userBean = userRepository.findByMail_address(user_pass);
      if(userBean == null){
        return "redirect:/login";
      }
      return profile_list(model, modelMap, httpServletRequest);
    }

    @Scheduled(cron="0 30 * * * *",zone="Asia/Tokyo")
    public void compday_check(){//開催日当日に参加者にメール送信
      System.out.println("mail start");
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime check = (now).plusDays(1);
      LocalDateTime check2 = (now).plusHours(23);
      List<CompBean> comp = compRepository.findOnlyStart(now,check,check2);
      for(int i = 0; i < comp.size(); i++){
        List<CompPartBean> comp_part = compPartRepository.findByComp_id(comp.get(i).getComp_id());
        System.out.println(comp_part.size());
        for(int j = 0; j < comp_part.size(); j++){
          String comp_name = compRepository.findComp_nameByComp_id(comp.get(i).getComp_id());
          SimpleMailMessage msg = new SimpleMailMessage();
          System.out.println(userRepository.findMail_address(comp_part.get(j).getUser_id()));
          msg.setFrom("onlinetaikai605@gmail.com"); // 送信元メールアドレス
          msg.setTo(userRepository.findMail_address(comp_part.get(j).getUser_id())); // 送信先メールアドレス
      //        msg.setCc(); //Cc用
      //        msg.setBcc(); //Bcc用
          msg.setSubject("大会開始前通知"); // タイトル               
          msg.setText("あなたが参加している大会の開始まで24時間を切りました。\r\n忘れずに参加しましょう。\r\n大会名："+ comp_name); //本文
          mailSender.send(msg);
        }
      }
    }
}