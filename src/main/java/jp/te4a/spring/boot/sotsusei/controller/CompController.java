package jp.te4a.spring.boot.sotsusei.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.CompService;

@Controller
@RequestMapping("comp")
public class CompController {
  @Autowired
  CompService compService;
  @Autowired
  UserRepository userRepository;

  @Autowired
  GameRepository gameRepository;

  @ModelAttribute 
  CompForm setUpForm() {
    return new CompForm();
  }
  @GetMapping //ホーム画面
  String list(Model model) {
    model.addAttribute("comp", compService.findAll());
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    return "comp/CreateComp";
  }
  @PostMapping(path="create") //大会作成画面
  String create(@Validated CompForm form, BindingResult result , Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    if(result.hasErrors()) {
      return list(model);
    }
     
    String user_id = httpServletRequest.getRemoteUser();
    /* 
    Integer login_user_id = userRepository.findByUser_id(Integer.valueOf(user_id));
    form.setHost_user_id(login_user_id);*/
    compService.create(form);
    return "redirect:/comp";
  }
  @PostMapping(path = "edit", params = "form") //編集画面に飛ぶ際の動き
  String editForm(@RequestParam Integer id, CompForm form) {
    CompForm compForm = compService.findOne(id);
    BeanUtils.copyProperties(compForm,  form);
    return "comp/edit";
  }
  @PostMapping(path = "edit") //編集した内容を登録する時の動き
  String edit(@RequestParam Integer id, @Validated CompForm form, BindingResult result) {
  if(result.hasErrors()) {
  return editForm(id, form);
  }
  compService.update(form);
  return "redirect:/comp";
  }


  @PostMapping(path = "delete") //削除
  String delete(@RequestParam Integer id) {
    compService.delete(id);
    return "redirect:/comp";
  }
  @PostMapping(path = "edit", params = "goToTop")
  String goToTop() {
    return "redirect:/comp";
  }
}
