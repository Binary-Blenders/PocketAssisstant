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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
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

//        list.add(new TransactionItems("400","11:09:23 02/09/2011","shopping","debit","wedding"));
//        list.add(new TransactionItems("500","11:09:23 02/04/2010","notebooks","credit","wedding"));
//        list.add(new TransactionItems("600","11:09:23 11/12/2011","dishwasher","debit","wedding"));


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        TransactionAdapter recyclerAdapter=new TransactionAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(recyclerAdapter);

        firebaseFirestore.collection("Aishwarya").document("Transactions").collection("transactions").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot record : queryDocumentSnapshots.getDocuments()){
                    HashMap<String,Object> item = (HashMap<String, Object>) record.getData();
                    String purpose;
                    String amount = String.valueOf(item.get("amount"));
                    try {
                        purpose = item.get("purpose").toString();
                    }
                    catch (Exception e){
                        purpose = "";
                    }
                    String source = item.get("source").toString();
                    String type = item.get("transaction_type").toString();
                    Date date = ((Timestamp) item.get("time")).toDate();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                    String time = formatter.format(date);
                    list.add(new TransactionItems(amount,purpose,source,time,type));
                }
                recyclerAdapter.notifyDataSetChanged();

            }
        });






    }
}