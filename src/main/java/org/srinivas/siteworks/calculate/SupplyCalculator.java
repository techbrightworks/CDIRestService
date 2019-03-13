package org.srinivas.siteworks.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;
import org.srinivas.siteworks.exception.CalculateException;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SupplyCalculation
public class SupplyCalculator implements Calculate {

    @Inject
    @Named("readwriter")
    PropertiesReadWriter propertiesReadWriter;


    private static final Logger log = LoggerFactory.getLogger(SupplyCalculator.class);

    public PropertiesReadWriter getPropertiesReadWriter() {
        return propertiesReadWriter;
    }

    public void setPropertiesReadWriter(PropertiesReadWriter propertiesReadWriter) {
        this.propertiesReadWriter = propertiesReadWriter;
    }

    @Override
    public List<Coin> calculate(Integer pence, Integer[] denominations) throws Exception {
        List<Coin> suppliedCoins = new ArrayList<>();
        try {
            Map<Integer, Integer> inventoryMap = propertiesReadWriter.getInventoryMap();
            Integer inventoryTotal = inventoryMapValueTotal(inventoryMap);
            List<Integer> shortSupplyList = new ArrayList<>();
            AtomicInteger remaining = new AtomicInteger(pence);
            if (inventoryTotal >= pence) {
                supplyCalculation(denominations, inventoryMap, shortSupplyList, remaining, suppliedCoins);
                propertiesReadWriter.writeInventoryData(inventoryMap);
            }
        } catch (Exception e) {
            suppliedCoins = new ArrayList<Coin>();
            log.error("SupplyCalculation Unsuccessful", e);
        }
        return suppliedCoins;
    }

    private void supplyCalculation(Integer[] denominations, Map<Integer, Integer> inventoryMap, List<Integer> shortSupplyList, AtomicInteger remaining, List<Coin> suppliedCoins) throws Exception {
        OptimalCalculator optimalCalculator = new OptimalCalculator();
        if (denominations.length == 0) {
            throw new CalculateException("Insufficient Supply of Coins");
        }
        List<Coin> optimalCalculatedCoins = optimalCalculator.calculate(remaining.get(), denominations);
        calculateSupplyCoins(denominations, inventoryMap, shortSupplyList, remaining, suppliedCoins, optimalCalculatedCoins);
    }

    private void calculateSupplyCoins(Integer[] denominations, Map<Integer, Integer> inventoryMap, List<Integer> shortSupplyList, AtomicInteger remaining, List<Coin> suppliedCoins, List<Coin> optimalCalculatedCoins) throws Exception {
        Arrays.stream(denominations).forEach(key -> {
            if (remaining.get() > 0 && denominations.length > 0 && isOneofOptimalValue(optimalCalculatedCoins, key)) {

                try {
                    Coin coin = filterByValue(optimalCalculatedCoins, key);

                    if (coin != null && coin.getCount() > inventoryMap.get(key)) {
                        insufficientInventoryChanges(inventoryMap, shortSupplyList, remaining, suppliedCoins, key);
                    } else {
                        inventoryAvailableChanges(inventoryMap, remaining, suppliedCoins, key, coin);
                    }
                    Integer[] denom = evaluateDenom(denominations, shortSupplyList, remaining);
                    recurseSupplyCalculation(inventoryMap, shortSupplyList, remaining, suppliedCoins, denom);
                } catch (Exception e) {
                    throw new RuntimeException("Insufficient Supply of Coins", e);
                }
            }
        });
    }

    private Integer[] evaluateDenom(Integer[] denominations, List<Integer> shortSupplyList, AtomicInteger remaining) {
        Integer[] denom = new Integer[]{};
        if (remaining.get() > 0 && shortSupplyList.size() > 0) {
            denom = Arrays.stream(denominations).filter(den -> (!shortSupplyList.contains(den))).toArray(Integer[]::new);
        } else if (remaining.get() > 0 && shortSupplyList.size() == 0 && denominations.length > 0) {
            denom = denominations;
        }
        return denom;
    }

    private void recurseSupplyCalculation(Map<Integer, Integer> inventoryMap, List<Integer> shortSupplyList, AtomicInteger remaining, List<Coin> suppliedCoins, Integer[] denom) throws CalculateException {
        if (remaining.get() > 0) {
            try {
                supplyCalculation(denom, inventoryMap, shortSupplyList, remaining, suppliedCoins);
            } catch (Exception e) {
                log.error("SupplyCalculation Unsuccessful", e);
                throw new CalculateException("Insufficient Supply of Coins", e);
            }
        }
    }

    private void insufficientInventoryChanges(Map<Integer, Integer> inventoryMap, List<Integer> shortSupplyList, AtomicInteger remaining, List<Coin> suppliedCoins, Integer key) {
        remaining.set(remaining.get() - (inventoryMap.get(key) * key));
        shortSupplyList.add(key);
        addCoin(key, inventoryMap.get(key), suppliedCoins);
        zeroValueInventory(inventoryMap, key);
    }

    private void inventoryAvailableChanges(Map<Integer, Integer> inventoryMap, AtomicInteger remaining, List<Coin> suppliedCoins, Integer key, Coin coin) {
        reduceValueInventory(coin.getCount(), inventoryMap, key);
        remaining.set(remaining.get() - (coin.getCount() * key));
        addCoin(key, coin.getCount(), suppliedCoins);
    }

    private void zeroValueInventory(Map<Integer, Integer> inventoryMap, Integer key) {
        if (inventoryMap.containsKey(key)) {
            inventoryMap.put(key, 0);
        }
    }

    private void reduceValueInventory(Integer reducyBy, Map<Integer, Integer> inventoryMap, Integer key) {
        if (inventoryMap.containsKey(key)) {
            inventoryMap.put(key, (inventoryMap.get(key) - reducyBy));
        }
    }

    private Coin filterByValue(List<Coin> coins, Integer value) throws Exception {
        return coins.stream().filter(coin -> coin.getValue() == value).findFirst().get();

    }

    private boolean isOneofOptimalValue(List<Coin> coins, Integer value) {

        return coins.stream().anyMatch(coin -> coin.getValue() == value);
    }

    private void addCoin(Integer value, Integer count, List<Coin> coins) {
        Coin coin = new Coin();
        coin.setValue(value);
        coin.setCount(count);
        if (count > 0) {
            coins.add(coin);
        }
    }

    private Integer inventoryMapValueTotal(Map<Integer, Integer> inventoryMap) {
        AtomicInteger total = new AtomicInteger(0);
        inventoryMap.entrySet().stream().forEach(e -> {
            total.set(total.get() + e.getKey() * e.getValue());
        });
        return total.get();
    }

}
