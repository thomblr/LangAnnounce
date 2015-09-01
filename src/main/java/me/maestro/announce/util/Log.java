package me.maestro.announce.util;

public class Log {

    public enum Level {
        INFO,
        WARNING,
        SEVERE,
        DEBUG
    }

    public static void log(Level level, String message) {
        System.out.println("[" + level.name() + "] " + message);
    }

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    public static void severe(String message) {
        log(Level.SEVERE, message);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message);
    }

}
