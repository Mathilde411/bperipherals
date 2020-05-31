package fr.bastoup.bperipherals.database;

public class DBException extends RuntimeException {
    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }
}