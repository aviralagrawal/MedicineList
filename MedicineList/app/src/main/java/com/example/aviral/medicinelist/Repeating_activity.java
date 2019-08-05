package com.example.aviral.medicinelist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Repeating_activity extends AppCompatActivity {

    DatabaseReference databaseReference;
    MedicineDetails medicineDetails;
    int dosage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeating_activity);

        Button yes = findViewById(R.id.yes_btn);
        Button no = findViewById(R.id.no_btn);
        final String keyofitem = getIntent().getExtras().getString("key");
        //Toast.makeText(this, FirebaseAuth.getInstance().getUid(), Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());;
        databaseReference = databaseReference.child(keyofitem);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MedicineDetails medicineDetails = dataSnapshot.getValue(MedicineDetails.class);
                dosage = Integer.valueOf(medicineDetails.getDosage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/dosage",String.valueOf(dosage-1));
                databaseReference.updateChildren(childUpdates);
                startActivity(intent);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(intent);
            }
        });
    }
}
