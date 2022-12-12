package jp.te4a.spring.boot.sotsusei.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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