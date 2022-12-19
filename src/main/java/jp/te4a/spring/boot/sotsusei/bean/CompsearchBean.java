package jp.te4a.spring.boot.sotsusei.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "competitionssearch")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompsearchBean {
  @Id
  private Integer comp_id;
  private String comp_name;
  private Integer host_user_id;
  //private Integer game_id;//
  /*private String description;
  private LocalDateTime start_date;
  private LocalDateTime end_date;
  private Integer limit_of_participants;
  private LocalDateTime deadline;*/
}