package jp.te4a.spring.boot.sotsusei.form;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PrivateCommentForm {
    private Integer comp_id;
    private Integer user_id;
    private String nickname;
    private String comment;
    private String comment_date;
    
}