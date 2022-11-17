package jp.te4a.spring.boot.sotsusei.form;
import java.sql.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
  
  private Date start_date;
  
  private Date end_date;
  
  private Integer limit_of_participants;
  
  private Date deadline;
}

