package org.geoserver.security;

import static org.junit.Assert.*;

import java.util.List;

import org.geoserver.test.GeoServerSystemTestSupport;
import org.geoserver.test.SystemTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(SystemTest.class)
public class GeoServerCustomSecurityProviderTest extends GeoServerSystemTestSupport {

    @Override
    protected void setUpSpring(List<String> springContextLocations) {
        super.setUpSpring(springContextLocations);
        springContextLocations.add(
            getClass().getResource(getClass().getSimpleName() + "-context.xml").toString());
    }

    public static class SecurityProvider extends GeoServerSecurityProvider {
        static boolean initCalled = false;
        static boolean destroyCalled = false;
        
        @Override
        public void init(GeoServerSecurityManager manager) {
            initCalled = true;
        }

        @Override
        public void destroy(GeoServerSecurityManager manager) {
            destroyCalled = true;
        }
    }

    @Test
    public void testThatInitIsCalled() {
        assertTrue("The Security provider's init method should be called", SecurityProvider.initCalled);
    }

    @Test
    public void testThatDestroyIsCalled() throws Exception {
        destroyGeoServer();
        assertTrue("The Security provider's destroy method should be called", SecurityProvider.destroyCalled);
    }
}
