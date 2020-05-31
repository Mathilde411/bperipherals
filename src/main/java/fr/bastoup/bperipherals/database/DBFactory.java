package fr.bastoup.bperipherals.database;

import java.sql.*;

public class DBFactory {
    private static final String URL_PREFIX = "jdbc:sqlite:";
    private static final String DRIVER = "org.sqlite.JDBC";

    public static DBFactory getInstance() throws DBException {

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new DBException("The driver could not be found", e);
        }

        DBFactory instance = new DBFactory();
        return instance;
    }

    public Connection getConnection(String path) throws SQLException {
        return DriverManager.getConnection(URL_PREFIX + path, null, null);
    }

    public SQLResult executeSQL(String path, String sql) {
        SQLResult res;
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String forbiddenSQL = DBUtil.getForbiddenSQL(sql);
            if (forbiddenSQL == null) {
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
                con.close();
            } else {
                res = new ErrorResult("Your SQL code contains forbidden instructions. (" + forbiddenSQL + ")");
            }
        } catch (SQLException e) {
            res = new ErrorResult(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ignore) {
                }
            }
        }
        return res;
    }
}
