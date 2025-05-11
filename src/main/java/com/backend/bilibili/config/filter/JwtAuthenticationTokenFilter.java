package com.backend.bilibili.config.filter;



import com.backend.bilibili.mapper.user.UserMapper;
import com.backend.bilibili.service.user.utils.UserDetailsImpl;
import com.backend.bilibili.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");


        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7);

        try {
            Claims claims = JwtUtil.parseJWT(token);
            String username = claims.getSubject();

            // 不要强转为 User，应该接收为 UserDetailsImpl
            UserDetailsImpl loginUser = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

            if (loginUser == null) {
                throw new RuntimeException("用户未登录");
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            throw new RuntimeException("Token 解析失败: " + e.getMessage());
        }


        filterChain.doFilter(request, response);
    }

}