package jp.te4a.spring.boot.sotsusei.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
import lombok.ToString;
@Service
public class CompService {
  @Autowired
  CompRepository compRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GameRepository gameRepository;

  public CompForm create(CompForm compForm, Integer game_id) {
	  CompBean compBean = new CompBean();
    GameBean gameBean = new GameBean();
    gameBean.setGame_id(game_id);
	  BeanUtils.copyProperties(compForm, compBean);
    compBean.setGameBean(gameBean);
	  compRepository.save(compBean);
	  return compForm;
	}

  public CompForm update(CompForm compForm, Integer game_id) {
	  CompBean compBean = new CompBean();
    GameBean gameBean = new GameBean();
    gameBean.setGame_id(game_id);
	  BeanUtils.copyProperties(compForm, compBean);
    compBean.setGameBean(gameBean);
	  compRepository.save(compBean);
	  return compForm;
	}
  public void delete(Integer id) { compRepository.deleteById(id); }
  public List<CompForm> findAll() {
    List<CompBean> beanList = compRepository.findAll();
    List<CompForm> formList = new ArrayList<CompForm>();
    for(CompBean compBean: beanList) {
      CompForm compForm = new CompForm();
      BeanUtils.copyProperties(compBean, compForm);
      String strDate = compForm.getStart_date().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss"));
      DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      compForm.setStart_date(LocalDateTime.parse((strDate), dtf )); 
      formList.add(compForm);
    }
    return formList;
    }
  public CompForm findOne(Integer id) {
    Optional<CompBean> compBean = compRepository.findById(id);
    CompForm compForm = new CompForm();
    BeanUtils.copyProperties(compBean, compForm);
    return compForm;
	  }
	}