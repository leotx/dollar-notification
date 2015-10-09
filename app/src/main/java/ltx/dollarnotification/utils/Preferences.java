package ltx.dollarnotification.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static void createPreferences(Context applicationContext, String dollar, String entryValue, boolean isPercentage){
        SharedPreferences.Editor sharedEditor = createEditor(applicationContext);
        sharedEditor.remove(Constants.PREFERENCES_PERCENTAGE);
        sharedEditor.remove(Constants.PREFERENCES_CURRENCY_VALUE);

        if (isPercentage) {
            sharedEditor.putString(Constants.PREFERENCES_PERCENTAGE, entryValue);
        } else {
            sharedEditor.putString(Constants.PREFERENCES_CURRENCY_VALUE, entryValue);
        }

        sharedEditor.putBoolean(Constants.PREFERENCES_IS_PERCENTAGE, isPercentage);
        sharedEditor.putString(Constants.PREFERENCES_QUOTATION_VALUE, dollar);
        sharedEditor.commit();
    }

    public static void activateNotification(Context applicationContext){
        Notification(applicationContext, true);
    }

    public static void deactivateNotification(Context applicationContext){
        Notification(applicationContext, false);
    }

    public static boolean getNotification(Context applicationContext){
        SharedPreferences settings = applicationContext.getSharedPreferences(Constants.PREFERENCES_NAME, 0);

        return settings.getBoolean(Constants.PREFERENCES_NOTIFICATION_ACTIVE, false);
    }

    private static SharedPreferences.Editor createEditor(Context applicationContext){
        SharedPreferences settings = applicationContext.getSharedPreferences(Constants.PREFERENCES_NAME, 0);

        return settings.edit();
    }

    private static void Notification(Context applicationContext, boolean activateNotification) {
        SharedPreferences.Editor sharedEditor = createEditor(applicationContext);

        sharedEditor.putBoolean(Constants.PREFERENCES_NOTIFICATION_ACTIVE, activateNotification);
        sharedEditor.commit();
    }

    public static void saveCurrentQuotation(Context applicationContext, double currentDollar) {
        SharedPreferences.Editor sharedEditor = createEditor(applicationContext);

        sharedEditor.putString(Constants.PREFERENCES_LAST_QUOTATION_VALUE, String.valueOf(currentDollar));
        sharedEditor.commit();
    }
}
