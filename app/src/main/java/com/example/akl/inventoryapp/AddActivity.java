package com.example.akl.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AddActivity extends AppCompatActivity {

    EditText etName,etQuantity,etPrice,etSupplierName,etSupplierEmail,etSupplierPhone;
    Uri mUri;
    boolean itemChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = findViewById(R.id.edit_name2);
        etPrice = findViewById(R.id.edit_price2);
        etQuantity = findViewById(R.id.edit_quantity2);
        etSupplierName = findViewById(R.id.edit_supplier_name2);
        etSupplierEmail = findViewById(R.id.edit_supplier_email2);
        etSupplierPhone = findViewById(R.id.edit_supplier_phone2);

        etName.setOnTouchListener(mTouchListener);
        etPrice.setOnTouchListener(mTouchListener);
        etQuantity.setOnTouchListener(mTouchListener);
        etSupplierPhone.setOnTouchListener(mTouchListener);
        etSupplierEmail.setOnTouchListener(mTouchListener);
        etSupplierName.setOnTouchListener(mTouchListener);
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
                        Intent intent = new Intent(AddActivity.this,MainActivity.class);
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
        mUri = getContentResolver().insert(BookEntry.CONTENT_URI,values);
        Toast.makeText(this, "Item saved successfully",Toast.LENGTH_SHORT).show();
        return true;
    }
}
