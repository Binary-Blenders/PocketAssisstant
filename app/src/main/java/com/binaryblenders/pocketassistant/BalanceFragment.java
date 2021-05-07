package com.binaryblenders.pocketassistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BalanceFragment extends Fragment {

    private final String userId;
    private RecyclerView balancerecyclerView;
    private ArrayList<BalanceDataModel> list=new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public BalanceFragment(String userId) {
        this.userId = userId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.balance_fragment,container,false);

        balancerecyclerView=root.findViewById(R.id.balance_recyclerview);
        balancerecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        balancerecyclerView.setHasFixedSize(true);

        BalanceAdapter balanceAdapter=new BalanceAdapter(getContext(),list);
        balancerecyclerView.setAdapter(balanceAdapter);


        firestore.collection(userId).document("Balance").collection("balance").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error==null){
                    list.clear();
                    for (DocumentSnapshot documentSnapshot : value){
                        BalanceDataModel balanceObject = documentSnapshot.toObject(BalanceDataModel.class);
                        balanceObject.setSource(documentSnapshot.getId());
                        list.add(balanceObject);
                    }
                    balanceAdapter.notifyDataSetChanged();
                }
            }
        });



        return root;

    }
}
