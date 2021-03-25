package nl.tudelft.oopp.demo.config.custommappings;

import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;

public class PaceVotesToPaceDetailsDtoConverter {

    private final ModelMapper mapper;

    public PaceVotesToPaceDetailsDtoConverter(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }


}
