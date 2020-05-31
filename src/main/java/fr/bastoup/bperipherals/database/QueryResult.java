package fr.bastoup.bperipherals.database;

import java.util.List;
import java.util.Map;

public class QueryResult implements SQLResult {
    private final List<Map<String, Object>> result;

    public QueryResult(List<Map<String, Object>> result) {
        this.result = result;
    }

    public List<Map<String, Object>> getResult() {
        return result;
    }
}
