package com.example.andreyu.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;

public class BarcodeScannerMainActivity extends AppCompatActivity {

    public DecoratedBarcodeView barcodeView;
    public Context context;
    public String previousBarcode = "";
    public ArrayList<Item> arrayOfItems;
    public ItemAdapter itemAdapter;
    public ListView lvItems;
    public Button btnStartScan;
    public TextView textViewTotal;
    public static float totalAmount = 0;

    public BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (!result.getText().equals(previousBarcode)) {
                Log.v("callback", "previous=" + previousBarcode + " current=" + result.getText());
                new BarcodeScannerHTTPActivity(context, itemAdapter).execute(result.getText());
                previousBarcode = result.getText();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner_main);
        context = this;
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcodeScanner);
        arrayOfItems = new ArrayList<>();
        itemAdapter = new ItemAdapter(context, 0, arrayOfItems);
        lvItems = (ListView) ((Activity) context).findViewById(R.id.lvItems);
        btnStartScan = (Button) findViewById(R.id.buttonScan);
        lvItems.setAdapter(itemAdapter);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        textViewTotal.setText(Float.toString(totalAmount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    public static float addItemAmount(float amount){
        totalAmount += amount;
        return totalAmount;
    }

    public static float removeItemAmount(float amount){
        totalAmount -= amount;
        return totalAmount;
    }

    public void triggerScan(View view) {
        assert barcodeView != null;
        barcodeView.setVisibility(View.VISIBLE);
        btnStartScan.setVisibility(View.GONE);
        textViewTotal.setVisibility(View.VISIBLE);
        barcodeView.decodeContinuous(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
