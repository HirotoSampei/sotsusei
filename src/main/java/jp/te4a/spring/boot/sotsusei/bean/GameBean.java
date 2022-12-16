package jp.te4a.spring.boot.sotsusei.bean;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    public Integer game_id;
    @Column(nullable = false)
    private String game_name;
    @Column(nullable = false)
    private String hard;
    @Column(nullable = false)
    private String genre;

    @OneToMany(mappedBy="gameBean") // 多対1
    private List<CompBean> compBean;

    //@OneToMany(mappedBy="gameBean", fetch = FetchType.EAGER) // 多対1
    //private List<GameplayBean> gameplayBean;

    
}
