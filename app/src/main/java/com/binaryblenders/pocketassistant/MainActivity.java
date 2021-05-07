package com.binaryblenders.pocketassistant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String accessToken = "542958517764-smjnudcunlu1gmq84deugp4ls5nmlpvv.apps.googleusercontent.com";
    private static final int RC_SIGN_IN = 100;
    private static String userId;

    Button passbook,addMoney,addDone,spendMoney,addClose,spendDone,spendClose;
    EditText addAmount,spendAmount,spendReason;
    AutoCompleteTextView addSource,spendSource;
    CardView addCard,spendcard;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    TextView total_balance,name;
    private ArrayAdapter<String> sourceAdapter;
    private Toolbar toolbar;
    private GoogleSignInClient user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passbook=findViewById(R.id.passbook);
        total_balance = findViewById(R.id.total_balance);
        name = findViewById(R.id.name);

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


        userId = getIntent().getStringExtra("id");
        name.setText("Hello " +getIntent().getStringExtra("name") + "!!!");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        user = GoogleSignIn.getClient(getApplicationContext(),gso);


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
                intent.putExtra("id",userId);
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
        sourceAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_source_dropdown_item,sources);
        sourceAdapter.setNotifyOnChange(true);
        addSource.setAdapter(sourceAdapter);
        spendSource.setAdapter(sourceAdapter);
        addSource.setThreshold(1);
        spendSource.setThreshold(0);


        firestore.collection(userId).document("Balance").collection("balance").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (error==null){
                    sources.clear();
                    long balance = 0;
                    for (DocumentSnapshot document : snapshots){
                        sources.add(document.getId());
                        balance += document.getLong("balance");
                    }
                    total_balance.setText(String.valueOf(balance));
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

                            firestore.collection(userId).document("Balance").collection("balance").document(source).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    long added, balance;
                                    added = documentSnapshot.getLong("added");
                                    balance = documentSnapshot.getLong("balance");
                                    added = added + Long.valueOf(addAmount.getText().toString());
                                    balance += Long.valueOf(addAmount.getText().toString());
                                    firestore.collection(userId).document("Balance").collection("balance").document(source).update("added", added, "balance", balance);

                                    Timestamp time = Timestamp.now();
                                    HashMap<String,Object> addMap = new HashMap<>();
                                    addMap.put("amount",Long.valueOf(addAmount.getText().toString()));
                                    addMap.put("source",addSource.getText().toString());
                                    addMap.put("time",time);
                                    addMap.put("transaction_type","credit");
                                    firestore.collection(userId).document("Transactions").collection("transactions").document().set(addMap);
                                }
                            });
                            isAdded = true;
                            break;
                        }
                    }
                    if (!isAdded) {
                        firestore.collection(userId).get();

                        HashMap<String, Object> map = new HashMap<>();
                        long add = Long.valueOf(addAmount.getText().toString());
                        map.put("added", add);
                        map.put("balance", add);
                        firestore.collection(userId).document("Balance").collection("balance").document(addSource.getText().toString()).set(map);

                        Timestamp time = Timestamp.now();
                        HashMap<String,Object> addMap = new HashMap<>();
                        addMap.put("amount",Long.valueOf(addAmount.getText().toString()));
                        addMap.put("source",addSource.getText().toString());
                        addMap.put("time",time);
                        addMap.put("transaction_type","credit");
                        firestore.collection(userId).document("Transactions").collection("transactions").document().set(addMap);
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
                    firestore.collection(userId).document("Balance").collection("balance").document(spendSource.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            long to_spend=Long.valueOf(spendAmount.getText().toString());
                            long available_balance=documentSnapshot.getLong("balance");
                            if(available_balance<to_spend){
                                Toast.makeText(MainActivity.this, "Not enough balance!", Toast.LENGTH_SHORT).show();
                                spendClose.callOnClick();
                            }
                            else if(available_balance==to_spend){
                                firestore.collection(userId).document("Balance").collection("balance").document(spendSource.getText().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Timestamp time = Timestamp.now();
                                HashMap<String,Object> spendMap = new HashMap<>();
                                spendMap.put("amount",to_spend);
                                spendMap.put("purpose",spendReason.getText().toString());
                                spendMap.put("source",spendSource.getText().toString());
                                spendMap.put("time",time);
                                spendMap.put("transaction_type","debit");
                                firestore.collection(userId).document("Transactions").collection("transactions").document().set(spendMap);

                                spendClose.callOnClick();
                            }
                            else{
                                available_balance-=to_spend;
                                firestore.collection(userId).document("Balance").collection("balance").document(spendSource.getText().toString()).update("balance",available_balance);

                                Timestamp time = Timestamp.now();
                                HashMap<String,Object> spendMap = new HashMap<>();
                                spendMap.put("amount",to_spend);
                                spendMap.put("purpose",spendReason.getText().toString());
                                spendMap.put("source",spendSource.getText().toString());
                                spendMap.put("time",time);
                                spendMap.put("transaction_type","debit");
                                firestore.collection(userId).document("Transactions").collection("transactions").document().set(spendMap);

                                spendClose.callOnClick();
                            }


                        }
                    });
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_tool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                user.signOut();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);


    }
}
