package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainEnterHere implements ApplicationRunner {
    private static final Logger LOGGER = LogManager.getLogger(MainEnterHere.class);

    public static void main(String[] args) {
        SpringApplication.run(MainEnterHere.class, args);
    }
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LOGGER.debug("Debugging log");
        LOGGER.info("Info log");
        LOGGER.warn("This is a warning!");
        LOGGER.error("Attention! There is an Error. OK");
        LOGGER.fatal("Danger! Fatal error. Please fix it.");
        LOGGER.info("start logger");
    }
}
