package ltx.dollarnotification;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testVerifyDollarValue(){
        PreferencesHelper.createPreferences("3.95", "3.94", false);
        Double currentDollar = 3.93;
        DollarHelper dollarHelper = new DollarHelper();
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyDollarValueRounded(){
        PreferencesHelper.createPreferences("3.95", "3.96", false);
        Double currentDollar = 3.9647;
        DollarHelper dollarHelper = new DollarHelper();
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyDollarValueFalse(){
        PreferencesHelper.createPreferences("3.95", "3.96", false);
        Double currentDollar = 3.97;
        DollarHelper dollarHelper = new DollarHelper();
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(false, dollarVerify);
    }

    public void testVerifyPercentage(){
        PreferencesHelper.createPreferences("3.95", "0.51", true);
        Double currentDollar = 3.93;
        DollarHelper dollarHelper = new DollarHelper();
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyPercentageWithoutRound(){
        PreferencesHelper.createPreferences("3.95", "0.5089", true);
        Double currentDollar = 3.93;
        DollarHelper dollarHelper = new DollarHelper();
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyPercentageFalse(){
        PreferencesHelper.createPreferences("3.95", "0.52", true);
        Double currentDollar = 3.93;
        DollarHelper dollarHelper = new DollarHelper();
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(false, dollarVerify);
    }

    public void testVerifyDollarValueWithoutRounded(){
        PreferencesHelper.createPreferences("3.95", "3.9647", false);
        Double currentDollar = 3.9647;
        DollarHelper dollarHelper = new DollarHelper();
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testRound(){
        DollarHelper dollarHelper = new DollarHelper();

        double dollarRounded = dollarHelper.roundedValue(3.95, 3.9548);
        assertEquals(3.95, dollarRounded);

        dollarRounded = dollarHelper.roundedValue(3.95, 3.9568);
        assertEquals(3.96, dollarRounded);
    }

    public void testWithoutRound(){
        DollarHelper dollarHelper = new DollarHelper();
        double dollarRounded = dollarHelper.roundedValue(3.957, 3.9548);

        assertEquals(3.9548, dollarRounded);
    }
}