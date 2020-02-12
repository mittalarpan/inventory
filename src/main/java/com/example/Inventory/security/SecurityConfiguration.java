package com.example.Inventory.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

    public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider()) ;
      /*  auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("admin123"))
                .authorities("API1", "API2", "ROLE_ADMIN")
                .and()
                .withUser("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .and()
                .withUser("manager")
                .password(passwordEncoder().encode("manager123"))
                .authorities("API1", "ROLE_MANAGER");
        *//*anyRequest().permitAll() ---> Allow all users to permit all resources of the website
         Order of antMatcher matters the most*/

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/inventory/signUp","/inventory/vendor/signUp",
                        "/inventory/login","/inventory/vendor/login").permitAll()
                *//*.antMatchers("/profile/index").authenticated()
                .antMatchers("/admin/index").hasAuthority("ROLE_ADMIN")
                .antMatchers("/management/index").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                .antMatchers("/api/public/test1").hasAuthority("API1")
                .antMatchers("/api/public/test2").hasAuthority("API2")
                .antMatchers("/api/public/users").permitAll()
                .antMatchers("/api/public/signUp").permitAll()*//*
                .antMatchers("/inventory/user/**").hasAuthority("ROLE_USER")
                .antMatchers("/inventory/vendor/**").hasAuthority("ROLE_VENDOR")
                .antMatchers("/inventory/supply").hasAuthority("ROLE_VENDOR")
                .and()
                .httpBasic();
    }*/
}