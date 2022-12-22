package jp.te4a.spring.boot.sotsusei.form;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CompsearchForm {
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