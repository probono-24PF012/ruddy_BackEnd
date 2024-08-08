package spring_security.security_practice1.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import spring_security.security_practice1.config.RestDocsConfig;
import spring_security.security_practice1.config.SecurityConfig;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest extends RestDocsConfig {

    @Autowired
    private MockMvc mockMvc;

    private final String username = "user";
    private final String password = "password";

    @Test
    public void signUpAndSignIn() throws Exception {
        // 회원가입 테스트
        String signUpRequest = String.format(
                "{ \"username\": \"%s\", \"password\": \"%s\", \"nickname\": \"nick\", \"schoolCode\": \"sejong\", \"nationality\": \"Korean\" }",
                username, password);

        this.mockMvc.perform(post("/members/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isOk())
                .andDo(document("members/sign-up",
                        requestFields(
                                fieldWithPath("username").description("The user's username"),
                                fieldWithPath("password").description("The user's password"),
                                fieldWithPath("nickname").description("The user's nickname"),
                                fieldWithPath("schoolCode").description("The user's school code"),
                                fieldWithPath("nationality").description("The user's nationality")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 고유 ID"),
                                fieldWithPath("username").description("The user's username"),
                                fieldWithPath("nickname").description("The user's nickname"),
                                fieldWithPath("schoolCode").description("The user's school code"),
                                fieldWithPath("nationality").description("The user's nationality")
                        )));

        // 로그인 테스트
        String signInRequest = String.format(
                "{ \"username\": \"%s\", \"password\": \"%s\" }",
                username, password);

        this.mockMvc.perform(post("/members/sign-in")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInRequest))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())	// 로그로 request, response 확인 가능

                .andDo(document("members/sign-in",
                        requestFields(
                                fieldWithPath("username").description("The user's username"),
                                fieldWithPath("password").description("The user's password")
                        ),
                        responseFields(
                                fieldWithPath("grantType").description("The grant type"),
                                fieldWithPath("accessToken").description("The access token"),
                                fieldWithPath("refreshToken").description("The refresh token")
                        )));
    }
}
