package com.example.aviral.medicinelist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private ArrayList<String> medicineNames = new ArrayList<>();
    private ArrayList<String> dosage = new ArrayList<>();
    private ArrayList<String> amount_purachased = new ArrayList<>();
    DatabaseReference databaseReference;
    HistoryViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        receivedata();
    }

    private void receivedata() {

        databaseReference.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MedicineDetails value = dataSnapshot1.getValue(MedicineDetails.class);
                    medicineNames.add(value.getMedicine_names());
                    dosage.add(value.getDosage());
                    amount_purachased.add(value.getAmount_purchased());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initRecyclerView();
    }
    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.history_recycler_view);
        adapter = new HistoryViewAdapter(this,medicineNames,dosage,amount_purachased);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
