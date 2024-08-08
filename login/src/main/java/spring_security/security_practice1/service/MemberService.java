package spring_security.security_practice1.service;

import spring_security.security_practice1.dto.JwtToken;
import spring_security.security_practice1.dto.MemberDto;
import spring_security.security_practice1.dto.SignUpDto;

public interface MemberService {
    JwtToken signIn(String username, String password);

    MemberDto signUp(SignUpDto signUpDto);

}
