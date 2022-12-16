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
import org.springframework.web.servlet.ModelAndView;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.CompsearchBean;
import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.form.UserForm;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompPartRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.GameplayRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import jp.te4a.spring.boot.sotsusei.repository.ReportRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserSearchRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompSearchRepository;
import jp.te4a.spring.boot.sotsusei.service.CompService;
import jp.te4a.spring.boot.sotsusei.service.UserService;
import jp.te4a.spring.boot.sotsusei.service.AdminService;
@Controller
@RequestMapping("admin")
public class AdminController {
  @Autowired
  UserService userService;
  
  @Autowired
  CompService compService;

  @Autowired
  AdminService adminService;

  @Autowired
  CompRepository compRepository;

  @Autowired
  CompPartRepository compPartRepository;

  @Autowired
  ReportRepository reportRepository;

  @Autowired
  CompSearchRepository compSearchRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GameplayRepository gameplayRepository;

  @Autowired
  UserSearchRepository userSearchRepository;

  @Autowired
  GameRepository gameRepository;

  @GetMapping //大会作成画面
  String admin_home(Model model) {
    return "admin/AdminHome";
  }
  @GetMapping("/userlist") //ホーム画面
  String user_list(Model model) {
    model.addAttribute("userList", userService.findAll());
    return "admin/UserSearch";
  }
  @PostMapping(path="searchuser", params = "form")
  String user_search(@RequestParam String username_searching, Model model){
    model.addAttribute("userList", userSearchRepository.findByUser_nameLike(username_searching));
    return "admin/Users";
  }
  @GetMapping("/complist") //大会作成画面
  String create_complist(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      return "admin/complist-sample";
  }
  @PostMapping(path="searchcomp", params = "form")
  String comp_search(@RequestParam Integer game_id, Model model){
    model.addAttribute("compList", compSearchRepository.findByGame_idLike(game_id));
    return "admin/CompSearch-sample";
  }
  @PostMapping(path="searchcomp-un", params = "form")
  String comp_search_ss(@RequestParam String username_ss, Model model){
    List<Integer> user_id_ss = new ArrayList<Integer>();
    user_id_ss = userSearchRepository.findIdByUser_nameLike(username_ss);
    List<CompBean> comp_s = new ArrayList<CompBean>();
    for(int i = 0; i < user_id_ss.size(); i++){
      comp_s.add(compRepository.findBeanByHost_user_id(user_id_ss.get(i)));
      
    }
    model.addAttribute("compList", comp_s);
    return "admin/comp-search-sample";
  }//検索した要素を含むユーザーのuser_idを所得、それがhost_user_idに含まれる大会をとってきたい
  @PostMapping(path="userdetail")
  String user_detail(@RequestParam Integer user_id, Model model, ModelMap modelMap){
    List<GameBean> game_List = new ArrayList<GameBean>();
    List<GameplayBean> gameplay_idList = gameplayRepository.findAllByGame_id(user_id);
      for (int i = 0; i < gameplay_idList.size(); i++){
          game_List.add(gameRepository.getById(gameplay_idList.get(i).getGame_id()));
      }
    model.addAttribute("game_List",game_List);
    model.addAttribute("userDetail", userSearchRepository.findByUser_idLike(user_id));
    return "admin/UserProfileForAdmin";
  }
  @PostMapping(path="compdetail")
  String comp_detail(@RequestParam Integer comp_id, Model model){
    model.addAttribute("compDetail", compRepository.findByComp_id(comp_id));
    return "admin/OverviewForAdmin";
  }
  @GetMapping("/reportlist") //通報一覧
  String create_reportlist(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
      model.addAttribute("reportList", reportRepository.findAllOrderByReport_id());
      return "admin/ReportedUsers";
  }
  @PostMapping(path="reportdetail")
  String report_detail(@RequestParam Integer report_id,Integer reporter_user_id,Integer suspicious_user_id, Model model){
    model.addAttribute("reuserDetail", userSearchRepository.findByUser_idLike(reporter_user_id));
    model.addAttribute("suuserDetail", userSearchRepository.findByUser_idLike(suspicious_user_id));
    model.addAttribute("reportDetail", reportRepository.findByReport_id(report_id));
    return "admin/reporting_details";
  }
  @PostMapping(path="compdelete")
  String comp_delete(@RequestParam Integer comp_id){
    compService.delete(comp_id);
    reportRepository.deleteByComp_id(comp_id);
    compPartRepository.deleteByuser_id(comp_id);
    return "redirect:/admin/complist";
  }
}