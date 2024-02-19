package exam.jin.companyexamone.service;

import exam.jin.companyexamone.dto.MemberJoinRequest;
import exam.jin.companyexamone.dto.MemberJoinResult;
import exam.jin.companyexamone.dto.MemberLoginRequest;
import exam.jin.companyexamone.dto.MemberLoginResult;
import exam.jin.companyexamone.entity.Member;
import exam.jin.companyexamone.exception.MemberCanNotFindException;
import exam.jin.companyexamone.repository.MemberRepository;
import exam.jin.companyexamone.util.JwtUtil;
import exam.jin.companyexamone.dto.*;
import exam.jin.companyexamone.exception.JwtExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Locale.*;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageSource messageSource;
	private final JwtUtil jwtUtil;

	public MemberJoinResult join(MemberJoinRequest request) {
		Member member = new Member(request.getLoginId(), passwordEncoder.encode(request.getPassword()), request.getUsername());
		return new MemberJoinResult(memberRepository.save(member));
	}

	public MemberLoginResult login(MemberLoginRequest request) {
		String loginId = request.getLoginId();
		String password = request.getPassword();

		String message = messageSource.getMessage("member.loginFail", null, KOREAN);
		MemberCanNotFindException memberCanNotFindException = new MemberCanNotFindException(message);

		Member findMember = memberRepository.findByLoginId(loginId)
				.orElseThrow(() -> memberCanNotFindException);

		if (!passwordEncoder.matches(password, findMember.getPassword())) {
			throw memberCanNotFindException;
		}

		String accessToken = jwtUtil.createJwt(
				loginId,
				1000 * 60 * 30L
		);

		return new MemberLoginResult(loginId, accessToken);
	}

	public MemberInfoResult info(String accessToken) {
		String loginId = jwtUtil.getLoginId(accessToken);

		String message = messageSource.getMessage("member.loginFail", null, KOREAN);
		MemberCanNotFindException memberCanNotFindException = new MemberCanNotFindException(message);

		Member findMember = memberRepository.findByLoginId(loginId)
				.orElseThrow(() -> memberCanNotFindException);

		if (jwtUtil.isExpired(accessToken)) {
			message = messageSource.getMessage("member.loginFail", null, KOREAN);
			JwtExpiredException jwtExpiredException = new JwtExpiredException(message);
			throw jwtExpiredException;
		}

		return new MemberInfoResult(findMember.getLoginId(), findMember.getUsername(), accessToken);
	}
}
