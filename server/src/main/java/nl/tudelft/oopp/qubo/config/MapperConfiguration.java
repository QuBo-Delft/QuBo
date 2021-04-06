package nl.tudelft.oopp.qubo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The Mapper configuration.
 */
@Configuration
public class MapperConfiguration {

    /**
     * Set up ModelMapper.
     *
     * @return A ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
