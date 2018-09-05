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
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AllItemsFragment extends CategoryFragment {
    @Override
    public CursorLoader createCursorLoader(String[] projection) {
        return new CursorLoader(
                MAIN_CONTEXT,
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }
}
