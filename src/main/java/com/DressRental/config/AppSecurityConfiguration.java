package com.DressRental.config;

import com.DressRental.models.enums.UserRoles;
import com.DressRental.repository.impl.UserRepositoryImpl;
import com.DressRental.service.impl.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class AppSecurityConfiguration {
    private UserRepositoryImpl userRepository;

    public AppSecurityConfiguration(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http
                .authorizeHttpRequests(
                        authorizeHttpRequests ->
                                authorizeHttpRequests.
                                        requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                        .permitAll()
                                        .requestMatchers("/favicon.ico").permitAll()
                                        .requestMatchers("/error").permitAll()
                                        .requestMatchers("/", "/user/login", "/user/register", "/user/login-error")
                                        .permitAll().
                                        requestMatchers("/user/profile").authenticated().
                                        requestMatchers("/rentals", "/ratings", "/dress/create").hasAnyRole(UserRoles.ADMIN.name()).
                                        anyRequest().authenticated()
                )
                .formLogin(
                        (formLogin) ->
                                formLogin
                                        .loginPage("/user/login")
                                        .usernameParameter("email")
                                        .passwordParameter("password")
//                                        usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
//                                        passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                                        .defaultSuccessUrl("/")
                                        .failureForwardUrl("/user/login-error")
                )
                .logout((logout) ->
                        logout.logoutUrl("/user/logout").
                                logoutSuccessUrl("/").
                                invalidateHttpSession(true)
                ).securityContext(
                        securityContext -> securityContext.
                                securityContextRepository(securityContextRepository)
                );

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }
}
