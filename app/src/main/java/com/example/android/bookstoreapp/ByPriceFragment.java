package com.example.android.bookstoreapp;

import android.support.v4.content.CursorLoader;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;

public class ByPriceFragment extends CategoryFragment {
    @Override
    public CursorLoader createCursorLoader(String[] projection) {
        String colName = InventoryEntry.COLUMN_PRICE;
        String sortOrder = String.format("%s ASC",colName);
        return new CursorLoader(
                MAIN_CONTEXT,
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        );
    }
}
