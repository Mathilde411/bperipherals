package fr.bastoup.bperipherals.beans;

public class ErrorResult implements SQLResult {

    private final String error;

    public ErrorResult(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
