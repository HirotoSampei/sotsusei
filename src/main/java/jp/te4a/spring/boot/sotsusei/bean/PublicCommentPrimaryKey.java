package jp.te4a.spring.boot.sotsusei.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class PublicCommentPrimaryKey implements Serializable{
    private Integer comp_id;
    private Integer user_id;
    private LocalDateTime commented_date;
    
}