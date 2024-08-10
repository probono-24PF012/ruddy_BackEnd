package spring_security.security_practice1.dto;

import lombok.*;
import spring_security.security_practice1.Entity.Member;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String username;
    private String nickname;
    private String schoolCode;
    private String nationality;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .schoolCode(member.getSchoolCode())
                .nationality(member.getNationality()).build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .schoolCode(schoolCode)
                .nationality(nationality).build();
    }
}
