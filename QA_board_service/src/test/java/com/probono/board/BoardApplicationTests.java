package com.probono.board;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.probono.board.dto.BoardCreateRequest;
import com.probono.board.entity.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlcyI6WyJVU0VSIl19.TXam8pxYmhfzIZwslJmt89EusXjJnLdSt9VyK3gqHrc";
	@Test
	@WithMockUser(username = "matkimchi", roles = "USER")
	@DisplayName("게시글 가져오기")
	void getPost() throws Exception{
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/qa-board/getmyboard").header("Authorization", jwtToken)
						.accept(MediaType.APPLICATION_JSON))// get("post")검사
				.andExpect(status().isOk())	// request 요청시의 Http Status 검증
				.andDo(print())	// 로그로 request, response 확인 가능
				.andDo(document("getPost",
						resourceDetails().description("게시물 조회"),
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestHeaders(	// request Header 설명 추가
								headerWithName("ACCEPT").description("Accept header"),
								headerWithName("AUTHORIZATION").description("Bearer token for authentication")
						),
						responseFields( // response Fields 설명 추가
								fieldWithPath("[].id").description("id"),
								fieldWithPath("[].title").description("title of post"),
								fieldWithPath("[].content").description("content of post"),
								fieldWithPath("[].username").description("username of post")
						)
				));
	}
	@Test
	@WithMockUser(username = "matkimchi", roles = "USER")
	@DisplayName("게시글 작성")
	void write() throws Exception {
		String requestBody = "{ \"title\": \"Test Title\", \"content\": \"Test Content\" }";
		this.mockMvc.perform(RestDocumentationRequestBuilders.post("/qa-board/write").header("Authorization", jwtToken)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("write",
						resourceDetails().description("게시물 작성"),
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestHeaders(
								headerWithName("ACCEPT").description("Accept header"),
								headerWithName("AUTHORIZATION").description("Bearer token for authentication")
						),
						requestFields(
								fieldWithPath("title").description("title of post"),
								fieldWithPath("content").description("content of post")
						)
				));
	}
	@Test
	@DisplayName("게시글 삭제")
	void delete() throws Exception{
		String id = "1";
		this.mockMvc.perform(RestDocumentationRequestBuilders.post("/qa-board/delete").header("Authorization", jwtToken)
				.param("id", "1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("delete",
						resourceDetails().description("게시물 삭제"),
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestHeaders(

								headerWithName("AUTHORIZATION").description("Bearer token for authentication")
						),
						formParameters(
								parameterWithName("id").description("id of post")
						)
				));
	}
	@Test
	@DisplayName("게시글 수정")
	@WithMockUser(username = "matkimchi", roles = "USER")
	void update() throws Exception {
		// Arrange: Create a Board object without id and username fields
		BoardCreateRequest boardCreateRequest = new BoardCreateRequest();
		boardCreateRequest.setTitle("Update Title");
		boardCreateRequest.setContent("Update Content");
		ObjectMapper objectMapper = new ObjectMapper();
		// Convert Board object to JSON
		String boardJson = objectMapper.writeValueAsString(boardCreateRequest);

		// Act & Assert: Send a POST request to the controller with the JSON data
		mockMvc.perform(RestDocumentationRequestBuilders.post("/qa-board/update/{id}", 3)
						.header("Authorization", jwtToken)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(boardJson)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("update",
						resourceDetails().description("게시물 수정"),
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("id").description("The ID of the board")
						),
						requestHeaders(
								headerWithName("Accept").description("Accept header"),
								headerWithName("Authorization").description("Bearer token for authentication")
						),
						requestFields(
								fieldWithPath("title").description("The title of the board"),
								fieldWithPath("content").description("The content of the board")
						)
				));
	}
}

