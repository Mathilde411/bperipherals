package fr.bastoup.bperipherals.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.beans.SQLColumn;
import fr.bastoup.bperipherals.database.DBUtil;
import fr.bastoup.bperipherals.tileentities.TileDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeripheralDatabase implements IPeripheral {

    public static final String TYPE = "database";

    private final TileDatabase tile;

    public PeripheralDatabase(TileDatabase tile) {
        this.tile = tile;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }

    @Override
    public Object getTarget() {
        return tile;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof PeripheralDatabase && ((TileDatabase) other.getTarget()).getWorld().equals(tile.getWorld()) &&
                ((TileDatabase) other.getTarget()).getPos().equals(tile.getPos());
    }

    @LuaFunction
    public final boolean isDiskInserted() {
        return tile.isDiskInserted();
    }

    @LuaFunction
    public final int getDatabaseId() throws LuaException {
        if (!tile.isDiskInserted())
            throw new LuaException("There is no disk inserted");
        return tile.getDatabaseId();
    }

    @LuaFunction
    public final String getDatabaseName() throws LuaException {
        if (!tile.isDiskInserted())
            throw new LuaException("There is no disk inserted");
        return tile.getDatabaseName();
    }

    @LuaFunction
    public final void setDatabaseName(String name) throws LuaException {
        if (!tile.isDiskInserted())
            throw new LuaException("There is no disk inserted");
        tile.setDatabaseName(name);
    }

    @LuaFunction
    public final List<Object> executeSQL(String sql) throws LuaException {
        File file = tile.getDatabaseFile();
        if (tile.isDiskInserted()) {
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
            throw new LuaException("The table names must only contain alphanumeric symbols and cannot start with a number.");
        }
        return new CCInsert(tableName, this);
    }

    @LuaFunction
    public final CCTableCreator prepareTableCreation(String tableName) throws LuaException {
        if (!tableName.matches("^[A-Za-z_]\\w*$")) {
            throw new LuaException("The table names must only contain alphanumeric symbols and cannot start with a number.");
        }
        return new CCTableCreator(tableName, this);
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
        public final void setParameter(int index, Object obj) throws LuaException {
            if (index < 1)
                throw new LuaException("Index must be equal or greater than 1.");
            parameters.put(index, obj);
        }

        @LuaFunction
        public final void removeParameter(int index) throws LuaException {
            if (index < 1)
                throw new LuaException("Index must be equal or greater than 1.");
            parameters.remove(index);
        }

        @LuaFunction
        public final List<Object> execute() throws LuaException {
            peripheralStillValid();
            TileDatabase tile = (TileDatabase) database.getTarget();
            File file = tile.getDatabaseFile();
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
        public final void addColumn(String name, String type) throws LuaException {
            addColumn(name, type, false, false);
        }

        @LuaFunction
        public final void addColumn(String name, String type, boolean notNull, boolean isUnique) throws LuaException {
            if (!name.matches("^[A-Za-z_]\\w*$")) {
                throw new LuaException("The column names must only contain alphanumeric symbols and cannot start with a number.");
            }

            if (!(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("integer") ||
                    type.equalsIgnoreCase("real") || type.equalsIgnoreCase("blob"))) {
                throw new LuaException("Type must be either TEXT, INTEGER, REAL or BLOB.");
            }

            columns.put(name.toLowerCase(), new SQLColumn(name, type, notNull, isUnique));
        }

        @LuaFunction
        public final void removeColumn(String name) throws LuaException {
            if (primaryKey.equalsIgnoreCase(name)) {
                primaryKey = null;
                autoIncrement = false;
            }
            columns.remove(name.toLowerCase());
        }

        @LuaFunction
        public final void setPrimaryKey(String name, boolean autoIncrement) throws LuaException {
            if (!columns.containsKey(name.toLowerCase())) {
                throw new LuaException("This column does not exist.");
            }

            if (autoIncrement && !columns.get(name.toLowerCase()).getType().equalsIgnoreCase("integer")) {
                throw new LuaException("This column is not INTEGER. Cannot auto increment.");
            }

            this.primaryKey = name.toLowerCase();
            this.autoIncrement = autoIncrement;

        }

        @LuaFunction
        public final List<Object> execute() throws LuaException {
            peripheralStillValid();
            TileDatabase tile = (TileDatabase) database.getTarget();
            File file = tile.getDatabaseFile();
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
        public final void addValue(String column, Object value) throws LuaException {
            values.put(column.toLowerCase(), value);
        }

        @LuaFunction
        public final void removeValue(String column) throws LuaException {
            values.remove(column);
        }


        @LuaFunction
        public final List<Object> execute() throws LuaException {
            peripheralStillValid();
            TileDatabase tile = (TileDatabase) database.getTarget();
            File file = tile.getDatabaseFile();
            if (tile.isDiskInserted()) {
                List<String> s = new ArrayList<>();
                Map<Integer, Object> obj = new HashMap<>();
                String[] keys = (String[]) values.keySet().toArray();
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
}
