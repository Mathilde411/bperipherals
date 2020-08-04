package fr.bastoup.bperipherals.beans;

public class SQLColumn {
    private String name;
    private String type;
    private boolean notNull;
    private boolean isUnique;


    public SQLColumn(String name, String type, boolean notNull, boolean isUnique) {
        this.name = name.toLowerCase();
        this.type = type.toUpperCase();
        this.notNull = notNull;
        this.isUnique = isUnique;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }
}
