package nl.tudelft.oopp.demo.dtos.question;

import java.util.UUID;

public class QuestionCreationDto {
    private UUID id;

    private UUID secretCode;

    public QuestionCreationDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(UUID secretCode) {
        this.secretCode = secretCode;
    }
}
