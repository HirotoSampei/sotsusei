package jp.te4a.spring.boot.sotsusei.form;

import lombok.Data;

@Data
public class PopuserForm {
    private Integer user_id;
    private Integer login_id;
    private String note;
    private String nickname;
}
