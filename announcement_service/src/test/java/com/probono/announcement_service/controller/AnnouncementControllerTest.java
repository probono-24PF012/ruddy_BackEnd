package com.probono.announcement_service.controller;

import com.probono.announcement_service.entity.Announcement;
import com.probono.announcement_service.service.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AnnouncementControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private AnnouncementService announcementService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllAnnouncements() throws Exception {
        Announcement announcement = new Announcement(1L, "Title", "Content", "author", "date");

        when(announcementService.getAllAnnouncements())
                .thenReturn(Collections.singletonList(announcement));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/announcement/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-all-announcements",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("The announcement ID"),
                                fieldWithPath("[].title").description("The announcement title"),
                                fieldWithPath("[].content").description("The announcement content"),
                                fieldWithPath("[].author").description("The announcement author"),
                                fieldWithPath("[].date").description("The announcement date")
                        )));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAnnouncementById() throws Exception {
        Announcement announcement = new Announcement(1L, "Title", "Content", "author", "date");

        when(announcementService.getAnnouncementById(anyLong())).thenReturn(announcement);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/announcement/detail/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-announcement-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("The ID of the announcement to retrieve")
                        ),
                        responseFields(
                                fieldWithPath("id").description("The announcement ID"),
                                fieldWithPath("title").description("The announcement title"),
                                fieldWithPath("content").description("The announcement content"),
                                fieldWithPath("author").description("The announcement author"),
                                fieldWithPath("date").description("The announcement date")
                        )));
    }
}



