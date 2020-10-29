package com.example.riviore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String title;
    private FirebaseAuth mAuth;
    TextView titletv, dischargeet, pollutionet, gainriseet;
    private static final String TAG = "DetailsActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dischargeet = findViewById(R.id.discharge_et);
        pollutionet = findViewById(R.id.pollution_et);
        gainriseet = findViewById(R.id.gainrise_et);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_nav);

        BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.map_menu:
                                return true;
                            case R.id.profile_menu:
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                }
                                return true;
                        }
                        return false;
                    }
                };

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        TextView titletv = findViewById(R.id.name_tv);
        if (getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
            titletv.setText(title);
        }
        Log.d(TAG, "onCreate: start");
        setDetails();
        Log.d(TAG, "onCreate: finish");
    }

    private void setDetails() {
        Log.d(TAG, "setDetails: running");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Rivers")
                .document(title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d(TAG, "onComplete: running");
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Log.d(TAG, "onComplete: found doc");
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "onComplete: " + documentSnapshot.getString("discharge"));
                                dischargeet.setText(documentSnapshot.getString("discharge"));
                                pollutionet.setText(documentSnapshot.getString("pollution"));
                                gainriseet.setText(documentSnapshot.getString("gainrise"));
                            }
                            else {
                                Toast.makeText(DetailsActivity.this, "Details not found", Toast.LENGTH_SHORT).show();
//                                Log.d(TAG, "onComplete: " + task.getException().getMessage());
                            }
                        }
                        else {
//                            Log.d(TAG, "onComplete: " + task.getException().getMessage());
                        }
                    }
                });
    }
}
