package com.probono.board;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken( request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Map<String, Object> json_data = jwtTokenProvider.getdata(token);
            String username = json_data.get("sub").toString();
            List<GrantedAuthority> authorities = new ArrayList<>();
            Object rolesObject = json_data.get("roles");
            System.out.println(rolesObject);
            if (rolesObject instanceof List) {
                List<?> roles = (List<?>) rolesObject;
                for (Object role : roles) {
                    if (role instanceof String) {
                        authorities.add(new SimpleGrantedAuthority((String) role));
                    }
                }
            } else if (rolesObject instanceof String) {
                authorities.add(new SimpleGrantedAuthority((String) rolesObject));
            }

            // 인증 객체 설정
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request, response);
        System.out.println("end");
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

