package jp.te4a.spring.boot.sotsusei.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import jp.te4a.spring.boot.sotsusei.form.CompForm;
import jp.te4a.spring.boot.sotsusei.repository.GameRepository;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;
@Service
public class CompService {
  @Autowired
  CompRepository compRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GameRepository gameRepository;

  public CompForm create(CompForm compForm) {
	  CompBean compBean = new CompBean();
	  BeanUtils.copyProperties(compForm, compBean);
	  compRepository.save(compBean);
	  return compForm;
	}

  public CompForm update(CompForm compForm) {
	  CompBean compBean = new CompBean();
	  BeanUtils.copyProperties(compForm, compBean);
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