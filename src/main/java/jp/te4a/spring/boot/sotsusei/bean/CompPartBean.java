package jp.te4a.spring.boot.sotsusei.bean;
import lombok.Data;
import java.io.Serializable;
import javax.persistence.*;
@Entity
@Table(name = "competitions_participants")
@Data
@IdClass(CompPartPrimaryKey.class)
public class CompPartBean implements Serializable{
  @Id
  private Integer comp_id;

  @Id
  private Integer user_id;

  private String nickname;
  
}
