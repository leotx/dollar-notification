package ltx.dollarnotification.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import java.math.BigDecimal;

import ltx.dollarnotification.R;
import ltx.dollarnotification.activity.DollarActivity;
import ltx.dollarnotification.model.Quotation;

public class Operations {

    public boolean verifyDollar(Context applicationContext, double currentDollar) {
        final SharedPreferences settings = applicationContext.getSharedPreferences(Constants.PREFERENCES_NAME, 0);
        Double percentageEntry = Double.parseDouble(settings.getString(Constants.PREFERENCES_PERCENTAGE, "0.0"));
        Double currencyEntry = Double.parseDouble(settings.getString(Constants.PREFERENCES_CURRENCY_VALUE, "0.0"));
        Double quotationDollar = Double.parseDouble(settings.getString(Constants.PREFERENCES_QUOTATION_VALUE, "0.0"));
        Double lastDollar = Double.parseDouble(settings.getString(Constants.PREFERENCES_LAST_QUOTATION_VALUE, "0.0"));

        currentDollar = roundedValue(currencyEntry, currentDollar);

        if (lastDollar == currentDollar || currentDollar == 0)
            return false;

        Preferences.saveCurrentQuotation(applicationContext, currentDollar);

        if (percentageEntry > 0) {
            Double currentPercentage = roundedValue(percentageEntry, ((quotationDollar / currentDollar) - 1) * 100);

            if (currentPercentage >= percentageEntry) {
                showNotification(applicationContext);
                return true;
            }
        } else if (currencyEntry > 0 && currentDollar <= currencyEntry) {
            showNotification(applicationContext);
            return true;
        }

        return false;
    }

    public double roundedValue(double entryValue, double compareValue) {
        if (entryValue == 0) return compareValue;

        BigDecimal compareDecimal = BigDecimal.valueOf(entryValue);

        if (compareDecimal.scale() <= 2) {
            return Math.round(compareValue * 100.0) / 100.0;
        }

        return compareValue;
    }

    private void showNotification(Context applicationContext) {
        PendingIntent pi = PendingIntent.getActivity(applicationContext, 0, new Intent(applicationContext, DollarActivity.class), 0);
        Resources r = applicationContext.getResources();
        Notification notification = new NotificationCompat.Builder(applicationContext)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.dollar)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .build();

        NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(applicationContext.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public static boolean isOnline(Context applicationContext) {
        ConnectivityManager cm = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
