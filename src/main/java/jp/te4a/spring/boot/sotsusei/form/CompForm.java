package jp.te4a.spring.boot.sotsusei.form;
import java.time.LocalDateTime;

import javax.persistence.JoinColumn;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class CompForm {
  private Integer comp_id ;
 
  @NotBlank
  @Length(max = 50)
  private String comp_name;
  
  private Integer host_user_id;
  
  @JoinColumn(name = "game_id", referencedColumnName = "game_id")
  private GameBean gameBean;

  @Length(max = 2000)
  private String description;

  @NotBlank
  @Length(max = 50)
  private String host_nickname;

  @Future
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private  LocalDateTime start_date;

  @Future
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime end_date;
  
  @Min(2)
  @Max(200)
  private Integer limit_of_participants;

  @Future
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime deadline;
}

