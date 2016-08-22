package com.example.andreyu.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BarcodeScannerHTTPActivity extends AsyncTask<String, Void, String> {

    private Context context;
    public String barcode;
    public ItemAdapter itemAdapter;
    public ListView lvItems;
    public TextView textViewTotal;
    public float totalAmount = 0;

    public BarcodeScannerHTTPActivity(Context context, ItemAdapter adapter) {
        this.context = context;
        itemAdapter = adapter;
        lvItems = (ListView) ((Activity) context).findViewById(R.id.lvItems);
        textViewTotal = (TextView) ((Activity) context).findViewById(R.id.textViewTotal);
        lvItems.setAdapter(itemAdapter);
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        barcode = params[0];
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
        Item item;
        if (result != null) {
            try {
                JSONArray arr = new JSONArray(result);
                JSONObject jsonObjectStatus = arr.getJSONObject(0);
                if (jsonObjectStatus.getString("query_status").equals("success")) {
                    JSONObject jsonObject = arr.getJSONObject(1);
                    item = new Item(jsonObject.getString("ItemCode"), jsonObject.getString("ItemName"), Float.parseFloat(jsonObject.getString("ItemPrice")));
                    totalAmount = BarcodeScannerMainActivity.addItemAmount(Float.parseFloat(jsonObject.getString("ItemPrice")));
                    textViewTotal.setText(String.format("%.0f", totalAmount));
                } else {
                    item = new Item(barcode, context.getResources().getString(R.string.ItemNotFound), 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                item = new Item("00000", context.getResources().getString(R.string.ReadJSONProblem), 0);
            }
        } else {
            item = new Item("00000", context.getResources().getString(R.string.CantGetJSON), 0);
        }
        itemAdapter.add(item);
        itemAdapter.notifyDataSetChanged();
    }
}