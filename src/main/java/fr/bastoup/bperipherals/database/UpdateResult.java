package fr.bastoup.bperipherals.database;

public class UpdateResult implements SQLResult {

    private final int updateCount;

    public UpdateResult(int updateCount) {
        this.updateCount = updateCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }
}
