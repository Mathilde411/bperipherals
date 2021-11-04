package fr.bastoup.bperipherals.database;

public enum SQLiteType {
    NULL("NULL"),
    TEXT("TEXT"),
    INTEGER("INTEGER"),
    REAL("REAL"),
    BLOB("BLOB");

    private final String type;

    SQLiteType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static SQLiteType getType(String strType) {
        for(SQLiteType type : SQLiteType.values()) {
            if(type.toString().equalsIgnoreCase(strType))
                return type;
        }
        return null;
    }
}
