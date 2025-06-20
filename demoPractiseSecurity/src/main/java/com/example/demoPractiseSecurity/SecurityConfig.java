package com.example.demoPractiseSecurity;

import com.example.demoPractiseSecurity.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

            http.authorizeHttpRequests((requests) -> {
        requests.requestMatchers("/h2-console/**")
                .permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
            });
            http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

//
//@Autowired
//DataSource dataSource;
//
//@Bean
//SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests((requests) -> {
//        requests.requestMatchers("/h2-console/**")
//                .permitAll()
//                .anyRequest().authenticated();
//    });
//    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
////        http.formLogin(Customizer.withDefaults());
//    http.httpBasic(Customizer.withDefaults());
//    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
//    http.csrf(csrf -> csrf.disable());
//    return http.build();
//}
//
//@Bean
//public UserDetailsService userDetailsService() {
//
//    UserDetails user1 = User.withUsername("arpan")
//            .password(passwordEncoder().encode("arpan@123"))
//            .roles("USER")
//            .build();
//
//    UserDetails user2 = User.withUsername("admin")
//            .password(passwordEncoder().encode("admin@123"))
//            .roles("ADMIN")
//            .build();
//
//    JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//    userDetailsManager.createUser(user1);
//    userDetailsManager.createUser(user2);
//    return userDetailsManager;
////        return new InMemoryUserDetailsManager(user1, user2);
//}
//
//@Bean
//public PasswordEncoder passwordEncoder(){
//    return new BCryptPasswordEncoder();
//}