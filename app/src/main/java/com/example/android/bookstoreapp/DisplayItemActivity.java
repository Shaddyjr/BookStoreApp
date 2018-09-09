package com.example.android.bookstoreapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class DisplayItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri mUri;
    private static final int LOADER_ID = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mUri = intent.getData();
        if(mUri==null) finish();

        getLoaderManager().initLoader(LOADER_ID, null, this);
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
//    TODO: add menu for delete and edit
//    TODO: Include Quantity changing logic
//    TODO: Add Quantity changing buttons
//    TODO: Add phone intent
}
