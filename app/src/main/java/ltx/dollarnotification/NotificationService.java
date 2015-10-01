package ltx.dollarnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    public static final long NOTIFY_INTERVAL = 30 * 60000; // 30 minutes
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    VerifyDollar();
                }
            });
        }

        private void VerifyDollar() {
            showNotification();

            final SharedPreferences settings = getSharedPreferences(getString(R.string.preferences_name), 0);
            Double percentageEntry = Double.parseDouble(settings.getString(getString(R.string.preferences_percentage), "0"));
            Double currencyEntry = Double.parseDouble(settings.getString(getString(R.string.preferences_currency_value), "0"));
            Double lastDollar = Double.parseDouble(settings.getString(getString(R.string.preferences_quotation_value), "0"));

            double currentDollar = DollarActivity.getCurrentDollar();

            if (percentageEntry > 0) {
                Double currentPercentage = ((currentDollar / lastDollar) - 1) * 100;

                if (currentPercentage >= percentageEntry) {
                    showNotification();
                }
            } else if (currencyEntry > 0 && currentDollar <= currencyEntry) {
                showNotification();
            }
        }
    }

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, NotificationService.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.dollar)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
