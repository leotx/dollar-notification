package ltx.dollarnotification.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ltx.dollarnotification.R;
import ltx.dollarnotification.utils.Operations;
import ltx.dollarnotification.model.Quotation;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);

        loadSwipe();
        Quotation quotation = Operations.getQuotation();
        getExchanges(quotation);
    }

    private void loadSwipe() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        final Quotation quotation = Operations.getQuotation();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getExchanges(quotation);
                            }
                        });
                    }
                });

                thread.start();

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
