package exam.jin.companyexamone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam.jin.companyexamone.dto.MemberJoinRequest;
import exam.jin.companyexamone.dto.MemberJoinResult;
import exam.jin.companyexamone.dto.MemberLoginRequest;
import exam.jin.companyexamone.dto.MemberLoginResult;
import exam.jin.companyexamone.entity.Member;
import exam.jin.companyexamone.exception.MemberCanNotFindException;
import exam.jin.companyexamone.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static java.util.Locale.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {MemberController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class MemberControllerTest {

    @MockBean
    MemberService memberService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원 가입 성공")
    void joinSuccess() throws Exception{
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest request = new MemberJoinRequest(loginId, password, username);

        when(memberService.join(any(MemberJoinRequest.class)))
                .thenReturn(new MemberJoinResult(new Member(loginId, password, username)));

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
    @DisplayName("회원 가입 실패")
    void joinFail() throws Exception{
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest request = new MemberJoinRequest(loginId, password, username);

        when(memberService.join(any(MemberJoinRequest.class)))
                .thenThrow(DataIntegrityViolationException.class);

        String content = objectMapper.writeValueAsString(request);

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
    @DisplayName("회원 로그인 성공")
    void loginSuccess() throws Exception{
        //given
        String url = "/api/member/login";

        String loginId = "hong12";
        String password = "123123";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        when(memberService.login(any(MemberLoginRequest.class)))
                .thenReturn(new MemberLoginResult(loginId, "accessToken"));

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.loginId").value(loginId),
                jsonPath("$.accessToken").value("accessToken")
        );
    }

    @Test
    @DisplayName("회원 로그인 실패")
    void loginFailed() throws Exception{
        //given
        String url = "/api/member/login";

        String loginId = "hong12";
        String password = "123123";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        MemberCanNotFindException exception =
                new MemberCanNotFindException(messageSource.getMessage("member.loginFail", null, KOREAN));

        when(memberService.login(any(MemberLoginRequest.class)))
                .thenThrow(exception);

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
}