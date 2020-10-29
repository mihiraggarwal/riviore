package com.example.riviore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.Document;

import java.util.HashMap;
import java.util.Map;

public class EditDetailsActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String title;
    private FirebaseAuth mAuth;
    TextView titletv, dischargeet, pollutionet, gainriseet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

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

        titletv = findViewById(R.id.name_tv);
        if (getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
            titletv.setText(title);
        }
	setDetails();
    }

    private void setDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference document = db.collection("Rivers").document(title);
        document.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                dischargeet.setText(documentSnapshot.getString("discharge"));
                                pollutionet.setText(documentSnapshot.getString("pollution"));
                                gainriseet.setText(documentSnapshot.getString("gainrise"));
                            }
                            else {
                                Toast.makeText(EditDetailsActivity.this, "Details not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void update(View view) {
        dischargeet = findViewById(R.id.discharge_et);
        pollutionet = findViewById(R.id.pollution_et);
        gainriseet = findViewById(R.id.gainrise_et);
        String discharge = dischargeet.getText().toString();
        String pollution = pollutionet.getText().toString();
        String gainrise = gainriseet.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Map<String, Object> river_details = new HashMap<>();
        river_details.put("discharge", discharge);
        river_details.put("pollution", pollution);
        river_details.put("gainrise", gainrise);
        river_details.put("name", title);

        db.collection("Rivers")
                .document(title)
                .set(river_details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditDetailsActivity.this, "River details updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditDetailsActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(EditDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
