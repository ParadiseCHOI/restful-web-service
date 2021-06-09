package com.example.restfulwebservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // /h2-console/{All-data-file} URL 에 대한 모든 요청 허용
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        // Cross Site Scripting 에 관련된 부분 사용하지 않음
        http.csrf().disable();
        // 헤더값의 프레임에 관련된 속성값 사용 X
        http.headers().frameOptions().disable();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {
        auth.inMemoryAuthentication()
                .withUser("paradise")
                .password("{noop}1234")
                .roles("USER");
    }
}
