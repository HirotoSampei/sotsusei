package jp.te4a.spring.boot.sotsusei.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportBean {
  @Id
  @GeneratedValue
  private Integer report_id ;
  @Column(nullable = false)
  private Integer reporter_user_id;
  @Column(nullable = false)
  private Integer suspicious_user_id;
  @Column(nullable = false)
  private String report_reason;
  @ManyToOne()
  @JoinColumn(name = "comp_id", referencedColumnName = "comp_id")
  private CompBean compBean;
  @Column(nullable = false)
  private LocalDateTime reported_date;
}
