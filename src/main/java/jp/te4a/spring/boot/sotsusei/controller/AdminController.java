package jp.te4a.spring.boot.sotsusei.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.bean.GameplayBean;
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

  @GetMapping //管理者ホーム画面
  String admin_home(Model model) {
    return "admin/AdminHome";
  }
  @GetMapping("/userlist") //ユーザー検索画面
  String user_list(Model model) {
    model.addAttribute("userList", userService.findAll());
    return "admin/UserSearch";
  }

  @GetMapping("/searchuser")//ユーザー検索結果の表示
  String user_search(@RequestParam String username_searching, Model model){
    if(username_searching!=null){
    List<UserBean> user_l=new ArrayList<UserBean>();
    user_l=userSearchRepository.findByUser_nameLike(username_searching);
      model.addAttribute("userList", user_l);
    }else{
      model.addAttribute("userList",userRepository.findAllOrderByUser_id());
    }
    return "admin/Users";
  }
  
  @GetMapping("/complist") //大会検索画面
  String create_complist(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
      model.addAttribute("gameList", gameRepository.findAllOrderByGame_id());
      return "admin/admin-search-comp";
  }
  @GetMapping("/searchcomp")//ゲームでの大会検索の結果表示
  String comp_search(@RequestParam Integer game_id, Model model){
    model.addAttribute("compList", compSearchRepository.findByGame_idLike(game_id));
    return "admin/ReportedComp";
  }
  @GetMapping("/searchcomp-un")//主催者ユーザーでの大会検索の結果表示
  String comp_search_ss(@RequestParam String username_ss, Model model){
    List<Integer> user_id_ss = userSearchRepository.findIdByUser_nameLike(username_ss);
    List<CompBean> comp_s = new ArrayList<CompBean>();
    for(int i = 0; i < user_id_ss.size(); i++){
      CompBean check=compRepository.findBeanByHost_user_id(user_id_ss.get(i));
      if(check!=null){
        comp_s.add(check);
      }
    }
    model.addAttribute("compList", comp_s);
    return "admin/ReportedComp";
  }
  @PostMapping(path="userdetail")//ユーザーの詳細表示
  String user_detail(@RequestParam Integer user_id, Model model){
    List<GameBean> game_List = new ArrayList<GameBean>();
    List<GameplayBean> gameplay_idList = gameplayRepository.findAllByGame_id(user_id);
      for (int i = 0; i < gameplay_idList.size(); i++){
          game_List.add(gameRepository.getById(gameplay_idList.get(i).getGame_id()));
      }
    model.addAttribute("game_List",game_List);
    model.addAttribute("userDetail", userSearchRepository.findByUser_idLike(user_id));
    return "admin/UserProfileForAdmin";
  }
  @GetMapping("/userdetail")//ユーザーの詳細画面での再読み込み時
  String userdetail_get(){
    return "redirect:/admin/userlist";
  }

  @PostMapping(path="compdetail")//大会の詳細の表示
  String comp_detail(@RequestParam Integer comp_id, Model model, HttpServletRequest httpServletRequest){
    String user_pass = httpServletRequest.getRemoteUser();
    UserBean userBean = userRepository.findByMail_address(user_pass);
    model.addAttribute("compDetail", compRepository.findByComp_id(comp_id));
    model.addAttribute("commentList",compService.publiccomment(compRepository.findComp_id(userBean.getUser_id())));
    return "admin/OverviewForAdmin";
  }
  @GetMapping("/reportlist") //通報一覧
  String create_reportlist(Model model, ModelMap modelMap, HttpServletRequest httpServletRequest) {
      model.addAttribute("reportList", reportRepository.findAllOrderByReport_id());
      return "admin/ReportedUsers";
  }
  @PostMapping(path="reportdetail")//通報の詳細の表示
  String report_detail(@RequestParam Integer report_id,Integer reporter_user_id,Integer suspicious_user_id, Model model){
    model.addAttribute("reuserDetail", userSearchRepository.findByUser_idLike(reporter_user_id));
    model.addAttribute("suuserDetail", userSearchRepository.findByUser_idLike(suspicious_user_id));
    model.addAttribute("reportDetail", reportRepository.findByReport_id(report_id));
    return "admin/ReportingDetails";
  }
  @PostMapping(path="compdelete")//大会削除処理
  String comp_delete(@RequestParam Integer comp_id){
    compService.delete(comp_id);
    reportRepository.deleteByComp_id(comp_id);
    compPartRepository.deleteByuser_id(comp_id);
    return "redirect:/admin/complist";
  }
  @PostMapping(path="reportdelete")//通報削除処理
  String report_delete(@RequestParam Integer report_id){
    reportRepository.deleteByReport_id(report_id);
    return "redirect:/admin/reportlist";
  }
  @PostMapping(path="ban_from_report")//通報詳細からのBAN処理
  String ban_r(@RequestParam Integer suspicious_user_id, Model model){
    userRepository.updateByUser_id(suspicious_user_id);
    compPartRepository.deleteByuser_id(suspicious_user_id);
    return "redirect:/admin/reportlist";
  }
  @PostMapping(path="ban_from_user")//ユーザー詳細からのBAN処理
  String ban_u(@RequestParam Integer user_id, Model model){
    userRepository.updateByUser_id(user_id);
    compPartRepository.deleteByuser_id(user_id);
    return "redirect:/admin/userlist";
  }
}