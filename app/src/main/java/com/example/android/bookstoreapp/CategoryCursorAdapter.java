package com.example.android.bookstoreapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.InventoryContract.InventoryEntry;

import java.util.Locale;

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
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameView = view.findViewById(R.id.itemName);
        TextView priceView = view.findViewById(R.id.itemPrice);
        final TextView quantityView = view.findViewById(R.id.itemQuantity);

        nameView.setText(cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_NAME)));

        double price = cursor.getDouble(cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE));
        priceView.setText(String.format(Locale.US,"$%.2f", price));

        quantityView.setText(cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY)));

        Button button = view.findViewById(R.id.saleButton);
        // Storing position to be used onclick
        button.setTag(cursor.getPosition());
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Using current view's cursor position
                int cursorPosition = (int) view.getTag();
                cursor.moveToPosition(cursorPosition);

                // DATABASE UPDATE
                int quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY)));
                if( quantity - 1 < 0) return;

                int updatedQuantity = quantity - 1;

                String cursorId = cursor.getString(cursor.getColumnIndex(InventoryEntry._ID));
                Uri uri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, Long.parseLong(cursorId));
                ContentValues values = new ContentValues();
                values.put(InventoryEntry.COLUMN_QUANTITY,updatedQuantity);

                String selection = InventoryEntry._ID + "=?";
                String[] selectionArgs = new String[] { cursorId };

                int id = context.getContentResolver().update(uri, values, selection, selectionArgs);
                // VISUAL CONFIRMATION
                if(id == 0){
                    // Failed to update
                    Toast.makeText(context, context.getString(R.string.itemEditedError), Toast.LENGTH_SHORT).show();
                }
                // No need for "else" statement - loader automatically updates with call to notifyDataSetChanged()
            }
        });
    }
}
