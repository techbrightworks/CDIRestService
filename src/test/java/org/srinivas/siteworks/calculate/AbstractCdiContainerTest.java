package org.srinivas.siteworks.calculate;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

public abstract class AbstractCdiContainerTest {
    protected static CdiContainer cdiContainer;

    @SuppressWarnings("unchecked")
    public void cdiContainerSetUp() {
        cdiContainer.getContextControl().stopContext(RequestScoped.class);
        cdiContainer.getContextControl().startContext(RequestScoped.class);
        BeanManager beanManager = cdiContainer.getBeanManager();
        CreationalContext creationalContext = beanManager.createCreationalContext(null);
        AnnotatedType annotatedType = beanManager.createAnnotatedType(this.getClass());
        InjectionTarget injectionTarget = beanManager.createInjectionTarget (annotatedType);
        injectionTarget.inject ( this, creationalContext );
    }


    public void cdiContainerTearDown() {
        if (cdiContainer != null) {
            cdiContainer.getContextControl().stopContext(RequestScoped.class);
            cdiContainer.getContextControl().startContext(RequestScoped.class);
        }
    }

    @BeforeClass
    public final synchronized static void startUpContainer() throws Exception {
        cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContexts();
    }

    @AfterClass
    public final synchronized static void shutdownContainer() throws Exception {
        if (cdiContainer != null) {
            cdiContainer.shutdown();
            cdiContainer = null;
        }
    }

}
