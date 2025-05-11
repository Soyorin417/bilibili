package com.backend.bilibili.config;
//这段代码是一个 Spring Security 的配置类，用于配置 Web 应用程序的安全性。

import com.backend.bilibili.config.filter.JwtAuthenticationTokenFilter;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableWebSecurity 注解表示启用 Spring Security。
// 这个类提供了一些默认的安全配置，可以在这个基础上进行自定义配置。
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //@Bean 注解用于创建一个密码编码器对象（PasswordEncoder），这里使用的是 BCryptPasswordEncoder。
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    //authenticationManagerBean 方法用于创建一个 AuthenticationManager 实例
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override

    //configure 方法用于配置 HttpSecurity 对象，它定义了如何保护 Web 应用程序。
    // 在这个方法中，首先禁用了 CSRF（跨站请求伪造）防护，然后设置了会话管理策略为 STATELESS（无状态）。
    // 接着使用 authorizeRequests 方法配置了 URL 的访问权限，
    // 这里允许 "/user/account/token/" 和 "/user/account/register/"
    // 这两个 URL 不需要认证就可以访问，允许 HTTP 的 OPTIONS 方法，其余 URL 需要进行认证才能访问。
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/register",
                        "/login"
                ).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();

        //最后，通过 addFilterBefore 方法添加了 JwtAuthenticationTokenFilter 过滤器，
        // 它用于处理 JWT（JSON Web Token）身份验证。
        // JwtAuthenticationTokenFilter 将在 UsernamePasswordAuthenticationFilter 前执行，
        // 因此可以保证在身份验证之前进行 JWT 的校验。
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }


}