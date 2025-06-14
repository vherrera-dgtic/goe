package es.caib.goe.ejb.interceptor;

import es.caib.goe.service.exception.RecursNoTrobatException;
import es.caib.goe.service.exception.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ejb.EJBException;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

@RunWith(MockitoJUnitRunner.class)
public class TestExceptionTranslatorInterceptor {

    private final ExceptionTranslatorInterceptor interceptor = new ExceptionTranslatorInterceptor();

    @Mock
    private InvocationContext context;

    @Test
    public void testEmptyEJBException() throws Exception {
        EJBException ejbException = new EJBException();

        Mockito.when(context.proceed()).thenThrow(ejbException);
        try {
            interceptor.aroundInvoke(context);
            Assert.fail("Hauria d'haver llançat una excepció");
        } catch (Exception exception) {
            Assert.assertEquals(ejbException, exception);
        }
    }

    @Test
    public void testEJBExceptionWithPersistenceException() throws Exception {
        PersistenceException persistenceException = new PersistenceException();
        EJBException ejbException = new EJBException(persistenceException);

        Mockito.when(context.proceed()).thenThrow(ejbException);
        try {
            interceptor.aroundInvoke(context);
            Assert.fail("Hauria d'haver llançat una excepció");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof ServiceException);
            Assert.assertEquals(persistenceException, exception.getCause());
        }
    }

    @Test
    public void testEJBExceptionWithEntityNotFoundException() throws Exception {
        PersistenceException persistenceException = new EntityNotFoundException();
        EJBException ejbException = new EJBException(persistenceException);

        Mockito.when(context.proceed()).thenThrow(ejbException);
        try {
            interceptor.aroundInvoke(context);
            Assert.fail("Hauria d'haver llançat una excepció");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof RecursNoTrobatException);
            Assert.assertNull(exception.getCause());
        }
    }

    @Test
    public void testRuntimeException() throws Exception {
        RuntimeException runtimeException = new RuntimeException();

        Mockito.when(context.proceed()).thenThrow(runtimeException);
        try {
            interceptor.aroundInvoke(context);
            Assert.fail("Hauria d'haver llançat una excepció");
        } catch (Exception exception) {
            Assert.assertEquals(runtimeException, exception);
        }
    }

    @Test
    public void testPlainException() throws Exception {
        Exception plainException = new Exception();

        Mockito.when(context.proceed()).thenThrow(plainException);
        try {
            interceptor.aroundInvoke(context);
            Assert.fail("Hauria d'haver llançat una excepció");
        } catch (Exception exception) {
            Assert.assertEquals(plainException, exception);
        }
    }

    @Test
    public void testPersistenceException() throws Exception {
        PersistenceException persistenceException = new PersistenceException();

        Mockito.when(context.proceed()).thenThrow(persistenceException);
        try {
            interceptor.aroundInvoke(context);
            Assert.fail("Hauria d'haver llançat una excepció");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof ServiceException);
            Assert.assertEquals(persistenceException, exception.getCause());
        }
    }

    @Test
    public void testEntityNotFoundException() throws Exception {
        PersistenceException persistenceException = new EntityNotFoundException();

        Mockito.when(context.proceed()).thenThrow(persistenceException);
        try {
            interceptor.aroundInvoke(context);
            Assert.fail("Hauria d'haver llançat una excepció");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof RecursNoTrobatException);
            Assert.assertNull(exception.getCause());
        }
    }
}
