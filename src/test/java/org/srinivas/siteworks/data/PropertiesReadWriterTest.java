package org.srinivas.siteworks.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesReadWriterTest {
    private static final String FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES = "test-coin-inventory.properties";
    private PropertiesReadWriter propertiesReadWriter;
    private Map<Integer, Integer> inventory;
    @Mock
    PropertiesReadWriter mockpropertiesReadWriter;

    @Before
    public void setUp() throws Exception {
        inventory = new HashMap<Integer, Integer>();
        inventory.put(5, 1000);
        MockitoAnnotations.initMocks(this);
        propertiesReadWriter = new PropertiesReadWriter();
        when(mockpropertiesReadWriter.getInventoryMap()).thenReturn(inventory);
        propertiesReadWriter.setResourceName(FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES);
    }

    @After
    public void tearDown() throws Exception {
        propertiesReadWriter = null;
    }

    @Test
    public void testReadInventoryData() throws Exception {
        propertiesReadWriter.readInventoryData();
        assertTrue(propertiesReadWriter.getInventoryMap().size() > 0);
    }

    @Test
    public void testWriteInventoryData() throws Exception {
        propertiesReadWriter.writeInventoryData(inventory);
        propertiesReadWriter.readInventoryData();
        assertEquals(propertiesReadWriter.getInventoryMap().size(), 1);
        assertTrue(propertiesReadWriter.getInventoryMap().get(5) == 1000);
        propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
    }

    @Test
    public void testDenominations() throws Exception {
        assertTrue(propertiesReadWriter.denominations().length == 0);
        propertiesReadWriter.readInventoryData();
        assertTrue(propertiesReadWriter.denominations().length > 0);
        assertEquals(propertiesReadWriter.getInventoryMap().keySet().size(), propertiesReadWriter.denominations().length);
    }

    @Test
    public void testGetInventoryMap() {
        assertTrue(propertiesReadWriter.getInventoryMap().size() == 0);
        propertiesReadWriter.setInventoryMap(inventory);
        assertEquals(propertiesReadWriter.getInventoryMap().size(), 1);
        assertEquals(mockpropertiesReadWriter.getInventoryMap().size(), 1);
    }

    @Test
    public void testSetInventoryMap() throws Exception {
        propertiesReadWriter.readInventoryData();
        assertTrue(propertiesReadWriter.getInventoryMap().size() > 0);
    }

}
