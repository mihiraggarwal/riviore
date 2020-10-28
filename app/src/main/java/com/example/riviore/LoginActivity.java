package com.example.riviore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String email, password;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    public void goToSignUp(View view) {
        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
    }

    public void login(View view) {
        TextInputEditText emailet = findViewById(R.id.email_et);
        TextInputEditText passwordet = findViewById(R.id.password_et);
        email = emailet.getText().toString();
        password = passwordet.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void goToOfficialLogin(View view) {
        startActivity(new Intent(LoginActivity.this, OfficialLoginActivity.class));
    }
}
