package com.marketingservice.gomni.furnituremarketingservice.modal;

import java.sql.Blob;
import java.util.Locale;

public class Product {

    public String id;
    public String productname;
    public String description;
    public String category;
    public String price;
    public byte[] photo;

    public Product(String id, String productame, String descrition, String category, String price,byte[] photo) {
        this.id = id;
        this.productname = productame;
        this.description = descrition;
        this.category = category;
        this.price=price;
        this.photo=photo;
    }
}
