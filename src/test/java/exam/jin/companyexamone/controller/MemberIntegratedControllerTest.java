package exam.jin.companyexamone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam.jin.companyexamone.dto.MemberJoinRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Locale.KOREAN;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class MemberIntegratedControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messageSource;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원 가입 성공")
    void joinSuccess() throws Exception {
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest request = new MemberJoinRequest(loginId, password, username);

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
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
    void joinDuplicated() throws Exception {
        //given
        String url = "/api/member/join";

        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        MemberJoinRequest request = new MemberJoinRequest(loginId, password, username);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value(messageSource.getMessage("member.duplicated", null, KOREAN)),
                jsonPath("$.data.loginId").value(loginId)
        );
    }
}