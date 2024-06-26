package com.todocodeacademy.springsecurity.security.config;

import com.todocodeacademy.springsecurity.security.config.filter.JwtTokenValidator;
import com.todocodeacademy.springsecurity.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Primera parte del curso( Configuracion Manual)
              /*  .authorizeHttpRequests( http -> {
                  //Endpoints
                  http.requestMatchers(HttpMethod.GET, "/holanoseg").permitAll();
                  http.requestMatchers(HttpMethod.GET, "/holaseg").hasAuthority("READ");
                  http.anyRequest().denyAll();
                }) */
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Primera parte del curso( Configuracion Manual)
    /*@Bean
    public UserDetailsService userDetailsService(){
        List userDetailsList = new ArrayList<>();

        userDetailsList.add(User.withUsername("admin")
                .password("1234")
                .roles("ADMIN")
                .authorities("CREATE","READ","UPDATE","DELETE")
                .build());

        userDetailsList.add(User.withUsername("seguidor")
                .password("1234")
                .roles("USER")
                .authorities("READ")
                .build());

        userDetailsList.add(User.withUsername("actualizador")
                .password("1234")
                .roles("USER")
                .authorities("UPDATE")
                .build());

        return new InMemoryUserDetailsManager(userDetailsList);
    }*/
}
