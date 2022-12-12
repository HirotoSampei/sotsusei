package jp.te4a.spring.boot.sotsusei.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import javax.persistence.*;
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
  private String game_name;
  /*private String description;
  private LocalDateTime start_date;
  private LocalDateTime end_date;
  private Integer limit_of_participants;
  private LocalDateTime deadline;*/
}