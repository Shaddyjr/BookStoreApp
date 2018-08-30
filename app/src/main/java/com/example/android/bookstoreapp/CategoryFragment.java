package com.example.android.bookstoreapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.bookstoreapp.data.InventoryDbHelper;

public class CategoryFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

//    CategoryFragmentAdapter mAdapter;
//    InventoryDbHelper mDbHelper;

//    static public Context MAIN_CONTEXT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        MAIN_CONTEXT = getContext();
//        mAdapter = new CategoryFragmentAdapter(MAIN_CONTEXT,null);
//        mDbHelper = new InventoryDbHelper(MAIN_CONTEXT);
        View rootView = inflater.inflate(R.layout.category_items_activity, container, false);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
