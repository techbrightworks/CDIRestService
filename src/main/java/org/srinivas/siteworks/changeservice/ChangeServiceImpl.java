package org.srinivas.siteworks.changeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.calculate.Calculate;
import org.srinivas.siteworks.calculate.OptimalCalculation;
import org.srinivas.siteworks.calculate.SupplyCalculation;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.Collections;

@Named("changeservice")
public class ChangeServiceImpl implements ChangeService {

    @Inject
    @Named("readwriter")
    public PropertiesReadWriter propertiesReadWriter;


    @Inject
    @SupplyCalculation
    public Calculate supplyCalculator;


    @Inject
    @OptimalCalculation
    public Calculate optimalCalculator;


    private static final Logger log = LoggerFactory.getLogger(ChangeServiceImpl.class);

    @Override
    public Collection<Coin> getOptimalChangeFor(int pence) {

        try {
            return optimalCalculator.calculate(pence, propertiesReadWriter.denominations());
        } catch (Exception e) {
            log.error("Optimal Calculation not Successful", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Coin> getChangeFor(int pence) {
        try {
            return supplyCalculator.calculate(pence, propertiesReadWriter.denominations());
        } catch (Exception e) {
            log.error("Supply Calculation not Successful", e);
            return Collections.emptyList();
        }
    }

}
