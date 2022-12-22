package jp.te4a.spring.boot.sotsusei.form;

import java.time.LocalDateTime;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import jp.te4a.spring.boot.sotsusei.bean.CompBean;
import lombok.Data;

@Data
public class ReportForm {

  private Integer report_id ;
  private Integer reporter_user_id;
  private Integer suspicious_user_id;
  private String report_reason;
  @ManyToOne()
  @JoinColumn(name = "comp_id", referencedColumnName = "comp_id")
  private CompBean compBean;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime reported_date;

}
