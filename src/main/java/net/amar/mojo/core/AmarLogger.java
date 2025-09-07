/*
 * To make it easy, i copied over the usual log
 * setup i use most of the time for my project
 * (feel free to improve what you think should get improved)
 * -amar
 */

package net.amar.mojo.core;
import org.slf4j.*;
public class AmarLogger {
    private static final Logger log = LoggerFactory.getLogger(AmarLogger.class);

    public static void info(String info){
    log.info(info);
    }

    public static void warn(String warn){
        log.warn(warn);
    }

    public static void error(String error, Throwable t){
        log.error(error,t);
    }
}
