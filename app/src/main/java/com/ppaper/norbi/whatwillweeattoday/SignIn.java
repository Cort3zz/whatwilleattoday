package com.ppaper.norbi.whatwillweeattoday;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ppaper.norbi.whatwillweeattoday.domain.Common;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    EditText edtName, edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Kérlek várj...");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(edtName.getText().toString()).exists()) {
                            mDialog.dismiss();
                            GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                            };
                            Map<String, String> map = dataSnapshot.child(edtName.getText().toString()).getValue(genericTypeIndicator);
                            if (map.get("password").equals(edtPassword.getText().toString())) {
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                 Common.setCurrentUserName( edtName.getText().toString());
                                 startActivity(homeIntent);
                                 finish();


                            } else {
                                Toast.makeText(SignIn.this, "Sikertelen Bejelentkezés!", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(SignIn.this, "Helytelen név!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
