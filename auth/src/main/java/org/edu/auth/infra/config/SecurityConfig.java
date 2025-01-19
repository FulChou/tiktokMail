package org.edu.auth.infra.config;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/login").permitAll() // 允许登录接口公开访问
                .antMatchers("/auth/validate").permitAll() // 允许令牌校验接口公开访问
                .anyRequest().authenticated(); // 其他接口需要认证
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 使用BCrypt加密密码
    }
}
