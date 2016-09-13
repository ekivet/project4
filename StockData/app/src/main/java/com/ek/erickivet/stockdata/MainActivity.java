package com.ek.erickivet.stockdata;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final Uri CONTENT_URI = StockContract.Stock.CONTENT_URI;
    ListView listview;
    CursorAdapter mCursorAdapter;
    TextView portfolioValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Portfolio");

        listview = (ListView) findViewById(R.id.stock_list);

        Cursor cursor = getContentResolver().query(CONTENT_URI,null,null,null,null);
        mCursorAdapter = new CursorAdapter(this, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2,
                        parent,false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                portfolioValue = (TextView) findViewById(R.id.portfolio_value);

                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String symbol = cursor.getString(cursor.getColumnIndex("Symbol"));
                double quant = cursor.getDouble(cursor.getColumnIndex("Quantity"));
                double price = cursor.getDouble(cursor.getColumnIndex("LastPrice"));
                double value = cursor.getDouble(cursor.getColumnIndex("Value"));
                //double value = (quant * price);



                //double sum = value.

                //text1.setText(name + " - " + symbol);
                //text2.setText(quant + " - $" + price);

                text1.setText(name + " - " + "(" + symbol + ")");
                text2.setText(String.valueOf(quant) + " Shares @ " + "$" + String.valueOf(price) +
                "   Value: $" + String.valueOf(value));

                portfolioValue.setText(String.valueOf(StockHelper.getInstance(context).getValueSum()));


            }
        };

        //listview.setAdapter(mCursorAdapter);
        //getContentResolver().registerContentObserver(CONTENT_URI,true,new Observer(new Handler()));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("COL_ID",id);
                startActivity(intent);

            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Stock");
                builder.setMessage("Are You Sure You Want to Delete Stock?");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //getContentResolver().delete(ContentUris.withAppendedId(CONTENT_URI,id),null,null);
                        StockHelper.getInstance(MainActivity.this).deleteStock(String.valueOf(id));
                        //getLoaderManager().restartLoader(id,);
                        //mCursorAdapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        listview.setAdapter(mCursorAdapter);
        getContentResolver().registerContentObserver(CONTENT_URI,true,new Observer(new Handler()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });
    }

    public void retrieveStock(final String symbol, final String quant){

        RequestQueue queue = Volley.newRequestQueue(this);
        String stockUrl = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol="+symbol;


        JsonObjectRequest stockJsonRequest = new JsonObjectRequest
                (Request.Method.GET, stockUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            if (response.has("Status") && response.getString("Status")
                                    .equals("SUCCESS")) {

                                //ContentResolver contentResolver = getContentResolver();
                                String name = response.getString("Name");
                                String price = response.getString("LastPrice");
                                String change = response.getString("Change");
                                String changePercent = response.getString("ChangePercent");
                                String timeStamp = response.getString("Timestamp");
                                /*
                                ContentValues values = new ContentValues();

                                values.put("Symbol", symbol);
                                values.put("Quantity", quant);
                                values.put("Name", name);
                                values.put("Price",price);
                                values.put("Change",change);
                                values.put("ChangePercent",changePercent);
                                values.put("Timestamp",timeStamp);

                                contentResolver.insert(CONTENT_URI, values);*/


                                retrieveExchange(symbol,quant, name, price, change, changePercent,
                                        timeStamp);
                            } else
                                Toast.makeText(MainActivity.this, "That Symbol Is Not In Our Database",
                                        Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
                );
        queue.add(stockJsonRequest);
    }

    public void retrieveExchange(final String symbol, final String quant, final String name,
                                 final String price, final String change, final String changePercent,
                                 final String timeStamp){

        RequestQueue queue = Volley.newRequestQueue(this);
        String exchangeUrl = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input="+symbol;
        Log.d(TAG, "Starting exchange request: "+exchangeUrl);
        JsonArrayRequest exchangeJsonRequest = new JsonArrayRequest(exchangeUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "Response 2: " + response.toString());
                try {
                    ContentResolver contentResolver = getContentResolver();
                    String exchange = ((JSONObject)response.get(0)).getString("Exchange");
                    ContentValues values = new ContentValues();
                    values.put("Symbol",symbol);
                    values.put("Quantity",quant);
                    values.put("Name",name);
                    values.put("LastPrice",price);
                    values.put("Change",change);
                    values.put("ChangePercent",changePercent);
                    values.put("Timestamp",timeStamp);
                    //adding line
                    values.put("Value",Double.parseDouble(quant)*Double.parseDouble(price));
                    values.put("Exchange",exchange);

                    contentResolver.insert(CONTENT_URI,values);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        queue.add(exchangeJsonRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.add_stock,null))
                .setPositiveButton("Add", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean isValid = true;
                        EditText symbolText = (EditText)((AlertDialog)dialog).findViewById(R.id.symbol_id);
                        EditText quantText = (EditText)((AlertDialog)dialog).findViewById(R.id.quant);
                        if(symbolText.getText().toString().length() == 0 || quantText.getText().toString().length() == 0){
                            Toast.makeText(MainActivity.this,"You must complete all fields", Toast.LENGTH_LONG).show();
                            isValid = false;
                        }else {
                            symbolText.setError("");
                        }

                        if(isValid){
                            retrieveStock(symbolText.getText().toString().toUpperCase(),
                                    quantText.getText().toString());
                        }
                    }
                }).setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class Observer extends ContentObserver {
        public Observer(Handler handler){
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            new AsyncTask<Void, Void, Cursor>(){
                @Override
                protected Cursor doInBackground(Void... params) {
                    return getContentResolver().query(CONTENT_URI,null,null,null,null);
                }

                @Override
                protected void onPostExecute(Cursor cursor) {
                    super.onPostExecute(cursor);
                    mCursorAdapter.swapCursor(cursor);
                }
            }.execute();
        }
    }
}
