package nl.tudelft.oopp.qubo.entities;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * The Question board entity.
 */
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

    @OneToOne(mappedBy = "questionBoard")
    private Poll poll;

    @OneToMany(mappedBy = "questionBoard")
    private Set<Ban> bans;

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

    /**
     * Instantiates a new Question board.
     */
    public QuestionBoard() {
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id The id.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets moderator code.
     *
     * @return the moderator code
     */
    public UUID getModeratorCode() {
        return moderatorCode;
    }

    /**
     * Sets moderator code.
     *
     * @param moderatorCode The moderator code.
     */
    public void setModeratorCode(UUID moderatorCode) {
        this.moderatorCode = moderatorCode;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime The start time.
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Sets closed.
     *
     * @param closed The closed.
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     * Gets questions.
     *
     * @return the questions
     */
    public Set<Question> getQuestions() {
        return questions;
    }

    /**
     * Sets questions.
     *
     * @param questions The questions.
     */
    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    /**
     * Gets pace votes.
     *
     * @return the pace votes
     */
    public Set<PaceVote> getPaceVotes() {
        return paceVotes;
    }

    /**
     * Sets pace votes.
     *
     * @param paceVotes The pace votes.
     */
    public void setPaceVotes(Set<PaceVote> paceVotes) {
        this.paceVotes = paceVotes;
    }

    /**
     * Gets poll.
     *
     * @return the poll
     */
    public Poll getPoll() {
        return poll;
    }

    /**
     * Sets poll.
     *
     * @param poll The poll.
     */
    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    /**
     * Gets bans.
     *
     * @return the bans
     */
    public Set<Ban> getBans() {
        return bans;
    }

    /**
     * Sets bans.
     *
     * @param bans The bans.
     */
    public void setBans(Set<Ban> bans) {
        this.bans = bans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuestionBoard board = (QuestionBoard) o;
        return closed == board.closed
            && Objects.equals(id, board.id)
            && Objects.equals(moderatorCode, board.moderatorCode)
            && Objects.equals(title, board.title)
            && Objects.equals(startTime, board.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moderatorCode, title, startTime, closed);
    }
}
