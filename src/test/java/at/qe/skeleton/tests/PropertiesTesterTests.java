package at.qe.skeleton.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PropertiesTesterTests {



    public static class TestBean {
        private boolean booleanField;
        private String stringField;
        private int intField;
        private String[] arrayField;

        public boolean isBooleanField() {
            return booleanField;
        }

        public void setBooleanField(boolean booleanField) {
            this.booleanField = booleanField;
        }

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }

        public String getDoubledString() {
            return stringField;
        }

        public void setDoubledString(String doubledString) {
            if (doubledString != null) {
                this.stringField = doubledString + doubledString;
            } else {
                this.stringField = null;
            }

        }

        public String[] getArrayField() {
            return arrayField;
        }

        public void setArrayField(String[] arrayField) {
            this.arrayField = arrayField;
        }

    }
    @Test
    void testBean() {
        PropertiesTester pt = new PropertiesTester(); // No need to create a new instance

       pt.testProperties(TestBean.class, /* except */ "setDoubledString");

        // extra tests for setDoubledString
        TestBean tb = new TestBean();
        tb.setDoubledString("abc");
        assertEquals("abcabc", tb.getDoubledString());
    }
}
