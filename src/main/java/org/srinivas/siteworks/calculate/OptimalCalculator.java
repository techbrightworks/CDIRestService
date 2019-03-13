package org.srinivas.siteworks.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.denomination.Coin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@OptimalCalculation
public class OptimalCalculator implements Calculate {


    private static final Logger log = LoggerFactory.getLogger(SupplyCalculator.class);

    @Override
    public List<Coin> calculate(Integer pence, Integer[] denominations) {
        List<Coin> coinsMap = new ArrayList<Coin>();
        try {
            AtomicInteger remainingPence = new AtomicInteger(pence);
            optimalCalculation(denominations, coinsMap, remainingPence);
        } catch (Exception e) {
            log.error("OptimalCalculation Unsuccessful", e);
        }
        return coinsMap;
    }

    private void optimalCalculation(Integer[] denominations, List<Coin> coinsMap, AtomicInteger remainingPence) {
        Arrays.stream(denominations).forEach(denomination -> {
            if (remainingPence.get() > 0) {
                denominationCalculation(remainingPence.get(), coinsMap, denomination);
                remainingPence.set(remainingCalculation(remainingPence.get(), denomination));
            }
        });
    }

    private void denominationCalculation(Integer pence, List<Coin> coinsMap, Integer denomination) {
        Integer coins = Math.floorDiv(pence, denomination);
        Coin coin = new Coin();
        coin.setValue(denomination);
        coin.setCount(coins);
        addToCoinsList(coinsMap, coins, coin);
    }

    private void addToCoinsList(List<Coin> coinsMap, Integer coins, Coin coin) {
        if (coins > 0) {
            coinsMap.add(coin);
        }
    }

    private Integer remainingCalculation(Integer pence, Integer denomination) {
        return pence % denomination;
    }
}
