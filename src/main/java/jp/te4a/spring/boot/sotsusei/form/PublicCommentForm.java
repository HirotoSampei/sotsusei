package jp.te4a.spring.boot.sotsusei.form;

import lombok.Data;

@Data
public class PublicCommentForm {
    private Integer comp_id;
    private Integer user_id;
    private String user_name;
    private String comment;
    
}
