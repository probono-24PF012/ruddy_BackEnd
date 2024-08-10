package com.probono.announcement_service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${secret-key}")
    private String secretkey;
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token);
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    public Map<String, Object> getdata(String token){
        Claims claims=Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
        return new HashMap<>(claims);
    }
}