package jp.te4a.spring.boot.sotsusei.controller;



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
import org.springframework.web.servlet.ModelAndView;
import antlr.collections.List;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.CompService;
import jp.te4a.spring.boot.sotsusei.service.UserService;

@Controller
@RequestMapping("admin")
public class AdminController {
  @Autowired
  UserService userService;
  
  @Autowired
  CompService compService;

  @Autowired
  CompRepository compRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GameRepository gameRepository;

  @GetMapping //大会作成画面
  String admin_home(Model model) {
    return "admin/admin-home-sample";
  }
  @GetMapping("/userlist") //ホーム画面
  String user_list(Model model) {
    model.addAttribute("userList", userService.findAll());
    return "admin/userlist-sample";
  }
  @GetMapping("/complist") //ホーム画面
  String comp_list(Model model) {
    model.addAttribute("complist", compService.findAll());
    return "admin/complist-sample";
  }
  @PostMapping(path="search")
  String user_search(@RequestParam String username_searching, Model model){
    model.addAttribute("userList", userService.findByUser_name(username_searching));
    return "admin/username-search-sample";
  }
}