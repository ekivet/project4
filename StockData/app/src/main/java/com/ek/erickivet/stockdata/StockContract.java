package com.ek.erickivet.stockdata;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by erickivet on 9/12/16.
 */
public class StockContract {
    public static final String AUTHORITY = "com.ek.erickivet.stockdata.StockContentProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Stock implements BaseColumns{
        public static final String STOCK_LIST_TABLE_NAME = "stock";
        public static final String COL_STOCK_NAME = "Name";
        public static final String COL_STOCK_SYMBOL = "Symbol";
        public static final String COL_STOCK_EXCHANGE = "Exchange";
        public static final String COL_STOCK_PRICE = "LastPrice";
        public static final String COL_STOCK_CHANGE = "Change";
        public static final String COL_STOCK_PERCENT = "ChangePercent";
        public static final String COL_STOCK_TIMESTAMP = "Timestamp";
        public static final String COL_STOCK_QUANTITY = "Quantity";
        //addition
        public static final String COL_STOCK_VALUE = "Value";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "stock");

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.com.ek.erickivet.stock";

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/vnd.com.ek.erickivet.stock";
    }
}
