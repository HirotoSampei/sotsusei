package jp.te4a.spring.boot.sotsusei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
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
    @ModelAttribute 
    UserForm setUpForm() {
        return new UserForm();
    }
    @GetMapping
    String list(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
        return "users/CreateUser2";
    }
    @PostMapping(path="create")
        String create(@Validated UserForm form, /*GameplayBean gameplayBean,*/ BindingResult result, Model model) {
            if(result.hasErrors()) {
                 return list(model);
            }
            userService.create(form);
            //gameplayService.create(gameplayBean);
            return "redirect:/login";
    }
}