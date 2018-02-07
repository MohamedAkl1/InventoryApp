package com.example.akl.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akl.inventoryapp.data.BookContract.BookEntry;
import com.example.akl.inventoryapp.data.BookDbHelper;

/**
 * Created by Mohamed Akl on 1/30/2018.
 */

public class InventoryCursorAdapter extends CursorAdapter {
    Context mContext;
    BookDbHelper mHelper;
    Cursor mCursor;

    public InventoryCursorAdapter(Context context,Cursor c){
        super(context,c,0);
        this.mCursor = c;
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.item_name_tv);
        TextView price = view.findViewById(R.id.item_price_tv);
        TextView quantity = view.findViewById(R.id.item_quantity_tv);
        Button sale = view.findViewById(R.id.sale_button);
        mCursor = cursor;
        name.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_NAME)));
        final Resources res = mContext.getResources();
        price.setText(String.format(res.getString(R.string.price),cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_PRICE))));
        quantity.setText(String.format(res.getString(R.string.quantity),cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY))));

    }
}
