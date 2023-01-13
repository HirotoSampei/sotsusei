package jp.te4a.spring.boot.sotsusei.controller;


import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.bean.PrivateCommentBean;
import jp.te4a.spring.boot.sotsusei.bean.PublicCommentBean;
import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.CompPartBean;
import jp.te4a.spring.boot.sotsusei.form.PrivateCommentForm;
import jp.te4a.spring.boot.sotsusei.form.PublicCommentForm;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.form.PopuserForm;
import jp.te4a.spring.boot.sotsusei.repository.PrivateCommentRepository;
import jp.te4a.spring.boot.sotsusei.repository.PublicCommentRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompPartRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompSearchRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.ReportRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.service.CompService;
import jp.te4a.spring.boot.sotsusei.service.ImageService;
import jp.te4a.spring.boot.sotsusei.service.ReportService;
import jp.te4a.spring.boot.sotsusei.validate.CompValidate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
  CompValidate compValidate;
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

  @Autowired
  PrivateCommentRepository privateCommentRepository;

  @Autowired
  PublicCommentRepository publicCommentRepository;

  @ModelAttribute 
  CompForm setUpForm() {
    return new CompForm();
  }
  @GetMapping //ホーム画面
  String display_list(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    //開催終了から一か月経った大会を削除
    LocalDateTime check = (LocalDateTime.now()).minusMonths(1);
    List<CompBean> comp_c = compRepository.findAllOrderByComp_id();
    for(int i = 0; i < comp_c.size(); i++){
      int comp_id_check = comp_c.get(i).getComp_id();
      if((compRepository.findEnd_dateByComp_id(comp_id_check)).isBefore(check)){
        compService.delete(comp_id_check);
        System.out.println("delete comp | comp_id="+comp_id_check);
      }
    }
    System.out.println("competitions_end_date checked.");

    imageService.getImage(model);
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);

    //BANの検問
    Boolean check_ban = userRepository.findIs_bannedByUser_id(userBean.getUser_id());
    if(check_ban){
      model.addAttribute("banned", "ban");
      return "login";
    }else{
      model.addAttribute("comp", compService.compAllgamesearch(userBean.getUser_id()));
      model.addAttribute("participated", compService.participated(userBean.getUser_id()));
      model.addAttribute("user_name", userBean.getUser_name());
      return "home/Home";
    }
  }

  @GetMapping("/CreateComp") //大会作成画面
  String create_list(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    int login_user_id = userBean.getUser_id();
    if(compRepository.findAllOrderByComp_id().size() == 0){
      imageService.getImage(model);
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      return "comp/CreateComp";
    }
    else if(compRepository.findBeanByHost_user_id(login_user_id) != null){
      imageService.getImage(model);
      return "redirect:/comp/OverViewForHost";
    }
    else{
      imageService.getImage(model);
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      return "comp/CreateComp";
    }
    
  }
  @GetMapping("/OverViewForHost") //大会概要画面
  String overviewforhost(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
     
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    imageService.getImage(model);
    model.addAttribute("overview", compService.hostoverview(userBean.getUser_id()));
    model.addAttribute("commentList",compService.publiccomment(compRepository.findComp_id(userBean.getUser_id())));
    model.addAttribute("comp_id", compRepository.findComp_id(userBean.getUser_id()));
    return "comp/OverViewForHost";
  }
  @PostMapping(path="Overview")
  String overview(Model model, @RequestParam Integer comp_id, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    if(compPartRepository.findByUser_id(comp_id).contains(userBean.getUser_id())){
      imageService.getImage(model);
      model.addAttribute("comp", compService.partoverview(comp_id));
      model.addAttribute("message", "True");
      model.addAttribute("user", compService.popuser(comp_id, userBean.getUser_id()));
      model.addAttribute("commentList",compService.privatecomment(comp_id));
      model.addAttribute("comp_id", comp_id);
      return "comp/OverviewForParticipants";//参加者専用画面
    }
    else{
      imageService.getImage(model);
      model.addAttribute("participant_overview", compService.partoverview(comp_id));
      model.addAttribute("commentList",compService.publiccomment(comp_id));
      model.addAttribute("comp_id", comp_id);
      return "comp/Overview";//参加前大会概要画面
    } 
  }

  @PostMapping(path="create") //大会作成処理
  String create(@RequestParam boolean radio_button, @Validated CompForm form, BindingResult result, Model model, Integer game_id, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    if(result.hasErrors()) {
      compValidate.compval(form, result, model, modelMap, httpServletRequest);
      return create_list(model, modelMap, httpServletRequest);
    }
    else if(form.getEnd_date().isBefore(form.getStart_date())){
      compValidate.compend_dataval(model);
      return create_list(model, modelMap, httpServletRequest);
    }
    else if(form.getDeadline().isAfter(form.getStart_date())){
      compValidate.compdeadlineval(model);
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
        imageService.getImage(model);
        model.addAttribute("participant_overview", compService.partoverview(comp_id));
        model.addAttribute("errorMessage", "既に参加している大会と日程が被っています。");
        model.addAttribute("commentList",compService.publiccomment(comp_id));
        model.addAttribute("comp_id", comp_id);
        return "comp/Overview";//参加前大会概要画面
      }
    }
    if(compRepository.findByComp_id(comp_id).getLimit_of_participants() == compPartRepository.countByComp_id(comp_id)){
      imageService.getImage(model);
      model.addAttribute("participant_overview", compService.partoverview(comp_id));
      model.addAttribute("limitMessage", "参加人数が上限に達しています。");
      model.addAttribute("commentList",compService.publiccomment(comp_id));
      model.addAttribute("comp_id", comp_id);
      return "comp/Overview";//参加前大会概要画面
    }
    else{
    CompPartBean compPartBean = new CompPartBean();
    compPartBean.setComp_id(comp_id);
    compPartBean.setUser_id(userBean.getUser_id());
    compPartBean.setNickname(nickname);
    compPartRepository.save(compPartBean);
    imageService.getImage(model);
    model.addAttribute("comp", compService.partoverview(comp_id));
    model.addAttribute("message", "True");
    model.addAttribute("user", compService.popuser(comp_id, userBean.getUser_id()));
    model.addAttribute("commentList",compService.privatecomment(comp_id));
    model.addAttribute("comp_id", comp_id);
    return "comp/OverviewForParticipants";//参加者専用画面
    }
  }

  @PostMapping(path = "edit", params = "form") //編集画面に飛ぶ際の動き
  String editForm(@RequestParam Integer comp_id, CompForm form, Model model) {
    //CompForm compForm = compService.findOne(comp_id);
    //BeanUtils.copyProperties(compForm,  form);
    imageService.getImage(model);
    model.addAttribute("editlist",compRepository.findByComp_id(comp_id));
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    return "comp/EditComp";
  }
  @PostMapping(path = "edit") //編集した内容を登録する時の動き
  String edit(@RequestParam Integer comp_id, @Validated CompForm form, BindingResult result, Integer game_id, ModelMap modelMap, HttpServletRequest httpServletRequest,Model model) {
  if(result.hasErrors()) {
    compValidate.compval(form, result, model, modelMap, httpServletRequest);
    return editForm(comp_id, form, model);
  }
  else if(form.getEnd_date().isBefore(form.getStart_date())){
    compValidate.compend_dataval(model);
    return editForm(comp_id, form, model);
  }
  else if(form.getDeadline().isAfter(form.getStart_date())){
    compValidate.compdeadlineval(model);
    return editForm(comp_id, form, model);
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
    imageService.getImage(model);
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    model.addAttribute("participated", compService.participated(userBean.getUser_id()));
    model.addAttribute("comp", compService.compgamesearch(game_id));
    return "home/Home";

  }

  @PostMapping(path="searchcomp", params = "form") //大会検索
  String comp_namesearch(@RequestParam String comp_name, Model model, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    imageService.getImage(model);
    model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
    model.addAttribute("participated", compService.participated(userBean.getUser_id()));
    model.addAttribute("comp", compService.compnamesearch(comp_name));
    return "home/Home";

  }

  @PostMapping("/OverviewForParticipants") //主催者ページから参加者専用画面に遷移
  String overviewForParticipants(@RequestParam Integer comp_id, Model model, ModelMap modelMap, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    if(compPartRepository.findByUser_id(comp_id).contains(userBean.getUser_id())){
      model.addAttribute("message", "True");
    }
    imageService.getImage(model);
    model.addAttribute("comppart", compPartRepository.findByComp_id(comp_id));
    model.addAttribute("comp", compService.partoverview(comp_id));
    model.addAttribute("user", compService.popuser(comp_id, userBean.getUser_id()));
    model.addAttribute("commentList",compService.privatecomment(comp_id));
    model.addAttribute("comp_id", comp_id);
    return "comp/OverviewForParticipants";
  }

  @PostMapping(path = "report") //通報画面遷移
  String report(@RequestParam Integer user_id, Integer comp_id, Model model) {
    imageService.getImage(model);
    model.addAttribute("user", user_id);
    model.addAttribute("comp", comp_id);
    return "comp/Report";
  }

  @PostMapping(path = "Reporting") //通報機能
  String Reporting(@RequestParam Integer rpuser_id, Integer comp_id, String remarks, Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    if(remarks == ""){
      String errormessage = "通報理由を入力してください";
      model.addAttribute("errormessage", errormessage);
      if(compPartRepository.findByUser_id(comp_id).contains(userBean.getUser_id())){
        return report(rpuser_id, comp_id, model);
      }
      return comp_report(rpuser_id, comp_id, model);
    }
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
      imageService.getImage(model);
      model.addAttribute("comppart", compPartRepository.findByComp_id(comp_id));
      model.addAttribute("comp", compService.partoverview(comp_id));
      model.addAttribute("user", compService.popuser(comp_id, user_id));
      model.addAttribute("commentList",compService.privatecomment(comp_id));
      model.addAttribute("comp_id", comp_id);
      return "comp/OverviewForParticipants";//参加者専用画面
    }
    imageService.getImage(model);
      model.addAttribute("participant_overview", compService.partoverview(comp_id));
      model.addAttribute("commentList",compService.publiccomment(comp_id));
      model.addAttribute("comp_id", comp_id);
      return "comp/Overview";//参加前大会概要画面
  }

  @PostMapping(path = "comp_report") //通報画面遷移
  String comp_report(@RequestParam Integer host_user_id, Integer comp_id, Model model) {
    imageService.getImage(model);
    model.addAttribute("user", host_user_id);
    model.addAttribute("comp", comp_id);
    return "comp/Report";
  }

  @PostMapping("/privatecheck")
  @ResponseBody
  String private_comp_comment(@RequestParam String comp_id, String comment, ModelMap modelMap, HttpServletRequest httpServletRequest){
    int comp_Id = Integer.parseInt(comp_id);
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    PrivateCommentBean commentBean = new PrivateCommentBean();
    commentBean.setComp_id(comp_Id);
    commentBean.setUser_id(userBean.getUser_id());
    commentBean.setCommented_date(LocalDateTime.now());
    commentBean.setComment(comment);
    privateCommentRepository.save(commentBean);

    return privategetJson(compService.privatecomment(comp_Id));
  }
  
  @PostMapping("/privatereload")
  @ResponseBody
  String private_reload_comment(@RequestParam String comp_id){
    int comp_Id = Integer.parseInt(comp_id);
    return privategetJson(compService.privatecomment(comp_Id));
  }

    /* 
     * 引数のUserDataオブジェクトをJSON文字列に変換する
     * @param userDataList UserDataオブジェクトのリスト
     * @return 変換後JSON文字列
    */ 
