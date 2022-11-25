package jp.te4a.spring.boot.sotsusei.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;

import javax.persistence.*;
@Entity
@Table(name = "competitions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompBean {
  @Id
  @GeneratedValue
  private Integer comp_id ;
  @Column(nullable = false)
  private String comp_name;
  @Column(nullable = false)
  private Integer host_user_id;
  @ManyToOne()
  @JoinColumn(name = "game_id", referencedColumnName = "game_id")
  private GameBean gameBean;
  private String description;
  @Column(nullable = false)
  private LocalDateTime start_date;
  @Column(nullable = false)
  private LocalDateTime end_date;
  @Column(nullable = false)
  private Integer limit_of_participants;
  @Column(nullable = false)
  private LocalDateTime deadline;
}
