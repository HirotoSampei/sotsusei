package jp.te4a.spring.boot.sotsusei.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class CompPartPrimaryKey implements Serializable{
    private Integer comp_id;
    private Integer user_id;
    
}
