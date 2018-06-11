package com.etermax.spacehorse.core.catalog.model;

import org.junit.Assert;
import org.junit.Test;

public class CatalogEntryTest {

    @Test
    public void testEqualsAndHashCode() {
        String id = "catalogEntryId";
        CatalogEntry x = new FakeCatalogEntry(id);
        CatalogEntry y = new FakeCatalogEntry(id);
        Assert.assertTrue(x.equals(y) && y.equals(x));
        Assert.assertTrue(x.hashCode() == y.hashCode());
    }

}
