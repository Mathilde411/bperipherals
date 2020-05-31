package fr.bastoup.bperipherals.database;

import dan200.computercraft.api.lua.LuaException;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBUtil {

    public static List<Map<String, Object>> mapResults(ResultSet resultSet) throws SQLException {
        int n = resultSet.getMetaData().getColumnCount();

        String[] keys = new String[n];
        for (int i = 0; i < n; i++) {
            keys[i] = resultSet.getMetaData().getColumnName(i + 1);
        }

        List<Object[]> tableList = new ArrayList<>();
        while (resultSet.next()) {
            Object[] rows = new Object[n];
            for (int i = 0; i < n; i++) {
                rows[i] = resultSet.getObject(i + 1);
            }
            tableList.add(rows);
        }

        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        for (Object[] r : tableList) {
            res.add(map(keys, r));
        }
        return res;
    }

    public static Map<String, Object> map(String[] keys, Object[] values) {
        Map<String, Object> hMap = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            hMap.put(keys[i], values[i]);
        }
        return hMap;
    }

    public static String getForbiddenSQL(String sql) {
        for (String exp : BPeripheralsProperties.FORBIDDEN_SQL) {
            Pattern p = Pattern.compile("(?i)\\b" + exp + "\\b");
            Matcher m = p.matcher(sql);
            if (m.find())
                return exp;
        }
        return null;
    }

    public static List<Object> factorizeResults(SQLResult res) throws LuaException {
        List<Object> ret = new ArrayList<>();
        if (res instanceof QueryResult) {
            ret.add("query");
            ret.add(((QueryResult) res).getResult());
        } else if (res instanceof UpdateResult) {
            ret.add("update");
            ret.add(((UpdateResult) res).getUpdateCount());
        } else if (res instanceof ErrorResult) {
            throw new LuaException(((ErrorResult) res).getError());
        }
        return ret;
    }
}
