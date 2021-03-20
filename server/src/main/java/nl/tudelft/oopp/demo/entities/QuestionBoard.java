package nl.tudelft.oopp.demo.entities;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "question_boards", indexes = {
    @Index(columnList = "moderator_code", unique = true)
})
public class QuestionBoard {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @Column(name = "moderator_code", nullable = false)
    private UUID moderatorCode;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "startTime", nullable = false)
    private Timestamp startTime;

    @Column(name = "closed", nullable = false)
    private boolean closed;

    @OneToMany(mappedBy = "questionBoard")
    private Set<Question> questions;

    @OneToMany(mappedBy = "questionBoard")
    private Set<PaceVote> paceVotes;

    /**
     * Create a new QuestionBoard instance.
     *
     * @param moderatorCode A secret code to be used by moderators.
     * @param title         The title of the question board.
     * @param startTime     The start time of the question board.
     */
    public QuestionBoard(UUID moderatorCode, String title, Timestamp startTime) {
        this.moderatorCode = moderatorCode;
        this.title = title;
        this.startTime = startTime;
        this.closed = false;
    }

    /**
     * Create a new QuestionBoard instance and automatically generate a moderatorCode.
     *
     * @param title     The title of the question board.
     * @param startTime The start time of the question board.
     */
    public QuestionBoard(String title, Timestamp startTime) {
        this(UUID.randomUUID(), title, startTime);
    }

    public QuestionBoard() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getModeratorCode() {
        return moderatorCode;
    }

    public void setModeratorCode(UUID moderatorCode) {
        this.moderatorCode = moderatorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<PaceVote> getPaceVotes() {
        return paceVotes;
    }

    public void setPaceVotes(Set<PaceVote> paceVotes) {
        this.paceVotes = paceVotes;
    }
}
