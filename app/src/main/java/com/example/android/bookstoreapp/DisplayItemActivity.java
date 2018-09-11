package com.example.android.bookstoreapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;

import java.util.Locale;


public class DisplayItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri mUri;
    TextView mItemName;
    TextView mItemPrice;
    TextView mItemQuantity;
    TextView mItemSupplierName;
    TextView mItemSupplierNumber;
    String mSupplierNumber;
    private CategoryCursorAdapter mCursorAdapter;

    private static final int PERMISSION_REQUEST_PHONE_CALL = 0;
    private static final int LOADER_ID = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_item_activity);

        Intent intent = getIntent();
        mUri = intent.getData();
        if(mUri==null) errorReadingItem();

        mItemName = findViewById(R.id.itemName);
        mItemPrice = findViewById(R.id.itemPrice);
        mItemQuantity = findViewById(R.id.itemQuantity);
        mItemSupplierName = findViewById(R.id.itemSupplierName);
        mItemSupplierNumber = findViewById(R.id.itemSupplierNumber);
        mCursorAdapter     = new CategoryCursorAdapter(this, null);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
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
                        mUri,
                        projection,
                        null,
                        null,
                        null
                );
            default:
                // invalid id
                errorReadingItem();
                return null;
        }
    }

    private void errorReadingItem(){
        Toast.makeText(DisplayItemActivity.this, getString(R.string.itemReadError), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // in case the cursor has no data
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if(cursor.moveToFirst()){
            int nameIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
            int priceIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplierNameIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            String name = cursor.getString(nameIndex);
            double price = cursor.getDouble(priceIndex);
            int quantity = cursor.getInt(quantityIndex);
            String supplierName = cursor.getString(supplierNameIndex);
            mSupplierNumber = cursor.getString(supplierPhoneIndex);


            mItemName.setText(name);
            mItemPrice.setText( String.format(Locale.US,"$%.2f", price));
            mItemQuantity.setText(Integer.toString(quantity));
            mItemSupplierName.setText(supplierName);
            mItemSupplierNumber.setText(mSupplierNumber);

//          NEEDED TO ADD to manifest <uses-permission android:name="android.permission.CALL_PHONE" />
            mItemSupplierNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // must check for (version and) permission to access phone app before starting intent
                    if (ContextCompat.checkSelfPermission(DisplayItemActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // request permission if not already granted
                        ActivityCompat.requestPermissions(DisplayItemActivity.this, new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_PHONE_CALL);
                    }
                    else
                    {
                        makePhoneCall();
                    }

                }
            });
        }
        mCursorAdapter.changeCursor(cursor);
    }

    private void makePhoneCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(String.format("tel:%s", mSupplierNumber)));
        PackageManager packageManager = getApplicationContext().getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            Toast.makeText(DisplayItemActivity.this, "Calling Supplier", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } else {
            Toast.makeText(DisplayItemActivity.this, "No phone app available to make call", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mItemName.setText("");
        mItemPrice.setText("");
        mItemQuantity.setText("");
        mItemSupplierName.setText("");
        mItemSupplierNumber.setText("");
        mCursorAdapter.swapCursor(null);
    }
//    TODO: add menu for delete and edit
//    TODO: Include Quantity changing logic
//    TODO: Add Quantity changing buttons
//    TODO: Add phone intent
    // IMPLEMENTING OPTIONS MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.save).setVisible(false); // hiding "save" menu item
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Edit" menu option
            case R.id.edit:
                // opens Editor Activity and finishes this one
                Intent intent = new Intent(DisplayItemActivity.this,EditorActivity.class);
                intent.setData(mUri);
                startActivity(intent);
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(DisplayItemActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void deleteItem() throws Exception{
        if(mUri != null){
            int rowsDeleted = getContentResolver().delete(mUri, null, null);
            if(rowsDeleted!=1) throw new Exception("Item could not be found");
            Toast.makeText(DisplayItemActivity.this,getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button
                try{
                    deleteItem();
                }
                catch (Exception err){
                    Toast.makeText(DisplayItemActivity.this, err.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_PHONE_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    makePhoneCall();
                } else {
                    // permission denied
                    Toast.makeText(DisplayItemActivity.this, "Cannot make call from app without user permission", Toast.LENGTH_LONG).show();               }
                return;
            }
        }
    }
}
