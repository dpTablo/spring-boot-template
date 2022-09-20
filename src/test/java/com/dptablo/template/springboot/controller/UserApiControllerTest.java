package com.dptablo.template.springboot.controller;

import com.dptablo.template.springboot.configuration.SecurityConfiguration;
import com.dptablo.template.springboot.model.dto.UserDto;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UserApiControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("전체 유저 리스트 조회 api 테스트")
    @Test
    void getAllUserList() {
        try {
            //given
            UserDto.SimpleUser user1 = new UserDto.SimpleUser(User.builder()
                    .userId("user1")
                    .phoneNumber("01011112222")
                    .build());

            UserDto.SimpleUser user2 = new UserDto.SimpleUser(User.builder()
                    .userId("user2")
                    .phoneNumber("01033334444")
                    .build());

            given(userService.getAllUserList()).willReturn(Arrays.asList(user1, user2));


            //when
            MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/list/all")
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(document("user-{methodName}",
//                            preprocessRequest(prettyPrint()),
//                            preprocessResponse(prettyPrint()),
//                            requestFields(),
                            responseFields(
                                    fieldWithPath("userId").description("사용자 id"),
                                    fieldWithPath("").description("휴대폰번호")
                            )
                    ))
                    .andReturn();

            //then
            ObjectMapper objectMapper = new ObjectMapper();

            List<Map> list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
            assertThat(list.size()).isEqualTo(2);
            assertThat(list.get(0).get("userId")).isEqualTo(user1.getUserId());
            assertThat(list.get(0).get("phoneNumber")).isEqualTo(user1.getPhoneNumber());
            assertThat(list.get(1).get("userId")).isEqualTo(user2.getUserId());
            assertThat(list.get(1).get("phoneNumber")).isEqualTo(user2.getPhoneNumber());
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }
}