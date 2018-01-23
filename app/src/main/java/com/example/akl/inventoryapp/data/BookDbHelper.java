package com.example.akl.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.akl.inventoryapp.data.BookContract.BookEntry;

/**
 * Created by Mohamed Akl on 1/23/2018.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_DATABASE_CREATE_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + "("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.BOOK_COLUMN_NAME + " TEXT NOT NULL, "
                + BookEntry.BOOK_COLUMN_PRICE + " INTEGER NOT NULL, "
                + BookEntry.BOOK_COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + BookEntry.BOOK_COLUMN_IMAGE + " BLOB, "
                + BookEntry.BOOK_COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL + " Text NOT NULL, "
                + BookEntry.BOOK_COLUMN_SUPPLIER_PHONE + " INTEGER);";

        db.execSQL(SQL_DATABASE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
