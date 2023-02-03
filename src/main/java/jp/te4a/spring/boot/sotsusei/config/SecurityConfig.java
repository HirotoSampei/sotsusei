package jp.te4a.spring.boot.sotsusei.config;

import javax.activation.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private javax.sql.DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/css/**" ,"/js/**");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    private static final String USER_SQL = "SELECT"
            + " mail_address,"
            + " password,"
            + " true"
            + " FROM"
            + " users"
            + " WHERE"
            + " mail_address = ?";

    private static final String ROLE_SQL = "SELECT"
            + " mail_address,"
            + " role"
            + " FROM"
            + " users"
            + " WHERE"
            + " mail_address = ?";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/home/Home").permitAll()
                .antMatchers("/comp/**").permitAll()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
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
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // ログイン処理時のユーザー情報を、DBから取得する
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(USER_SQL)
                .authoritiesByUsernameQuery(ROLE_SQL)
                .passwordEncoder(passwordEncoder());
    }
}