package com.example.android.bookstoreapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;

public class CategoryCursorAdapter extends CursorAdapter{
    public CategoryCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    /**
     * Determines which layout to use for each row of data.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent, false);
    }

    /**
     * Determines how data from row is incorporated into the layout.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = view.findViewById(R.id.itemName);
        TextView priceView = view.findViewById(R.id.itemPrice);
        TextView quantityView = view.findViewById(R.id.itemQuantity);

        nameView.setText(cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_NAME)));
        priceView.setText(cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE)));
        quantityView.setText(cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY)));

        Button button = view.findViewById(R.id.saleButton);
//      TODO: add "Sale" logic
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.v("CursorAdapter: ","This 'Sale' button was clicked!");
            }
        });
    }
}
