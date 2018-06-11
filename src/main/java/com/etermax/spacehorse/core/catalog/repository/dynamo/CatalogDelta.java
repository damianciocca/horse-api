package com.etermax.spacehorse.core.catalog.repository.dynamo;

public class CatalogDelta {
    private String sheet;
    private String id;
    private String col;
    private String val;

    public CatalogDelta(String sheet, String id, String col, String val) {
        this.sheet = sheet;
        this.id = id;
        this.col = col;
        this.val = val;
    }

    public String getSheet() {
        return sheet;
    }

    public String getId() {
        return id;
    }

    public String getCol() {
        return col;
    }

    public String getVal() {
        return val;
    }
}
