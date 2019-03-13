package org.srinivas.siteworks.calculate;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.srinivas.siteworks.data.CoinsInventoryData;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(CdiTestRunner.class)
public class OptimalCalculatorTest extends AbstractCdiContainerTest {

    @Before
    public final void setUp() throws Exception {
        cdiContainerSetUp();
        MockitoAnnotations.initMocks(this);
        propertiesReadWriter.setResourceName("test-coin-inventory.properties");
        propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
    }

    @After
    public final void tearDown() throws Exception {
        propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
        propertiesReadWriter = null;
        cdiContainerTearDown();
    }

    @Inject
    @OptimalCalculation
    private Calculate optimalCalculator;

    @Inject
    @Named("readwriter")
    private PropertiesReadWriter propertiesReadWriter;


    @Test
    public void testCalculate() throws Exception {
        propertiesReadWriter.readInventoryData();
        List<Coin> coins = optimalCalculator.calculate(576, propertiesReadWriter.denominations());
        assertTrue(coins.size() == 5);
        assertEquals(5, filterByValue(coins, 100).getCount().intValue());
        assertEquals(1, filterByValue(coins, 50).getCount().intValue());
        assertEquals(1, filterByValue(coins, 20).getCount().intValue());
        assertEquals(1, filterByValue(coins, 5).getCount().intValue());
        assertEquals(1, filterByValue(coins, 1).getCount().intValue());
    }

    @Test
    public void testErrorScenario() throws Exception {
        propertiesReadWriter.readInventoryData();
        List<Coin> coins = optimalCalculator.calculate(576, null);
        assertTrue(coins.size() == 0);
    }

    private Coin filterByValue(List<Coin> coins, Integer value) {
        return coins.stream().filter(coin -> coin.getValue() == value).findFirst().get();
    }

}
