package org.srinivas.siteworks.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.changeservice.ChangeService;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/change")
public class ChangeController {

    private static final Logger logger = LoggerFactory.getLogger(ChangeController.class);

    @Inject
    @Named("readwriter")
    public PropertiesReadWriter propertiesReadWriter;

    @Inject
    @Named("changeservice")
    ChangeService changeServiceImpl;

    @GET
    @Path("/optimal")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Coin> handleGetOptimalCalculation(@QueryParam("pence") Integer pence) {
        logger.info("ChangeController:handleGetOptimalCalculation Method");
        try {
            propertiesReadWriter.readInventoryData();
            return changeServiceImpl.getOptimalChangeFor(pence).stream().collect(Collectors.toList());
        } catch (Exception e) {
            logger.info("Error:" + e.getMessage());
            return new ArrayList<Coin>();
        }
    }

    @GET
    @Path("/supply")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Coin> handleGetSupplyCalculation(@QueryParam("pence") Integer pence) {
        logger.info("ChangeController:handleGetSupplyCalculation Method");
        try {
            propertiesReadWriter.readInventoryData();
            return changeServiceImpl.getChangeFor(pence).stream().collect(Collectors.toList());
        } catch (Exception e) {
            logger.info("Error:" + e.getMessage());
            return new ArrayList<Coin>();
        }
    }

}
