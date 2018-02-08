package com.example.akl.inventoryapp;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.akl.inventoryapp.data.BookContract.BookEntry;

/**
 * Created by Mohamed Akl on 1/30/2018.
 */

public class InventoryCursorAdapter extends CursorAdapter {

    private Cursor mCursor;
    private Context mContext;

    InventoryCursorAdapter(Context context, Cursor c){
        super(context,c,0);
        this.mCursor = c;
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView== null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item,
                    null);
        }
        convertView.setTag(position);
        return super.getView(position, convertView, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.item_name_tv);
        TextView price = view.findViewById(R.id.item_price_tv);
        TextView quantity = view.findViewById(R.id.item_quantity_tv);
        Button sale = view.findViewById(R.id.sale_button);
        mCursor = cursor;
        final int position=(Integer) view.getTag();
        name.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_NAME)));
        Resources res = mContext.getResources();
        price.setText(String.format(res.getString(R.string.price),cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_PRICE))));
        quantity.setText(String.format(res.getString(R.string.quantity),cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY))));
        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCursor.moveToPosition(position);
                if(mCursor.getInt(mCursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY)) >= 1){
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.BOOK_COLUMN_QUANTITY,mCursor.getInt(mCursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY)) - 1);
                    mContext.getContentResolver().update(ContentUris.withAppendedId(BookEntry.CONTENT_URI,mCursor.getLong(mCursor.getColumnIndexOrThrow(BookEntry._ID))),
                            values,
                            null,
                            null);
                }
            }
        });
    }
}