package com.example.akl.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * Created by Mohamed Akl on 1/23/2018.
 */

public final class BookContract {
    public static abstract class BookEntry implements BaseColumns{
        public static final String TABLE_NAME = "Books";
        public static final String _ID = BaseColumns._ID;
        public static final String BOOK_COLUMN_NAME = "name";
        public static final String BOOK_COLUMN_PRICE = "price";
        public static final String BOOK_COLUMN_QUANTITY = "quantity";
        public static final String BOOK_COLUMN_IMAGE = "product_image";
        public static final String BOOK_COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String BOOK_COLUMN_SUPPLIER_EMAIL = "supplier_email";
        public static final String BOOK_COLUMN_SUPPLIER_PHONE = "supplier_phone_number";
    }
}
