package jp.te4a.spring.boot.sotsusei.form;

import java.time.LocalDateTime;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class CompForm {
  private Integer comp_id ;
 
  @NotBlank(message = "大会名を入力してください")
  @Length(max = 50, message = "大会名は50桁以内で入力してください")
  private String comp_name;
  
  private Integer host_user_id;
  
  @JoinColumn(name = "game_id", referencedColumnName = "game_id")
  private GameBean gameBean;

  @Length(max = 2000, message = "大会概要は2000桁以内で入力してください")
  private String description;

  @NotBlank(message = "主催者名を入力してください")
  @Length(max = 50, message = "主催者名は50桁以内で入力してください")
  private String host_nickname;

  @NotNull(message = "開始日時を入力してください")
  @Future(message = "開始日時は現在の日付以降で入力してください")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private  LocalDateTime start_date;

  @NotNull(message = "終了日時を入力してください")
  @Future(message = "終了日時は現在の日付以降で入力してください")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime end_date;
  
  @NotNull(message = "参加者上限を入力してください")
  @Range(min = 2, max = 200, message = "参加者上限は2人以上200人以内で入力してください")
  private Integer limit_of_participants;

  @NotNull(message = "締め切り日時を入力してください")
  @Future(message = "締め切り日時は現在の日付以降で入力してください")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime deadline;
}

