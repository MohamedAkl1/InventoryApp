package com.example.akl.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mohamed Akl on 1/23/2018.
 */

public final class BookContract {

    public static final String CONTENT_AUTHORITY = "com.example.akl.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "items";

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
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ITEMS);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +"/" + PATH_ITEMS;
        public static final String Content_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
    }
}
