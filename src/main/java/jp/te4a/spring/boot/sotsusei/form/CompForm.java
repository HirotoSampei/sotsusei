package jp.te4a.spring.boot.sotsusei.form;
import java.sql.Date;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import jp.te4a.spring.boot.sotsusei.validate.Writter;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class CompForm {
  private Integer comp_id ;
 
  private String comp_name;
  
  private Integer host_user_id;
  
  private Integer game_id;
  private String description;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private  LocalDateTime start_date;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime end_date;
  
  private Integer limit_of_participants;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime deadline;
}

