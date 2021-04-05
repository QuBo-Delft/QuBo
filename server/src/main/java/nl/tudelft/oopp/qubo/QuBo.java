package nl.tudelft.oopp.qubo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The startup file of the application.
 */
@SpringBootApplication
public class QuBo {

    /**
     * The entry point of this Spring application.
     *
     * @param args The input arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(QuBo.class, args);
    }

}
