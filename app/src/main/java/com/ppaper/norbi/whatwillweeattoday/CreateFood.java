package com.ppaper.norbi.whatwillweeattoday;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppaper.norbi.whatwillweeattoday.domain.Food;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CreateFood extends Activity {

    private static int RESULT_LOAD_IMAGE = 1;
    private Button createBtn;
    private String picturePath;
    private TextView newFoodName;
    private ImageView imageView;
    private TextView imgBackgroundText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_food);

        newFoodName = (TextView) findViewById(R.id.newFoodName);
        createBtn = (Button) findViewById(R.id.btnCreate);
        imageView = (ImageView) findViewById(R.id.uploadedImage);
        imgBackgroundText= (TextView) findViewById(R.id.imgBackgroundText);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        createBtn.setOnClickListener(event -> {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_food = database.getReference("Food");

            table_food.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     boolean isExist=false;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.getKey().equals(newFoodName.getText().toString())) {
                            Toast.makeText(CreateFood.this, "Ilyen néven szerepel már étel az adatbázisban!", Toast.LENGTH_SHORT).show();
                            isExist = true;
                            break;

                        }
                    }
                    if(!isExist) {
                        Food food = new Food();
                        try {
                            food.setEncodedPicture(Base64.getEncoder().encodeToString(Files.readAllBytes(new File(picturePath).toPath())));

                        } catch (IOException e) {
                            e.printStackTrace();

                        }

                        DatabaseReference foodReference = table_food.child(newFoodName.getText().toString());
                        final ProgressDialog mDialog = new ProgressDialog(CreateFood.this);
                        mDialog.setMessage("Kérlek várj...");
                        mDialog.show();
                        foodReference.setValue(food);
                        Toast.makeText(CreateFood.this, "Az Étel mentése sikeres!", Toast.LENGTH_SHORT).show();
                        Intent foodListIntent = new Intent(CreateFood.this, CreateFood.class);
                        startActivity(foodListIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//                table_food.child("kajanagy").setValue(foodReference);

//                table_food.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Food foodReference = dataSnapshot.child("kajanagy").getValue(Food.class);
//                        try {
//                            FileUtils.writeByteArrayToFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/asd.jpg"),Base64.getDecoder().decode( foodReference.getEncodedPicture()));
//                            System.err.println(Environment.getExternalStorageDirectory().getAbsolutePath());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();


            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imgBackgroundText.setText("");
        }


    }

}