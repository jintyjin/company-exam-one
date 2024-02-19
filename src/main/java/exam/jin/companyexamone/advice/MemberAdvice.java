package exam.jin.companyexamone.advice;

import exam.jin.companyexamone.dto.CommonResponse;
import exam.jin.companyexamone.dto.MemberExistsResult;
import exam.jin.companyexamone.dto.MemberJoinResult;
import exam.jin.companyexamone.dto.MemberLoginResult;
import exam.jin.companyexamone.exception.MemberCanNotFindException;
import exam.jin.companyexamone.exception.MemberExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static java.util.Locale.KOREAN;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class MemberAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public CommonResponse<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors()
                    .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

            return new CommonResponse<>(null, errors);
        }

        throw new RuntimeException(messageSource.getMessage("exception", null, KOREAN), ex);
    }

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
