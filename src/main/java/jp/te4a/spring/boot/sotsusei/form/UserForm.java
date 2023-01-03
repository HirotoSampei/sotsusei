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

    @NotBlank(message = "名前を入力してください")
    @Length(max = 50, message = "名前は50桁以内で入力してください")
    private String user_name;

    @NotBlank(message = "パスワードを入力してください")
    @Length(max = 100, message = "パスワードは100桁以内で入力してください")
    private String password;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email
    @Length(max = 50, message = "メールアドレスは50桁以内で入力してください")
    private String mail_address;

    @Length(max = 2000, message = "自由記入欄は2000桁以内で入力してください")
    private String note;

    private boolean is_banned;
    private boolean is_admin;
}
