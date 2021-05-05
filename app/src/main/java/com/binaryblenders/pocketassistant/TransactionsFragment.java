package com.binaryblenders.pocketassistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TransactionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<TransactionItems> list=new ArrayList<>();
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private Spinner spinner;
    private ArrayList<String> sources = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.transactions_fragment,container,false);

        recyclerView=root.findViewById(R.id.transactions_recycler);
        spinner=root.findViewById(R.id.source);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.custom_source_dropdown_item,sources);
        spinner.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        TransactionAdapter recyclerAdapter=new TransactionAdapter(getContext(),list);
        recyclerView.setAdapter(recyclerAdapter);

        sources.add("ALL");


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

                    if (! sources.contains(source)) sources.add(source);

                    String type = item.get("transaction_type").toString();
                    Date date = ((Timestamp) item.get("time")).toDate();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    String time = formatter.format(date);
                    list.add(new TransactionItems(amount,purpose,source,time,type));
                }
                recyclerAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<TransactionItems> temp_list = new ArrayList<>();
                if (sources.get(position).contentEquals("ALL")){
                    temp_list = list;
                }
                else {
                    for(TransactionItems transactionItem : list){
                        if (transactionItem.getSource().contentEquals(sources.get(position))){
                            temp_list.add(transactionItem);
                        }
                    }
                }
                TransactionAdapter temp_adapter = new TransactionAdapter(getContext(),temp_list);
                recyclerView.setAdapter(temp_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        return root;
    }
}
