package org.srinivas.siteworks.config;

import org.srinivas.siteworks.controller.ChangeController;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("app")
public class AppConfig extends Application {

    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(ChangeController.class);
        return s;
    }

}
