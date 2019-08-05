package com.example.aviral.medicinelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    TextView Name;
    TextView Email;
    TextView Id;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Name=(TextView)findViewById(R.id.textview1);
        Email=(TextView)findViewById(R.id.textview2);
        Id=(TextView)findViewById(R.id.textview3);
        img=(ImageView)findViewById(R.id.imageview);

        Intent intent=getIntent();
        String name =intent.getStringExtra("name");
        String email =intent.getStringExtra("email");
        String id =intent.getStringExtra("id");
        String url=intent.getStringExtra("url");
        Picasso.with(this).load(url).resize(600,600).into(img);

        Name.setText(name);
        Email.setText(email);
        Id.setText(id);
    }
}
