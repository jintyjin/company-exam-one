package exam.jin.companyexamone.advice;

import exam.jin.companyexamone.dto.CommonResponse;
import exam.jin.companyexamone.dto.MemberExistsResult;
import exam.jin.companyexamone.dto.MemberLoginResult;
import exam.jin.companyexamone.exception.MemberCanNotFindException;
import exam.jin.companyexamone.exception.MemberExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class MemberAdvice {

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public CommonResponse<MemberExistsResult> memberExistsExceptionHandler(MemberExistsException exception) {
        return new CommonResponse<>(exception.getMessage(), new MemberExistsResult(exception.getRequest().getLoginId()));
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public CommonResponse<MemberLoginResult> memberCanNotFindExceptionHandler(MemberCanNotFindException exception) {
        return new CommonResponse<>(exception.getMessage(), null);
    }
}
