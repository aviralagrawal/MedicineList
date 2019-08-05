package com.example.aviral.medicinelist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateMedicineDetails extends AppCompatActivity {

    private TextView medicine_name,amount_purchased,notifications,timings;
    private EditText dosage, added_amount;
    private Button dosage_update_btn, dosage_add_btn;
    DatabaseReference databaseReference;
    private int new_dosage;
    private int previous_amount;
    private int new_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicine_details);

        medicine_name = findViewById(R.id.final_medicine_name);
        amount_purchased = findViewById(R.id.final_amount_purchased);
        notifications = findViewById(R.id.notificationsdetails);
        timings = findViewById(R.id.timings_details);
        dosage = findViewById(R.id.changed_dosage);
        dosage_update_btn = findViewById(R.id.update_dosage_btn);
        dosage_add_btn = findViewById(R.id.added_dosage_btn);
        added_amount = findViewById(R.id.added_amount);


        String keyofitem = getIntent().getExtras().getString("key");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());;
        databaseReference = databaseReference.child(keyofitem);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MedicineDetails medicineDetails = dataSnapshot.getValue(MedicineDetails.class);
                medicine_name.setText("Medicine name "+medicineDetails.getMedicine_names());
                amount_purchased.setText("Amount originally purchased "+medicineDetails.getAmount_purchased());
                notifications.setText("Notification "+medicineDetails.getNotifications().toString());
                timings.setText("Timings of the medicine "+medicineDetails.getTimings());
                dosage.setText(medicineDetails.getDosage());
                previous_amount = Integer.valueOf(medicineDetails.getAmount_purchased());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dosage_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_dosage = Integer.valueOf(dosage.getText().toString());
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/dosage",String.valueOf(new_dosage));
                databaseReference.updateChildren(childUpdates);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        dosage_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_dosage = Integer.valueOf(dosage.getText().toString());
                new_dosage = new_dosage+Integer.valueOf(added_amount.getText().toString());
                new_amount = previous_amount+Integer.valueOf(added_amount.getText().toString());
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/dosage",String.valueOf(new_dosage));
                childUpdates.put("/amount_purchased",String.valueOf(new_amount));
                databaseReference.updateChildren(childUpdates);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
