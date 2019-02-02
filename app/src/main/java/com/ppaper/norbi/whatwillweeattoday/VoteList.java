package com.ppaper.norbi.whatwillweeattoday;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ppaper.norbi.whatwillweeattoday.Interface.ItemClickListener;
import com.ppaper.norbi.whatwillweeattoday.MenuHolder.MenuViewHolder;
import com.ppaper.norbi.whatwillweeattoday.domain.Common;
import com.ppaper.norbi.whatwillweeattoday.domain.Food;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class VoteList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference voteReference;

    TextView userName;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_vote);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mit eszünk ma?");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.food_vote_menu_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //név beállítása a menühöz
        View headerView = navigationView.getHeaderView(0);
        userName = (TextView) headerView.findViewById(R.id.menuUserName);
        userName.setText("Üdvözöllek " + Common.getCurrentUserName());

        //adatbázis betöltése
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("Food");

        //menü betöltése
        recyclerView = (RecyclerView) findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadMenu();

    }

    private void loadMenu() {
        ProgressDialog mDialog = new ProgressDialog(VoteList.this);
        mDialog.setMessage("Kérlek várj...");
        mDialog.show();

        FirebaseRecyclerAdapter<Food, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<Food, MenuViewHolder>(Food.class, R.layout.list_item, MenuViewHolder.class, databaseReference) {


            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Food model, int position) {

                viewHolder.txtMenuName.setText(getRef(position).getKey());
                try {
                    File picture = File.createTempFile(getRef(position).getKey() + ".jpg", null, getCacheDir());
                    FileUtils.writeByteArrayToFile(picture, android.util.Base64.decode(model.getEncodedPicture(), android.util.Base64.DEFAULT));
                    Picasso.with(getBaseContext()).load(picture).into(viewHolder.imageView);
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Toast.makeText(VoteList.this, getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        recyclerView.setAdapter(adapter);

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return false;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_menu) {
            Intent foodListIntent = new Intent(VoteList.this
                    , Home.class);
            startActivity(foodListIntent);
            finishAffinity();
        }
        if (id == R.id.food_list_menu) {

        } else if (id == R.id.food_vote_menu) {
            Intent foodListIntent = new Intent(VoteList.this, FoodVote.class);
            startActivity(foodListIntent);
        } else if (id == R.id.food_create_menu) {
            if (android.os.Build.VERSION.SDK_INT > 25) {
                Intent foodListIntent = new Intent(VoteList.this, CreateFood.class);
                startActivity(foodListIntent);
            } else {
                Toast.makeText(VoteList.this, "A Készüléked túl régi ehhez a funkcióhoz!", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.logout) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.food_list_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
