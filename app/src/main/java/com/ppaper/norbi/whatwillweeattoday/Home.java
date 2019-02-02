package com.ppaper.norbi.whatwillweeattoday;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppaper.norbi.whatwillweeattoday.domain.Common;
import com.ppaper.norbi.whatwillweeattoday.domain.Food;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Random;

import javax.xml.transform.Result;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference foodRef;

    TextView questionText;
    TextView userName;
    ImageView generatorImg;
    RecyclerView.LayoutManager layoutManager;
    TextView resultFoodNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mit eszünk ma?");
        setSupportActionBar(toolbar);
        generatorImg = (ImageView) findViewById(R.id.menu_image);
        questionText = (TextView) findViewById(R.id.questionText);
        resultFoodNameText = (TextView) findViewById(R.id.resultFoodNameText);

        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        questionText.setTypeface(face);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        //név beállítása a menühöz
        View headerView = navigationView.getHeaderView(0);
        userName = (TextView) headerView.findViewById(R.id.menuUserName);
        userName.setText("Üdvözöllek " + Common.getCurrentUserName());

        //adatbázis betöltése
        database = FirebaseDatabase.getInstance();
        foodRef = database.getReference("Food");

        generatorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatorImg.setOnClickListener(null);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.mixed_anim);
                generatorImg.startAnimation(animation);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setRandomFoodImage();
                    }
                }, 3000);

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRandomFoodImage(){
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot childSnapshot;

                int questionCount = (int) dataSnapshot.getChildrenCount();
                int rand = new Random().nextInt(questionCount);
                Iterator itr = dataSnapshot.getChildren().iterator();

                for (int i = 0; i < rand; i++) {
                    itr.next();
                }
                childSnapshot = (DataSnapshot) itr.next();
                File picture = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + childSnapshot.getKey() + ".jpg");
                try {
                    FileUtils.writeByteArrayToFile(picture, android.util.Base64.decode(childSnapshot.getValue(Food.class).getEncodedPicture(), android.util.Base64.DEFAULT));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Picasso.with(getBaseContext()).load(picture).into(generatorImg);
                Animation animationReverse = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.mixed_anim_reverse);
                generatorImg.startAnimation(animationReverse);
                resultFoodNameText.setText(childSnapshot.getKey());
                questionText.setText("A Mai vacsora:");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home_menu) {

        }
        if (id == R.id.food_list_menu) {
            Intent foodListIntent = new Intent(Home.this
                    , FoodList.class);
            startActivity(foodListIntent);
            finishAffinity();
        } else if (id == R.id.food_vote_menu) {
            Intent foodListIntent = new Intent(Home.this, FoodVote.class);
            startActivity(foodListIntent);
        } else if (id == R.id.food_create_menu) {
            if (android.os.Build.VERSION.SDK_INT>25){
            Intent foodListIntent = new Intent(Home.this, CreateFood.class);
            startActivity(foodListIntent);}else{
                Toast.makeText(Home.this, "A Készüléked túl régi ehhez a funkcióhoz!", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.logout) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
