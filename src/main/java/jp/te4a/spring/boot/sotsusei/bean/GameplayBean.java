package jp.te4a.spring.boot.sotsusei.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="games_to_play")
@IdClass(GameplayPrimaryKey.class)
public class GameplayBean implements Serializable{
    @Id
    private Integer user_id;

    @Id
    private Integer game_id;

    //@ManyToOne()
    //@JoinColumn(name = "user_id", referencedColumnName = "user_id")
    //public UserBean userBean;
    
    //@OneToOne()
    //@JoinColumn(name = "game_id", referencedColumnName = "game_id")
    //public GameBean gameBean;

}

