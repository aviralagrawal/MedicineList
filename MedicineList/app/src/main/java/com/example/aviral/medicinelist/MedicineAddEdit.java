package com.example.aviral.medicinelist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

public class MedicineAddEdit extends AppCompatActivity {

    private String[] timings = {"Morning","Evening","Night"};
    String selected_timings;
    private EditText medicine_name, dosage;
    private Switch notifications;
    private ImageView imageHolder;
    static final int CAMERA_CAPTURE = 1;
    final int PIC_CROP = 3;
    private Bitmap bitmap;
    private Uri picUri;
    MedicineDetails medicine;
    DatabaseReference databaseReference;
    private Calendar calendar;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_add_edit);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        imageHolder = (ImageView)findViewById(R.id.captured_photo);
        imageHolder.setImageResource(R.drawable.combiflame);
        Button capturedImageButton = findViewById(R.id.camera_btn);
        capturedImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                File imageFile = new File(imageFilePath);
                picUri = Uri.fromFile(imageFile); // convert path to Uri
                takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT,  picUri );
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE);

            }
        });

        Spinner spin = (Spinner) findViewById(R.id.timings_spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_timings = timings[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,timings);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        medicine_name = findViewById(R.id.medicine_name);
        dosage = findViewById(R.id.dosage);
        notifications = findViewById(R.id.notificatons_switch);

        Button ok_btn = findViewById(R.id.okbtn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTextFromImage();
            }
        });

        Button submit_btn = findViewById(R.id.submitbtn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicineadd = new Intent(getApplicationContext(),MainActivity.class);
                sendData(medicine_name,dosage,dosage,selected_timings,notifications);
                if(notifications.isChecked())
                {
                    calendar = Calendar.getInstance();
                    if(selected_timings.equals("Morning"))
                    {
                        calendar.set(Calendar.HOUR_OF_DAY,9);
                        calendar.set(Calendar.MINUTE,45);
                        calendar.set(Calendar.SECOND,20);
                    }
                    else if(selected_timings.equals("Evening"))
                    {
                        calendar.set(Calendar.HOUR_OF_DAY,19);
                        calendar.set(Calendar.MINUTE,25);
                        calendar.set(Calendar.SECOND,20);
                    }
                    else
                    {
                        calendar.set(Calendar.HOUR_OF_DAY,21);
                        calendar.set(Calendar.MINUTE,0);
                        calendar.set(Calendar.SECOND,20);
                    }

                    Intent intent = new Intent(getApplicationContext(),Notification_receiver.class);
                    intent.putExtra("keyofitem",key);
                    intent.putExtra("data","Please take "+medicine_name.getText().toString());
                    int random = new Random().nextInt(100);
                    int id=random;
                    intent.putExtra("id",id);
                    PendingIntent pendingIntent =
                            PendingIntent.getBroadcast(getApplicationContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                }
                startActivity(medicineadd);
            }
        });

    }

    public void sendData(EditText medicine_name,EditText dosage, EditText amount_purchased,String selected_timings,Switch notifications){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        DatabaseReference newChildRef = databaseReference.push();
        key = newChildRef.getKey();
        MedicineDetails medicineDetails = new MedicineDetails(key,medicine_name.getText().toString(),dosage.getText().toString(),
                amount_purchased.getText().toString(),selected_timings,notifications.isChecked());

        databaseReference.child(key).setValue(medicineDetails);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_CAPTURE){
//                //get the returned data
//                Bundle extras = data.getExtras();
//                //get the cropped bitmap
//                bitmap = (Bitmap) extras.get("data");
//                //display the returned cropped image
//                imageHolder.setImageBitmap(bitmap);

                //get the Uri for the captured image
                Uri uri = picUri;
                //carry out the crop operation
                performCrop();
            }
            else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                bitmap = (Bitmap) extras.get("data");
                //display the returned cropped image
                imageHolder.setImageBitmap(bitmap);
            }
        }
    }

    private void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void getTextFromImage(){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(textRecognizer.isOperational())
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);

            StringBuilder sb = new StringBuilder();

            for(int i=0;i<items.size();i++)
            {
                TextBlock myitem = items.valueAt(i);
                sb.append(myitem.getValue());
            }
            medicine_name.setText(sb.toString());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Could not get the Text", Toast.LENGTH_SHORT).show();
        }
    }

}
