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
        String data;
        if (result != null) {
            try {
                JSONArray arr = new JSONArray(result);
                JSONObject jsonObjectStatus = arr.getJSONObject(0);
                scanningResultTextView.setVisibility(View.VISIBLE);
                if (jsonObjectStatus.getString("query_status").equals("success")) {
                    JSONObject jsonObject = arr.getJSONObject(1);
                    data =
                            context.getResources().getString(R.string.ItemCode) + jsonObject.getString("ItemCode") + "\n" +
                                    context.getResources().getString(R.string.ItemName) + jsonObject.getString("ItemName") + "\n" +
                                    context.getResources().getString(R.string.UnitQty) + jsonObject.getString("UnitQty") + "\n" +
                                    context.getResources().getString(R.string.Quantity) + jsonObject.getString("Quantity") + "\n" +
                                    context.getResources().getString(R.string.UnitOfMeasure) + jsonObject.getString("UnitOfMeasure") + "\n" +
                                    context.getResources().getString(R.string.bIsWeighted) + jsonObject.getString("bIsWeighted") + "\n" +
                                    context.getResources().getString(R.string.QtyInPackage) + jsonObject.getString("QtyInPackage") + "\n" +
                                    context.getResources().getString(R.string.ItemPrice) + jsonObject.getString("ItemPrice");

                } else {
                    data = context.getResources().getString(R.string.ItemNotFound);
                }
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
