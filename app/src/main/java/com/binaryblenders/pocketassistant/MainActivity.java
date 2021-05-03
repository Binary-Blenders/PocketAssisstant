package com.binaryblenders.pocketassistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button passbook,addMoney,addDone,spendMoney,addClose,spendDone,spendClose;
    EditText addAmount,spendAmount,spendReason;
    AutoCompleteTextView addSource,spendSource;
    CardView addCard,spendcard;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passbook=findViewById(R.id.passbook);

        addCard=findViewById(R.id.add_card);
        addMoney = findViewById(R.id.add_money);
        addAmount=findViewById(R.id.add_amount);
        addDone=findViewById(R.id.add_done);
        addSource=findViewById(R.id.add_source);
        addClose = findViewById(R.id.add_close);

        spendcard=findViewById(R.id.spend_card);
        spendMoney=findViewById(R.id.spend_money);
        spendAmount=findViewById(R.id.spend_amount);
        spendDone=findViewById(R.id.spend_done);
        spendSource=findViewById(R.id.spend_source);
        spendClose = findViewById(R.id.spend_close);
        spendReason=findViewById(R.id.spend_reason);


        spendSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spendSource.showDropDown();
            }
        });





        passbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PassbookActivity.class);
                startActivity(intent);
            }
        });

        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard.setVisibility(View.VISIBLE);
                spendMoney.setVisibility(View.GONE);
                passbook.setVisibility(View.GONE);
            }
        });

        addClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard.setVisibility(View.GONE);
                spendMoney.setVisibility(View.VISIBLE);
                passbook.setVisibility(View.VISIBLE);
                addAmount.setText("");
                addSource.setText("");
            }
        });


        ArrayList<String> sources= new ArrayList<>();
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_source_dropdown_item,sources);
        addSource.setAdapter(sourceAdapter);
        spendSource.setAdapter(sourceAdapter);
        addSource.setThreshold(1);
        spendSource.setThreshold(0);


        firestore.collection("Aishwarya").document("Balance").collection("balance").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (error==null){
                    sources.clear();
                    for (DocumentSnapshot document : snapshots.getDocuments()){
                        sources.add(document.getId());
                    }
                    sourceAdapter.notifyDataSetChanged();
                }
            }
        });

        addDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = false;
                if (addAmount.getText().toString().isEmpty() | Long.valueOf(addAmount.getText().toString()) == 0 | addSource.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    for (String source : sources) {
                        if (addSource.getText().toString().contentEquals(source)) {
                            firestore.collection("Aishwarya").document("Balance").collection("balance").document(source).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    long added, balance;
                                    added = documentSnapshot.getLong("added");
                                    balance = documentSnapshot.getLong("balance");
                                    added = added + Long.valueOf(addAmount.getText().toString());
                                    balance += Long.valueOf(addAmount.getText().toString());
                                    firestore.collection("Aishwarya").document("Balance").collection("balance").document(source).update("added", added, "balance", balance);

                                    Timestamp time = Timestamp.now();
                                    HashMap<String,Object> addMap = new HashMap<>();
                                    addMap.put("amount",Long.valueOf(addAmount.getText().toString()));
                                    addMap.put("source",addSource.getText().toString());
                                    addMap.put("time",time);
                                    addMap.put("transaction_type","credit");
                                    firestore.collection("Aishwarya").document("Transactions").collection("transactions").document().set(addMap);
                                }
                            });
                            isAdded = true;
                            break;
                        }
                    }
                    if (!isAdded) {
                        HashMap<String, Object> map = new HashMap<>();
                        long add = Long.valueOf(addAmount.getText().toString());
                        map.put("added", add);
                        map.put("balance", add);
                        firestore.collection("Aishwarya").document("Balance").collection("balance").document(addSource.getText().toString()).set(map);

                        Timestamp time = Timestamp.now();
                        HashMap<String,Object> addMap = new HashMap<>();
                        addMap.put("amount",Long.valueOf(addAmount.getText().toString()));
                        addMap.put("source",addSource.getText().toString());
                        addMap.put("time",time);
                        addMap.put("transaction_type","credit");
                        firestore.collection("Aishwarya").document("Transactions").collection("transactions").document().set(addMap);
                    }
                }

            }
        });

        spendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spendcard.setVisibility(View.VISIBLE);
                addMoney.setVisibility(View.GONE);
                passbook.setVisibility(View.GONE);
            }
        });

        spendClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spendcard.setVisibility(View.GONE);
                addMoney.setVisibility(View.VISIBLE);
                passbook.setVisibility(View.VISIBLE);
                spendAmount.setText("");
                spendSource.setText("");
            }
        });

        spendDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spendAmount.getText().toString().isEmpty() | Long.valueOf(spendAmount.getText().toString()) == 0 | spendSource.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    firestore.collection("Aishwarya").document("Balance").collection("balance").document(spendSource.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            long to_spend=Long.valueOf(spendAmount.getText().toString());
                            long available_balance=documentSnapshot.getLong("balance");
                            if(available_balance<to_spend){
                                Toast.makeText(MainActivity.this, "Not enough balance!", Toast.LENGTH_SHORT).show();
                                spendClose.callOnClick();
                            }
                            else{
                                available_balance-=to_spend;
                                firestore.collection("Aishwarya").document("Balance").collection("balance").document(spendSource.getText().toString()).update("balance",available_balance);

                                Timestamp time = Timestamp.now();
                                HashMap<String,Object> spendMap = new HashMap<>();
                                spendMap.put("amount",to_spend);
                                spendMap.put("purpose",spendReason.getText().toString());
                                spendMap.put("source",spendSource.getText().toString());
                                spendMap.put("time",time);
                                spendMap.put("transaction_type","debit");
                                firestore.collection("Aishwarya").document("Transactions").collection("transactions").document().set(spendMap);

                                spendClose.callOnClick();
                            }


                        }
                    });
                }

            }
        });

    }



}
