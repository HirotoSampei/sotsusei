package jp.te4a.spring.boot.sotsusei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/css/**");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/users").permitAll()
                .antMatchers("/users/create").permitAll()
                .antMatchers("/comp").permitAll()
                .antMatchers("/home/Home").permitAll()
                .antMatchers("/comp/Overview").permitAll()
                .antMatchers("/comp/OverviewForHost").permitAll()
                .antMatchers("/comp/CreateComp").permitAll()
                .antMatchers("/comp/OverviewForParticipants").permitAll()
                .antMatchers("/comp/EditComp").permitAll()
                .antMatchers("/comp/create").permitAll()
                .antMatchers("/comp/edit").permitAll()
                .antMatchers("/comp/delete").permitAll()
                .antMatchers("/comp/cancel").permitAll()

                .antMatchers("/admin").permitAll()
                .antMatchers("/admin/userlist").permitAll()
                .antMatchers("/admin/userlist/search").permitAll()
                .antMatchers("/admin/search").permitAll()
                .antMatchers("/admin/username-search-sample").permitAll()
                .anyRequest().authenticated()     
            .and()
                .formLogin()
                .loginProcessingUrl("/login")
                     .loginPage("/login")
                     .failureUrl("/login?error")
                     .defaultSuccessUrl("/comp", true)
                     .usernameParameter("mail_address").passwordParameter("password")
            .and()
                .logout()
                .logoutSuccessUrl("/login");
            
    }
}