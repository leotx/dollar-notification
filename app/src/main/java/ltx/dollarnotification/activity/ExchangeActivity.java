package ltx.dollarnotification.activity;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import ltx.dollarnotification.R;
import ltx.dollarnotification.utils.Operations;
import ltx.dollarnotification.model.Quotation;
import ltx.dollarnotification.utils.QuotationTask;
import ltx.dollarnotification.utils.TaskDelegate;

public class ExchangeActivity extends AppCompatActivity {

    @Bind(R.id.lblDollarValue)
    TextView lblDollarValue;
    @Bind(R.id.lblDollarVariation)
    TextView lblDollarVariation;
    @Bind(R.id.lblEuroValue)
    TextView lblEuroValue;
    @Bind(R.id.lblEuroVariation)
    TextView lblEuroVariation;
    @Bind(R.id.lblBovespaValue)
    TextView lblBovespaValue;
    @Bind(R.id.lblBovespaVariation)
    TextView lblBovespaVariation;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        loadSwipe();
        new QuotationTask(getApplicationContext(), getQuotationDelegate()).execute();
    }

    private void loadSwipe() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new QuotationTask(getApplicationContext(), getQuotationDelegate()).execute();

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
    }

    @NonNull
    private TaskDelegate getQuotationDelegate() {
        return new TaskDelegate() {
            @Override
            public void taskCompletionResult(Quotation quotation) {
                getExchanges(quotation);
            }
        };
    }

    public void getExchanges(Quotation quotation) {
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
