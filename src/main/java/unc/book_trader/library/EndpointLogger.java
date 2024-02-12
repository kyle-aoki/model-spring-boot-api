package unc.book_trader.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndpointLogger {
    public static Logger New(String endpoint) {
        return LoggerFactory.getLogger(String.format("%s %s", RandomString.ofLength(8), endpoint));
    }
}
