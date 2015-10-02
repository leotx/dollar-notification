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

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    public static final long NOTIFY_INTERVAL = 10 * 60000; // 10 minutes
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
                    Double currentDollar = DollarHelper.getCurrentDollar();
                    DollarHelper dollarHelper = new DollarHelper();
                    dollarHelper.verifyDollar(currentDollar);
                }
            });
        }
    }
}
