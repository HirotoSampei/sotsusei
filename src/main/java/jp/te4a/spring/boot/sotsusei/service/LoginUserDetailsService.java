package jp.te4a.spring.boot.sotsusei.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.te4a.spring.boot.sotsusei.bean.UserBean;
import jp.te4a.spring.boot.sotsusei.security.LoginUserDetails;
import jp.te4a.spring.boot.sotsusei.repository.UserRepository;

@Service
public class LoginUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail_address) throws UsernameNotFoundException {
        if (mail_address == null || "".equals(mail_address)) {
            throw new UsernameNotFoundException("mail_address is empty");
        }

        UserBean user = userRepository.findByMail_address(mail_address);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + mail_address);
        }

        return new LoginUserDetails(user);
    }
    
}