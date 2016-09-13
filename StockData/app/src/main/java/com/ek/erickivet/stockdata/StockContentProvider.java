package com.ek.erickivet.stockdata;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by erickivet on 9/12/16.
 */
public class StockContentProvider extends ContentProvider {

    private StockHelper myDB;
    private static final String AUTHORITY = StockContract.AUTHORITY;
    private static final String STOCK_LIST_TABLE_NAME = StockContract.Stock.STOCK_LIST_TABLE_NAME;
    public static final Uri CONTENT_URI = StockContract.Stock.CONTENT_URI;

    public static final int STOCK = 1;
    public static final int STOCK_ID = 2;
    public static final int STOCK_SYMBOL = 3;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, STOCK_LIST_TABLE_NAME, STOCK);
        sUriMatcher.addURI(AUTHORITY, STOCK_LIST_TABLE_NAME + "/#", STOCK_ID);
        sUriMatcher.addURI(AUTHORITY, STOCK_LIST_TABLE_NAME + "/#", STOCK_SYMBOL);
    }

    @Override
    public boolean onCreate() {
        myDB = StockHelper.getInstance(getContext());
        return false;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);

        long id = 0;
        switch (uriType){
            case STOCK:
                id = myDB.addStock(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown UTI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(CONTENT_URI,id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        int uriType = sUriMatcher.match(uri);
        Cursor cursor = null;

        switch (uriType){
            case STOCK_ID:
                break;
            case STOCK:
                cursor = myDB.getStock(selection);
                break;
            case STOCK_SYMBOL:
                cursor = myDB.getStockSymbol(selection);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case STOCK:
                return StockContract.Stock.CONTENT_TYPE;
            case STOCK_ID:
                return StockContract.Stock.CONTENT_TYPE;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType){
            case STOCK:
                break;
            case STOCK_ID:
                String id = uri.getLastPathSegment();
                rowsDeleted = myDB.deleteStock(id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

