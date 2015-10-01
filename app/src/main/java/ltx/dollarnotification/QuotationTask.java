package ltx.dollarnotification;

import android.content.res.Resources;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

class QuotationTask extends AsyncTask<Void, Void, String> {
    private final String URL = "http://developers.agenciaideias.com.br/cotacoes/json";

    @Override
    protected String doInBackground(Void... v) {
        InputStream is = null;

        try {
            is = new URL(URL).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject dollarObject = new JSONObject(jsonText).getJSONObject(App.getContext().getString(R.string.json_dolar));
            return dollarObject.getString(App.getContext().getString(R.string.json_cotacao));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
