package com.example.akl.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akl.inventoryapp.data.BookContract.BookEntry;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView detailName,detailPrice,detailQuantity,detailSupplierName,detailSupplierEmail,detailSupplierPhone;
    Button salePlus,saleMinus,messageSupplier;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        detailName = findViewById(R.id.detail_name);
        detailPrice = findViewById(R.id.detail_price);
        detailQuantity = findViewById(R.id.detail_quantity);
        detailSupplierName = findViewById(R.id.detail_supplier_name);
        detailSupplierEmail = findViewById(R.id.detail_supplier_email);
        detailSupplierPhone = findViewById(R.id.detail_supplier_phone);
        salePlus = findViewById(R.id.sales_plus);
        salePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(BookEntry.BOOK_COLUMN_QUANTITY,mCursor.getInt(mCursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY))+1);
                getContentResolver().update(getIntent().getData(),values,null,null);
            }
        });
        saleMinus = findViewById(R.id.sales_minus);
        saleMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCursor.getInt(mCursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY)) >= 1){
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.BOOK_COLUMN_QUANTITY,mCursor.getInt(mCursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY))-1);
                    getContentResolver().update(getIntent().getData(),values,null,null);
                }
            }
        });
        messageSupplier = findViewById(R.id.message_supplier);
        messageSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mCursor.getString(mCursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL));
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_menu_button){
            Intent intent = new Intent(DetailActivity.this,EditActivity.class);
            intent.setData(getIntent().getData());
            intent.putExtra(BookEntry._ID,getIntent().getExtras().getString(BookEntry._ID));
            startActivity(intent);
        }
        if (item.getItemId() == R.id.delete_item){
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(),
                getIntent().getData(),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        if(cursor.moveToFirst()){
            detailName.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_NAME)));
            detailQuantity.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY))));
            detailPrice.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_PRICE))));
            detailSupplierName.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_SUPPLIER_NAME)));
            if(cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE)) == 0){
                detailSupplierPhone.setText("not available");
            }else {
                detailSupplierPhone.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE))));
            }
            detailSupplierEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        detailName.setText("");
        detailPrice.setText("");
        detailQuantity.setText("");
        detailSupplierName.setText("");
        detailSupplierEmail.setText("");
        detailSupplierPhone.setText("");
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        getContentResolver().delete(getIntent().getData(),null,null);
        Toast.makeText(this,"item deleted successfully",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DetailActivity.this,MainActivity.class));
    }
}
