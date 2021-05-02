package com.binaryblenders.pocketassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PassbookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<TransactionItems> list=new ArrayList<>();
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);

        recyclerView=findViewById(R.id.transactions_recycler);

        list.add(new TransactionItems("400","11:09:23 02/09/2011","shopping","debit","wedding"));
        list.add(new TransactionItems("500","11:09:23 02/04/2010","notebooks","credit","wedding"));
        list.add(new TransactionItems("600","11:09:23 11/12/2011","dishwasher","debit","wedding"));


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        TransactionAdapter recyclerAdapter=new TransactionAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(recyclerAdapter);

        firebaseFirestore.collection("Aishwarya").document("Transactions").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<String,Object> transaction = (HashMap<String, Object>) documentSnapshot.getData();
                for (String key: transaction.keySet()){
                    ArrayList<HashMap<String,Object>> tranfers = (ArrayList<HashMap<String, Object>>) documentSnapshot.get(key);
                    for(HashMap<String,Object> transfer : tranfers){
                        long amount = (long) transfer.get("amount");
                        Timestamp time = (Timestamp) transfer.get("time");
                        Date date = time.toDate();
                        String transaction_time = ""+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+" "+(date.getDay()+2)+"."+(date.getMonth()+1)+"."+(date.getYear()+1900);
                        String transaction_type = transfer.get("transaction_type").toString();
                        String transaction_for="";
                        if (transaction_type.contentEquals("debit"))
                            transaction_for = transfer.get("transaction_for").toString();

                        TransactionItems item = new TransactionItems(String.valueOf(amount),transaction_time,transaction_for,transaction_type,key);
                        list.add(item);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });






    }
}