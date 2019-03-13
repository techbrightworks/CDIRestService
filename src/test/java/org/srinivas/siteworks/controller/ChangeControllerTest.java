package org.srinivas.siteworks.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.srinivas.siteworks.calculate.AbstractCdiContainerTest;
import org.srinivas.siteworks.calculate.Calculate;
import org.srinivas.siteworks.calculate.OptimalCalculation;
import org.srinivas.siteworks.calculate.SupplyCalculator;
import org.srinivas.siteworks.changeservice.ChangeServiceImpl;
import org.srinivas.siteworks.data.CoinsInventoryData;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ChangeControllerTest extends AbstractCdiContainerTest {

    private ChangeController changeContoller;
    private SupplyCalculator supplyCalculator;
    private ChangeServiceImpl changeServiceImpl;

    @Inject
    @OptimalCalculation
    private Calculate optimalCalculator;

    @Inject
    @Named("readwriter")
    private PropertiesReadWriter propertiesReadWriter;

    @Before
    public void setUp() throws Exception {
        cdiContainerSetUp();
        MockitoAnnotations.initMocks(this);
        supplyCalculator = new SupplyCalculator();
        supplyCalculator.setPropertiesReadWriter(propertiesReadWriter);
        propertiesReadWriter.setResourceName("test-coin-inventory.properties");
        propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
        changeServiceImpl = new ChangeServiceImpl();
        changeServiceImpl.supplyCalculator = supplyCalculator;
        changeServiceImpl.propertiesReadWriter = propertiesReadWriter;
        changeServiceImpl.optimalCalculator = optimalCalculator;
        changeContoller = new ChangeController();
        changeContoller.changeServiceImpl = changeServiceImpl;
        changeContoller.propertiesReadWriter = propertiesReadWriter;
    }

    @After
    public void tearDown() throws Exception {
        propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
        propertiesReadWriter = null;
        supplyCalculator = null;
        changeServiceImpl = null;
        cdiContainerTearDown();
    }

    @Test
    public void testGetOptimalCalculation() throws Exception {
        propertiesReadWriter.readInventoryData();
        Collection<Coin> coins = changeContoller.handleGetOptimalCalculation(576);
        assertTrue(coins.size() == 5);
        assertEquals(5, filterByValue(coins, 100).getCount().intValue());
        assertEquals(1, filterByValue(coins, 50).getCount().intValue());
        assertEquals(1, filterByValue(coins, 20).getCount().intValue());
        assertEquals(1, filterByValue(coins, 5).getCount().intValue());
        assertEquals(1, filterByValue(coins, 1).getCount().intValue());
    }

    @Test
    public void testGetChangeFor() throws Exception {
        propertiesReadWriter.readInventoryData();
        Collection<Coin> coins = changeContoller.handleGetSupplyCalculation(2896);
        assertTrue(coins.size() == 5);
        assertEquals(11, filterByValue(coins, 100).getCount().intValue());
        assertEquals(24, filterByValue(coins, 50).getCount().intValue());
        assertEquals(59, filterByValue(coins, 10).getCount().intValue());
        assertEquals(1, filterByValue(coins, 5).getCount().intValue());
        assertEquals(1, filterByValue(coins, 1).getCount().intValue());
    }

    private Coin filterByValue(Collection<Coin> coins, Integer value) {
        return coins.stream().filter(coin -> coin.getValue() == value).findFirst().get();
    }


}
