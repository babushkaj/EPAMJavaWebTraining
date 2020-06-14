package by.training.module3.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AnnotationTest {

    @Test
    public void shouldReturnTrue1(){
        TestClass test = new TestClass(85,15);

        ValidAnnotationHandler v = new ValidAnnotationHandler();

        boolean result = v.isValid(test);

        Assert.assertTrue(result);
    }

    @Test
    public void shouldReturnTrue2(){
        TestClass test = new TestClass(85,15);
        WrapperClass wrapperClass = new WrapperClass(test,85);

        ValidAnnotationHandler v = new ValidAnnotationHandler();

        boolean result = v.isValid(wrapperClass);

        Assert.assertTrue(result);
    }

    @Test
    public void shouldReturnFalse1(){
        TestClass test = new TestClass(95,15);

        ValidAnnotationHandler v = new ValidAnnotationHandler();

        boolean result = v.isValid(test);

        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalse2(){
        TestClass test = new TestClass(85,35);

        ValidAnnotationHandler v = new ValidAnnotationHandler();

        boolean result = v.isValid(test);

        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalse3(){
        TestClass test = new TestClass(85,35);
        WrapperClass wrapperClass = new WrapperClass(test,95);

        ValidAnnotationHandler v = new ValidAnnotationHandler();

        boolean result = v.isValid(wrapperClass);

        Assert.assertFalse(result);
    }
}

@Valid
class TestClass{
    @ValidDoubleSize(minSize = 80, maxSize = 90)
    private double d;
    @ValidIntSize(minSize = 10, maxSize = 30)
    private int i;

    TestClass(double d, int i) {
        this.d = d;
        this.i = i;
    }
}

@Valid
class WrapperClass{
    private TestClass testClass;
    @ValidDoubleSize(minSize = 80, maxSize = 90)
    private double d;

    WrapperClass(TestClass testClass, double d) {
        this.testClass = testClass;
        this.d = d;
    }

    public TestClass getTestClass() {
        return testClass;
    }
}

