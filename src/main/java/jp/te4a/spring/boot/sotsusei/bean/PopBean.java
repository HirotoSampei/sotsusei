package jp.te4a.spring.boot.sotsusei.bean;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="users")
public class PopBean {
    @Id
    private Integer user_id;
    private String note;
    
}
