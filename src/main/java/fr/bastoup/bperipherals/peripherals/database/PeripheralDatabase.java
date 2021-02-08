package fr.bastoup.bperipherals.peripherals.database;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.beans.SQLColumn;
import fr.bastoup.bperipherals.database.DBUtil;
import fr.bastoup.bperipherals.util.peripherals.BPeripheral;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeripheralDatabase extends BPeripheral {

    public static final String TYPE = "database";

    public PeripheralDatabase(TileDatabase tile) {
        super(tile);
    }

    private TileDatabase getTile() {
        return ((TileDatabase) tile);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof PeripheralDatabase && ((TileDatabase) other.getTarget()).getWorld().equals(tile.getWorld()) &&
                ((TileDatabase) other.getTarget()).getPos().equals(tile.getPos());
    }

    @LuaFunction
    public final boolean isDiskInserted() {
        return getTile().isDiskInserted();
    }

    @LuaFunction
    public final int getDatabaseId() throws LuaException {
        if (!getTile().isDiskInserted())
            throw new LuaException("There is no disk inserted");
        return getTile().getDatabaseId();
    }

    @LuaFunction
    public final String getDatabaseName() throws LuaException {
        if (!getTile().isDiskInserted())
            throw new LuaException("There is no disk inserted");
        return getTile().getDatabaseName();
    }

    @LuaFunction
    public final void setDatabaseName(String name) throws LuaException {
        if (!getTile().isDiskInserted())
            throw new LuaException("There is no disk inserted");
        getTile().setDatabaseName(name);
    }

    @LuaFunction
    public final List<Object> executeSQL(String sql) throws LuaException {
        File file = null;
        try {
            file = getTile().getDatabaseFile();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (getTile().isDiskInserted()) {
            return DBUtil.factorizeResults(BPeripherals.getDBFactory().executeSQL(file.getPath(), sql));
        } else {
            throw new LuaException("There is no disk inserted");
        }
    }

    @LuaFunction
    public final CCPreparedStatement prepareStatement(String sql) {
        return new CCPreparedStatement(sql, this);
    }

    @LuaFunction
    public final CCInsert prepareInsert(String tableName) throws LuaException {
        if (!tableName.matches("^[A-Za-z_]\\w*$")) {
            throw new LuaException("The table names may only contain alphanumeric symbols and cannot start with a number.");
        }
        return new CCInsert(tableName, this);
    }

    @LuaFunction
    public final CCTableCreator prepareTableCreation(String tableName) throws LuaException {
        if (!tableName.matches("^[A-Za-z_]\\w*$")) {
            throw new LuaException("The table names may only contain alphanumeric symbols and cannot start with a number.");
        }
        return new CCTableCreator(tableName, this);
    }

    @LuaFunction
    public final CCSelect prepareSelect(String tableName) throws LuaException {
        if (!tableName.matches("^[A-Za-z_]\\w*$")) {
            throw new LuaException("The table names may only contain alphanumeric symbols and cannot start with a number.");
        }
        return new CCSelect(tableName, this);
    }

    @LuaFunction
    public final CCDelete prepareDelete(String tableName) throws LuaException {
        if (!tableName.matches("^[A-Za-z_]\\w*$")) {
            throw new LuaException("The table names may only contain alphanumeric symbols and cannot start with a number.");
        }
        return new CCDelete(tableName, this);
    }

    public static class CCPreparedStatement {
        private final PeripheralDatabase database;
        private final String sql;
        private final Map<Integer, Object> parameters;

        CCPreparedStatement(String sql, PeripheralDatabase database) {
            this.sql = sql;
            this.database = database;
            this.parameters = new HashMap<>();
        }

        CCPreparedStatement(String sql, Map<Integer, Object> parameters, PeripheralDatabase database) {
            this.sql = sql;
            this.database = database;
            this.parameters = parameters;
        }

        private void peripheralStillValid() throws LuaException {
            TileDatabase t = (TileDatabase) database.getTarget();
            if (t.isRemoved())
                throw new LuaException("The peripheral does not exist.");
        }

        @LuaFunction
        public final CCPreparedStatement setParameter(int index, Object obj) throws LuaException {
            if (index < 1)
                throw new LuaException("Index must be equal or greater than 1.");
            parameters.put(index, obj);
            return this;
        }

        @LuaFunction
        public final CCPreparedStatement removeParameter(int index) throws LuaException {
            if (index < 1)
                throw new LuaException("Index must be equal or greater than 1.");
            parameters.remove(index);
            return this;
        }

        @LuaFunction
        public final List<Object> execute() throws LuaException {
            peripheralStillValid();
            TileDatabase tile = (TileDatabase) database.getTarget();
            File file = null;
            try {
                file = tile.getDatabaseFile();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (tile.isDiskInserted()) {
                return DBUtil.factorizeResults(BPeripherals.getDBFactory().executePrepared(file.getPath(), this));
            } else {
                throw new LuaException("There is no disk inserted");
            }
        }

        public String getSQL() {
            return sql;
        }

        public Map<Integer, Object> getParameters() {
            return parameters;
        }
    }

    public static class CCTableCreator {
        private final PeripheralDatabase database;
        private final String tableName;
        private final Map<String, SQLColumn> columns = new HashMap<>();
        private String primaryKey = null;
        private boolean autoIncrement = false;

        CCTableCreator(String tableName, PeripheralDatabase database) {
            this.tableName = tableName;
            this.database = database;
        }

        private void peripheralStillValid() throws LuaException {
            TileDatabase t = (TileDatabase) database.getTarget();
            if (t.isRemoved())
                throw new LuaException("The peripheral does not exist.");
        }

        @LuaFunction
        public final CCTableCreator addColumn(String name, String type) throws LuaException {
            return addColumn(name, type, false, false);

        }

        @LuaFunction
        public final CCTableCreator addColumn(String name, String type, boolean notNull, boolean isUnique) throws LuaException {
            if (!name.matches("^[A-Za-z_]\\w*$")) {
                throw new LuaException("The column names may only contain alphanumeric symbols and cannot start with a number.");
            }

            if (!(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("integer") ||
                    type.equalsIgnoreCase("real") || type.equalsIgnoreCase("blob"))) {
                throw new LuaException("Type must be either TEXT, INTEGER, REAL or BLOB.");
            }

            columns.put(name.toLowerCase(), new SQLColumn(name, type, notNull, isUnique));
            return this;
        }

        @LuaFunction
        public final CCTableCreator removeColumn(String name) throws LuaException {
            if (primaryKey.equalsIgnoreCase(name)) {
                primaryKey = null;
                autoIncrement = false;
            }
            columns.remove(name.toLowerCase());
            return this;
        }

        @LuaFunction
        public final CCTableCreator setPrimaryKey(String name, boolean autoIncrement) throws LuaException {
            if (!columns.containsKey(name.toLowerCase())) {
                throw new LuaException("This column does not exist.");
            }

            if (autoIncrement && !columns.get(name.toLowerCase()).getType().equalsIgnoreCase("integer")) {
                throw new LuaException("This column is not INTEGER. Cannot auto increment.");
            }

            this.primaryKey = name.toLowerCase();
            this.autoIncrement = autoIncrement;
            return this;

        }

        @LuaFunction
        public final List<Object> execute() throws LuaException {
            peripheralStillValid();
            TileDatabase tile = (TileDatabase) database.getTarget();
            File file = null;
            try {
                file = tile.getDatabaseFile();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (tile.isDiskInserted()) {
                if (this.primaryKey == null) {
                    if (columns.containsKey("id")) {
                        throw new LuaException("Column id already exists, cannot create primary key.");
                    }
                    addColumn("id", "INTEGER");
                    setPrimaryKey("id", true);
                }
                List<String> statements = new ArrayList<>();
                for (String key : columns.keySet()) {
                    SQLColumn col = columns.get(key);
                    String statement = col.getName() + " " + col.getType();
                    if (col.getName().equalsIgnoreCase(this.primaryKey)) {
                        statement += " PRIMARY KEY" + (this.autoIncrement ? " AUTOINCREMENT" : "");
                    } else {
                        statement += (col.isUnique() ? " UNIQUE" : "") + (col.isNotNull() ? " NOT NULL" : "");
                    }
                    statements.add(statement);
                }
                String sql = "CREATE TABLE " + tableName + " (" + String.join(", ", statements) + ");";
                return DBUtil.factorizeResults(BPeripherals.getDBFactory().executeSQL(file.getPath(), sql));
            } else {
                throw new LuaException("There is no disk inserted");
            }
        }
    }

    public static class CCInsert {
        private final PeripheralDatabase database;
        private final String tableName;
        private final Map<String, Object> values = new HashMap<>();

        CCInsert(String tableName, PeripheralDatabase database) {
            this.tableName = tableName;
            this.database = database;
        }

        private void peripheralStillValid() throws LuaException {
            TileDatabase t = (TileDatabase) database.getTarget();
            if (t.isRemoved())
                throw new LuaException("The peripheral does not exist.");
        }

        @LuaFunction
        public final CCInsert addValue(String column, Object value) throws LuaException {
            values.put(column.toLowerCase(), value);
            return this;
        }

        @LuaFunction
        public final CCInsert removeValue(String column) throws LuaException {
            values.remove(column);
            return this;
        }


        @LuaFunction
        public final List<Object> execute() throws LuaException {
            peripheralStillValid();
            TileDatabase tile = (TileDatabase) database.getTarget();
            if (tile.isDiskInserted()) {
                List<String> s = new ArrayList<>();
                Map<Integer, Object> obj = new HashMap<>();
                Object[] keys = values.keySet().toArray();
                for (int i = 1; i <= values.size(); i++) {
                    obj.put(i, values.get(keys[i - 1]));
                    s.add("?");
                }
                String sql = "INSERT INTO " + tableName + " (" + String.join(", ", values.keySet()) +
                        ") VALUES (" + String.join(", ", s) + ");";

                return new CCPreparedStatement(sql, obj, database).execute();
            } else {
                throw new LuaException("There is no disk inserted");
            }
        }
    }

    public static class CCSelect {
        private final PeripheralDatabase database;
        private final String tableName;
        private final Map<String, Object> conditions = new HashMap<>();

        CCSelect(String tableName, PeripheralDatabase database) {
            this.tableName = tableName;
            this.database = database;
        }

        private void peripheralStillValid() throws LuaException {
            TileDatabase t = (TileDatabase) database.getTarget();
            if (t.isRemoved())
                throw new LuaException("The peripheral does not exist.");
        }

        @LuaFunction
        public final CCSelect addCondition(String column, Object value) throws LuaException {
            conditions.put(column.toLowerCase(), value);
            return this;
        }

        @LuaFunction
        public final CCSelect removeCondition(String column) throws LuaException {
            conditions.remove(column);
            return this;
        }


        @LuaFunction
        public final List<Object> execute() throws LuaException {
            peripheralStillValid();
            TileDatabase tile = (TileDatabase) database.getTarget();
            if (tile.isDiskInserted()) {
                Map<Integer, Object> obj = new HashMap<>();
                List<String> k = new ArrayList<>();
                Object[] keys = conditions.keySet().toArray();
                for (int i = 1; i <= conditions.size(); i++) {
                    obj.put(i, conditions.get(keys[i - 1]));
                    k.add(keys[i - 1] + " = ?");
                }
                String sql = "SELECT * FROM " + tableName + (conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", k)) + ";";
                return new CCPreparedStatement(sql, obj, database).execute();
            } else {
                throw new LuaException("There is no disk inserted");
            }
        }
    }

    public static class CCDelete {
        private final PeripheralDatabase database;
        private final String tableName;
        private final Map<String, Object> conditions = new HashMap<>();

        CCDelete(String tableName, PeripheralDatabase database) {
            this.tableName = tableName;
            this.database = database;
        }

        private void peripheralStillValid() throws LuaException {
            TileDatabase t = (TileDatabase) database.getTarget();
            if (t.isRemoved())
                throw new LuaException("The peripheral does not exist.");
        }

        @LuaFunction
        public final CCDelete addCondition(String column, Object value) throws LuaException {
            conditions.put(column.toLowerCase(), value);
            return this;
        }

        @LuaFunction
        public final CCDelete removeCondition(String column) throws LuaException {
            conditions.remove(column);
            return this;
        }


        @LuaFunction
        public final List<Object> execute() throws LuaException {
            peripheralStillValid();
            TileDatabase tile = (TileDatabase) database.getTarget();
            if (tile.isDiskInserted()) {
                Map<Integer, Object> obj = new HashMap<>();
                List<String> k = new ArrayList<>();
                Object[] keys = conditions.keySet().toArray();
                for (int i = 1; i <= conditions.size(); i++) {
                    obj.put(i, conditions.get(keys[i - 1]));
                    k.add(keys[i - 1] + " = ?");
                }
                String sql = "DELETE FROM " + tableName + (conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", k)) + ";";
                return new CCPreparedStatement(sql, obj, database).execute();
            } else {
                throw new LuaException("There is no disk inserted");
            }
        }
    }
}
