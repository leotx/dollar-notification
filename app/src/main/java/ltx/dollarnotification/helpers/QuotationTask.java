package ltx.dollarnotification.helpers;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;
import ltx.dollarnotification.R;
import ltx.dollarnotification.models.Quotation;

public class QuotationTask extends AsyncTask<Void, Void, Quotation> {
    private static Quotation quotation;
    private static final String URL_EXCHANGE = "http://developers.agenciaideias.com.br/cotacoes/json";

    @Override
    protected Quotation doInBackground(Void... v) {
        Quotation quotation = loadQuotations();

        return quotation;
    }

    private static Quotation loadQuotations() {
        if (!Operations.isOnline()) {
            return null;
        }

        SyncHttpClient client = new SyncHttpClient();
        client.get(URL_EXCHANGE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                quotation = gson.fromJson(new String(responseBody), Quotation.class);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(App.getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });

        return quotation;
    }
}
