package com.example.andreyu.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
    public ImageButton btnStartScan;
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
        registerForContextMenu(lvItems);
        btnStartScan = (ImageButton) findViewById(R.id.buttonScan);
        lvItems.setAdapter(itemAdapter);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvItems) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Object object = lvItems.getItemAtPosition(info.position);
            Item item = (Item) object;
            menu.setHeaderTitle(item.itemDescription);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.item_menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        Object object = lvItems.getItemAtPosition(info.position);
        Item item = (Item) object;
        switch (menuItem.getItemId()) {
            case R.id.remove_item:
                itemAdapter.remove(item);
                totalAmount = removeItemAmount(item.itemPrice);
                break;

            case R.id.increase_item_quantity:
                totalAmount = addItemAmount(item.itemPrice);
                item.itemQuantity = item.itemQuantity + 1;
                break;

            case R.id.decrease_item_quantity:
                totalAmount = removeItemAmount(item.itemPrice);
                if (1 >= item.itemQuantity) {
                    itemAdapter.remove(item);
                } else {
                    item.itemQuantity = item.itemQuantity - 1;
                }
                break;
        }

        itemAdapter.notifyDataSetChanged();
        textViewTotal.setText(String.format("%.0f", totalAmount));
        return super.onContextItemSelected(menuItem);
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

    public static float addItemAmount(float amount) {
        totalAmount += amount;
        return totalAmount;
    }

    public static float removeItemAmount(float amount) {
        totalAmount -= amount;
        return totalAmount;
    }

    public void triggerScan(View view) {
        assert barcodeView != null;
        barcodeView.setVisibility(View.VISIBLE);
        btnStartScan.setVisibility(View.GONE);
        barcodeView.decodeContinuous(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
