package at.qe.skeleton.configs;

import javax.sql.DataSource;

import at.qe.skeleton.internal.model.UserxRole;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring configuration for web security.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String ADMIN = UserxRole.ADMIN.name();
    private static final String MANAGER = UserxRole.MANAGER.name();
    private static final String USER = UserxRole.USER.name();
    private static final String LOGIN = "/login.xhtml";
    private static final String ACCESSDENIED = "/error/access_denied.xhtml";

    private static final String WELCOME = "/welcome.xhtml";
    
    @Autowired
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        try {   

            http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin)) // needed for H2 console
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher(WELCOME)).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/register_page.xhtml")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/resources/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/verification/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/confirm")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/**.jsf")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/jakarta.faces.resource/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/error/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAnyAuthority("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/manager/**")).hasAnyAuthority("MANAGER")
                .requestMatchers(new AntPathRequestMatcher("/secured/**")).hasAnyAuthority(ADMIN, MANAGER, USER)
                .requestMatchers(new AntPathRequestMatcher("/user/reset_password.xhtml")).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage(LOGIN)
                .permitAll()
                .defaultSuccessUrl(WELCOME)
                .loginProcessingUrl("/login")
                .successForwardUrl("/secured/welcome.xhtml") //before changing secured/welcome.xhtlm
                .failureUrl(ACCESSDENIED)
            )
            .logout(logout -> logout
                .logoutSuccessUrl(WELCOME)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            )
            .sessionManagement(session -> session
                .invalidSessionUrl("/error/invalid_session.xhtml")
            );

            return http.build();
        } catch (Exception ex) {
            throw new BeanCreationException("Wrong spring security configuration", ex);
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //Configure roles and passwords via datasource
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from userx where username=?")
                .authoritiesByUsernameQuery("select userx_username, roles from userx_userx_role where userx_username=?")
                .passwordEncoder(passwordEncoder());
    }

    /**
     * Retuns the PasswordEncoder which used to encode the password form the database for the login
     * @return BCryptPasswortEncoder
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
