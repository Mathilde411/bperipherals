package fr.bastoup.bperipherals.database;

import fr.bastoup.bperipherals.beans.ErrorResult;
import fr.bastoup.bperipherals.beans.QueryResult;
import fr.bastoup.bperipherals.beans.SQLResult;
import fr.bastoup.bperipherals.beans.UpdateResult;
import fr.bastoup.bperipherals.peripherals.database.PeripheralDatabase;
import fr.bastoup.bperipherals.util.Config;

import java.sql.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class DBFactory {
    private static final String URL_PREFIX = "jdbc:sqlite:";
    private static final String URL_SUFFIX = "?limit_attached=0&page_size=1024&max_page_count=%d";
    private final SortedMap<String, Connection> conn_map=new TreeMap<>();

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBFactory getInstance() {

        return new DBFactory();
    }

    public Connection getConnection(String path) throws SQLException {
        Connection conn=conn_map.get(path);
        if(conn!=null && !conn.isClosed()){
            return conn;
        }
        if(conn!=null) { //if closed remove it
            conn_map.remove(path);
            conn=null;
        }
        conn=DriverManager.getConnection(URL_PREFIX + path + String.format(URL_SUFFIX, Config.MAX_DATABASE_SIZE), null, null);
        conn_map.put(path, conn);
        return conn;
    }

    public SQLResult executeSQL(String path, String sql) {
        SQLResult res;
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            con = getConnection(path);
            statement = con.createStatement();
            boolean stmtExec = statement.execute(sql);
            if (stmtExec) {
                resultSet = statement.getResultSet();
                res = new QueryResult(DBUtil.mapResults(resultSet));
            } else {
                res = new UpdateResult(statement.getUpdateCount());
            }
            statement.close();
            //con.close();
        } catch (SQLException e) {
            res = new ErrorResult(e.getMessage());
        } finally {
            DBUtil.closeAll(statement, resultSet);
        }
        return res;
    }

    public SQLResult executePrepared(String path, PeripheralDatabase.CCPreparedStatement statement) {
        SQLResult res;
        Connection con = null;
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;
        try {
            con = getConnection(path);
            prepStatement = con.prepareStatement(statement.getSQL());
            for (int key : statement.getParameters().keySet()) {
                prepStatement.setObject(key, statement.getParameters().get(key));
            }
            boolean stmtExec = prepStatement.execute();
            if (stmtExec) {
                resultSet = prepStatement.getResultSet();
                res = new QueryResult(DBUtil.mapResults(resultSet));
            } else {
                res = new UpdateResult(prepStatement.getUpdateCount());
            }
        } catch (SQLException e) {
            res = new ErrorResult(e.getMessage());
        } finally {
            DBUtil.closeAll(prepStatement, resultSet);
        }
        return res;
    }
}
