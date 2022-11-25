package jp.te4a.spring.boot.sotsusei.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="games_to_play")
public class GameplayBean {
    @Id
    private Integer user_id;
    @Column(nullable = false)
    private Integer game_id;
}
