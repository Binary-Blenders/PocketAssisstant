package com.binaryblenders.pocketassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewpager);

        MyViewpageAdapter viewpageAdapter=new MyViewpageAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(viewpageAdapter);

        String[] tabNames = {"Balance","Transactions"};
        new TabLayoutMediator(tabLayout,viewPager,(tab, position) -> tab.setText(tabNames[position])).attach();

    }
}