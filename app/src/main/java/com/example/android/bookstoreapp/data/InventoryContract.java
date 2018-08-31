package com.example.android.bookstoreapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.bookstoreapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse(String.format("content://%s", CONTENT_AUTHORITY));
    public static final String PATH_INVENTORY = "inventory"; // path for a table in db

    /**
     * Can't instantiate.
     */
    private InventoryContract() {
    }

    public static abstract class InventoryEntry implements BaseColumns {
        public final static String TABLE_NAME = "inventory";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /** You can use the MIME type information to find out if your application can handle data that the provider offers, or to choose a type of handling based on the MIME type. You usually need the MIME type when you are working with a provider that contains complex data structures or files
        */

        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static boolean isValidName(String name){
            return name != null;
        }

        public static boolean isValidPrice (Integer price){
            return price != null && price >= 0;
        }

        public static boolean isValidQuantity(Integer quantity){
            return quantity != null && quantity >= 0;
        }
    }
}
