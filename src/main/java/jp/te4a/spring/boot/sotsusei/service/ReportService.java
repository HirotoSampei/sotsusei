package jp.te4a.spring.boot.sotsusei.service;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.ReportBean;
import jp.te4a.spring.boot.sotsusei.form.ReportForm;
import jp.te4a.spring.boot.sotsusei.repository.CompRepository;
import jp.te4a.spring.boot.sotsusei.repository.ReportRepository;

@Service
public class ReportService {
  @Autowired
  CompRepository compRepository;
  @Autowired
  ReportRepository reportRepository;
  public ReportForm report(Integer user_id, Integer rpuser_id, Integer comp_id, String report_reason){

    ReportBean reportBean = new ReportBean();
    ReportForm reportform = new ReportForm();
    reportform.setReporter_user_id(user_id);
    reportform.setSuspicious_user_id(rpuser_id);
    reportform.setReport_reason(report_reason);
    reportform.setCompBean(compRepository.findByComp_id(comp_id));
    reportform.setReported_date(LocalDateTime.now());
    BeanUtils.copyProperties(reportform, reportBean);
    reportRepository.save(reportBean);

    return reportform;
  }
}
