package com.example.android.bookstoreapp;


import android.content.DialogInterface;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;

import java.util.Locale;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mName;
    private EditText mPrice;
    private EditText mQuantity;
    private EditText mSupplierName;
    private EditText mSupplierNumber;
    private Uri mUri;
    private static final int LOADER_ID = 0;

    private CategoryCursorAdapter mCursorAdapter;
    /**
     * Boolean flag that keeps track of whether the item has been edited (true) or not (false)
     */
    private boolean mItemHasChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        mCursorAdapter = new CategoryCursorAdapter(this, null);

        mName = findViewById(R.id.editName);
        mPrice = findViewById(R.id.editPrice);
        mQuantity = findViewById(R.id.editQuantity);
        mSupplierName = findViewById(R.id.editSupplierName);
        mSupplierNumber = findViewById(R.id.editSupplierNumber);

        mName.setOnTouchListener(mTouchListener);
        mPrice.setOnTouchListener(mTouchListener);
        mQuantity.setOnTouchListener(mTouchListener);
        mSupplierName.setOnTouchListener(mTouchListener);
        mSupplierNumber.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();
        mUri = intent.getData();
        // depending if the data exists, then will set title accordingly
        if (mUri == null) {
            setTitle(getString(R.string.editorTitleNewItem));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete something that hasn't been created yet)
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editorTitleEditItem));
            // kicking off loader
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the boolean flag variable to true.
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
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // in case the cursor has no data
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if(cursor.moveToFirst()){
            int nameIndex          = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
            int priceIndex         = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityIndex      = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplierNameIndex  = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            String name            = cursor.getString(nameIndex);
            double price           = cursor.getDouble(priceIndex);
            int quantity           = cursor.getInt(quantityIndex);
            String supplierName    = cursor.getString(supplierNameIndex);
            String supplierPhone   = cursor.getString(supplierPhoneIndex);


            mName.setText(name);
            mPrice.setText(String.format(Locale.US, "%f", price));
            mQuantity.setText(String.format(Locale.US, "%d", quantity));
            mSupplierName.setText(supplierName);
            mSupplierNumber.setText(supplierPhone);
        }
        mCursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName.setText("");
        mPrice.setText("");
        mQuantity.setText("");
        mSupplierName.setText("");
        mSupplierNumber.setText("");
        mCursorAdapter.swapCursor(null);
    }

    /**
     * Returns the user data as a ContentValue object.
     */
    private ContentValues getValues() {
        String name           = mName.getText().toString().trim();
        String price          = mPrice.getText().toString().trim();
        String quantity       = mQuantity.getText().toString().trim();
        String supplierName   = mSupplierName.getText().toString().trim();
        String supplierNumber = mSupplierNumber.getText().toString().trim();

//      DATA VALIDATION
        if (TextUtils.isEmpty(name)){
            toast(getString(R.string.noName));
            return null;
        }

        if (TextUtils.isEmpty(price) && TextUtils.isEmpty(quantity) && TextUtils.isEmpty(supplierName) && TextUtils.isEmpty(supplierNumber)){
            toast(getString(R.string.missingAll));
            return null;
        }


        if (TextUtils.isEmpty(price)) price = getString(R.string.zero);
        if (TextUtils.isEmpty(quantity)) quantity = getString(R.string.zero);

//      CONSTRUCTING VALUES
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_NAME, name);
        values.put(InventoryEntry.COLUMN_PRICE, Double.parseDouble(price));
        values.put(InventoryEntry.COLUMN_QUANTITY, Integer.parseInt(quantity));
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierNumber);
        return values;
    }

    /**
     * Helper to make toast for user.
     */
    private void toast(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Saves user data to database, depending on activity state.
     */
    private void saveItem() {
        ContentValues values = getValues();

        if (values == null) return;
        if (mUri == null) {
            addItem(values);
        } else {
            editItem(values);
        }
    }

    /**
     * Adds new item to database.
     */
    private void addItem(ContentValues values) {
        Uri newItemUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

        if (newItemUri == null) {
            toast(getString(R.string.itemAddedError));
        } else {
            toast(getString(R.string.itemAdded));
        }
    }

    /**
     * Saves edits to item to database.
     */
    private void editItem(ContentValues values) {
        String selection = InventoryEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mUri))};
        int id = getContentResolver().update(mUri, values, selection, selectionArgs);

        if (id == 0) {
            toast(getString(R.string.itemEditedError));
        } else {
            toast(getString(R.string.itemEdited));
        }
    }

    /**
     * Deletes current item from database.
     * @throws Exception
     */
    private void deleteItem() throws Exception{
        if(mUri != null){
            int rowsDeleted = getContentResolver().delete(mUri, null, null);
            if(rowsDeleted!=1) throw new Exception("Item could not be found");
            Toast.makeText(EditorActivity.this,getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show();
        }
        finish();
    }
    // IMPLEMENTING OPTIONS MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.edit).setVisible(false); // always hide edit menu item
        // If this is a new item, hide the "Delete" menu item
        MenuItem menuItem = menu.findItem(R.id.delete);
        if (mUri == null) {
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.save:
                saveItem();
                // finished the current activity, jumping back to Catalog
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the item hasn't changed, continue with handling back button press
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    /**
     * Prompts user for verification to discard changes.
     */
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
//      NEEDED TO ADD import android.support.v7.app.AlertDialog;
//      NEEDED TO ADD theme to styles.xml
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setCancelable(false);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /**
     * Prompts user for verification to delete item.
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setCancelable(false);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button
                try{
                    deleteItem();
                }
                catch (Exception err){
                    Toast.makeText(EditorActivity.this,err.toString(), Toast.LENGTH_SHORT).show();
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
