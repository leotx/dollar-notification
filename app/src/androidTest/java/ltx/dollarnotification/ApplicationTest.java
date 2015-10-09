package ltx.dollarnotification;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import ltx.dollarnotification.utils.Operations;
import ltx.dollarnotification.utils.Preferences;

public class ApplicationTest extends ApplicationTestCase<Application> {
    private final Context applicationContext;

    public ApplicationTest() {
        super(Application.class);
        applicationContext = getContext().getApplicationContext();
    }

    public void testVerifyDollarValue(){
        Preferences.createPreferences(applicationContext, "3.95", "3.94", false);
        Double currentDollar = 3.93;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(applicationContext, 0);
        boolean dollarVerify = dollarHelper.verifyDollar(applicationContext, currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyDollarValueRounded(){
        Preferences.createPreferences(applicationContext, "3.95", "3.96", false);
        Double currentDollar = 3.9647;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(applicationContext, 0);
        boolean dollarVerify = dollarHelper.verifyDollar(applicationContext, currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyDollarValueFalse(){
        Preferences.createPreferences(applicationContext, "3.95", "3.96", false);
        Double currentDollar = 3.97;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(applicationContext, 0);
        boolean dollarVerify = dollarHelper.verifyDollar(applicationContext, currentDollar);

        assertEquals(false, dollarVerify);
    }

    public void testVerifyPercentage(){
        Preferences.createPreferences(applicationContext, "3.95", "0.51", true);
        Double currentDollar = 3.93;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(applicationContext, 0);
        boolean dollarVerify = dollarHelper.verifyDollar(applicationContext, currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyPercentageWithoutRound(){
        Preferences.createPreferences(applicationContext, "3.95", "0.5089", true);
        Double currentDollar = 3.93;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(applicationContext, 0);
        boolean dollarVerify = dollarHelper.verifyDollar(applicationContext, currentDollar);

        assertEquals(true, dollarVerify);
    }

    public void testVerifyPercentageFalse(){
        Preferences.createPreferences(applicationContext, "3.95", "0.52", true);
        Double currentDollar = 3.93;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(applicationContext, 0);
        boolean dollarVerify = dollarHelper.verifyDollar(applicationContext, currentDollar);

        assertEquals(false, dollarVerify);
    }

    public void testVerifyDollarValueWithoutRounded(){
        Preferences.createPreferences(applicationContext, "3.95", "3.9647", false);
        Double currentDollar = 3.9647;
        Operations dollarHelper = new Operations();
        Preferences.saveCurrentQuotation(applicationContext, 0);
        boolean dollarVerify = dollarHelper.verifyDollar(applicationContext, currentDollar);

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