package com.marketingservice.gomni.furnituremarketingservice.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.marketingservice.gomni.furnituremarketingservice.R;
import com.marketingservice.gomni.furnituremarketingservice.modal.Product;
import com.marketingservice.gomni.furnituremarketingservice.sql.SqliteHelper;

import java.io.ByteArrayOutputStream;


public class EditActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutProductName;
    TextInputLayout textInputLayoutDescription;
    Spinner textInputLayoutCategory;
    TextInputLayout textInputLayoutPrice;

    ImageButton LoadImage;
    ImageView imageViewLoad;
    Intent intent;
    String ImageDecode;

    Button buttonAddProduct;

    SqliteHelper sqliteHelper;


    EditText editTextProductName;
    EditText editTextDescription;
    EditText editTextCategory;
    EditText editTextPrice;


    private static int IMG_RESULT = 1;
    private static final String TAG = "AddProductActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        sqliteHelper = new SqliteHelper(this);
        initViews();

        buttonAddProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String ProductName = editTextProductName.getText().toString();
                String Description = editTextDescription.getText().toString();
                String Category = textInputLayoutCategory.getSelectedItem().toString();
                String Price = editTextPrice.getText().toString();

                Bitmap bitmap = ((BitmapDrawable) imageViewLoad.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();
                String s = getIntent().getStringExtra("id");

                sqliteHelper.updateProduct(new Product(s ,ProductName,Description,Category,Price,imageInByte));

                Intent intent = new Intent(EditActivity.this, ConsultingActivity.class);
                startActivity(intent);

            }

        });

        LoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStoragePermissionGranted();
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMG_RESULT);

            }
        });


    }


    private void initViews()
    {
        editTextProductName = (EditText) findViewById(R.id.editTextProductName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);

        buttonAddProduct = (Button) findViewById(R.id.buttonEditProduct);
        textInputLayoutProductName=(TextInputLayout) findViewById(R.id.textInputLayoutProductName);
        textInputLayoutDescription=(TextInputLayout) findViewById(R.id.textInputLayoutDescription);

        textInputLayoutCategory=(Spinner) findViewById(R.id.spinner1);
        textInputLayoutPrice=(TextInputLayout) findViewById(R.id.textInputLayoutPrice);
        imageViewLoad = (ImageView) findViewById(R.id.imageView1);
        LoadImage = (ImageButton) findViewById(R.id.button1);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK && null != data) {

                Uri URI = data.getData();
                String[] FILE = { MediaStore.Images.Media.DATA };


                Cursor cursor = getContentResolver().query(URI, FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();

                imageViewLoad.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));

            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show();
        }

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

}

