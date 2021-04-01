package nl.tudelft.oopp.qubo.services.profiles;

import nl.tudelft.oopp.qubo.services.providers.CurrentTimeProvider;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * A configuration profile to allow injection of a mock CurrentTimeProvider.
 */
@Profile("mockCurrentTimeProvider")
@Configuration
public class MockCurrentTimeProviderConfiguration {

    /**
     * Mocks the CurrentTimeProvider.
     *
     * @return A mocked CurrentTimeProvider.
     */
    @Bean
    @Primary
    public CurrentTimeProvider getMockCurrentTimeProvider() {
        return Mockito.mock(CurrentTimeProvider.class);
    }
}
