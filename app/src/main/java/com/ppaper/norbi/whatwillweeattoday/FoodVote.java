package com.ppaper.norbi.whatwillweeattoday;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ppaper.norbi.whatwillweeattoday.Interface.ItemClickListener;
import com.ppaper.norbi.whatwillweeattoday.MenuHolder.MenuViewHolder;
import com.ppaper.norbi.whatwillweeattoday.domain.Common;
import com.ppaper.norbi.whatwillweeattoday.domain.Food;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class FoodVote extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference food;

    TextView userName;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mit eszünk ma?");
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.food_list_drawer_layout);
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
        food = database.getReference("Food");

        //menü betöltése
        recyclerView = (RecyclerView) findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        loadMenu();

    }

    private void loadMenu() {
        FirebaseRecyclerAdapter<Food, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<Food, MenuViewHolder>(Food.class, R.layout.vote_item, MenuViewHolder.class, food) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Food model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                try {
                    File picture =   new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + model.getName() +".jpg");
                    System.out.println(picture.getAbsolutePath());
                    FileUtils.writeByteArrayToFile(picture,Base64.getDecoder().decode(model.getEncodedPicture()));
                    Picasso.with(getBaseContext()).load(picture).into(viewHolder.imageView);
                    final Food clickItem = model;
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Toast.makeText(FoodVote.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.food_list_menu) {
            Intent foodListIntent = new Intent(FoodVote.this, FoodList.class);
            startActivity(foodListIntent);
        }else if (id == R.id.food_vote_menu) {
            Intent foodListIntent = new Intent(FoodVote.this, FoodVote.class);
            startActivity(foodListIntent);
        }
        else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.food_list_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
