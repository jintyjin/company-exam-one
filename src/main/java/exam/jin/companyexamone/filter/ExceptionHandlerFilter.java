package exam.jin.companyexamone.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam.jin.companyexamone.dto.CommonResponse;
import exam.jin.companyexamone.exception.JwtException;
import exam.jin.companyexamone.exception.JwtExpiredException;
import exam.jin.companyexamone.exception.JwtNotExistsException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtNotExistsException exception) {
            sendErrorMessage(response, exception.getMessage(), BAD_REQUEST.value());
        } catch (JwtException exception) {
            sendErrorMessage(response, exception.getMessage(), BAD_REQUEST.value());
        } catch (JwtExpiredException exception) {
            sendErrorMessage(response, exception.getMessage(), BAD_REQUEST.value());
        } catch (Exception exception) {
            sendErrorMessage(response, "exception", INTERNAL_SERVER_ERROR.value());
        }
    }

    private void sendErrorMessage(HttpServletResponse response, String message, int statusCode) throws IOException {
        String content = new ObjectMapper().writeValueAsString(new CommonResponse<>(message, null));
        response.setStatus(statusCode);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
    }
}
