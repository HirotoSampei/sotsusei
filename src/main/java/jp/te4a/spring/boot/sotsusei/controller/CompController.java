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

import antlr.collections.List;
import ch.qos.logback.core.joran.conditional.ElseAction;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.bean.CompPartBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.repository.CompPartRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.CompService;
import jp.te4a.spring.boot.sotsusei.service.ImageService;

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

  @Autowired
  CompPartRepository compPartRepository;
  @Autowired
  ImageService imageService;

  @ModelAttribute 
  CompForm setUpForm() {
    return new CompForm();
  }
  @GetMapping //ホーム画面
  String display_list(Model model) {
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("comp", compService.findAll());
    return "home/Home";
  }

  @GetMapping("/CreateComp") //大会作成画面
  String create_list(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    int login_user_id = userBean.getUser_id();
    if(compRepository.findAllOrderByComp_id().size() == 0){
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      return "comp/CreateComp";
    }
    else if(compRepository.findByHost_user_id(login_user_id).size() != 0){
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      return "redirect:/comp/OverViewForHost";
    }
    else{
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      return "comp/CreateComp";
    }
    
  }
  @GetMapping("/OverViewForHost") //大会概要画面
  String overviewforhost(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
     
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("overview", compRepository.findByHost_user_id(userBean.getUser_id()));
    return "comp/OverViewForHost";
  }
  @PostMapping(path="Overview")//参加前大会概要画面
  String overview(Model model, @RequestParam Integer comp_id, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    if(compPartRepository.findByUser_id(comp_id).contains(userBean.getUser_id())){
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("comppart", compPartRepository.findByComp_id(comp_id));
      model.addAttribute("comp", compRepository.findByComp_id(comp_id));
      return "comp/OverviewForParticipants";
    }
    else{
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("participant_overview", compRepository.findByComp_id(comp_id));
    return "comp/Overview";
    } 
  }

  @PostMapping(path="create") //大会作成処理
  String create(@Validated CompForm form, BindingResult result , Model model, Integer game_id, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    if(result.hasErrors()) {
      return create_list(model, modelMap, httpServletRequest);
    }
    String user_pass = httpServletRequest.getRemoteUser();
    compService.create(form, game_id, user_pass);
    return "redirect:/comp/OverViewForHost";
  }

  @PostMapping(path="entry")
  String entry(@RequestParam Integer comp_id, Model model, String nickname, ModelMap modelMap, HttpServletRequest httpServletRequest){
    CompPartBean compPartBean = new CompPartBean();
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    compPartBean.setComp_id(comp_id);
    compPartBean.setUser_id(userBean.getUser_id());
    compPartBean.setNickname(nickname);
    compPartRepository.save(compPartBean);
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("comppart", compPartRepository.findByComp_id(comp_id));
    model.addAttribute("comp", compRepository.findByComp_id(comp_id));
    //model.addAttribute("user", userRepository.findByUser_id(compPartRepository.findByComp_id(comp_id).getUser_id()));
    return "comp/OverviewForParticipants";
  }

  @PostMapping(path = "edit", params = "form") //編集画面に飛ぶ際の動き
  String editForm(@RequestParam Integer comp_id, CompForm form, Model model) {
    //CompForm compForm = compService.findOne(comp_id);
    //BeanUtils.copyProperties(compForm,  form);
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("editlist",compRepository.findByComp_id(comp_id));
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    return "comp/EditComp";
  }
  @PostMapping(path = "edit") //編集した内容を登録する時の動き
  String edit(@RequestParam Integer comp_id, @Validated CompForm form, BindingResult result, Integer game_id) {
  if(result.hasErrors()) {
  return editForm(comp_id, form, null);
  }
  compService.update(form,game_id);
  return "redirect:/comp/OverViewForHost";
  }


  @PostMapping(path = "delete") //削除
  String delete(@RequestParam Integer comp_id) {
    compService.delete(comp_id);
    return "redirect:/comp";
  }

  @GetMapping("/cancel")
  String cancel(ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    compPartRepository.deleteByuser_id(userBean.getUser_id());

    return "redirect:/comp";
  }
  
}
