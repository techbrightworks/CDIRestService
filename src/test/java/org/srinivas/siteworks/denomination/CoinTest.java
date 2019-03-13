package org.srinivas.siteworks.denomination;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CoinTest {

    private Coin coin;

    @Before
    public void setUp() throws Exception {
        coin = new Coin();
    }

    @After
    public void tearDown() throws Exception {
        coin = null;
    }

    @Test
    public void testGetValue() {
        assertNull(coin.getValue());
        coin.setValue(100);
        assertEquals(100, coin.getValue().intValue());
    }

    @Test
    public void testSetValue() {
        coin.setValue(100);
        assertEquals(100, coin.getValue().intValue());
    }

    @Test
    public void testGetCount() {
        assertNull(coin.getCount());
        coin.setCount(20);
        assertEquals(20, coin.getCount().intValue());
    }

    @Test
    public void testSetCount() {
        coin.setCount(20);
        assertEquals(20, coin.getCount().intValue());
    }

    @Test
    public void testGetName() {
        assertNull(coin.getName());
        coin.setName("One Pound");
        assertEquals("One Pound", coin.getName());
    }

    @Test
    public void testSetName() {
        coin.setName("One Pound");
        assertEquals("One Pound", coin.getName());
    }

}
