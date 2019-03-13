package org.srinivas.siteworks.exception;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class CalculateExceptionTest {

    @Mock
    PropertiesReadWriter mockpropertiesReadWriter;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @After
    public void tearDown() throws Exception {
        mockpropertiesReadWriter = null;
    }

    @Test
    public void testCalculateExceptionStringThrowable() throws Exception {
        doThrow(new CalculateException("Failed to Read or Write")).when(mockpropertiesReadWriter).readInventoryData();
        try {
            mockpropertiesReadWriter.readInventoryData();
            fail("Expected to throw CalculateException");
        } catch (Exception e) {
            assertTrue(e instanceof CalculateException);
        }
    }

    @Test
    public void testCalculateExceptionString() throws Exception {
        doThrow(new CalculateException("Failed to Read or Write", new RuntimeException("File Not Closed Properly"))).when(mockpropertiesReadWriter).readInventoryData();
        try {
            mockpropertiesReadWriter.readInventoryData();
            fail("Expected to throw CalculateException");
        } catch (Exception e) {
            assertTrue(e instanceof CalculateException);
            assertTrue(e.getCause() instanceof RuntimeException);
        }

    }

}
