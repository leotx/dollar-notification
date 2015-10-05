package ltx.dollarnotification.helpers;

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
import java.util.concurrent.ExecutionException;

import ltx.dollarnotification.R;
import ltx.dollarnotification.activities.DollarActivity;
import ltx.dollarnotification.model.Quotation;

public class Operations {

    public boolean verifyDollar(double currentDollar) {
        Context appContext = App.getContext();

        final SharedPreferences settings = appContext.getSharedPreferences(appContext.getString(R.string.preferences_name), 0);
        Double percentageEntry = Double.parseDouble(settings.getString(appContext.getString(R.string.preferences_percentage), "0.0"));
        Double currencyEntry = Double.parseDouble(settings.getString(appContext.getString(R.string.preferences_currency_value), "0.0"));
        Double quotationDollar = Double.parseDouble(settings.getString(appContext.getString(R.string.preferences_quotation_value), "0.0"));
        Double lastDollar = Double.parseDouble(settings.getString(appContext.getString(R.string.preferences_last_quotation_value), "0.0"));

        currentDollar = roundedValue(currencyEntry, currentDollar);

        if (lastDollar == currentDollar || currentDollar == 0)
            return false;

        Preferences.saveCurrentQuotation(currentDollar);

        if (percentageEntry > 0) {
            Double currentPercentage = roundedValue(percentageEntry, ((quotationDollar / currentDollar) - 1) * 100);

            if (currentPercentage >= percentageEntry) {
                showNotification();
                return true;
            }
        } else if (currencyEntry > 0 && currentDollar <= currencyEntry) {
            showNotification();
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

    private void showNotification() {
        Context appContext = App.getContext();

        PendingIntent pi = PendingIntent.getActivity(appContext, 0, new Intent(appContext, DollarActivity.class), 0);
        Resources r = appContext.getResources();
        Notification notification = new NotificationCompat.Builder(appContext)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.dollar)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .build();

        NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(appContext.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public static boolean isOnline() {
        Context appContext = App.getContext();

        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Quotation getQuotation(){
        try {
            return new QuotationTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
