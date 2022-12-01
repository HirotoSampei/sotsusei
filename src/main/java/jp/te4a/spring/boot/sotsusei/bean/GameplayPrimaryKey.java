package jp.te4a.spring.boot.sotsusei.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;

@Embeddable
@Data
public class GameplayPrimaryKey implements Serializable{
   private Integer user_id;
   private Integer game_id;
}
