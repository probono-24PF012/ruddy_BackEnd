package spring_security.security_practice1.dto;

import lombok.*;
import spring_security.security_practice1.Entity.Member;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String username;
    private String password;
    private String nickname;
    private String schoolCode;
    private String nationality;
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {

        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .schoolCode(schoolCode)
                .nationality(nationality)
                .roles(roles)
                .build();
    }
}
