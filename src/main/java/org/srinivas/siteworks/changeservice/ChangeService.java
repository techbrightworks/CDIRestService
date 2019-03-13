package org.srinivas.siteworks.changeservice;

import org.srinivas.siteworks.denomination.Coin;
import java.util.Collection;


public interface ChangeService {

    Collection<Coin> getOptimalChangeFor(int pence);

    Collection<Coin> getChangeFor(int pence);


}
