package cn.tedu.codeadmin.config;

import cn.tedu.codeadmin.filter.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;





@Configuration
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder(){
        log.debug("創建@Bean方法定義的對象:PasswordEncoder");
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        log.debug("創建@Bean方法定義的對象:authenticationManagerBean");
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] url = {
                // 【配置白名单】
                // 在配置路径时，星号是通配符
                // 1个星号只能匹配任何文件夹或文件的名称，但不能跨多个层级
                // 例如：/*/test.js，可以匹配到 /a/test.js 和 /b/test.js，但不可以匹配到 /a/b/test.js
                // 2个连续的星号可以匹配若干个文件夹的层级
                // 例如：/**/test.js，可以匹配 /a/test.js 和 /b/test.js 和 /a/b/test.js
                "/doc.html",
                "/**/*.css",
                "/**/*.js",
                "/swagger-resources",
                "/v2/api-docs",
                "/admins/login"
        };

        http.csrf().disable();

        http.cors(); //啟用SpringSecurity框架的處理跨域的過濾器,此過濾器將放行跨域請求,包括預檢

        http.authorizeRequests() //對請求執行認證與授權
                .antMatchers(url) //匹配某些請求路徑
                .permitAll() //(對此前匹配的請求路徑)不需要通過認證即予許訪問
                .anyRequest() //除以上配置過的請求路徑以外的所有請求路徑
                .authenticated(); //要求是已經通過驗證的



        //開啟表單驗證,即視為未通過認證時,將重定向到登入表單,如果無此配置則直接響應403
        //    http.formLogin();
        // 将自定义的JWT过滤器添加在Spring Security框架内置的过滤器之前
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;
}
