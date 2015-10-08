package ltx.dollarnotification.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ltx.dollarnotification.helpers.Operations;
import ltx.dollarnotification.helpers.NotificationService;
import ltx.dollarnotification.helpers.Preferences;
import ltx.dollarnotification.R;
import ltx.dollarnotification.models.Quotation;

public class DollarActivity extends AppCompatActivity {

    @Bind(R.id.btnNotification)
    Button btnNotification;
    @Bind(R.id.btnRefresh)
    Button btnRefresh;
    @Bind(R.id.lblCurrentDollarValue)
    TextView lblDollarValue;
    @Bind(R.id.txtEntryValue)
    EditText txtPercentage;
    @Bind(R.id.rPercentage)
    RadioButton rPercentage;
    @Bind(R.id.rValue)
    RadioButton rValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dollar);
        ButterKnife.bind(this);

        startService(new Intent(this, NotificationService.class));

        final SharedPreferences settings = getSharedPreferences(getString(R.string.preferences_name), 0);

        verifyType(settings);
        refreshDollar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDollar();
    }

    private void verifyType(SharedPreferences settings) {
        boolean isPercentage = settings.getBoolean(getString(R.string.preferences_ispercentage), true);
        boolean notificationActive = Preferences.getNotification();

        if (isPercentage) {
            rPercentage.setChecked(true);
            txtPercentage.setText(settings.getString(getString(R.string.preferences_percentage), ""));
        } else {
            rValue.setChecked(true);
            txtPercentage.setText(settings.getString(getString(R.string.preferences_currency_value), ""));
        }

        if (notificationActive) {
            btnNotification.setText(R.string.deactivate_notification);
        } else {
            btnNotification.setText(R.string.activate_notification);
        }
    }

    @OnClick(R.id.btnRefresh)
    public void refreshDollar() {
        final AnimationDrawable dAnimate = (AnimationDrawable) btnRefresh.getCompoundDrawables()[0];
        dAnimate.start();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                Quotation quotation = Operations.getQuotation();

                if (quotation == null) {
                    dAnimate.stop();
                    return;
                }

                final double dollarValue = Double.parseDouble(quotation.getDolar().getCotacao());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                        DecimalFormat formatter = new DecimalFormat("R$ ##0.00##", otherSymbols);

                        lblDollarValue.setText(formatter.format(dollarValue));
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    dAnimate.stop();
                }
            }
        });

        thread.start();
    }

    @OnClick(R.id.btnNotification)
    public  void notificationClick() {
        boolean notificationActive = Preferences.getNotification();

        if (!notificationActive) {
            if (txtPercentage.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.notification_error, Toast.LENGTH_SHORT).show();
                return;
            }

            String dollarValue = lblDollarValue.getText().toString().replace("R$ ", "");

            Preferences.createPreferences(dollarValue, String.valueOf(txtPercentage.getText()), rPercentage.isChecked());
            Preferences.activateNotification();

            btnNotification.setText(R.string.deactivate_notification);

            Toast.makeText(getApplicationContext(), R.string.notification_done, Toast.LENGTH_SHORT).show();
        } else {
            btnNotification.setText(R.string.activate_notification);

            Preferences.deactivateNotification();

            Toast.makeText(getApplicationContext(), R.string.notification_deactivated, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dolar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exchange) {
            Intent myIntent = new Intent(DollarActivity.this, ExchangeActivity.class);
            DollarActivity.this.startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
