package ltx.dollarnotification.activities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ltx.dollarnotification.R;
import ltx.dollarnotification.helpers.Operations;
import ltx.dollarnotification.model.Quotation;

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
        Quotation quotation = Operations.getQuotation();

        if (quotation == null)
            return;

        lblDollarValue.setText(quotation.getDolar().getCotacao());
        lblDollarVariation.setText(quotation.getDolar().getVariacao());

        lblEuroValue.setText(quotation.getEuro().getCotacao());
        lblEuroVariation.setText(quotation.getEuro().getVariacao());

        lblBovespaValue.setText(quotation.getBovespa().getCotacao());
        lblBovespaVariation.setText(quotation.getBovespa().getVariacao());

    }
}
