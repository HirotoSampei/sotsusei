package jp.te4a.spring.boot.sotsusei.security;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import lombok.Data;
@Data
public class LoginUserDetails extends User {
    private final UserBean user;
    public LoginUserDetails(UserBean userBean) {
        super(userBean.getMail_address(), userBean.getPassword(),
        		AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.user = userBean;
    }
}