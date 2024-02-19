package exam.jin.companyexamone.advice;

import exam.jin.companyexamone.dto.CommonResponse;
import exam.jin.companyexamone.dto.MemberExistsResult;
import exam.jin.companyexamone.exception.MemberExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<MemberExistsResult> memberExistsExceptionHandler(MemberExistsException exception) {
        return new CommonResponse<>(exception.getMessage(), new MemberExistsResult(exception.getRequest().getLoginId()));
    }
}
