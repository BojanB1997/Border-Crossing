package Aplikacija;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MojLogger {
    static private FileHandler file;
    static private SimpleFormatter formatter;
    static private Logger logger;

    public static void setup() throws IOException {
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);
        file = new FileHandler("logger.txt", true);
        formatter = new SimpleFormatter();
        file.setFormatter(formatter);
        logger.addHandler(file);
    }

    public static void log(Level level, String poruka, Exception e) {
        logger.log(level, poruka, e);
    }

}
