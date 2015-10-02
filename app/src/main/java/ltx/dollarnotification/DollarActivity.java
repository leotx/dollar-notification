package ltx.dollarnotification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
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
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

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
        txtPercentage.setText(settings.getString(getString(R.string.preferences_percentage), ""));

        rPercentage = (RadioButton) findViewById(R.id.rPercentage);
        rValue = (RadioButton) findViewById(R.id.rValue);

        btnNotification = (Button) findViewById(R.id.btnNotification);
        notificationClick(settings);

        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        refreshClick();

        btnRefresh.performClick();
    }

    private void refreshClick() {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                final AnimationDrawable dAnimate = (AnimationDrawable) btnRefresh.getCompoundDrawables()[0];
                dAnimate.start();

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        final double dollarValue = getCurrentDollar();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NumberFormat formatter = new DecimalFormat("$##0.00##");
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

    private void notificationClick(final SharedPreferences settings) {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                SharedPreferences.Editor sharedEditor = settings.edit();
                sharedEditor.remove(getString(R.string.preferences_percentage));
                sharedEditor.remove(getString(R.string.preferences_currency_value));

                if (rPercentage.isChecked()) {
                    sharedEditor.putString(getString(R.string.preferences_percentage), String.valueOf(txtPercentage.getText()));
                } else {
                    sharedEditor.putString(getString(R.string.preferences_currency_value), String.valueOf(txtPercentage.getText()));
                }

                String dollarValue = txtDollarValue.getText().toString().replace("$","");

                sharedEditor.putString(getString(R.string.preferences_quotation_value), dollarValue);
                sharedEditor.commit();

                Toast.makeText(getApplicationContext(), R.string.notification_done, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    public static double getCurrentDollar() {
        double currentDollar = 0;

        try {
            currentDollar = Double.parseDouble(new QuotationTask().execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return currentDollar;
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
