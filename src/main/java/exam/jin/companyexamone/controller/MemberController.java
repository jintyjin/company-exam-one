package exam.jin.companyexamone.controller;

import exam.jin.companyexamone.dto.*;
import exam.jin.companyexamone.exception.MemberExistsException;
import exam.jin.companyexamone.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static java.util.Locale.*;
import static org.springframework.http.HttpHeaders.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final MessageSource messageSource;

    @PostMapping("/join")
    public CommonResponse<MemberJoinResult> join(@Validated @RequestBody MemberJoinRequest request) {
        try {
            return new CommonResponse(null, memberService.join(request));
        } catch (DataIntegrityViolationException exception) {
            throw new MemberExistsException(request, messageSource.getMessage("member.duplicated", null, KOREAN));
        }
    }

    @PostMapping("/login")
    public MemberLoginResponse login(@Validated @RequestBody MemberLoginRequest request) {
        return new MemberLoginResponse(memberService.login(request));
    }

    @PostMapping("/info")
    public CommonResponse<MemberInfoResult> info(HttpServletRequest request) {
        String accessToken = (String) request.getAttribute(AUTHORIZATION);
        return new CommonResponse<>("OK", memberService.info(accessToken));
    }
}
