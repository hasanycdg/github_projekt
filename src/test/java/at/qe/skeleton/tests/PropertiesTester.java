package at.qe.skeleton.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Controller
public class PropertiesTester {

    private static final Logger LOGGER = LogManager.getLogger(PropertiesTester.class);

    /**
     *
     * @param beanClass the class to test
     * @param setterExceptions the names of setters that should not be tested.
     */
    public void testProperties(Class<?> beanClass, String... setterExceptions) {
        // first try to instantiate bean;
        Object bean = null;
        try {
            bean = beanClass.getConstructor().newInstance();
        } catch (
                InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException
                | SecurityException e
        ) {
            LOGGER.info("Cannot instantiate " + beanClass.getCanonicalName(), e);
            return;
        }
        testSetters(beanClass, bean, setterExceptions);
    }

    /**
     * just tests all setters on bean
     * @param beanClass
     * @param bean
     * @param setterExceptions
     */
    public void testSetters(Class<?> beanClass, Object bean, String... setterExceptions) {
        final Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                if (!Arrays.stream(setterExceptions).anyMatch(noTest -> method.getName().equals(noTest))) testSetter(
                        beanClass,
                        bean,
                        method
                );
            }
        }

        // finally test toString
        String stringRepresentation = bean.toString();
        Assertions.assertNotNull(stringRepresentation);
    }

    public void testSetter(Class<?> beanClass, Object bean, Method setter) {
        if (setter.getParameterCount() != 1) {
            return;
        }
        Parameter p = setter.getParameters()[0];

        String propertyName = setter.getName().substring(3);
        Class<?> type = p.getType();

        Method getter = null;
        Object property = null;
        boolean jHipsterFilter = false;
        if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            try {
                property = Boolean.TRUE;
                getter = beanClass.getMethod("is" + propertyName);
            } catch (NoSuchMethodException | SecurityException e) {
                try {
                    getter = beanClass.getMethod("get" + propertyName);
                } catch (NoSuchMethodException | SecurityException ex) {
                    LOGGER.info(
                            "Cannot find getter (neither is... nor get... for " + beanClass.getCanonicalName() + "." + setter.getName(),
                            ex
                    );
                    return;
                }
            }
        } else {
            try {
                getter = beanClass.getMethod("get" + propertyName);
                if (Integer.class.isAssignableFrom(type) || "int".equals(type.getName())) {
                    property = Integer.valueOf(2343234);
                } else if (Long.class.isAssignableFrom(type) || "long".equals(type.getName())) {
                    property = Long.valueOf(12321332343234L);
                } else if (Byte.class.isAssignableFrom(type) || "byte".equals(type.getName())) {
                    property = Byte.valueOf((byte) 22);
                } else if (String.class.isAssignableFrom(type)) {
                    property = "xyzzy";
                } else if (byte[].class.isAssignableFrom(type)) {
                    property = new byte[] { 3, 4 };
                } else if (type.isArray()) {
                    property = Array.newInstance(type.getComponentType(), 5);
                } else if (Timestamp.class.isAssignableFrom(type)) {
                    property = new Timestamp(System.currentTimeMillis());
                } else if (Date.class.isAssignableFrom(type)) {
                    property = new Date();
                } else if (List.class.isAssignableFrom(type)) {
                    property = new ArrayList<>();
                }
                /*
                 * else if (IntegerFilter.class.isAssignableFrom(type)) {
                 * property = new IntegerFilter();
                 * jHipsterFilter = true;
                 * } else if (LongFilter.class.isAssignableFrom(type)) {
                 * property = new LongFilter();
                 * jHipsterFilter = true;
                 * } else if (StringFilter.class.isAssignableFrom(type)) {
                 * property = new StringFilter();
                 * jHipsterFilter = true;
                 * } else if (BooleanFilter.class.isAssignableFrom(type)) {
                 * property = new BooleanFilter();
                 * jHipsterFilter = true;
                 * } else if (LocalDateFilter.class.isAssignableFrom(type)) {
                 * property = new LocalDateFilter();
                 * jHipsterFilter = true;
                 * } else if (StringFilter.class.isAssignableFrom(type)) {
                 * property = new StringFilter();
                 * jHipsterFilter = true;
                 * }
                 */
                else if (Enum.class.isAssignableFrom(type)) {
                    Object o;
                    try {
                        o = type.getMethod("values").invoke(type);
                        if (o.getClass().isArray() && Array.getLength(o) >= 0) {
                            property = Array.get(o, 0);
                        } else {
                            LOGGER.warn("Cannot get first element of enumeration {}", type.getName());
                            property = null;
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        LOGGER.info("Cannot instantiate Enum for " + type.getCanonicalName() + "." + setter.getName(), e);
                        property = null;
                    }
                } else if (Set.class.isAssignableFrom(type)) {
                    property = Collections.emptySet();
                } else if (Instant.class.isAssignableFrom(type)) {
                    property = Instant.now();
                } else if (Float.class.isAssignableFrom(type)) {
                    property = Float.valueOf(1.33f);
                } else if (Double.class.isAssignableFrom(type)) {
                    property = Double.valueOf(1.77777d);
                } else if (type.isInterface()) {
                    LOGGER.info(
                            "Cannot instantiate interface {} in {} for class {}",
                            type.getName(),
                            setter.getName(),
                            bean.getClass().getName()
                    );
                } else {
                    try {
                        property = type.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        LOGGER.info("Cannot instantiate Property for " + beanClass.getCanonicalName() + "." + setter.getName(), e);
                        property = null;
                    }
                }
            } catch (NoSuchMethodException | SecurityException e) {
                LOGGER.info("Cannot find getter for " + beanClass.getCanonicalName() + "." + setter.getName(), e);
                return;
            }
        }

        if (getter == null) {
            // not getter found
            return;
        }
        try {
            // if jHipsterFilter then do extra tests
            if (jHipsterFilter) {
                String propertyNameLower = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
                try {
                    Method fluentGetter = beanClass.getMethod(propertyNameLower);
                    Object result = fluentGetter.invoke(bean);
                    Object getterResult = getter.invoke(bean);
                    assertEquals( getterResult, result, "Testing " + propertyNameLower + "() of " + beanClass.getCanonicalName());
                    assertEquals(fluentGetter.invoke(bean),result,
                            "Testing " + propertyNameLower + "() of " + beanClass.getCanonicalName()


                    );
                } catch (NoSuchMethodException | SecurityException e) {

                    e.printStackTrace();
                }
            }
            setter.invoke(bean, property);
            Object result = getter.invoke(bean);
            assertEquals(property, result, "Testing " + setter.getName() + " of " + beanClass.getCanonicalName());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOGGER.info("Cannot invoke ..." + beanClass.getCanonicalName() + "." + setter.getName(), e);
            return;
        }
    }
}
