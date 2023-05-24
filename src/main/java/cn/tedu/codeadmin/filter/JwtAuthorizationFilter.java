package cn.tedu.codeadmin.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSON;

import cn.tedu.codeadmin.security.LoginPrincipal;
import cn.tedu.codeadmin.web.JsonResult;
import cn.tedu.codeadmin.web.ServiceCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final int JWT_MIN_LENGTH = 100;

    @Value("${jwt.secret-key}")
    String secretKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 清除SecurityContext中原有的数据（认证信息）
        SecurityContextHolder.clearContext();

        // 尝试获取客户端提交请求时可能携带的JWT
        String jwt = request.getHeader("Authorization");
        log.debug("接收到JWT数据：{}", jwt);

        // 判断是否获取到有效的JWT
        if (!StringUtils.hasText(jwt) || jwt.length() < JWT_MIN_LENGTH) {
            // 直接放行
            log.debug("未获取到有效的JWT数据，将直接放行");
            filterChain.doFilter(request, response);
            return;
        }

        // 尝试解析JWT，从中获取用户的相关数据，例如id、username等
        log.debug("将尝试解析JWT……");


        //設置響應的文檔類型
        response.setContentType("application/json;charset=utf-8");

        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        }  catch (SignatureException e) {
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_MALFORMED, "非法訪問");
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter(); //輸出字符流
            writer.println(jsonResultString);
            writer.close();
            return;
        }
        catch (MalformedJwtException e) {
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_MALFORMED, "非法訪問");
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter(); //輸出字符流

            writer.println(jsonResultString);
            writer.close();
            return;
        } catch (ExpiredJwtException e) {
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_EXPIRED, "登入已過期，請重新登入");
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter(); //輸出字符流
            writer.println(jsonResultString);
            writer.close();
            return;
        } catch (Throwable e) {
            e.printStackTrace();
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_UNKNOWN, "服務器繁忙,請稍後再嘗試");
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter(); //輸出字符流
            writer.println(jsonResultString);
            writer.close();
            return;
        }


        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        String authoritiesJsonString = claims.get("authorities", String.class);
        log.debug("从JWT中解析得到数据：id={}", id);
        log.debug("从JWT中解析得到数据：username={}", username);
        log.debug("從JWT中解析得到數據:authorities1={}", authoritiesJsonString);
        log.debug("從JWT中解析得到數據:authorities1的數據類型={}", authoritiesJsonString.getClass().getName());

        //準備用於創建認證信息的權限數據
        List<SimpleGrantedAuthority> authorities =
                JSON.parseArray(authoritiesJsonString, SimpleGrantedAuthority.class);

        //準備用於創建認證信息的當事人數據
        LoginPrincipal loginPrincipal = new LoginPrincipal();
        loginPrincipal.setId(id);
        loginPrincipal.setUsername(username);

        //創建認證信息
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginPrincipal, null, authorities);

        log.debug("即將向SecurityContext中存入認證信息:{}", authentication);

        // 将认证信息存储到SecurityContext中
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 放行
        filterChain.doFilter(request, response);

    }
}
