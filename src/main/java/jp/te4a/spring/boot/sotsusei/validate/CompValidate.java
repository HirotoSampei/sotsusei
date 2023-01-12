package jp.te4a.spring.boot.sotsusei.validate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import jp.te4a.spring.boot.sotsusei.form.CompForm;

@Service
public class CompValidate {
    
  public Model compval(CompForm form, BindingResult result, Model model, ModelMap modelMap, HttpServletRequest httpServletRequest){
    List<String> errorList = new ArrayList<String>();
    for (ObjectError error : result.getAllErrors()) {
      errorList.add(error.getDefaultMessage());
    }
    if(form.getEnd_date() != null && form.getStart_date() != null && form.getEnd_date().isBefore(form.getStart_date())){
      errorList.add("終了日時は開始日時以降で入力してください");
    }
    if(form.getDeadline() != null && form.getStart_date() != null && form.getDeadline().isAfter(form.getStart_date())){
      errorList.add("締め切り日時は開始日時前で入力してください");
    }
    return model.addAttribute("validationError", errorList);
  }

  public Model compend_dataval(Model model){
    List<String> errorList = new ArrayList<String>();
    errorList.add("終了日時は開始日時以降で入力してください");
    return model.addAttribute("validationError", errorList);
  }

  public Model compdeadlineval(Model model){
    List<String> errorList = new ArrayList<String>();
    errorList.add("締め切り日時は開始日時前で入力してください");
    return model.addAttribute("validationError", errorList);
  }
}
