package jp.te4a.spring.boot.sotsusei.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.TournamentBean;
import jp.te4a.spring.boot.sotsusei.form.TournamentForm;
import jp.te4a.spring.boot.sotsusei.repository.TournamentRepository;
@Service
public class TournamentService {
  @Autowired
  TournamentRepository bookRepository;
  public TournamentForm create(TournamentForm bookForm) {
	  TournamentBean bookBean = new TournamentBean();
	  BeanUtils.copyProperties(bookForm, bookBean);
	  bookRepository.save(bookBean);
	  return bookForm;
	}

  public TournamentForm update(TournamentForm bookForm) {
	  TournamentBean bookBean = new TournamentBean();
	  BeanUtils.copyProperties(bookForm, bookBean);
	  bookRepository.save(bookBean);
	  return bookForm;
	}
  public void delete(Integer id) { bookRepository.deleteById(id); }
  public List<TournamentForm> findAll() {
    List<TournamentBean> beanList = bookRepository.findAll();
    List<TournamentForm> formList = new ArrayList<TournamentForm>();
    for(TournamentBean bookBean: beanList) {
      TournamentForm bookForm = new TournamentForm();
      BeanUtils.copyProperties(bookBean, bookForm);
      formList.add(bookForm);
    }
    return formList;
    }
  public TournamentForm findOne(Integer id) {
    Optional<TournamentBean> bookBean = bookRepository.findById(id);
    TournamentForm bookForm = new TournamentForm();
    BeanUtils.copyProperties(bookBean, bookForm);
    return bookForm;
	  }
	}