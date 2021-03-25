package nl.tudelft.oopp.demo.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.dtos.question.QuestionDetailsDto;
import nl.tudelft.oopp.demo.dtos.questionboard.QuestionBoardDetailsDto;

import java.util.ArrayList;
import java.util.List;

public class StudentViewController {
    @FXML
    private ListView<Question> questionList;
    @FXML
    private HBox sideScreen;
    @FXML
    private VBox sideBar;
    @FXML
    private VBox sideMenu;
    @FXML
    private Pane paceVotePane;
    @FXML
    private ToggleButton hamburger;
    @FXML
    private ToggleButton ansQuestions;
    @FXML
    private ToggleButton polls;
    @FXML
    private Button leaveQuBo;

    private QuestionBoardDetailsDto quBo;
    private QuestionDetailsDto[] answeredQuestions;
    private QuestionDetailsDto[] unansweredQuestions;

    /**
     * Method that sets the QuestionBoardDetailsDto of the student view.
     *
     * @param quBo  The QuestionBoardDetailsDto of the question board that the student joined.
     */
    public void setQuBo(QuestionBoardDetailsDto quBo) {
        this.quBo = quBo;
    }

    /**
     * Code that is run upon loading StudentView.fxml
     */
    @FXML
    private void initialize() {
        //Display the questions
        displayQuestions();

        //Hide side menu and sidebar
        sideScreen.managedProperty().bind(sideScreen.visibleProperty());
        sideBar.managedProperty().bind(sideBar.visibleProperty());
        sideMenu.managedProperty().bind(sideMenu.visibleProperty());
        sideScreen.setVisible(false);
        sideMenu.setVisible(false);
    }

    /**
     * Method that displays the questions that are in the question board on the screen. Answered questions
     * will be sorted by the time at which they were answered, and unanswered questions will be sorted by
     * the number of upvotes they have received.
     */
    private void displayQuestions() {
        //Retrieve the questions and convert them to an array of QuestionDetailsDtos.
        String jsonQuestions = ServerCommunication.retrieveQuestions(quBo.getId());
        if (jsonQuestions == null) {
            divideQuestions(null);
        } else {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                    .create();
            QuestionDetailsDto[] questions = gson.fromJson(jsonQuestions, QuestionDetailsDto[].class);

            //Divide the questions over two lists and sort them.
            divideQuestions(questions);
        }

        //TODO: Display the questions in the list view by accessing class attributes.
    }

    /**
     * This method will be used to divide the question list into a list of answered questions,
     * and a list of unanswered questions.
     *
     * @param questions The question array that needs to be divided.
     */
    private void divideQuestions(QuestionDetailsDto[] questions) {
        //Initialise two lists to contain the answered and unanswered questions.
        List<QuestionDetailsDto> answered = new ArrayList<>();
        List<QuestionDetailsDto> unanswered = new ArrayList<>();

        if(questions == null || questions.length == 0) {
            return;
        }

        //Divide the questions over the two lists.
        for (QuestionDetailsDto question : questions) {
            if (question.getAnswered() != null) {
                answered.add(question);
            } else {
                unanswered.add(question);
            }
        }

        //Convert the list of answered and unanswered questions to arrays and store them in their
        //respective class attributes.
        answeredQuestions = answered.toArray(new QuestionDetailsDto[0]);
        unansweredQuestions = unanswered.toArray(new QuestionDetailsDto[0]);
    }

    /**
     * Toggles the visibility of the sideScreen.
     */
    public void showHideSideScreen() {
        if (sideScreen.isVisible() && sideMenu.isVisible()) {
            sideScreen.setVisible(false);
            paceVotePane.setVisible(true);
        } else {
            sideScreen.setVisible(!sideScreen.isVisible());
        }
    }

    /**
     * Toggles the visibility of the answered questions menu.
     */
    public void showHideAnsQuestions() {
        if (sideMenu.isVisible() && polls.isSelected()) {
            polls.setSelected(false);
            sideMenu.getChildren().clear();
            showAnsQuestions();
        } else if (!sideMenu.isVisible()) {
            paceVotePane.setVisible(false);
            showAnsQuestions();
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
            paceVotePane.setVisible(true);
        }
    }

    /**
     * Shows the content of the answered questions menu.
     */
    public void showAnsQuestions() {
        Label title = new Label("Answered Questions");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);

        //TODO: Fetch questions and display in a ListView
    }

    /**
     * Toggles the visibility of the poll menu.
     */
    public void showHidePolls() {
        if (sideMenu.isVisible() && ansQuestions.isSelected()) {
            ansQuestions.setSelected(false);
            sideMenu.getChildren().clear();
            showPolls();
        } else if (!sideMenu.isVisible()) {
            paceVotePane.setVisible(false);
            showPolls();
        } else {
            sideMenu.getChildren().clear();
            sideMenu.setVisible(false);
            paceVotePane.setVisible(true);

        }
    }

    /**
     * Shows the content of the poll menu.
     */
    public void showPolls() {
        Label title = new Label("Polls");
        sideMenu.setVisible(true);
        sideMenu.getChildren().add(title);

        //TODO: Fetch polls and display in a ListView
    }

    private static class Question {
        private int upvoteNumber;
        private String questionContent;

        public int getUpvoteNumber() {
            return upvoteNumber;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public Question(int upvoteNumber, String questionContent) {
            this.upvoteNumber = upvoteNumber;
            this.questionContent = questionContent;
        }
    }

    private class QuestionListCell extends ListCell<Question> {
        private GridPane content;
        private Label upvoteNumber;
        private Text questionContent;

        public QuestionListCell() {
            super();
            upvoteNumber = new Label();
            questionContent = new Text();

            //Create the Vbox for placing the upvote button and upvote number
            ToggleButton upvoteTriangle = new ToggleButton("up");
            VBox upvote = new VBox(upvoteTriangle, upvoteNumber);
            upvote.setSpacing(5);
            upvote.setAlignment(Pos.CENTER);

            //Create options menu with edit and delete options
            MenuButton options = new MenuButton();
            options.getItems().addAll(new MenuItem("Edit"), new MenuItem("Delete"));

            //Create GridPane and add nodes to it
            content = new GridPane();
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHgrow(Priority.ALWAYS);

            content.setGridLinesVisible(true);
            content.getColumnConstraints().addAll(new ColumnConstraints(50), col2,
                    new ColumnConstraints(50));

            content.addColumn(0, upvote);
            content.addColumn(1, questionContent);
            content.addColumn(2, options);

            //Make questionContent resize with width of cell
            double paddingWidth = questionList.getPadding().getLeft()
                    +  questionList.getPadding().getRight();
            questionContent.wrappingWidthProperty().bind(questionList.widthProperty()
                    .subtract(paddingWidth + 120));

            //Set alignment of children in the GridPane
            upvote.setAlignment(Pos.TOP_CENTER);
            GridPane.setValignment(options, VPos.TOP);
            GridPane.setHalignment(options, HPos.RIGHT);
        }

        @Override
        protected void updateItem(Question item, boolean empty) {
            super.updateItem(item, empty);
            //If the item was not null and empty was false, add content to the graphic
            if (item != null && !empty) {
                upvoteNumber.setText(Integer.toString(item.getUpvoteNumber()));
                questionContent.setText(item.getQuestionContent());
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

}
