package com.ek.erickivet.stockdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by erickivet on 9/12/16.
 */
public class StockHelper extends SQLiteOpenHelper {
    private static final String TAG = StockHelper.class.getCanonicalName();

    private static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "stockDB.db";
    public static final String STOCK_LIST_TABLE_NAME = StockContract.Stock.STOCK_LIST_TABLE_NAME;

    public static final String COL_ID = "_id";
    public static final String COL_STOCK_NAME = StockContract.Stock.COL_STOCK_NAME;
    public static final String COL_STOCK_SYMBOL = StockContract.Stock.COL_STOCK_SYMBOL;
    public static final String COL_STOCK_EXCHANGE = StockContract.Stock.COL_STOCK_EXCHANGE;
    public static final String COL_STOCK_PRICE = StockContract.Stock.COL_STOCK_PRICE;
    public static final String COL_STOCK_CHANGE = StockContract.Stock.COL_STOCK_CHANGE;
    public static final String COL_STOCK_PERCENT = StockContract.Stock.COL_STOCK_PERCENT;
    public static final String COL_STOCK_TIMESTAMP = StockContract.Stock.COL_STOCK_TIMESTAMP;
    public static final String COL_STOCK_QUANTITY = StockContract.Stock.COL_STOCK_QUANTITY;

    public static final String [] STOCK_COLUMNS = {COL_ID,COL_STOCK_NAME,COL_STOCK_SYMBOL, COL_STOCK_EXCHANGE,
            COL_STOCK_PRICE,COL_STOCK_CHANGE,COL_STOCK_PERCENT,COL_STOCK_TIMESTAMP,COL_STOCK_QUANTITY};

    public static final String CREATE_STOCK_TABLE =
            "CREATE TABLE " + STOCK_LIST_TABLE_NAME + "(" + COL_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_STOCK_NAME + " TEXT," +
                    COL_STOCK_SYMBOL + " TEXT," +
                    COL_STOCK_EXCHANGE + " TEXT," +
                    COL_STOCK_PRICE + " DOUBLE," +
                    COL_STOCK_CHANGE + " DOUBLE," +
                    COL_STOCK_PERCENT + " DOUBLE," +
                    COL_STOCK_TIMESTAMP + " TEXT," +
                    COL_STOCK_QUANTITY + " DOUBLE" + ")";

    public StockHelper (Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    private static StockHelper instance;

    public static synchronized StockHelper getInstance(Context context){
        if(instance == null)
            instance = new StockHelper(context.getApplicationContext());
        return instance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Stock Table onCreate");
        db.execSQL(CREATE_STOCK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STOCK_LIST_TABLE_NAME);
        onCreate(db);
    }

    public long addStock(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        long insertedRow = db.insert(STOCK_LIST_TABLE_NAME,null,values);
        db.close();
        return insertedRow;
    }

    public Cursor getStockSymbol(String selection){
        String [] projection = {COL_STOCK_SYMBOL};

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(STOCK_LIST_TABLE_NAME,projection,selection,null,null,null,null);
        return cursor;
    }

    public Cursor getStock(String selection){
        String[] projection = STOCK_COLUMNS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(STOCK_LIST_TABLE_NAME,projection,selection,null,null,null,null);

        return cursor;
    }

    public int deleteStock(String id){
        SQLiteDatabase db = getWritableDatabase();

        int rowsDeleted = db.delete(STOCK_LIST_TABLE_NAME,COL_ID+"=?",new String[]{id});
        db.close();
        return rowsDeleted;
    }
}
