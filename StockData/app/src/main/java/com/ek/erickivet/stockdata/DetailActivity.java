package com.ek.erickivet.stockdata;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private long columnId;
    List list;
    ListView listView;
    private static final String TAG = "DetailActivity";
    public static final Uri CONTENT_URI = StockContract.Stock.CONTENT_URI;
    CursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Stock Info");

        listView = (ListView) findViewById(R.id.details);

        columnId = getIntent().getIntExtra("COL_ID",1);

        Cursor cursor = getContentResolver().query(CONTENT_URI,null,null,null,null);
        mCursorAdapter = new CursorAdapter(this,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return null;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

            }
        };








    }
}
