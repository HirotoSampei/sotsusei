package jp.te4a.spring.boot.sotsusei.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserForm {
    private Integer user_id;

    @NotBlank
    @Length(max = 50)
    private String user_name;

    @NotBlank
    @Length(max = 100)
    private String password;

    @NotBlank
    @Email
    @Length(max = 50)
    private String mail_address;

    @Length(max = 2000)
    private String note;

    private boolean is_banned;
    private boolean is_admin;
}
