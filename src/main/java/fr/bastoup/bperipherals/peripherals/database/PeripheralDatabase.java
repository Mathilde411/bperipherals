package fr.bastoup.bperipherals.peripherals.database;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.database.DBUtil;
import fr.bastoup.bperipherals.database.SQLiteType;
import fr.bastoup.bperipherals.peripherals.BPeripheral;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PeripheralDatabase extends BPeripheral {

    public static final String TYPE = "database";

    public PeripheralDatabase(BlockEntityDatabase tile) {
        super(tile);
    }

    private BlockEntityDatabase getTile() {
        return ((BlockEntityDatabase) blockEntity);
    }

    protected static void checkName(String name) throws LuaException {
        if (!name.matches("^[a-zA-Z_][a-zA-Z_#@$0-9]*$")) {
            throw new LuaException("Names may only contain alphanumeric symbols, underscores (_), number signs (#), " +
                    "dollar signs ($), or at signs (@), and may only start with a letter or underscore.");
        }
    }

    @Nonnull
    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return this == other || other instanceof PeripheralDatabase && ((PeripheralDatabase) other).blockEntity == blockEntity;
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
    public final Map<String, Object> executeSQL(String sql) throws LuaException {
        Path file;
        try {
            file = getTile().getDatabaseFile();
            if (getTile().isDiskInserted()) {
                return DBUtil.factorizeResults(BPeripherals.DB_FACTORY.executeSQL(file.toString(), sql));
            } else {
                throw new LuaException("There is no disk inserted");
            }
        } catch (IllegalAccessException | IOException e) {
            e.printStackTrace();
            throw new LuaException("Internal Error. Please send an issue if the problem persists.");
        }

    }

    @LuaFunction
    public final CCPreparedStatement prepareStatement(String sql) {
        return new CCPreparedStatement(sql, this);
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
            BlockEntityDatabase t = (BlockEntityDatabase) database.getTarget();
            if (t == null || t.isRemoved())
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
        public final Map<String, Object> execute() throws LuaException {
            peripheralStillValid();
            BlockEntityDatabase tile = (BlockEntityDatabase) database.getTarget();
            Path file;

            if (tile == null)
                throw new LuaException("Internal Error. Please send an issue if the problem persists.");

            try {
                file = tile.getDatabaseFile();
            } catch (IllegalAccessException | IOException e) {
                e.printStackTrace();
                throw new LuaException("Internal Error. Please send an issue if the problem persists.");
            }
            if (tile.isDiskInserted()) {
                return DBUtil.factorizeResults(BPeripherals.DB_FACTORY.executePrepared(file.toString(), this));
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

    public static class CCOrmModel {
        private final PeripheralDatabase database;
        private final String modelName;
        private final Map<String, Column> columns;
        private final String primaryKey;

        public CCOrmModel(PeripheralDatabase database, String modelName, Map<String, Column> columns, String primaryKey) {
            this.database = database;
            this.modelName = modelName;
            this.columns = columns;
            this.primaryKey = primaryKey;
        }

        private void peripheralStillValid() throws LuaException {
            BlockEntityDatabase t = (BlockEntityDatabase) database.getTarget();
            if (t == null || t.isRemoved())
                throw new LuaException("The peripheral does not exist.");
        }

        private void init() {

        }

        public static class Builder {
            private final PeripheralDatabase database;
            private final String modelName;
            private final Map<String, Column> columns;
            private String primaryKey = null;


            Builder(PeripheralDatabase database, String modelName) {
                this.database = database;
                this.modelName = modelName;
                this.columns = new HashMap<>();
            }

            private void peripheralStillValid() throws LuaException {
                BlockEntityDatabase t = (BlockEntityDatabase) database.getTarget();
                if (t == null || t.isRemoved())
                    throw new LuaException("The peripheral does not exist.");
            }

            @LuaFunction
            public Builder addColumn(String columnName, String type) throws LuaException {
                return addColumn(columnName, type, true, false, false);
            }

            @LuaFunction
            public Builder addColumn(String columnName, String type, boolean nullable) throws LuaException {
                return addColumn(columnName, type, nullable, false, false);
            }

            @LuaFunction
            public Builder addColumn(String columnName, String type, boolean nullable, boolean primaryKey) throws LuaException {
                return addColumn(columnName, type, nullable, primaryKey, false);
            }

            @LuaFunction
            public Builder addColumn(String columnName, String strType, boolean nullable, boolean primaryKey, boolean autoIncrement) throws LuaException {
                peripheralStillValid();

                String name = columnName.strip();
                if(name == null || name.isBlank())
                    throw new LuaException("The column name must not be empty.");

                if(columns.containsKey(name))
                    throw new LuaException("A column named '" + name + "' already exists.");

                SQLiteType type = SQLiteType.getType(strType);
                if(type == null)
                    throw new LuaException("A column named '" + name + "' already exists.");

                if(primaryKey) {
                    if(this.primaryKey == null) {
                        this.primaryKey = columnName;
                    } else {
                        throw new LuaException(this.primaryKey + " is already a primary key.");
                    }
                }

                columns.put(columnName, new Column(columnName, type, nullable, primaryKey, autoIncrement));
                return this;
            }

            @LuaFunction
            public CCOrmModel build() throws LuaException {
                peripheralStillValid();
                
                if(primaryKey == null) {
                    if(columns.containsKey("id")) {
                        throw new LuaException("A column named 'id' already exists. Cannot create primary key.");
                    } else {
                        columns.put("id", new Column("id", SQLiteType.INTEGER, false, true, true));
                    }
                }
                CCOrmModel model = new CCOrmModel(database, modelName, columns, primaryKey == null ? "id": primaryKey);
                model.init();
                return model;
            }
        }

        public static class Column {
            private final String columnName;
            private final SQLiteType type;
            private final boolean primaryKey;
            private final boolean nullable;
            private final boolean autoIncrement;

            Column(String columnName, SQLiteType type) {
                this.type = type;
                this.columnName = columnName;
                this.primaryKey = false;
                this.nullable = false;
                this.autoIncrement = false;
            }

            Column(String columnName, SQLiteType type, boolean nullable) {
                this.type = type;
                this.columnName = columnName;
                this.nullable = nullable;
                this.primaryKey = false;
                this.autoIncrement = false;
            }

            Column(String columnName, SQLiteType type, boolean nullable, boolean primaryKey) {
                this.type = type;
                this.columnName = columnName;
                this.nullable = nullable;
                this.primaryKey = primaryKey;
                this.autoIncrement = false;
            }

            Column(String columnName, SQLiteType type, boolean nullable, boolean primaryKey, boolean autoIncrement) {
                this.type = type;
                this.columnName = columnName;
                this.nullable = nullable;
                this.primaryKey = primaryKey;
                this.autoIncrement = autoIncrement;
            }

            public String getColumnName() {
                return columnName;
            }

            public SQLiteType getType() {
                return type;
            }

            public boolean isPrimaryKey() {
                return primaryKey;
            }

            public boolean isNullable() {
                return nullable;
            }

            public boolean isAutoIncrement() {
                return autoIncrement;
            }
        }
    }
}
