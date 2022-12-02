package jp.te4a.spring.boot.sotsusei.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class UserBean {

    @Id
    @GeneratedValue
    private Integer user_id;
    @Column(nullable = false)
    private String user_name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String mail_address;
    private String note;
    @Column(nullable = false)
    private boolean is_banned;
    @Column(nullable = false)
    private boolean is_admin;

    //@OneToOne(mappedBy="userBean", fetch = FetchType.EAGER) // 多対1
    //private List<GameplayBean> gameplayBean;

}