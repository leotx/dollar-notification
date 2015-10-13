package ltx.dollarnotification.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import ltx.dollarnotification.utils.Constants;
import ltx.dollarnotification.utils.NotificationService;
import ltx.dollarnotification.utils.Preferences;
import ltx.dollarnotification.R;
import ltx.dollarnotification.model.Quotation;
import ltx.dollarnotification.utils.QuotationTask;
import ltx.dollarnotification.utils.TaskDelegate;

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
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dollar);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(R.string.dollar_activity);

        startService(new Intent(this, NotificationService.class));

        final SharedPreferences settings = getSharedPreferences(Constants.PREFERENCES_NAME, 0);

        verifyType(settings);
        refreshDollar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDollar();
    }

    private void verifyType(SharedPreferences settings) {
        boolean isPercentage = settings.getBoolean(Constants.PREFERENCES_IS_PERCENTAGE, true);
        boolean notificationActive = Preferences.getNotification(getApplicationContext());

        if (isPercentage) {
            rPercentage.setChecked(true);
            txtPercentage.setText(settings.getString(Constants.PREFERENCES_PERCENTAGE, ""));
        } else {
            rValue.setChecked(true);
            txtPercentage.setText(settings.getString(Constants.PREFERENCES_CURRENCY_VALUE, ""));
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

        new QuotationTask(getApplicationContext(), getQuotationDelegate()).execute();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dAnimate.stop();
            }
        }, 2000);
    }

    @NonNull
    private TaskDelegate getQuotationDelegate() {
        return new TaskDelegate() {
            @Override
            public void taskCompletionResult(Quotation quotation) {
                final double dollarValue = Double.parseDouble(quotation.getDolar().getCotacao());

                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat formatter = new DecimalFormat("R$ ##0.00##", otherSymbols);

                lblDollarValue.setText(formatter.format(dollarValue));
            }
        };
    }

    @OnClick(R.id.btnNotification)
    public void notificationClick() {
        boolean notificationActive = Preferences.getNotification(getApplicationContext());

        if (!notificationActive) {
            if (txtPercentage.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.notification_error, Toast.LENGTH_SHORT).show();
                return;
            }

            String dollarValue = lblDollarValue.getText().toString().replace("R$ ", "");

            Preferences.createPreferences(getApplicationContext(), dollarValue, String.valueOf(txtPercentage.getText()), rPercentage.isChecked());
            Preferences.activateNotification(getApplicationContext());

            btnNotification.setText(R.string.deactivate_notification);

            Toast.makeText(getApplicationContext(), R.string.notification_done, Toast.LENGTH_SHORT).show();
        } else {
            btnNotification.setText(R.string.activate_notification);

            Preferences.deactivateNotification(getApplicationContext());

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
