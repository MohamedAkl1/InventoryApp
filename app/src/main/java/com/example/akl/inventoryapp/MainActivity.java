package com.example.akl.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.akl.inventoryapp.data.BookContract.BookEntry;
import com.example.akl.inventoryapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    BookDbHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new BookDbHelper(this);
        insertBook();
        insertBook();
        readDb();
    }

    private void insertBook(){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.BOOK_COLUMN_NAME,"the secret");
        values.put(BookEntry.BOOK_COLUMN_PRICE, 15);
        values.put(BookEntry.BOOK_COLUMN_QUANTITY,35);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_NAME,"word press");
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL,"s123@gmail.com");

        db.insert(BookEntry.TABLE_NAME,null,values);
        Cursor cursor = db.query(BookEntry.TABLE_NAME,null,null,null,null,null,null);
        Log.i("insert book","number of elements in database are "+cursor.getCount());
        cursor.close();
    }

    private Cursor readDb(){
        SQLiteDatabase database = helper.getReadableDatabase();
        String[] project = {BookEntry._ID,BookEntry.BOOK_COLUMN_NAME,BookEntry.BOOK_COLUMN_QUANTITY};
        return database.query(BookEntry.TABLE_NAME,project,null,null,null,null,null);
    }
}
