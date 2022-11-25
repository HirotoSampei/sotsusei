package jp.te4a.spring.boot.sotsusei.form;
import java.time.LocalDateTime;

import javax.persistence.JoinColumn;

import org.springframework.format.annotation.DateTimeFormat;

import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class CompForm {
  private Integer comp_id ;
 
  private String comp_name;
  
  private Integer host_user_id;
  
  @JoinColumn(name = "game_id", referencedColumnName = "game_id")
  private GameBean gameBean;
  private String description;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private  LocalDateTime start_date;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime end_date;
  
  private Integer limit_of_participants;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime deadline;
}

