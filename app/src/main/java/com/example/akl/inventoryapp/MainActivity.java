package com.example.akl.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.akl.inventoryapp.data.BookContract.BookEntry;
import com.example.akl.inventoryapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    BookDbHelper helper;
    InventoryCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });
        helper = new BookDbHelper(this);
        ListView lv = findViewById(R.id.inventory_lv);
        mAdapter = new InventoryCursorAdapter(this, null);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra(BookEntry._ID,id);
                intent.setData(ContentUris.withAppendedId(BookEntry.CONTENT_URI,id));
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert_dummy_data:
                insertItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertItem() {
        ContentValues values = new ContentValues();
        values.put(BookEntry.BOOK_COLUMN_NAME,"google mini");
        values.put(BookEntry.BOOK_COLUMN_QUANTITY,15);
        values.put(BookEntry.BOOK_COLUMN_PRICE,35);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_NAME, "ahmed");
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL,"123@a1223");
        getContentResolver().insert(BookEntry.CONTENT_URI,values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] project = {BookEntry._ID,BookEntry.BOOK_COLUMN_NAME,BookEntry.BOOK_COLUMN_PRICE,BookEntry.BOOK_COLUMN_QUANTITY};
        return new CursorLoader(this,BookEntry.CONTENT_URI,project,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}