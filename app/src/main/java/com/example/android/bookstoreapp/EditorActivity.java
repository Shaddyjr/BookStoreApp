package com.example.android.bookstoreapp;



import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mEditName;
    private EditText mEditPrice;
    private EditText mEditQuantity;
    private EditText mEditSupplierName;
    private EditText mEditSupplierNumber;
    private Uri mUri;
    private static final int LOADER_ID = 0;

    private CategoryCursorAdapter mCursorAdapter;
    /**
     * Boolean flag that keeps track of whether the pet has been edited (true) or not (false)
     */
    private boolean mItemHasChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        mCursorAdapter = new CategoryCursorAdapter(this,null);
//        Find all relevant views
        mEditName             = findViewById(R.id.editName);
        mEditPrice            = findViewById(R.id.editPrice);
        mEditQuantity         = findViewById(R.id.editQuantity);
        mEditSupplierName     = findViewById(R.id.editSupplierName);
        mEditSupplierNumber   = findViewById(R.id.editSupplierNumber);

        mEditName.setOnTouchListener(mTouchListener);
        mEditPrice.setOnTouchListener(mTouchListener);
        mEditQuantity.setOnTouchListener(mTouchListener);
        mEditSupplierName.setOnTouchListener(mTouchListener);
        mEditSupplierNumber.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();
        mUri = intent.getData();
        // depending if the data exists, then will set title accordingly
        if(mUri== null){
            setTitle(getString(R.string.editorTitleNewItem));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
//            invalidateOptionsMenu();
        }else{
            setTitle(getString(R.string.editorTitleEditItem));
            // kicking off loader
            getLoaderManager().initLoader(LOADER_ID,null,this);
        }
    }

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch(id){
            case LOADER_ID:
                String[] projection = {
                        InventoryEntry._ID,
                        InventoryEntry.COLUMN_NAME,
                        InventoryEntry.COLUMN_PRICE,
                        InventoryEntry.COLUMN_QUANTITY,
                        InventoryEntry.COLUMN_SUPPLIER_NAME,
                        InventoryEntry.COLUMN_SUPPLIER_PHONE
                };
                return new CursorLoader(
                        getApplicationContext(),
                        InventoryEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );
            default:
                // invalid id
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

//
//    private void insertData(String name, int price, int quantity, String supplier_name, String supplier_phone) {
//        ContentValues values = new ContentValues();
//        values.put(InventoryEntry.COLUMN_NAME, name);
//        values.put(InventoryEntry.COLUMN_PRICE, price);
//        values.put(InventoryEntry.COLUMN_QUANTITY, quantity);
//        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplier_name);
//        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplier_phone);
//
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        long id = db.insert(InventoryEntry.TABLE_NAME, null, values);
//        if (id > 0) {
//            Toast.makeText(MAIN_CONTEXT.getApplicationContext(), String.format(getString(R.string.itemAdded), name ), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MAIN_CONTEXT.getApplicationContext(), String.format(getString(R.string.itemError),name), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void viewDb() {
//        Cursor cursor = queryData();
//        try {
//            mTextView.setText(String.format(getString(R.string.itemCount),cursor.getCount()));
//            mTextView.append(String.format(getString(R.string.rowContentHeader),
//                    InventoryEntry._ID,
//                    InventoryEntry.COLUMN_NAME,
//                    InventoryEntry.COLUMN_PRICE,
//                    InventoryEntry.COLUMN_QUANTITY,
//                    InventoryEntry.COLUMN_SUPPLIER_NAME,
//                    InventoryEntry.COLUMN_SUPPLIER_PHONE
//            ));
//            int idColIndex = cursor.getColumnIndex(InventoryEntry._ID);
//            int nameColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
//            int priceColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
//            int quantityColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
//            int supplier_nameColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
//            int supplier_phoneColIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);
//
//            while (cursor.moveToNext()) {
//                mTextView.append(String.format(getString(R.string.rowContentValues),
//                        cursor.getInt(idColIndex),
//                        cursor.getString(nameColIndex),
//                        cursor.getInt(priceColIndex),
//                        cursor.getInt(quantityColIndex),
//                        cursor.getString(supplier_nameColIndex),
//                        cursor.getString(supplier_phoneColIndex)
//                ));
//            }
//        } finally {
//            cursor.close();
//        }
//    }
//
//    public void addFakeData(View v) {
//        insertData("Bob's wrench", 500, 34, "Bob's", "123456789");
//        viewDb();
//    }
//
//    private Cursor queryData() {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        String[] projection = {
//                InventoryEntry._ID,
//                InventoryEntry.COLUMN_NAME,
//                InventoryEntry.COLUMN_PRICE,
//                InventoryEntry.COLUMN_QUANTITY,
//                InventoryEntry.COLUMN_SUPPLIER_NAME,
//                InventoryEntry.COLUMN_SUPPLIER_PHONE
//        };
//        String selection = InventoryEntry.COLUMN_QUANTITY + ">?";
//        String[] selectionArgs = {"0"};
//
//        return db.query(
//                InventoryEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//    }
}
