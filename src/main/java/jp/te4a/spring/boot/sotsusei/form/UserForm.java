package jp.te4a.spring.boot.sotsusei.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserForm {
	private Integer user_id;
	private String user_name;
	private String password;
	private String mail_address;
    private String note;
	private boolean is_banned;
	private boolean is_admin;
}
