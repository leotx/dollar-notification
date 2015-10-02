package ltx.dollarnotification;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
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
}
