package com.example.aviral.medicinelist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DailyRoutine extends AppCompatActivity {

    private ArrayList<String> medicineNames = new ArrayList<>();
    private ArrayList<String> dosage = new ArrayList<>();
    private ArrayList<String> amount_purachased = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    DatabaseReference databaseReference;
    MedicineViewAdapter adapter;
    Time time = new Time();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_routine);

        time.setToNow();
        //Toast.makeText(this, time.hour+" "+time.minute, Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        receivedata();
    }

    private void receivedata() {

        databaseReference.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MedicineDetails value = dataSnapshot1.getValue(MedicineDetails.class);
                    if(value.getTimings().equals("Morning")&&time.hour<9&&Integer.valueOf(value.getDosage())>0){
                        medicineNames.add(value.getMedicine_names());
                        dosage.add(value.getDosage());
                        amount_purachased.add(value.getAmount_purchased());
                        keys.add(value.getKey());
                        adapter.notifyDataSetChanged();
                    }
                    else if(value.getTimings().equals("Evening")&&time.hour<15&&Integer.valueOf(value.getDosage())>0){
                        medicineNames.add(value.getMedicine_names());
                        dosage.add(value.getDosage());
                        amount_purachased.add(value.getAmount_purchased());
                        keys.add(value.getKey());
                        adapter.notifyDataSetChanged();
                    }
                    else if(value.getTimings().equals("Night")&&time.hour<24&&Integer.valueOf(value.getDosage())>0)
                    {
                        medicineNames.add(value.getMedicine_names());
                        dosage.add(value.getDosage());
                        amount_purachased.add(value.getAmount_purchased());
                        keys.add(value.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initRecyclerView();
    }

    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_daily_routine);
        adapter = new MedicineViewAdapter(this,medicineNames,dosage,amount_purachased,keys);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
