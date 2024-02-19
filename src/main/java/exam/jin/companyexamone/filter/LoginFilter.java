package exam.jin.companyexamone.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import exam.jin.companyexamone.dto.CommonResponse;
import exam.jin.companyexamone.exception.JwtException;
import exam.jin.companyexamone.exception.JwtExpiredException;
import exam.jin.companyexamone.exception.JwtNotExistsException;
import exam.jin.companyexamone.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Locale.*;
import static org.springframework.http.HttpHeaders.*;

@RequiredArgsConstructor
public class LoginFilter extends OncePerRequestFilter {

    private String[] whiteList = {"/api/member/join", "/api/member/login"};

    private final MessageSource messageSource;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (isLoginCheckPath(requestURI)) {
            String accessToken = request.getHeader("Authorization");
            if (accessToken == null) {
                throw new JwtNotExistsException(messageSource.getMessage("jwt.notExists", null, KOREAN));
            } else if (!accessToken.startsWith("Bearer ")) {
                throw new JwtException(messageSource.getMessage("jwt.exception", null, KOREAN));
            }
            try {
                accessToken = accessToken.split(" ")[1];
                if (jwtUtil.isExpired(accessToken)) {
                    throw new JwtExpiredException(messageSource.getMessage("jwt.isExpired", null, KOREAN));
                }

                request.setAttribute(AUTHORIZATION, accessToken);
            } catch (ArrayIndexOutOfBoundsException exception) {
                throw new JwtException(messageSource.getMessage("jwt.exception", null, KOREAN));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
