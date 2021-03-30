package nl.tudelft.oopp.qubo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class CommonsRequestLoggingFilterConfig {

    /**
     * Configures the CommonsRequestLoggingFilter.
     *
     * @return The newly-created CommonsRequestLoggingFilter.
     */
    @Bean
    public CommonsRequestLoggingFilter getCommonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(3000);
        filter.setIncludeClientInfo(true);
        return filter;
    }
}
