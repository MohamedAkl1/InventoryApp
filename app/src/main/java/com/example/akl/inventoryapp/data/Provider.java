package com.example.akl.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.akl.inventoryapp.data.BookContract.BookEntry;

/**
 * Created by Mohamed Akl on 1/29/2018.
 */

public class Provider extends ContentProvider {

    BookDbHelper mBookDbHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;

    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_ITEMS,ITEMS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_ITEMS + "/#",ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        mBookDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mBookDbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                cursor = database.query(BookEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case ITEM_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BookEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException();
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                String name = values.getAsString(BookEntry.BOOK_COLUMN_NAME);
                if(name == null){
                    throw new IllegalArgumentException("item requires a name");
                }
                Integer price = values.getAsInteger(BookEntry.BOOK_COLUMN_PRICE);
                if(price == null || price < 0){
                    throw new IllegalArgumentException("item requires a valid price");
                }
                Integer quantity = values.getAsInteger(BookEntry.BOOK_COLUMN_QUANTITY);
                if(quantity < 0){
                    throw new IllegalArgumentException("item requires a valid quantity");
                }
                String supplierName = values.getAsString(BookEntry.BOOK_COLUMN_SUPPLIER_NAME);
                if(supplierName == null){
                    throw new IllegalArgumentException("item requires a valid supplier name");
                }
                String supplierEmail = values.getAsString(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL);
                if(supplierEmail == null){
                    throw new IllegalArgumentException("item requires a valid supplier email");
                }
                SQLiteDatabase database = mBookDbHelper.getWritableDatabase();
                long id = database.insert(BookEntry.TABLE_NAME,null, values);
                if(id == -1){
                    Log.e("Provider.java","failed to add new row");
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return ContentUris.withAppendedId(uri,id);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mBookDbHelper.getWritableDatabase();
        switch (match){
            case ITEMS:
                return database.delete(BookEntry.TABLE_NAME,selection,selectionArgs);
            case ITEM_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                int rowsDeleted = database.delete(BookEntry.TABLE_NAME,selection,selectionArgs);
                if(rowsDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion isn't complete");
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                return updateItem(uri,values,selection,selectionArgs);
            case ITEM_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("update isn't complete");
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mBookDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(BookEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                return BookEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return BookEntry.Content_ITEM_TYPE;
            default:throw new IllegalStateException();
        }
    }
}
