package nl.tudelft.oopp.demo.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "question_votes")
public class PaceVote {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "question_board_id", nullable = false)
    private QuestionBoard questionBoard;

    @Column(name = "pace_type", nullable = false)
    private PaceType paceType;

    public PaceVote(QuestionBoard questionBoard, PaceType paceType) {
        this.questionBoard = questionBoard;
        this.paceType = paceType;
    }

    public PaceVote() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public QuestionBoard getQuestionBoard() {
        return questionBoard;
    }

    public void setQuestionBoard(QuestionBoard questionBoard) {
        this.questionBoard = questionBoard;
    }

    public PaceType getPaceType() {
        return paceType;
    }

    public void setPaceType(PaceType paceType) {
        this.paceType = paceType;
    }
}
