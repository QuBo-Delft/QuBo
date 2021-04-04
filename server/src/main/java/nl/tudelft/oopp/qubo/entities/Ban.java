package nl.tudelft.oopp.qubo.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "Ban")
@Table(name = "ban")
public class Ban {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private QuestionBoard questionBoard;

    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * Instantiates a new banned IP address.
     *
     * @param questionBoard The Question Board from which the IP address is banned.
     * @param ip            The IP address to be banned.
     */
    public Ban(QuestionBoard questionBoard, String ip) {
        this.questionBoard = questionBoard;
        this.ip = ip;
    }

    /**
     * Instantiates a new Ban.
     */
    public Ban() {
    }

    /**
     * Gets the id.
     *
     * @return The unique identifier of the ban.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id The unique identifier of the ban.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the Question Board from which an IP address is banned.
     *
     * @return The Question Board.
     */
    public QuestionBoard getQuestionBoard() {
        return questionBoard;
    }

    /**
     * Sets the Question Board from which an IP address is banned.
     *
     * @param questionBoard The Question Board.
     */
    public void setQuestionBoard(QuestionBoard questionBoard) {
        this.questionBoard = questionBoard;
    }

    /**
     * Gets the banned IP address.
     *
     * @return The banned IP address.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the banned IP address.
     *
     * @param ip The banned IP address.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Ban{"
            + "id=" + id
            + ", questionBoard=" + questionBoard
            + ", ip='" + ip + '\''
            + '}';
    }
}
