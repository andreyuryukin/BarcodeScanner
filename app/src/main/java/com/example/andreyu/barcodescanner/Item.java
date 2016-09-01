package com.example.andreyu.barcodescanner;

public class Item {
    public String itemBarcode;
    public String itemDescription;
    public float itemPrice;
    public float itemQuantity;

    public Item(String itemBarcode, String itemDescription, float itemPrice, float itemQuantity){
        this.itemBarcode = itemBarcode;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }
}
