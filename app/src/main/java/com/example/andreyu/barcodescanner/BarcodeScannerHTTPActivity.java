package com.example.andreyu.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BarcodeScannerHTTPActivity extends AsyncTask<String, Void, String> {

    private Context context;
    public TextView scanningResultTextView;

    public BarcodeScannerHTTPActivity(Context context) {
        this.context = context;
        scanningResultTextView = (TextView) ((Activity) context).findViewById(R.id.textViewScanResult);
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        String barcode = params[0];
        URL url;
        HttpURLConnection con;
        BufferedReader bufferedReader;
        String resultPHP;

        String link = "http://37.142.226.146/query.php?itemcode=" + barcode;

        try {

            url = new URL(link);
            con = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            resultPHP = bufferedReader.readLine();

            return resultPHP;

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONArray arr = new JSONArray(result);
                JSONObject jsonObject = arr.getJSONObject(0);
                scanningResultTextView.setVisibility(View.VISIBLE);
                String data = "Barcode        : " + jsonObject.getString("ItemCode") + "\n" +
                        "Name           : " + jsonObject.getString("ItemName") +"\n" +
                        "Unit Quantity  : " + jsonObject.getString("UnitQty") + "\n" +
                        "Quantity       : " + jsonObject.getString("Quantity") + "\n" +
                        "Unit of Measure: " + jsonObject.getString("UnitOfMeasure") + "\n" +
                        "Is Weighted    : " + jsonObject.getString("bIsWeighted") + "\n" +
                        "Qty in Pack    : " + jsonObject.getString("QtyInPackage") + "\n" +
                        "Price          : " + jsonObject.getString("ItemPrice");

                scanningResultTextView.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_LONG).show();
        }
    }
}
