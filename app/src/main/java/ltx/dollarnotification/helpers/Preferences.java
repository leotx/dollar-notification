package ltx.dollarnotification.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import ltx.dollarnotification.R;

public class Preferences {
    public static void createPreferences(String dollar, String entryValue, boolean isPercentage){
        Context appContext = App.getContext();

        final SharedPreferences settings = appContext.getSharedPreferences(appContext.getString(R.string.preferences_name), 0);
        SharedPreferences.Editor sharedEditor = settings.edit();
        sharedEditor.remove(appContext.getString(R.string.preferences_percentage));
        sharedEditor.remove(appContext.getString(R.string.preferences_currency_value));

        if (isPercentage) {
            sharedEditor.putString(appContext.getString(R.string.preferences_percentage), entryValue);
        } else {
            sharedEditor.putString(appContext.getString(R.string.preferences_currency_value), entryValue);
        }

        sharedEditor.putBoolean(appContext.getString(R.string.preferences_ispercentage), isPercentage);
        sharedEditor.putString(appContext.getString(R.string.preferences_quotation_value), dollar);
        sharedEditor.commit();
    }

    public static void activateNotification(){
        Notification(true);
    }

    public static void deactivateNotification(){
        Notification(false);
    }

    public static boolean getNotification(){
        Context appContext = App.getContext();
        SharedPreferences settings = appContext.getSharedPreferences(appContext.getString(R.string.preferences_name), 0);
        return settings.getBoolean(appContext.getString(R.string.preferences_notification_active), false);
    }

    private static void Notification(boolean activateNotification) {
        Context appContext = App.getContext();

        SharedPreferences settings = appContext.getSharedPreferences(appContext.getString(R.string.preferences_name), 0);
        SharedPreferences.Editor sharedEditor = settings.edit();

        sharedEditor.putBoolean(appContext.getString(R.string.preferences_notification_active), activateNotification);
        sharedEditor.commit();
    }

    public static void saveCurrentQuotation(double currentDollar) {
        Context appContext = App.getContext();

        SharedPreferences settings = appContext.getSharedPreferences(appContext.getString(R.string.preferences_name), 0);
        SharedPreferences.Editor sharedEditor = settings.edit();

        sharedEditor.putString(appContext.getString(R.string.preferences_last_quotation_value), String.valueOf(currentDollar));
        sharedEditor.commit();
    }
}
