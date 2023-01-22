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

public class Create extends AppCompatActivity {
    private EditText insertName, insertPrice;
    private ImageView insertImage;
    private Button insertBtnCreate, insertBtnCancel, insertBtnChooseImage;
    final int REQUEST_CODE_GALLERY = 999;

    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //FIND VIEW
        insertName = findViewById(R.id.insertitemname);
        insertPrice = findViewById(R.id.insertitemprice);
        insertImage = findViewById(R.id.insertimageview);
        insertBtnChooseImage = findViewById(R.id.insertbtnchooseimage);
        insertBtnCreate = findViewById(R.id.insertbtncreate);
        insertBtnCancel = findViewById(R.id.insertbtncancel);

        dbHelper = new DbHelper(this);

        //INSERT BUTTON CHOOSE IMAGE
        insertBtnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(Create.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        //INSERT BUTTON CREATE
        insertBtnCreate.setOnClickListener(v -> {
            if (insertName.getText().toString().isEmpty()) {
                Toast.makeText(Create.this, "Error: Name Must be Filled!", Toast.LENGTH_SHORT).show();
            } else if (insertPrice.getText().toString().isEmpty()) {
                Toast.makeText(Create.this, "Error: Price must be Filled!", Toast.LENGTH_SHORT).show();
            }else {
                dbHelper.createItem(insertName.getText().toString(), insertPrice.getText().toString(), imageViewToByte(insertImage));

                insertName.setText("");
                insertPrice.setText("");
                insertImage.setImageResource(R.drawable.edittextbackground);
                Toast.makeText(Create.this, "Create a New Order Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Create.this, Home.class);
                startActivity(intent);
            }
        });

        //INSERT BUTTON CANCEL
        insertBtnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(Create.this, Home.class);
            startActivity(intent);
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
                insertImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}