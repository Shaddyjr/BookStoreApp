package com.example.android.bookstoreapp;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;


public class CategoryFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView mListView;
    private View mEmptyView;
    CategoryCursorAdapter mCursorAdapter;
    private static final int LOADER_ID = 0;
    private Context MAIN_CONTEXT;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MAIN_CONTEXT = getContext();
        View rootView = inflater.inflate(R.layout.category_items_activity, container, false);

        mListView = rootView.findViewById(R.id.category_items_container);
        mEmptyView = rootView.findViewById(R.id.emptyDB);

        mListView.setEmptyView(mEmptyView);
        mCursorAdapter = new CategoryCursorAdapter(getActivity(), null);
        mListView.setAdapter(mCursorAdapter);
        // adding click listener to each item
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.v("onCreateView","item clicked: " + position);
                Intent intent = new Intent(MAIN_CONTEXT, EditorActivity.class);
                Uri uri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI,id);
                // can set uri data directly with intent
                intent.setData(uri);
                startActivity(intent);
            }
        });

        // TODO: add menu options (delete all)
        addFakeData();
        // kicking off loader
        getLoaderManager().initLoader(LOADER_ID,null,this);
        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
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
                        MAIN_CONTEXT,
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void addFakeData(){
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_NAME, "Bob");
        values.put(InventoryEntry.COLUMN_PRICE, 15.04);
        values.put(InventoryEntry.COLUMN_QUANTITY, 2);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, "Juan");
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, "456-457-1234");
        MAIN_CONTEXT.getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
    }
}
