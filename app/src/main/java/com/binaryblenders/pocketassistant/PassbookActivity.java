package com.binaryblenders.pocketassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PassbookActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String userId;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewpager);

        userId = getIntent().getStringExtra("id");

        MyViewpageAdapter viewpageAdapter=new MyViewpageAdapter(getSupportFragmentManager(),getLifecycle(),userId);
        viewPager.setAdapter(viewpageAdapter);

        String[] tabNames = {"Balance","Transactions"};
        new TabLayoutMediator(tabLayout,viewPager,(tab, position) -> tab.setText(tabNames[position])).attach();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.passbook_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear :
                firestore.collection(userId).document("Transactions").collection("transactions").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        for (DocumentSnapshot documentSnapshot : snapshots){
                            try {
                                documentSnapshot.getReference().delete();
                            }
                            catch (Exception e){
                                Toast.makeText(PassbookActivity.this, "Error deleting documents", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }
                        Toast.makeText(PassbookActivity.this, "Cleared Transactions", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.clear_bal:
                firestore.collection(userId).document("Balance").collection("balance").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        for(DocumentSnapshot documentSnapshot:snapshots){
                            try{
                                documentSnapshot.getReference().delete();
                            }
                            catch (Exception e){
                                Toast.makeText(PassbookActivity.this, "Error deleting documents", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }
                        Toast.makeText(PassbookActivity.this, "Cleared Balance", Toast.LENGTH_SHORT).show();
                    }
                });

        }

        return super.onOptionsItemSelected(item);
    }
}