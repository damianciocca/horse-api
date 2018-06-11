package com.etermax.spacehorse.core.inapps.market;

import com.etermax.spacehorse.core.inapps.domain.market.MemoryMarkets;
import com.etermax.spacehorse.core.inapps.domain.market.editor.EditorMarket;
import com.etermax.spacehorse.core.user.model.Platform;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryMarketsTest {

    private MemoryMarkets memoryMarkets;

    @Before
    public void setUp() {
        this.memoryMarkets = new MemoryMarkets();
        this.memoryMarkets.registerMarket(Platform.EDITOR, new EditorMarket());
    }

    @Test
    public void testMemoryMarkets() {
        assertTrue(this.memoryMarkets.findById(Platform.EDITOR).isPresent());
        assertFalse(this.memoryMarkets.findById(Platform.ANDROID).isPresent());
        assertFalse(this.memoryMarkets.findById(Platform.IOS).isPresent());
    }

}
