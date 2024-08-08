/*

package spring_security.security_practice1;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class BoardApplicationTests {
    MockMvc mockMvc;

    @BeforeEach
    void setup (WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }
    @Test
    @WithMockUser(username = "matkimchi", roles = "USER")
    void getPost() throws Exception{
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/qa-board/getmyboard").accept(MediaType.APPLICATION_JSON))	// get("post")검사
                .andExpect(MockMvcResultMatchers.status().isOk())	// request 요청시의 Http Status 검증
                .andDo(MockMvcResultHandlers.print())	// 로그로 request, response 확인 가능
                .andDo(document("getPost",	// snippet 생성, 이름 = "getPost"
                        HeaderDocumentation.requestHeaders(	// request Header 설명 추가
                                HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description("accept header")
                        ),
                        HeaderDocumentation.responseHeaders( // response Header 설명 추가
                                HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        PayloadDocumentation.responseFields( // response Fields 설명 추가
                                PayloadDocumentation.fieldWithPath("[].id").description("id"),
                                PayloadDocumentation.fieldWithPath("[].title").description("title of post"),
                                PayloadDocumentation.fieldWithPath("[].content").description("content of post"),
                                PayloadDocumentation.fieldWithPath("[].username").description("username of post")
                        )
                ));
    }
}

 */


