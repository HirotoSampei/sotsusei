package jp.te4a.spring.boot.sotsusei.form;

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
