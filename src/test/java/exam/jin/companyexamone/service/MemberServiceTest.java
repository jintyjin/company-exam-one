package exam.jin.companyexamone.service;

import exam.jin.companyexamone.dto.MemberJoinRequest;
import exam.jin.companyexamone.dto.MemberJoinResult;
import exam.jin.companyexamone.dto.MemberLoginRequest;
import exam.jin.companyexamone.dto.MemberLoginResult;
import exam.jin.companyexamone.entity.Member;
import exam.jin.companyexamone.exception.MemberCanNotFindException;
import exam.jin.companyexamone.repository.MemberRepository;
import exam.jin.companyexamone.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional(readOnly = true)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    MessageSource messageSource;

    @Test
    @Transactional
    @DisplayName("회원 가입 성공")
    void joinSuccess() {
        //given
        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        Member member = new Member(loginId, loginId, username);

        MemberJoinRequest request = new MemberJoinRequest(loginId, password, username);

        when(passwordEncoder.encode(any(String.class)))
                .thenReturn(password);

        when(memberRepository.save(any(Member.class)))
                .thenReturn(member);

        //when
        MemberJoinResult result = memberService.join(request);

        //then
        assertThat(result.getLoginId()).isEqualTo(loginId);
        assertThat(result.getUsername()).isEqualTo(username);
    }

    @Test
    @Transactional
    @DisplayName("중복 회원 가입")
    void joinFail() {
        //given
        String loginId = "hong12";
        String password = "123123";
        String username = "홍길동";

        Member member = new Member(loginId, loginId, username);

        MemberJoinRequest request = new MemberJoinRequest(loginId, password, username);

        when(passwordEncoder.encode(any(String.class)))
                .thenReturn(password);

        when(memberRepository.save(any(Member.class)))
                .thenThrow(DataIntegrityViolationException.class);

        //when //then
        assertThatThrownBy(() -> memberService.join(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        //given
        String loginId = "hong12";
        String password = "123123";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        when(memberRepository.findByLoginId(any(String.class)))
                .thenReturn(of(new Member(loginId, password, "홍길동")));

        when(passwordEncoder.matches(any(String.class), any(String.class)))
                .thenReturn(true);

        when(jwtUtil.createJwt(any(String.class), any(String.class), any(Long.class)))
                .thenReturn("accessToken");

        //when
        MemberLoginResult result = memberService.login(request);

        //then
        assertThat(result.getLoginId()).isEqualTo(loginId);
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("로그인 실패 - 회원 없음")
    void loginFailed() {
        //given
        String loginId = "hong12";
        String password = "123123";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        when(memberRepository.findByLoginId(any(String.class)))
                .thenReturn(ofNullable(null));

        //when //then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(MemberCanNotFindException.class);
    }

    @Test
    @DisplayName("로그인 실패 - 암호 틀림")
    void loginFailedByPassword() {
        //given
        String loginId = "hong12";
        String password = "123123";

        MemberLoginRequest request = new MemberLoginRequest(loginId, password);

        when(memberRepository.findByLoginId(any(String.class)))
                .thenReturn(of(new Member(loginId, password, "홍길동")));

        when(passwordEncoder.matches(any(String.class), any(String.class)))
                .thenReturn(false);

        //when //then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(MemberCanNotFindException.class);
    }
}