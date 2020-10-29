package com.example.riviore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "UserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_nav);

        BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.map_menu:
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                return true;
                            case R.id.profile_menu:
                                return true;
                        }
                        return false;
                    }
                };

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        getUser();
    }

    public void logout(View view) {
        mAuth.signOut();
        startActivity(new Intent(UserActivity.this, MainActivity.class));
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void donate(View view) {
        startActivity(new Intent(UserActivity.this, DonateActivity.class));
    }

    private void getUser() {
        final TextInputEditText nameet = findViewById(R.id.name_et);
        final TextInputEditText locationet = findViewById(R.id.location_et);
        final TextInputEditText emailet = findViewById(R.id.email_et);
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                                nameet.setText(documentSnapshot.getString("name"));
                                emailet.setText(documentSnapshot.getString("email"));
                                locationet.setText(documentSnapshot.getString("location"));
                            }
                        }
                        else {
                            Toast.makeText(UserActivity.this, "Error getting details", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + task.getException().getMessage());
                        }
                    }
                });
    }
}
