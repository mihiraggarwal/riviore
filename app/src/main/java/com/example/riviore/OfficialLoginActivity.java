package com.example.riviore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class OfficialLoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    String selected_text;
    Spinner spinner;
    String email, password;
    private FirebaseAuth mAuth;
    private static final String TAG = "OfficialLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_login);

        mAuth = FirebaseAuth.getInstance();

        spinner = findViewById(R.id.login_sp);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_ar, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setPrompt("Choose department");

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
        startActivity(new Intent(OfficialLoginActivity.this, ProfileActivity.class));
    }

    public void login(View view) {
        if (selected_text == "Choose department"){
        }
        else {
            email = selected_text.toLowerCase();
            email = email.replace(" ", "");
            email += "@gmail.com";
            Log.d(TAG, "login: " + email);

            TextInputEditText passwordet = findViewById(R.id.password_et);
            password = passwordet.getText().toString();
            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(OfficialLoginActivity.this, MainActivity.class));
                                    Toast.makeText(OfficialLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(OfficialLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selected_text = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
    }
}
