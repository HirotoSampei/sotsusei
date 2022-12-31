package jp.te4a.spring.boot.sotsusei.controller;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import jp.te4a.spring.boot.sotsusei.bean.CompPartBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.repository.CompPartRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompSearchRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.ReportRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.CompService;
import jp.te4a.spring.boot.sotsusei.service.ImageService;
import jp.te4a.spring.boot.sotsusei.service.ReportService;

@Controller
@RequestMapping("comp")
public class CompController {
  @Autowired
  CompService compService;

  @Autowired
  ImageService imageService;

  @Autowired
  ReportService reportService;

  @Autowired
  CompRepository compRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GameRepository gameRepository;

  @Autowired
  CompPartRepository compPartRepository;

  @Autowired
  CompSearchRepository compSearchRepository;

  @Autowired
  ReportRepository reportRepository;

  @ModelAttribute 
  CompForm setUpForm() {
    return new CompForm();
  }
  @GetMapping //ホーム画面
  String display_list(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    model.addAttribute("comp", compService.findAll());
    model.addAttribute("participated", compService.participated(userBean.getUser_id()));
    model.addAttribute("user_name", userBean.getUser_name());
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
    else if(compRepository.findBeanByHost_user_id(login_user_id) != null){
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
    model.addAttribute("overview", compService.hostoverview(userBean.getUser_id()));
    return "comp/OverViewForHost";
  }
  @PostMapping(path="Overview")
  String overview(Model model, @RequestParam Integer comp_id, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    if(compPartRepository.findByUser_id(comp_id).contains(userBean.getUser_id())){
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("comp", compService.partoverview(comp_id));
      model.addAttribute("message", "True");
      model.addAttribute("user", compService.popuser(comp_id, userBean.getUser_id()));
      return "comp/OverviewForParticipants";//参加者専用画面
    }
    else{
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("participant_overview", compService.partoverview(comp_id));
      return "comp/Overview";//参加前大会概要画面
    } 
  }

  @PostMapping(path="create") //大会作成処理
  String create(@Validated CompForm form, @RequestParam boolean radio_button, BindingResult result , Model model, Integer game_id, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    if(result.hasErrors()) {
      return create_list(model, modelMap, httpServletRequest);
    }
    String user_pass = httpServletRequest.getRemoteUser();
    compService.create(form, game_id, user_pass, radio_button);
    return "redirect:/comp/OverViewForHost";
  }

  @PostMapping(path="entry")//大会参加処理
  String entry(@RequestParam Integer comp_id, Model model, String nickname, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    List<Integer> list = compPartRepository.findByComp_idToUser_id(userBean.getUser_id());
    for(Integer id: list) { 
      if(compRepository.findByComp_id(id).getStart_date().isAfter(compRepository.findByComp_id(comp_id).getEnd_date()) || compRepository.findByComp_id(id).getEnd_date().isBefore(compRepository.findByComp_id(comp_id).getStart_date())){
      }
      else{
        imageService.getlogoImage(model);
        imageService.geticonImage(model);
        model.addAttribute("participant_overview", compService.partoverview(comp_id));
        model.addAttribute("errorMessage", "既に参加している大会と日程が被っています。");
        return "comp/Overview";
      }
    }
    if(compRepository.findByComp_id(comp_id).getLimit_of_participants() == compPartRepository.countByComp_id(comp_id)){
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("participant_overview", compService.partoverview(comp_id));
      model.addAttribute("limitMessage", "参加人数が上限に達しています。");
      return "comp/Overview";//参加前大会概要画面
    }
    else{
    CompPartBean compPartBean = new CompPartBean();
    compPartBean.setComp_id(comp_id);
    compPartBean.setUser_id(userBean.getUser_id());
    compPartBean.setNickname(nickname);
    compPartRepository.save(compPartBean);
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("comp", compService.partoverview(comp_id));
    model.addAttribute("message", "True");
    model.addAttribute("user", compService.popuser(comp_id, userBean.getUser_id()));
    return "comp/OverviewForParticipants";
    }
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
  String edit(@RequestParam Integer comp_id, @Validated CompForm form, BindingResult result, Integer game_id, ModelMap modelMap, HttpServletRequest httpServletRequest) {
  if(result.hasErrors()) {
  return editForm(comp_id, form, null);
  }
  String user_pass = httpServletRequest.getRemoteUser();
  UserBean userBean = userRepository.findByMail_address(user_pass);
  compService.update(form,game_id,userBean.getUser_id());
  return "redirect:/comp/OverViewForHost";
  }


  @PostMapping(path = "delete") //削除
  String delete(@RequestParam Integer comp_id) {
    compService.delete(comp_id);
    return "redirect:/comp";
  }

  @PostMapping("/cancel")
  String cancel(@RequestParam Integer comp_id,ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    compPartRepository.deleteByuser(userBean.getUser_id(), comp_id);

    return "redirect:/comp";
  }

  @PostMapping(path="searchgamecomp", params = "form") //大会ゲーム名検索
  String comp_gamesearch(@RequestParam Integer game_id, Model model, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    model.addAttribute("participated", compService.participated(userBean.getUser_id()));
    model.addAttribute("comp", compService.compgamesearch(game_id));
    return "home/Home";

  }

  @PostMapping(path="searchcomp", params = "form") //大会検索
  String comp_namesearch(@RequestParam String comp_name, Model model, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    model.addAttribute("participated", compService.participated(userBean.getUser_id()));
    model.addAttribute("comp", compService.compnamesearch(comp_name));
    return "home/Home";

  }

  @PostMapping("/OverviewForParticipants")
  String overviewForParticipants(@RequestParam Integer comp_id, Model model, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    if(compPartRepository.findByUser_id(comp_id).contains(userBean.getUser_id())){
      model.addAttribute("message", "True");
    }
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("comppart", compPartRepository.findByComp_id(comp_id));
    model.addAttribute("comp", compService.partoverview(comp_id));
    model.addAttribute("user", compService.popuser(comp_id, userBean.getUser_id()));
    return "comp/OverviewForParticipants";
  }

  @PostMapping(path = "report") //通報画面遷移
  String report(@RequestParam Integer user_id, Integer comp_id, Model model) {
    imageService.getlogoImage(model);
    imageService.geticonImage(model);
    model.addAttribute("user", user_id);
    model.addAttribute("comp", comp_id);
    return "comp/Report";
  }

  @PostMapping(path = "Reporting") //通報機能
  String Reporting(@RequestParam Integer rpuser_id, Integer comp_id, String remarks, Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    List<Integer> list = reportRepository.findByreporter_user_id(userBean.getUser_id());
    Integer user_id = userBean.getUser_id();
    if(list.contains(rpuser_id)){
      String report = reportRepository.findBeanByuser_id(user_id, rpuser_id).getReport_reason();
      String point = "・";
      report = report.concat(point);
      remarks = report.concat(remarks);
      reportRepository.deleteByuser_id(user_id, rpuser_id);

    }
    reportService.report(user_id, rpuser_id, comp_id, remarks);
    if(compPartRepository.findByUser_id(comp_id).contains(userBean.getUser_id())){
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("comppart", compPartRepository.findByComp_id(comp_id));
      model.addAttribute("comp", compService.partoverview(comp_id));
      model.addAttribute("user", compService.popuser(comp_id, user_id));
      return "comp/OverviewForParticipants";//参加者専用画面
    }
      imageService.getlogoImage(model);
      imageService.geticonImage(model);
      model.addAttribute("participant_overview", compService.partoverview(comp_id));
      return "comp/Overview";//参加前大会概要画面
  }

}
