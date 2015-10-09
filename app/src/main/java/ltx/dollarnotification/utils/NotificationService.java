package ltx.dollarnotification.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

import ltx.dollarnotification.model.Quotation;

public class NotificationService extends Service {
    public static final long NOTIFY_INTERVAL = 1 * 60000; // 1 minute
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
                    boolean notificationActive = Preferences.getNotification(getApplicationContext());

                    if (!Operations.isOnline(getApplicationContext()) || !notificationActive){
                        return;
                    }

                    Quotation quotation = Operations.getQuotation(getApplicationContext());
                    if (quotation == null) return;

                    Double currentDollar = Double.parseDouble(quotation.getDolar().getCotacao());
                    Operations dollarHelper = new Operations();
                    dollarHelper.verifyDollar(getApplicationContext(), currentDollar);
                }
            });
        }
    }
}