private String privategetJson(List<PrivateCommentForm> list){
  String retVal = null;
  ObjectMapper objectMapper = new ObjectMapper();
    try{
        retVal = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
          System.err.println(e);
        }
        return retVal;
    }

    @PostMapping("/publiccheck")
  @ResponseBody
  String public_comp_comment(@RequestParam String comp_id, String comment, ModelMap modelMap, HttpServletRequest httpServletRequest){
    int comp_Id = Integer.parseInt(comp_id);
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    PublicCommentBean commentBean = new PublicCommentBean();
    commentBean.setComp_id(comp_Id);
    commentBean.setUser_id(userBean.getUser_id());
    commentBean.setCommented_date(LocalDateTime.now());
    commentBean.setComment(comment);
    publicCommentRepository.save(commentBean);

    return publicgetJson(compService.publiccomment(comp_Id));
  }
  
  @PostMapping("/publicreload")
  @ResponseBody
  String public_reload_comment(@RequestParam String comp_id){
    int comp_Id = Integer.parseInt(comp_id);
    return publicgetJson(compService.publiccomment(comp_Id));
  }

    /* 
     * 引数のUserDataオブジェクトをJSON文字列に変換する
     * @param userDataList UserDataオブジェクトのリスト
     * @return 変換後JSON文字列
    */ 
private String publicgetJson(List<PublicCommentForm> list){
  String retVal = null;
  ObjectMapper objectMapper = new ObjectMapper();
    try{
        retVal = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
          System.err.println(e);
        }
        return retVal;
    }

}
