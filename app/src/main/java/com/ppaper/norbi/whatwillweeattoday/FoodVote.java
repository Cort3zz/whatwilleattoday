package com.ppaper.norbi.whatwillweeattoday;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.ppaper.norbi.whatwillweeattoday.domain.Vote;
import com.ppaper.norbi.whatwillweeattoday.domain.VoteItem;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

public class FoodVote extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference voteReference;
    DatabaseReference newVoteReference;


    TextView userName;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView titleWithCounter;
    List<String> listOfSelectedFoods;
    ImageView toolbarCheckMarkImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_vote);

        listOfSelectedFoods = new ArrayList<>();

        titleWithCounter = (TextView) findViewById(R.id.toolbarTextView);
        toolbarCheckMarkImg = (ImageView) findViewById(R.id.toolbarCheckMarkImg);
        refreshTitle(0);

        toolbarCheckMarkImg.setVisibility(View.VISIBLE);

        //menü betöltése
        recyclerView = (RecyclerView) findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);


        //adatbázis betöltése
        database = FirebaseDatabase.getInstance();

        database.getReference("Vote").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        databaseReference = postSnapshot.getRef().child("voteItems");
                    }


                }else{
                    databaseReference = database.getReference("Food");

                }

                voteReference = database.getReference("Vote");

                loadMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadMenu() {
        FirebaseRecyclerAdapter<Food, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<Food, MenuViewHolder>(Food.class, R.layout.list_item, MenuViewHolder.class, databaseReference) {

            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Food model, int position) {
                viewHolder.txtMenuName.setText(getRef(position).getKey());
                try {
                    File picture = File.createTempFile(getRef(position).getKey() + ".jpg", null, getCacheDir());
                    FileUtils.writeByteArrayToFile(picture, android.util.Base64.decode(model.getEncodedPicture(), android.util.Base64.DEFAULT));
                    Picasso.with(getBaseContext()).load(picture).into(viewHolder.imageView);
                    final Food clickItem = model;
                    viewHolder.voteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (listOfSelectedFoods.size() == 5 && !listOfSelectedFoods.contains(getRef(position).getKey())) {
                                Toast.makeText(FoodVote.this, "Maximum 5 ételt választhatsz ki!", Toast.LENGTH_SHORT).show();
                                compoundButton.toggle();
                            } else {
                                if (b) {
                                    listOfSelectedFoods.add(getRef(position).getKey());
                                } else {
                                    listOfSelectedFoods.remove(getRef(position).getKey());
                                }
                                refreshTitle(listOfSelectedFoods.size());

                                if (listOfSelectedFoods.size() >= 2) {
                                    toolbarCheckMarkImg.setImageResource(R.drawable.ic_check_circle_white_24dp);
                                    toolbarCheckMarkImg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startVote();

                                            voteReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                                                        Vote vote = postSnapshot.getValue(Vote.class);

                                                        System.out.println(vote.getPrincipal());

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    System.out.println("The DB read failed");
                                                }
                                            });
                                        }
                                    });
                                }else{
                                    toolbarCheckMarkImg.setOnClickListener(null);
                                    toolbarCheckMarkImg.setImageResource(R.drawable.ic_check_circle_black_24dp);
                                }
                            }
                        }
                    });
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            viewHolder.voteCheckBox.toggle();

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
        } else if (id == R.id.food_vote_menu) {
            Intent foodListIntent = new Intent(FoodVote.this, FoodVote.class);
            startActivity(foodListIntent);
        } else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.food_list_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refreshTitle(int numberOfSelectedItem) {
        titleWithCounter.setText("Szavazás létrehozása (" + numberOfSelectedItem + ")");
    }

    public void startVote(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = format.format( new Date()   );

        newVoteReference = voteReference.child(dateString);

        Vote vote = new Vote();
        List<VoteItem> voteItems = new ArrayList<>();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Food");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            for(String foodName : listOfSelectedFoods) {
                                if(issue.getKey().equals(foodName)) {
                                    Food food = issue.getValue(Food.class);
                                    VoteItem voteItem = new VoteItem();

                                    voteItem.setFoodName(issue.getKey());
                                    voteItem.setEncodedPicture(food.getEncodedPicture());
                                    voteItem.setNumberOfVote(0);
                                    voteItems.add(voteItem);
                                }
                            }
                        }
                        System.out.println("----------SIKER---------");
                        vote.setVoteItems(voteItems);
                        vote.setPrincipal(Common.getCurrentUserName());
                        vote.setPrincipal("True");

                        newVoteReference.setValue(vote);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



    }

}
