package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.repositories.QuestionBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionBoardService {

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

}
