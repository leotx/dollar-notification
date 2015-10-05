package ltx.dollarnotification;

import android.app.Application;
import android.test.ApplicationTestCase;

import ltx.dollarnotification.helpers.Operations;
import ltx.dollarnotification.helpers.Preferences;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testVerifyDollarValue(){
        Preferences.createPreferences("3.95", "3.94", false);
        Double currentDollar = 3.93;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(0);
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyDollarValueRounded(){
        Preferences.createPreferences("3.95", "3.96", false);
        Double currentDollar = 3.9647;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(0);
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyDollarValueFalse(){
        Preferences.createPreferences("3.95", "3.96", false);
        Double currentDollar = 3.97;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(0);
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(false, dollarVerify);
    }

    public void testVerifyPercentage(){
        Preferences.createPreferences("3.95", "0.51", true);
        Double currentDollar = 3.93;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(0);
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyPercentageWithoutRound(){
        Preferences.createPreferences("3.95", "0.5089", true);
        Double currentDollar = 3.93;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(0);
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyPercentageFalse(){
        Preferences.createPreferences("3.95", "0.52", true);
        Double currentDollar = 3.93;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(0);
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(false, dollarVerify);
    }

    public void testVerifyDollarValueWithoutRounded(){
        Preferences.createPreferences("3.95", "3.9647", false);
        Double currentDollar = 3.9647;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(0);
        boolean dollarVerify = dollarHelper.verifyDollar(currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testRound(){
        Operations dollarHelper = new Operations();

        double dollarRounded = dollarHelper.roundedValue(3.95, 3.9548);
        assertEquals(3.95, dollarRounded);

        dollarRounded = dollarHelper.roundedValue(3.95, 3.9568);
        assertEquals(3.96, dollarRounded);
    }

    public void testWithoutRound(){
        Operations dollarHelper = new Operations();
        double dollarRounded = dollarHelper.roundedValue(3.957, 3.9548);

        assertEquals(3.9548, dollarRounded);
    }
}