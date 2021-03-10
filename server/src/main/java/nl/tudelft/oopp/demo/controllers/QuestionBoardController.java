package nl.tudelft.oopp.demo.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.sql.Timestamp;

import nl.tudelft.oopp.demo.bindingmodels.QuestionBoardBindingModel;
import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.services.QuestionBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuestionBoardController {

    @Autowired
    QuestionBoardService service;

    @RequestMapping(value = "/api/board", method = POST, consumes = "application/json")
    @ResponseBody
    public QuestionBoard createBoard(@RequestBody QuestionBoardBindingModel qb) {
        return service.saveBoard(qb);
    }
}
