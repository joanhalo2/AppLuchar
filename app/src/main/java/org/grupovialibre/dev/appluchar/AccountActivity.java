package org.grupovialibre.dev.appluchar;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.core.Repo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseReports;
    private RecyclerView mBlogList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Report> reports;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar my_toolbar  = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(my_toolbar);

        reports = new ArrayList<Report>();
        mDatabaseReports = FirebaseDatabase.getInstance().getReference().child("Reports");

        mAuth = FirebaseAuth.getInstance();

        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(AccountActivity.this,MainActivity.class));
        }else{

            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //System.out.println(dataSnapshot.getValue());
                    Map<String,String> map = (Map)dataSnapshot.getValue();
                    String u_name  = map.get("username");
                    getSupportActionBar().setTitle(u_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            getSupportActionBar().setIcon(R.drawable.ic_toolbar_user);
            //getSupportActionBar().setSubtitle(R.string.my_tb_subtitle);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newReportButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(AccountActivity.this,ReportActivity.class));
                startActivity(new Intent(AccountActivity.this,MapsActivity.class));

            }
        });


        //The reports are loaded;
        mDatabaseReports.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllReports(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAllReports(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getAllReports(DataSnapshot dataSnapshot){

        //for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            String reportID = dataSnapshot.getKey();

            Map<String,Object> map = (Map)dataSnapshot.getValue();

            String u_id  = (String) map.get("userID");
            String location = (String) map.get("location");
            String place = (String) map.get("place");
            String actors = (String) map.get("actors");
            String type = (String) map.get("type");
            String section = (String) map.get("section");
            String dateTime = (String) map.get("date");
            String tags = (String) map.get("tags");
            String desc = (String) map.get("desc");
            String username = (String) map.get("username");

            Report rescuedReport = new Report(
                    u_id,
                    location,
                    place,
                    actors,
                    type,
                    section,
                    dateTime,
                    tags,
                    desc,
                    username
            );

            rescuedReport.setReportID(reportID);


            reports.add(rescuedReport);
            recyclerViewAdapter = new RecyclerViewAdapter(AccountActivity.this, reports);
            mBlogList.setAdapter(recyclerViewAdapter);
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_1:
                Toast.makeText(AccountActivity.this,"Option 1 clicked!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_2:
                Toast.makeText(AccountActivity.this,"Option 2 clicked!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_3:
                logOut();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(AccountActivity.this,MainActivity.class));
    }
}
