package com.example.android.bookstoreapp;




import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;

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
