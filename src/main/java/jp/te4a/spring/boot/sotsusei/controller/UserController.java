package jp.te4a.spring.boot.sotsusei.controller;

import java.util.ArrayList;

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

import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.GameplayService;
import jp.te4a.spring.boot.sotsusei.service.UserService;

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
    @ModelAttribute 
    UserForm setUpForm() {
        return new UserForm();
    }
    @GetMapping
    String list(Model model) {
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
      model.addAttribute("profile",userRepository.findByMail_address(user_pass));
      return "users/profile";
    }
    @PostMapping(path = "edit", params = "form") //編集画面に飛ぶ際の動き
    String editForm(@RequestParam Integer user_id, @RequestParam String password, UserForm form) {
      UserForm userForm = userService.findOne(user_id);
      BeanUtils.copyProperties(userForm,  form);
      return "users/EditUser";
    }
    @PostMapping(path = "edit") //編集した内容を登録する時の動き
    String edit(@RequestParam Integer user_id, @RequestParam String password, @Validated UserForm form, BindingResult result) {
    if(result.hasErrors()) {
    return editForm(user_id, password, form);
    }
    form.setPassword(password);
    userService.update(form);
    return "redirect:/users/profile";
    }
    @PostMapping(path = "delete") //削除
    String delete(@RequestParam Integer user_id) {
      userService.delete(user_id);
      return "redirect:/comp";
    }
}