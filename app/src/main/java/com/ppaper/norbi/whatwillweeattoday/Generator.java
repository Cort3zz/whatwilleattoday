package com.ppaper.norbi.whatwillweeattoday;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppaper.norbi.whatwillweeattoday.domain.Food;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class Generator extends AppCompatActivity implements View.OnClickListener {
Button btnSignIn;
TextView txtSlogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(Generator.this, SignIn.class);
                startActivity(signIn);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_food = database.getReference("Food");


//        Food food = new Food();
//        food.setReceipt("recept");
//                try {
//                    food.setEncodedPicture(  Base64.getEncoder().encodeToString(Files.readAllBytes(new File("/storage/self/primary/temp_file/Photo-01-02-2016-06-14-10.jpg").toPath())));
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                table_food.child("kajanagy").setValue(food);

//                table_food.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Food food = dataSnapshot.child("kajanagy").getValue(Food.class);
//                        try {
//                            FileUtils.writeByteArrayToFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/asd.jpg"),Base64.getDecoder().decode( food.getEncodedPicture()));
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
            }
        });

    }


    @Override
    public void onClick(View view) {



    }
}
