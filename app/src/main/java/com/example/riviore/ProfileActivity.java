package com.example.riviore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
    }

    public void signUp(View view) {
        TextInputEditText emailet = findViewById(R.id.email_et);
        TextInputEditText passwordet = findViewById(R.id.password_et);
        final TextInputEditText nameet = findViewById(R.id.name_et);
        TextInputEditText locationet = findViewById(R.id.location_et);
        String email = emailet.getText().toString();
        String password = passwordet.getText().toString();
        final String name = nameet.getText().toString();
        String location = locationet.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Map<String, Object> user_details = new HashMap<>();
        user_details.put("name", name);
        user_details.put("email", email);
        user_details.put("location", location);
        user_details.put("password", password);
        user_details.put("official", false);

        if (TextUtils.isEmpty(email) || (TextUtils.isEmpty(password)) || (TextUtils.isEmpty(name))) {
            Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                DocumentReference doc = db.collection("Users").document();
                                user_details.put("uid", user.getUid());
                                user_details.put("document_id", doc.getId());
                                doc.set(user_details);
                                Toast.makeText(ProfileActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                user.updateProfile(profileUpdate);
                                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void goToLogin(View view) {
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
    }

    public void goToOfficialLogin(View view) {
        startActivity(new Intent(ProfileActivity.this, OfficialLoginActivity.class));
    }
}
