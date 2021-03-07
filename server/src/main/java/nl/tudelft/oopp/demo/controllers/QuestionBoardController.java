package nl.tudelft.oopp.demo.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.sql.Timestamp;

import nl.tudelft.oopp.demo.entities.QuestionBoard;
import nl.tudelft.oopp.demo.services.QuestionBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuestionBoardController {

    @Autowired
    QuestionBoardService service;

    @RequestMapping(value = "/api/board", method = POST)
    @ResponseBody
    public QuestionBoard createBoard(@RequestParam("title") String title,
                                     @RequestParam("startdate") Timestamp startDate,
                                     @RequestParam("enddate") Timestamp endDate) {
        return service.saveBoard(new QuestionBoard(title, startDate, endDate));
    }
}
