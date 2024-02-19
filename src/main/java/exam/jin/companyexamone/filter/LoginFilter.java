package exam.jin.companyexamone.filter;

import exam.jin.companyexamone.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Locale.*;

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
            String message = messageSource.getMessage("jwt.notExists", null, KOREAN);
            if (accessToken == null || accessToken.startsWith("Bearer") || jwtUtil.isExpired(accessToken)) {
                response.getWriter().write(message);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
