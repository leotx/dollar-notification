package ltx.dollarnotification.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import ltx.dollarnotification.R;

public class Operations {

    public boolean verifyDollar(double currentDollar) {
        Context appContext = App.getContext();

        final SharedPreferences settings = appContext.getSharedPreferences(appContext.getString(R.string.preferences_name), 0);
        Double percentageEntry = Double.parseDouble(settings.getString(appContext.getString(R.string.preferences_percentage), "0.0"));
        Double currencyEntry = Double.parseDouble(settings.getString(appContext.getString(R.string.preferences_currency_value), "0.0"));
        Double lastDollar = Double.parseDouble(settings.getString(appContext.getString(R.string.preferences_quotation_value), "0.0"));

        currentDollar = roundedValue(currencyEntry, currentDollar);

        if (percentageEntry > 0) {
            Double currentPercentage = roundedValue(percentageEntry, ((lastDollar / currentDollar) - 1) * 100);

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

    public double roundedValue(double entryValue, double compareValue){
        if (entryValue == 0) return compareValue;

        BigDecimal compareDecimal = BigDecimal.valueOf(entryValue);

        if (compareDecimal.scale() <= 2) {
            return Math.round(compareValue * 100.0) / 100.0;
        }

        return compareValue;
    }

    private void showNotification() {
        Context appContext = App.getContext();

        PendingIntent pi = PendingIntent.getActivity(appContext, 0, new Intent(appContext, NotificationService.class), 0);
        Resources r = appContext.getResources();
        Notification notification = new NotificationCompat.Builder(appContext)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.dollar)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(appContext.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    @Nullable
    public static double getCurrentDollar() {
        try {
            JSONObject dollarObject = new QuotationTask().execute().get()
                    .getJSONObject(App.getContext().getString(R.string.json_dolar));

            return Double.parseDouble(dollarObject.getString(App.getContext()
                    .getString(R.string.json_cotacao)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}
