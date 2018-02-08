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
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.akl.inventoryapp.data.BookContract.BookEntry;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText etName,etQuantity,etPrice,etSupplierName,etSupplierEmail,etSupplierPhone;
    Uri mUri;
    boolean itemChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if(mUri == null){
            setTitle("Add new Item");
            invalidateOptionsMenu();
        }

        etName = findViewById(R.id.edit_name);
        etPrice = findViewById(R.id.edit_price);
        etQuantity = findViewById(R.id.edit_quantity);
        etSupplierName = findViewById(R.id.edit_supplier_name);
        etSupplierEmail = findViewById(R.id.edit_supplier_email);
        etSupplierPhone = findViewById(R.id.edit_supplier_phone);
        Bundle extra = getIntent().getExtras();
        if(extra == null){
            setTitle("Add an Item");
        }else {
            setTitle("Edit an Item");
        }
        getLoaderManager().initLoader(0,null,this);

        etName.setOnTouchListener(mTouchListener);
        etPrice.setOnTouchListener(mTouchListener);
        etQuantity.setOnTouchListener(mTouchListener);
        etSupplierPhone.setOnTouchListener(mTouchListener);
        etSupplierEmail.setOnTouchListener(mTouchListener);
        etSupplierName.setOnTouchListener(mTouchListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(getIntent().getData() != null){
            return new CursorLoader(getApplicationContext(),
                    getIntent().getData(),
                    null,
                    null,
                    null,
                    null);
        }else {
            return new CursorLoader(getApplicationContext(),
                    BookEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }
    }

    @Override
    public void onBackPressed() {
        if(!itemChanged){
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            itemChanged = true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClckListener){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClckListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(getIntent().getData() != null){
            if(cursor.moveToFirst()){
                etName.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_NAME)));
                etPrice.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_PRICE)));
                etQuantity.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_QUANTITY)));
                etSupplierName.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_SUPPLIER_NAME)));
                etSupplierEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL)));
                etSupplierPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.edit_activity_menu,menu);
    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_item:
                if(saveItem()){
                    finish();
                }
                return true;
            case android.R.id.home:
                if(!itemChanged){
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditActivity.this,DetailActivity.class);
                        intent.setData(getIntent().getData());
                        startActivity(intent);
                    }
                };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveItem() {
        String itemName = etName.getText().toString().trim();
        String itemPrice = etPrice.getText().toString().trim();
        String itemQuantity = etQuantity.getText().toString().trim();
        String itemSupplierName = etSupplierName.getText().toString().trim();
        String itemSupplierEmail = etSupplierEmail.getText().toString().trim();
        String itemSupplierPhone = etSupplierPhone.getText().toString().trim();

        if(TextUtils.isEmpty(itemName) || TextUtils.isEmpty(itemSupplierName) || TextUtils.isEmpty(itemSupplierEmail)){
            Toast.makeText(this,"please enter valid information",Toast.LENGTH_SHORT).show();
            return false;
        }

        int priceInt,quantityInt,supplierPhoneInt;
        if(TextUtils.isEmpty(itemPrice) || Integer.parseInt(itemPrice) < 0){
            Toast.makeText(this,"enter valid price",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            priceInt = Integer.parseInt(itemPrice);
        }
        if(TextUtils.isEmpty(itemQuantity) || Integer.parseInt(itemQuantity) < 0){
            Toast.makeText(this,"enter valid quantity",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            quantityInt = Integer.parseInt(itemQuantity);
        }
        if (TextUtils.isEmpty(itemSupplierPhone) || Integer.parseInt(itemSupplierPhone) <0){
            Toast.makeText(this,"enter valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            supplierPhoneInt = Integer.parseInt(itemSupplierPhone);
        }

        ContentValues values = new ContentValues();
        values.put(BookEntry.BOOK_COLUMN_NAME,itemName);
        values.put(BookEntry.BOOK_COLUMN_PRICE,priceInt);
        values.put(BookEntry.BOOK_COLUMN_QUANTITY,quantityInt);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_NAME,itemSupplierName);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL,itemSupplierEmail);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE,supplierPhoneInt);
        if(getIntent().getData() != null){
            getContentResolver().update(getIntent().getData(),
                    values,
                    null,
                    null);
            Toast.makeText(this, "item edited",Toast.LENGTH_SHORT).show();
            return true;
        }else{
            mUri = getContentResolver().insert(BookEntry.CONTENT_URI,values);
            Toast.makeText(this, "Item saved successfully",Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        etName.setText("");
        etPrice.setText("");
        etQuantity.setText("");
        etSupplierName.setText("");
        etSupplierEmail.setText("");
        etSupplierPhone.setText("");
    }
}
