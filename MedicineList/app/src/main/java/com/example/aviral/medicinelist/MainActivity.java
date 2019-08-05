package com.example.aviral.medicinelist;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> medicineNames = new ArrayList<>();
    private ArrayList<String> dosage = new ArrayList<>();
    private ArrayList<String> amount_purachased = new ArrayList<>();
    private ArrayList<MedicineDetails> listmedicine = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    DatabaseReference databaseReference;
    MedicineViewAdapter adapter;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;

    Button logout_button;
    Button profile;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        logout_button = (Button) findViewById(R.id.logout_btn);
        profile=(Button) findViewById(R.id.profile_btn);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        account=GoogleSignIn.getLastSignedInAccount(this);


        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                intent.putExtra("name",account.getDisplayName());
                intent.putExtra("email",account.getEmail());
                intent.putExtra("id",account.getId());
                intent.putExtra("url",account.getPhotoUrl().toString());
                startActivity(intent);
            }
        });




        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MedicineAddEdit.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.daily_remaining).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daily_routine_intent = new Intent(getApplicationContext(),DailyRoutine.class);
                startActivity(daily_routine_intent);
            }
        });
        findViewById(R.id.history_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daily_history_intent = new Intent(getApplicationContext(),History.class);
                startActivity(daily_history_intent);
            }
        });
        receivedata();
    }


    private void receivedata() {

        databaseReference.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MedicineDetails value = dataSnapshot1.getValue(MedicineDetails.class);
                    if(Integer.valueOf(value.getDosage())>0)
                    {
                        listmedicine.add(value);
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new MedicineViewAdapter(this,medicineNames,dosage,amount_purachased,keys);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
