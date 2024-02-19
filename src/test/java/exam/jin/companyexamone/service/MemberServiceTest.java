package exam.jin.companyexamone.service;

import exam.jin.companyexamone.dto.MemberJoinRequest;
import exam.jin.companyexamone.dto.MemberJoinResult;
import exam.jin.companyexamone.entity.Member;
import exam.jin.companyexamone.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
}