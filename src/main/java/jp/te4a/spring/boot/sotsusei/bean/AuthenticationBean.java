package jp.te4a.spring.boot.sotsusei.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "authentication")
@Data
public class AuthenticationBean {
    @Id
    Integer user_id;
    Integer authentication_pass;
}
