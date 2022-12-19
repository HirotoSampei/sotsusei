package jp.te4a.spring.boot.sotsusei.form;

import java.time.LocalDateTime;

import jp.te4a.spring.boot.sotsusei.bean.GameBean;
import lombok.Data;

@Data
public class ParticipatedForm {

    private Integer comp_id ;
    private String comp_name;
    private Integer count;
    private Integer host_user_id;
    private GameBean gameBean;
    private String description;
    private String host_nickname;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Integer limit_of_participants;
    private LocalDateTime deadline;
    private String nickname;
        
}
