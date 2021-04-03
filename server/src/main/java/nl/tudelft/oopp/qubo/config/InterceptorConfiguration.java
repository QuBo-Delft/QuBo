package nl.tudelft.oopp.qubo.config;

import nl.tudelft.oopp.qubo.interceptors.RateLimitingHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final RateLimitingHandlerInterceptor rateLimitingHandlerInterceptor;

    public InterceptorConfiguration(RateLimitingHandlerInterceptor rateLimitingHandlerInterceptor) {
        this.rateLimitingHandlerInterceptor = rateLimitingHandlerInterceptor;
    }

    /**
     * Registers the interceptors used by the application.
     *
     * @param registry The InterceptorRegistry.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(rateLimitingHandlerInterceptor)
            .addPathPatterns("/api/**");
    }
}
