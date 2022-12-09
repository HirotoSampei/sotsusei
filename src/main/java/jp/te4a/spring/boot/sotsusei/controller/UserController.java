package jp.te4a.spring.boot.sotsusei.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.GameplayService;
import jp.te4a.spring.boot.sotsusei.service.ImageService;
import jp.te4a.spring.boot.sotsusei.service.UserService;

@Controller
@RequestMapping("users")
public class UserController {
    private static final Model Model = null;
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
	  ImageService imageService;
    @ModelAttribute 
    UserForm setUpForm() {
        return new UserForm();
    }
    @GetMapping
    String list(Model model) { //新規登録画面に飛ぶ際の動き
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      return "users/CreateUser";
    }
    @PostMapping(path="create")
      String create(@Validated UserForm form, BindingResult result, Model model, String[] game_id) {
        if(result.hasErrors()) {
          return list(model);
        }
        userService.create(form, game_id);
        return "redirect:/login";
    }
    @GetMapping(path = "profile") //プロフィール画面に飛ぶ際の動き
    String profile_list(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
      String user_pass = httpServletRequest.getRemoteUser();
      UserBean userBean = userRepository.findByMail_address(user_pass);
      List<GameBean> game_List = new ArrayList<GameBean>();
      List<GameplayBean> gameplay_idList = gameplayRepository.findAllByGame_id(userBean.getUser_id());
        for (int i = 0; i < gameplay_idList.size(); i++){
            game_List.add(gameRepository.getById(gameplay_idList.get(i).getGame_id()));
        }
        imageService.getlogoImage(model);
        imageService.geticonImage(model);
      model.addAttribute("game_List",game_List);
      model.addAttribute("profile",userBean);
      return "users/Userprofile";
    }
    @PostMapping(path = "edit", params = "form") //編集画面に飛ぶ際の動き
    String editForm(@RequestParam Integer user_id,/*@RequestParam List<GameBean> game_List,*/ UserForm form, Model model) {
      UserForm userForm = userService.findOne(user_id);
      UserBean userBean = userRepository.getById(user_id);
      BeanUtils.copyProperties(userForm,  form);
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      model.addAttribute("edit",userBean);
      return "users/Edituser2";
    }
    @PostMapping(path = "edit") //編集した内容を登録する時の動き
    String edit(@RequestParam Integer user_id,/*@RequestParam List<GameBean> game_List,*/ @Validated UserForm form, BindingResult result, String[] game_id) {
      if(result.hasErrors()) {
      return editForm(user_id,/*game_List,*/ form, Model);
      }
      UserBean userBean = userRepository.getById(user_id);
      form.setPassword(userBean.getPassword());
      form.setMail_address(userBean.getMail_address());
      gameplayRepository.deleteByuser_id(user_id);
      userService.update(form, game_id);
      return "redirect:/users/profile";
    }
    @PostMapping(path = "delete") //削除
    String delete(@RequestParam Integer user_id) {
      userService.delete(user_id);
      return "redirect:/comp";
    }
}