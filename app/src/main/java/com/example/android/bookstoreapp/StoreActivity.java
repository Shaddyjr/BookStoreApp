package com.example.android.bookstoreapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;
import com.example.android.bookstoreapp.data.InventoryDbHelper;

public class StoreActivity extends AppCompatActivity {

    InventoryDbHelper mDbHelper;
    TextView mTextView;
    ListView mListView;
    CategoryCursorFragmentPagerAdapter mAdapter;
    // TODO: Add cursor adapter
    // TODO: Add loader manager

    static public Context MAIN_CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_store_activity);

        mDbHelper = new InventoryDbHelper(this);
        MAIN_CONTEXT = this;

        mListView = findViewById(R.id.)
        mAdapter = new CategoryCursorFragmentPagerAdapter(this,null);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryCursorFragmentPagerAdapter adapter = new CategoryCursorFragmentPagerAdapter(this,getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void insertData(String name, int price, int quantity, String supplier_name, String supplier_phone) {
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_NAME, name);
        values.put(InventoryEntry.COLUMN_PRICE, price);
        values.put(InventoryEntry.COLUMN_QUANTITY, quantity);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplier_name);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplier_phone);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(InventoryEntry.TABLE_NAME, null, values);
        if (id > 0) {
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.itemAdded), name ), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.itemError),name), Toast.LENGTH_SHORT).show();
        }
    }

    private void viewDb() {
        Cursor cursor = queryData();
        try {
            mTextView.setText(String.format(getString(R.string.itemCount),cursor.getCount()));
            mTextView.append(String.format(getString(R.string.rowContentHeader),
                    InventoryEntry._ID,
                    InventoryEntry.COLUMN_NAME,
                    InventoryEntry.COLUMN_PRICE,
                    InventoryEntry.COLUMN_QUANTITY,
                    InventoryEntry.COLUMN_SUPPLIER_NAME,
                    InventoryEntry.COLUMN_SUPPLIER_PHONE
            ));
            int idColIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
            int priceColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplier_nameColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplier_phoneColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            while (cursor.moveToNext()) {
                mTextView.append(String.format(getString(R.string.rowContentValues),
                        cursor.getInt(idColIndex),
                        cursor.getString(nameColIndex),
                        cursor.getInt(priceColIndex),
                        cursor.getInt(quantityColIndex),
                        cursor.getString(supplier_nameColIndex),
                        cursor.getString(supplier_phoneColIndex)
                ));
            }
        } finally {
            cursor.close();
        }
    }

    public void addFakeData(View v) {
        insertData("Bob's wrench", 500, 34, "Bob's", "123456789");
        viewDb();
    }

    private Cursor queryData() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE
        };
        String selection = InventoryEntry.COLUMN_QUANTITY + ">?";
        String[] selectionArgs = {"0"};

        return db.query(
                InventoryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

}
