package fr.bastoup.bperipherals.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.database.DBUtil;
import fr.bastoup.bperipherals.tileentities.TileDatabase;

import java.io.File;
import java.util.List;

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
}
