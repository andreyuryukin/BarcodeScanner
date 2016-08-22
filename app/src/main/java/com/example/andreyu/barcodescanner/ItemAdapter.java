package com.example.andreyu.barcodescanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item>{
    public ItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_barcode_scanner, parent, false);
        }
        // Lookup view for data population
        TextView tvItemBarcode = (TextView) convertView.findViewById(R.id.tvItemBarcode);
        TextView tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
        TextView tvItemPrice = (TextView) convertView.findViewById(R.id.tvItemPrice);
        TextView tvItemQuantity = (TextView) convertView.findViewById(R.id.tvItemQuantity);
        ImageButton imgBtnAdd = (ImageButton) convertView.findViewById(R.id.imageButtonAdd);
        // Populate the data into the template view using the data object
        tvItemBarcode.setText(item.itemBarcode);
        tvItemName.setText(item.itemDescription);
        tvItemPrice.setText(Float.toString(item.itemPrice));
        tvItemQuantity.setText("1");
        imgBtnAdd.setImageResource(R.drawable.add);
        // Return the completed view to render on screen
        return convertView;
    }
}
