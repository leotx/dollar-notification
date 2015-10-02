package ltx.dollarnotification.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import ltx.dollarnotification.helpers.Operations;
import ltx.dollarnotification.helpers.NotificationService;
import ltx.dollarnotification.helpers.Preferences;
import ltx.dollarnotification.R;

public class DollarActivity extends ActionBarActivity {

    Button btnNotification;
    Button btnRefresh;
    TextView txtDollarValue;
    EditText txtPercentage;
    RadioButton rPercentage;
    RadioButton rValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dolar);

        startService(new Intent(this, NotificationService.class));

        final SharedPreferences settings = getSharedPreferences(getString(R.string.preferences_name), 0);

        txtDollarValue = (TextView) findViewById(R.id.lblCurrentDollarValue);

        txtPercentage = (EditText) findViewById(R.id.txtEntryValue);

        rPercentage = (RadioButton) findViewById(R.id.rPercentage);
        rValue = (RadioButton) findViewById(R.id.rValue);

        verifyType(settings);

        btnNotification = (Button) findViewById(R.id.btnNotification);
        notificationClick();

        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        refreshClick();

        btnRefresh.performClick();
    }

    private void verifyType(SharedPreferences settings) {
        boolean isPercentage = settings.getBoolean(getString(R.string.preferences_ispercentage), true);

        if (isPercentage){
            rPercentage.setChecked(true);
            txtPercentage.setText(settings.getString(getString(R.string.preferences_percentage), ""));
        }
        else {
            rValue.setChecked(true);
            txtPercentage.setText(settings.getString(getString(R.string.preferences_currency_value), ""));
        }
    }

    private void refreshClick() {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                final AnimationDrawable dAnimate = (AnimationDrawable) btnRefresh.getCompoundDrawables()[0];
                dAnimate.start();

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        final double dollarValue = Operations.getCurrentDollar();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                                DecimalFormat formatter = new DecimalFormat("$##0.00##", otherSymbols);

                                txtDollarValue.setText(formatter.format(dollarValue));
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
        });
    }

    private void notificationClick() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                if (txtPercentage.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), R.string.notification_error, Toast.LENGTH_SHORT).show();
                }

                String dollarValue = txtDollarValue.getText().toString().replace("$","");

                Preferences.createPreferences(dollarValue, String.valueOf(txtPercentage.getText()), rPercentage.isChecked());

                Toast.makeText(getApplicationContext(), R.string.notification_done, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dolar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
