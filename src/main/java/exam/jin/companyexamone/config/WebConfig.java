package exam.jin.companyexamone.config;

import exam.jin.companyexamone.filter.LoginFilter;
import exam.jin.companyexamone.util.JwtUtil;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final MessageSource messageSource;
    private final JwtUtil jwtUtil;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginFilter(messageSource, jwtUtil));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
