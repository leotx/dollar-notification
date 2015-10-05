package ltx.dollarnotification.activities;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import ltx.dollarnotification.R;
import ltx.dollarnotification.helpers.App;
import ltx.dollarnotification.helpers.Operations;
import ltx.dollarnotification.helpers.QuotationTask;

public class ExchangeActivity extends AppCompatActivity {

    TextView lblDollarValue;
    TextView lblDollarVariation;
    TextView lblEuroValue;
    TextView lblEuroVariation;
    TextView lblBovespaValue;
    TextView lblBovespaVariation;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        lblDollarValue = (TextView) findViewById(R.id.lblDollarValue);
        lblDollarVariation = (TextView) findViewById(R.id.lblDollarVariation);
        lblEuroValue = (TextView) findViewById(R.id.lblEuroValue);
        lblEuroVariation = (TextView) findViewById(R.id.lblEuroVariation);
        lblBovespaValue = (TextView) findViewById(R.id.lblBovespaValue);
        lblBovespaVariation = (TextView) findViewById(R.id.lblBovespaVariation);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getExchanges();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getExchanges();
    }

    public void getExchanges() {
        try {
            JSONObject exchangeObject = new QuotationTask().execute().get();

            if (exchangeObject == null) return;

            JSONObject dollarObject = exchangeObject.getJSONObject(getString(R.string.json_dolar));
            JSONObject euroObject = exchangeObject.getJSONObject(getString(R.string.json_euro));
            JSONObject bovespaObject = exchangeObject.getJSONObject(getString(R.string.json_bovespa));

            lblDollarValue.setText(dollarObject.getString(getString(R.string.json_cotacao)));
            lblDollarVariation.setText(dollarObject.getString(getString(R.string.json_variation)));

            lblEuroValue.setText(euroObject.getString(getString(R.string.json_cotacao)));
            lblEuroVariation.setText(euroObject.getString(getString(R.string.json_variation)));

            lblBovespaValue.setText(bovespaObject.getString(getString(R.string.json_cotacao)));
            lblBovespaVariation.setText(bovespaObject.getString(getString(R.string.json_variation)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
