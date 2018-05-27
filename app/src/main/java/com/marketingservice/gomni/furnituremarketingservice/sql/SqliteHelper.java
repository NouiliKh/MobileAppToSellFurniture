package com.marketingservice.gomni.furnituremarketingservice.sql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Debug;
import android.util.Log;

import com.marketingservice.gomni.furnituremarketingservice.modal.Product;
import com.marketingservice.gomni.furnituremarketingservice.modal.User;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.os.Build.ID;

public class SqliteHelper extends SQLiteOpenHelper{
    //DATABASE NAME
    public static final String DATABASE_NAME = "MarketingService";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //TABLE NAME
    public static final String TABLE_USERS = "users";

    //TABLE NAME
    public static final String TABLE_PRODUCTS = "products";

    //TABLE USERS COLUMNS
    //ID COLUMN @primaryKey
    public static final String KEY_ID = "id";

    //COLUMN user name
    public static final String KEY_USER_NAME = "username";

    //COLUMN email
    public static final String KEY_EMAIL = "email";

    //COLUMN password
    public static final String KEY_PASSWORD = "password";

    //SQL for creating users table
    public static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_USER_NAME + " TEXT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT"
            + " ) ";

    private static final String HISTORY_TABLE = "HISTORY";

    private static final String LAST_READ = "LAST_READ";
    private static final String LAST_UPDATE = "LAST_UPDATE";
    private static final String LAST_DELETE = "LAST_DELETE";

    private static final String CREATE_HISTORY_SCHEMA = "CREATE TABLE HISTORY (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, LAST_READ TEXT, LAST_UPDATE TEXT, LAST_DELETE TEXT)";

    private static final String INIT_HISTORY = "INSERT INTO " + HISTORY_TABLE + "(" +
            LAST_READ + "," +
            LAST_UPDATE + "," +
            LAST_DELETE + ") VALUES ('','','')";


    public static final String KEYP_ID = "id";

    //COLUMN product name
    public static final String KEY_PRODUCT_NAME = "productname";

    //COLUMN description of product
    public static final String KEY_DESCRIPTION = "description";

    //COLUMN category
    public static final String KEY_CATEGORY = "category";

    //COLUMN category
    public static final String KEY_PRICE = "price";

    //COLUMN category
    public static final String KEY_PHOTO = "photo";

    //SQL for creating users table
    public static final String SQL_TABLE_PRODUCTS = " CREATE TABLE " + TABLE_PRODUCTS
            + " ( "
            + KEYP_ID + " INTEGER PRIMARY KEY, "
            + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_CATEGORY + " TEXT, "
            + KEY_PRICE + " TEXT, "
            + KEY_PHOTO + " BLOB"
            + " ) ";





    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create Table when oncreate gets called
        sqLiteDatabase.execSQL(SQL_TABLE_USERS);
        sqLiteDatabase.execSQL(SQL_TABLE_PRODUCTS);
        sqLiteDatabase.execSQL(CREATE_HISTORY_SCHEMA);
        sqLiteDatabase.execSQL(INIT_HISTORY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table to create new one if database version updated
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + HISTORY_TABLE);
    }

    //using this method we can add users to user table
    public void addUser(User user) {
        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put username in  @values
        values.put(KEY_USER_NAME, user.userName);

        //Put email in  @values
        values.put(KEY_EMAIL, user.email);

        //Put password in  @values
        values.put(KEY_PASSWORD, user.password);

        // insert row
        long todo_id = db.insert(TABLE_USERS, null, values);
    }

    //using this method we can add users to user table
    public void addProduct(Product product) {
        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        values.put(KEY_PRODUCT_NAME, product.productname);
        values.put(KEY_DESCRIPTION, product.description);
        values.put(KEY_CATEGORY, product.category);
        values.put(KEY_PRICE, product.price);
        values.put(KEY_PHOTO, product.photo);

        // insert row
        long todo_id = db.insert(TABLE_PRODUCTS, null, values);
        updateHistory(db,LAST_UPDATE);
    }

    public List<Product> listProducts(){


        String sql = "select * from " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Product> storeProducts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(0);

                String productname = cursor.getString(1);
                String Description = cursor.getString(2);
                String Category = cursor.getString(3);
                String Price = cursor.getString(4);
                byte[] Photo = cursor.getBlob(5);
                storeProducts.add(new Product(id,productname, Description,Category,Price,Photo));
            }while (cursor.moveToNext());
            updateHistory(db,LAST_READ);
        }
        cursor.close();
        return storeProducts;
    }



    public void updateProduct(Product product){
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, product.productname);
        values.put(KEY_DESCRIPTION, product.description);
        values.put(KEY_CATEGORY, product.category);
        values.put(KEY_PRICE, product.price);
        values.put(KEY_PHOTO, product.photo);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PRODUCTS, values, KEY_ID    + "    = ?", new String[] { String.valueOf(product.id)});
        updateHistory(db,LAST_UPDATE);

    }

//    public Product findProduct(String name){
//        String query = "Select * FROM "    + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = " + "name";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Product mProduct = null;
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()){
//            int id = Integer.parseInt(cursor.getString(0));
//            String productName = cursor.getString(1);
//            int productQuantity = Integer.parseInt(cursor.getString(2));
//            mProduct = new Product(id, productName, productQuantity);
//        }
//        cursor.close();
//        return mProduct;
//    }

    public void deleteProduct(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID    + "    = ?", new String[] { String.valueOf(id)});
        updateHistory(db,LAST_DELETE);
    }



    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{user.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //Match both passwords check they are same or not
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }

        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }

    private void updateHistory(SQLiteDatabase db, String Column) {
        final String currentTime = new SimpleDateFormat("dd-MM-yy:HH:mm", Locale.getDefault()).format(new Date());
        ContentValues historyContentValues = new ContentValues();
        historyContentValues.put(Column, currentTime);
        db.update(HISTORY_TABLE, historyContentValues, "ID=?", new String[]{"1"});
    }

    public HashMap<String, String> getDBHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, String> history = new HashMap<>();
        history.put(LAST_READ, "NA");
        history.put(LAST_UPDATE, "NA");
        history.put(LAST_DELETE, "NA");

        Cursor cursor = db.query(HISTORY_TABLE, new String[]{LAST_READ, LAST_UPDATE, LAST_DELETE}, "ID=?", new String[]{"1"}, null, null, null);
        if (cursor.moveToFirst()) {
            history.put(LAST_READ, cursor.getString(0));
            history.put(LAST_UPDATE, cursor.getString(1));
            history.put(LAST_DELETE, cursor.getString(2));
            cursor.close();
        }
        return history;
    }


    //Content Provider

    public Cursor getAllItemsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRODUCTS, new String[]{KEYP_ID, KEY_PRODUCT_NAME, KEY_DESCRIPTION, KEY_CATEGORY, KEY_PRICE,KEY_PHOTO}, null, null, null, null, null);
    }

}
