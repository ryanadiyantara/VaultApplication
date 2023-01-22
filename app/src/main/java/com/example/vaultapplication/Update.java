package com.example.vaultapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import db.DbHelper;
import model.Item;

public class Update extends AppCompatActivity {

    private EditText editName, editPrice;
    private ImageView editImage;
    private Button editBtnChooseImage, editBtnUpdate, editBtnDelete, editBtnCancel ;
    final int REQUEST_CODE_GALLERY = 999;
    private Item item;

    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //FIND VIEW
        editName = findViewById(R.id.edititemname);
        editPrice = findViewById(R.id.edititemprice);
        editImage = findViewById(R.id.editimageview);
        editBtnChooseImage = findViewById(R.id.editbtnchooseimage);
        editBtnUpdate = findViewById(R.id.editbtnupdate);
        editBtnDelete = findViewById(R.id.editbtndelete);
        editBtnCancel = findViewById(R.id.editbtncancel);

        dbHelper = new DbHelper(this);

        //SET
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");

        editName.setText(item.getItem_name());
        editPrice.setText(item.getItem_price());
        byte[] gunImage = (item.getItem_pict());
        Bitmap bitmap = BitmapFactory.decodeByteArray(gunImage, 0, gunImage.length);
        editImage.setImageBitmap(bitmap);

        //BUTTON CHOOSE IMAGE
        editBtnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(Update.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        //EDIT BUTTON UPDATE
        editBtnUpdate.setOnClickListener(v -> {
            if (editName.getText().toString().isEmpty()) {
                Toast.makeText(Update.this, "Error: Name Must be Filled!", Toast.LENGTH_SHORT).show();
            } else if (editPrice.getText().toString().isEmpty()) {
                Toast.makeText(Update.this, "Error: Price must be Filled!", Toast.LENGTH_SHORT).show();
            }else {
                dbHelper.updateItem(item.getItem_id(), editName.getText().toString(), editPrice.getText().toString(), imageViewToByte(editImage));

                editName.setText("");
                editPrice.setText("");
                editImage.setImageResource(R.drawable.edittextbackground);
                Toast.makeText(Update.this, "Create a New Order Successfully!", Toast.LENGTH_SHORT).show();
                Intent intentupdate = new Intent(Update.this, Home.class);
                startActivity(intentupdate);
            }
        });

        //EDIT BUTTON DELETE
        editBtnDelete.setOnClickListener(v -> {
            dbHelper.deleteItem(item.getItem_id());
            Toast.makeText(Update.this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
            Intent intenteditdelete = new Intent(Update.this, Home.class);
            startActivity(intenteditdelete);
        });

        //EDIT BUTTON CANCEL
        editBtnCancel.setOnClickListener(v -> {
            Intent intenteditcancel = new Intent(Update.this, Home.class);
            startActivity(intenteditcancel);
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                editImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}