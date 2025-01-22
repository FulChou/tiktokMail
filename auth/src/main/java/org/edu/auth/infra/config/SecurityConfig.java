package org.edu.auth.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/auth/login").permitAll() // 允许登录接口公开访问
//                .antMatchers("/auth/validate").permitAll() // 允许令牌校验接口公开访问
//                .anyRequest().authenticated(); // 其他接口需要认证
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // 使用BCrypt加密密码
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF 防护（根据需求配置）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll() // 允许登录接口公开访问
                        .requestMatchers("/auth/validate").permitAll() // 允许令牌校验接口公开访问
                        .anyRequest().authenticated() // 其他接口需要认证
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 使用 BCrypt 加密密码
    }
}
