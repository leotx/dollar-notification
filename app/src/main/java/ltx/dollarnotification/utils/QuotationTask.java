package ltx.dollarnotification.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;
import ltx.dollarnotification.R;
import ltx.dollarnotification.model.Quotation;

public class QuotationTask extends AsyncTask<Void, Void, Quotation> {
    private static Quotation quotation;
    private static final String URL_EXCHANGE = "http://developers.agenciaideias.com.br/cotacoes/json";
    private static Context applicationContext = null;
    private final TaskDelegate delegate;

    public QuotationTask (Context context, TaskDelegate delegate){
        applicationContext = context;
        this.delegate = delegate;
    }

    @Override
    protected Quotation doInBackground(Void... v) {
        Quotation quotation = loadQuotations();

        return quotation;
    }

    private static Quotation loadQuotations() {
        if (!Operations.isOnline(applicationContext)) {
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
                Toast.makeText(applicationContext, R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });

        return quotation;
    }

    @Override
    protected void onPostExecute(Quotation  quotation) {
        super.onPostExecute(quotation);
        delegate.taskCompletionResult(quotation);
    }
}
