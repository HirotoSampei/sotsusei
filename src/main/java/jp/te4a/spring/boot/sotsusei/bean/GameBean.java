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
@Table(name="games")
public class GameBean {

    @Id
    @GeneratedValue
    private Integer game_id;
    @Column(nullable = false)
    private String game_name;
    @Column(nullable = false)
    private String hard;

    
}
