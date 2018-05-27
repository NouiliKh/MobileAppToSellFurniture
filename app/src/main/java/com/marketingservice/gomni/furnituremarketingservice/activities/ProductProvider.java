package com.marketingservice.gomni.furnituremarketingservice.activities;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.marketingservice.gomni.furnituremarketingservice.sql.SqliteHelper;

public class ProductProvider extends ContentProvider {


    private static final String PROVIDER_NAME = "com.marketingservice.gomni.furnituremarketingservice.activities.ProductProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/products");
    private static final int PRODUCTS = 1;
    private static final int PRODUCT_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "product", PRODUCTS);
        uriMatcher.addURI(PROVIDER_NAME, "products/#", PRODUCT_ID);
        return uriMatcher;
    }

    private SqliteHelper db;
    @Override
    public boolean onCreate() {
        db = new SqliteHelper(getContext());
        return db.getWritableDatabase() != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                cursor = db.getAllItemsCursor();
                break;
//            case PRODUCT_ID:
//                cursor = db.getItemCursorById(Integer.parseInt(uri.getLastPathSegment()));
//                break;
            default:
                cursor = db.getAllItemsCursor();
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}