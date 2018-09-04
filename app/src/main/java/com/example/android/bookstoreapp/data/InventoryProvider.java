package com.example.android.bookstoreapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;

public class InventoryProvider extends ContentProvider{

    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    /** URI matcher code for the content URI for the inventory table */
    private static final int ITEMS = 1337;

    /** URI matcher code for the content URI for a single item in the inventory table */
    private static final int ITEM_ID = 69;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    InventoryDbHelper mDbHelper;

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // Sets up the UriMatcher to only recognize each pattern and assign correct associated code.

        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY, ITEMS);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY + "/#", ITEM_ID);
    }
    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get Database Object
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        final int match = sUriMatcher.match(uri); // Set up Uri Matcher to determine table query type
        switch(match){
            case ITEMS:
                cursor = db.query(InventoryEntry.TABLE_NAME,projection,selection,selectionArgs,null, null, sortOrder);
                break;
            case ITEM_ID:
                selection = String.format("%s=?", InventoryEntry._ID);
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(InventoryEntry.TABLE_NAME, projection,selection,selectionArgs,null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }
        // Setting up notification Uri, so that any changes at the uri location will trigger update on cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    private Uri insertItem(Uri uri, ContentValues contentValues){
//      DATA VALIDATION
        String name = contentValues.getAsString(InventoryEntry.COLUMN_NAME);
        if(!InventoryEntry.isValidName(name)){
            throw new IllegalArgumentException("Item requires a name");
        }

        Double price = contentValues.getAsDouble(InventoryEntry.COLUMN_PRICE);
        if(!InventoryEntry.isValidPrice(price)){
            throw new IllegalArgumentException("Item requires a non-negative price");
        }

        Integer quantity = contentValues.getAsInteger(InventoryEntry.COLUMN_QUANTITY);
        if(!InventoryEntry.isValidQuantity(quantity)){
            throw new IllegalArgumentException("Item requires a non-negative quantity");
        }

//      INSERTING INTO DB
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(InventoryEntry.TABLE_NAME, null, contentValues);
        if(id<0){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // notify all listeners of change
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                // Delete a single row given by the ID in the URI
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if(rowsDeleted>0){
            // notify all listeners of change
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                // For the ITEM_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        // If there are no values to update, then nothing to do
        if (contentValues.size() == 0) {
            return 0;
        }
//      DATA VALIDATION
        if (contentValues.containsKey(InventoryEntry.COLUMN_NAME)) {
            String name = contentValues.getAsString(InventoryEntry.COLUMN_NAME);
            if (!InventoryEntry.isValidName(name)) {
                throw new IllegalArgumentException("Item requires a name");
            }
        }

        if (contentValues.containsKey(InventoryEntry.COLUMN_PRICE)) {
            Double price = contentValues.getAsDouble(InventoryEntry.COLUMN_PRICE);
            if (!InventoryEntry.isValidPrice(price)) {
                throw new IllegalArgumentException("Item requires a non-negative price");
            }
        }

        if (contentValues.containsKey(InventoryEntry.COLUMN_QUANTITY)) {
            Integer quantity = contentValues.getAsInteger(InventoryEntry.COLUMN_QUANTITY);
            if (!InventoryEntry.isValidQuantity(quantity)) {
                throw new IllegalArgumentException("Item requires a non-negative quantity");
            }
        }
//      UPDATING DB
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(InventoryEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if(rowsUpdated > 0){
            // notify all listeners of change
            getContext().getContentResolver().notifyChange(uri,null);
        }

        // Returns the number of database rows affected by the update statement
        return rowsUpdated;
    }
}
