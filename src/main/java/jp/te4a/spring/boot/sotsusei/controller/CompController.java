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

import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.CompService;

@Controller
@RequestMapping("comp")
public class CompController {
  @Autowired
  CompService compService;
  @Autowired
  CompRepository compRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GameRepository gameRepository;

  @ModelAttribute 
  CompForm setUpForm() {
    return new CompForm();
  }
  @GetMapping //ホーム画面
  String display_list(Model model) {
    model.addAttribute("comp", compService.findAll());
    return "comp/Display-Sample";
  }

  @GetMapping("/CreateComp") //大会作成画面
  String create_list(Model model) {
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    return "comp/CreateComp";
  }
  @GetMapping("/Overview-Sample") //大会概要画面
  String overview_list(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    /*  
    String user_id = httpServletRequest.getRemoteUser();
    Integer login_user_id = userRepository.findByUser_id(Integer.valueOf(user_id));
    form.setHost_user_id(login_user_id);*/
    model.addAttribute("overview", compRepository.findByHost_user_id(1));
    return "comp/Overview-Sample";
  }
  @PostMapping(path="create") //大会作成処理
  String create(@Validated CompForm form, BindingResult result , Model model, Integer game_id) {
    if(result.hasErrors()) {
      return create_list(model);
    }
    compService.create(form, game_id);
    return "redirect:/comp/Overview-Sample";
  }
  @PostMapping(path = "edit", params = "form") //編集画面に飛ぶ際の動き
  String editForm(@RequestParam Integer comp_id, CompForm form, Model model) {
    CompForm compForm = compService.findOne(comp_id);
    BeanUtils.copyProperties(compForm,  form);
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    return "comp/Edit-Sample";
  }
  @PostMapping(path = "edit") //編集した内容を登録する時の動き
  String edit(@RequestParam Integer comp_id, @Validated CompForm form, BindingResult result, Integer game_id) {
  if(result.hasErrors()) {
  return editForm(comp_id, form, null);
  }
  compService.update(form,game_id);
  return "redirect:/comp/Overview-Sample";
  }


  @PostMapping(path = "delete") //削除
  String delete(@RequestParam Integer comp_id) {
    compService.delete(comp_id);
    return "redirect:/comp";
  }
  
}
