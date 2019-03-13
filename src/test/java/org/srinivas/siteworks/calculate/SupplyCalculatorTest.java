package org.srinivas.siteworks.calculate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.srinivas.siteworks.data.CoinsInventoryData;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


public class SupplyCalculatorTest extends AbstractCdiContainerTest {

    private SupplyCalculator supplyCalculator;
    private static final String FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES = "test-coin-inventory.properties";


    @Inject
    @Named("readwriter")
    private PropertiesReadWriter propertiesReadWriter;
    @Mock
    PropertiesReadWriter mockpropertiesReadWriter;


    @Before
    public void setUp() throws Exception {
        cdiContainerSetUp();
        MockitoAnnotations.initMocks(this);
        supplyCalculator = new SupplyCalculator();
        supplyCalculator.setPropertiesReadWriter(propertiesReadWriter);
        propertiesReadWriter.setResourceName("test-coin-inventory.properties");
        propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
        when(mockpropertiesReadWriter.getResourceName()).thenReturn("resource");
    }

    @After
    public void tearDown() throws Exception {
        propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
        propertiesReadWriter = null;
        supplyCalculator = null;
        cdiContainerTearDown();
    }

    @Test
    public void testCalculateBasedOnSufficientSupply() throws Exception {
        propertiesReadWriter.readInventoryData();
        List<Coin> coins = supplyCalculator.calculate(2896, propertiesReadWriter.denominations());
        assertTrue(coins.size() == 5);
        assertEquals(11, filterByValue(coins, 100).getCount().intValue());
        assertEquals(24, filterByValue(coins, 50).getCount().intValue());
        assertEquals(59, filterByValue(coins, 10).getCount().intValue());
        assertEquals(1, filterByValue(coins, 5).getCount().intValue());
        assertEquals(1, filterByValue(coins, 1).getCount().intValue());
    }

    @Test
    public void testCalculateBasedInSufficientSecondSupply() throws Exception {
        propertiesReadWriter.readInventoryData();
        List<Coin> coins = supplyCalculator.calculate(2896, propertiesReadWriter.denominations());
        assertTrue(coins.size() == 5);
        assertEquals(11, filterByValue(coins, 100).getCount().intValue());
        assertEquals(24, filterByValue(coins, 50).getCount().intValue());
        assertEquals(59, filterByValue(coins, 10).getCount().intValue());
        assertEquals(1, filterByValue(coins, 5).getCount().intValue());
        assertEquals(1, filterByValue(coins, 1).getCount().intValue());
        coins = supplyCalculator.calculate(6, propertiesReadWriter.denominations());
        assertTrue(coins.size() == 2);
        assertEquals(1, filterByValue(coins, 5).getCount().intValue());
        assertEquals(1, filterByValue(coins, 1).getCount().intValue());
    }

    @Test
    public void testCalculateBasedOnInSufficientSupply() throws Exception {
        propertiesReadWriter.readInventoryData();
        List<Coin> coins = supplyCalculator.calculate(5000, propertiesReadWriter.denominations());
        assertTrue(coins.size() == 0);
    }

    @Test
    public void testGetPropertiesReadWriter() {
        assertEquals(supplyCalculator.getPropertiesReadWriter().getResourceName(), FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES);
    }

    @Test
    public void testSetPropertiesReadWriter() {
        supplyCalculator.setPropertiesReadWriter(mockpropertiesReadWriter);
        assertEquals(supplyCalculator.getPropertiesReadWriter().getResourceName(), "resource");
    }

    @Test
    public void testErrorScenario() throws Exception {
        supplyCalculator.setPropertiesReadWriter(mockpropertiesReadWriter);
        doThrow(new RuntimeException("Failed to Read or Write")).when(mockpropertiesReadWriter).getInventoryMap();
        propertiesReadWriter.readInventoryData();
        List<Coin> coins = supplyCalculator.calculate(2896, propertiesReadWriter.denominations());
        assertTrue(coins.size() == 0);
    }


    private Coin filterByValue(List<Coin> coins, Integer value) {
        return coins.stream().filter(coin -> coin.getValue() == value).findFirst().get();
    }

}
