package exam.jin.companyexamone.repository;

import exam.jin.companyexamone.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional(readOnly = true)
    @DisplayName("아이디로 회원 찾기")
    void searchByLoginId() {
        //given
        Member member = new Member("hong12", "123123", "홍길동");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByLoginId("hong12").get();

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
    }
}