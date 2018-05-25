package com.marketingservice.gomni.furnituremarketingservice.activities;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.marketingservice.gomni.furnituremarketingservice.R;
import com.weiwangcn.betterspinner.library.BetterSpinner;


public class AddProduct extends AppCompatActivity {


    TextInputLayout textInputLayoutProductName;
    TextInputLayout textInputLayoutDescription;
    TextInputLayout textInputLayoutCategory;
    TextInputLayout textInputLayoutPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
    }

//    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//            android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//    BetterSpinner textView = (BetterSpinner)
//            findViewById(R.id.countries_list);
//         textView.setAdapter(adapter);
//    }

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
}
