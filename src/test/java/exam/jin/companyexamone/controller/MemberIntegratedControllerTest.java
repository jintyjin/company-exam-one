package exam.jin.companyexamone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam.jin.companyexamone.dto.MemberJoinRequest;
import exam.jin.companyexamone.dto.MemberLoginRequest;
import exam.jin.companyexamone.dto.MemberLoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Locale.KOREAN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional(readOnly = true)
public class MemberIntegratedControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messageSource;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원 가입 성공")
    @Transactional
    void joinSuccess() throws Exception {
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest request = new MemberJoinRequest(loginId, password, username);

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.data.loginId").value(loginId),
                jsonPath("$.data.username").value(username)
        );
    }

    @Test
    @DisplayName("중복 회원 가입")
    @Transactional
    void joinDuplicated() throws Exception {
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest request = new MemberJoinRequest(loginId, password, username);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value(messageSource.getMessage("member.duplicated", null, KOREAN)),
                jsonPath("$.data.loginId").value(loginId)
        );
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws Exception{
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest joinRequest = new MemberJoinRequest(loginId, password, username);

        String joinContent = objectMapper.writeValueAsString(joinRequest);

        mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(joinContent));

        url = "/api/member/login";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.loginId").value(loginId)
        );
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFailed() throws Exception{
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest joinRequest = new MemberJoinRequest(loginId, password, username);

        String joinContent = objectMapper.writeValueAsString(joinRequest);

        mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(joinContent));

        url = "/api/member/login";

        loginId = "hong13";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value(messageSource.getMessage("member.loginFail", null, KOREAN))
        );
    }

    @Test
    @DisplayName("정보 가져오기 성공")
    void infoSuccess() throws Exception {
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest joinRequest = new MemberJoinRequest(loginId, password, username);

        String joinContent = objectMapper.writeValueAsString(joinRequest);

        mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(joinContent));

        url = "/api/member/login";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        String content = objectMapper.writeValueAsString(request);

        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        url = "/api/member/info";

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        MemberLoginResponse response = objectMapper.readValue(contentAsString, MemberLoginResponse.class);

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + response.getAccessToken()));

        //then
        actions.andExpectAll(
                status().isOk(),
                jsonPath("$.data.loginId").value(loginId),
                jsonPath("$.data.accessToken").value(response.getAccessToken())
        );
    }

    @Test
    @DisplayName("정보 가져오기 실패")
    void infoFailed() throws Exception {
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest joinRequest = new MemberJoinRequest(loginId, password, username);

        String joinContent = objectMapper.writeValueAsString(joinRequest);

        mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(joinContent));

        url = "/api/member/login";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        String content = objectMapper.writeValueAsString(request);

        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        url = "/api/member/info";

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjoiaG9uZzEyIiwiZXhwIjoxNzA4MzU3MzkzfQ.UDeqLz94_vdZEOBWlPUHY7HqDeoXb7N6JaLBmkgiDJU"));

        //then
        actions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value(messageSource.getMessage("jwt.exception", null, KOREAN))
        );
    }
}
