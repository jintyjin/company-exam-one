package exam.jin.companyexamone.service;

import exam.jin.companyexamone.dto.MemberJoinRequest;
import exam.jin.companyexamone.dto.MemberJoinResult;
import exam.jin.companyexamone.entity.Member;
import exam.jin.companyexamone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberJoinResult join(MemberJoinRequest request) {
        Member member = new Member(request.getLoginId(), passwordEncoder.encode(request.getPassword()), request.getUsername());
        return new MemberJoinResult(memberRepository.save(member));
    }
}
