package jp.te4a.spring.boot.sotsusei.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "private_comments")
@Data
@IdClass(PrivateCommentPrimaryKey.class)
public class PrivateCommentBean implements Serializable{
  @Id
  private Integer comp_id;
  @Id
  private Integer user_id;
  @Id
  private LocalDateTime commented_date;

  private String comment;
    
}
