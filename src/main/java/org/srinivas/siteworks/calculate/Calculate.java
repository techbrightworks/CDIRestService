package org.srinivas.siteworks.calculate;

import org.srinivas.siteworks.denomination.Coin;
import java.util.List;


public interface Calculate {

    List<Coin> calculate(Integer pence, Integer[] denominations) throws Exception;

}
