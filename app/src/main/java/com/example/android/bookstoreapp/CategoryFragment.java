package com.example.android.bookstoreapp;


import android.content.ContentUris;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;


public abstract class CategoryFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    CategoryCursorAdapter mCursorAdapter;
    private static final int LOADER_ID = 0;
    public Context MAIN_CONTEXT;

    public abstract CursorLoader createCursorLoader(String[] projection);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MAIN_CONTEXT = getContext();
        View rootView = inflater.inflate(R.layout.category_items_activity, container, false);

        ListView mListView = rootView.findViewById(R.id.category_items_container);
        RelativeLayout mEmptyView = rootView.findViewById(R.id.emptyDB);

        mListView.setEmptyView(mEmptyView);
        mCursorAdapter = new CategoryCursorAdapter(getActivity(), null);
        mListView.setAdapter(mCursorAdapter);

        // adding click listener to each item
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), DisplayItemActivity.class);
                Uri uri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI,id);
                // can set uri data directly with intent
                intent.setData(uri);
                startActivity(intent);
            }
        });

        mCursorAdapter.notifyDataSetChanged(); // good practice to call when data changes!
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
                return createCursorLoader(projection);
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
}
