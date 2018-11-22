package com.turkcellgroup38.kanka.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.turkcellgroup38.kanka.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button  btnLogin;
    private TextView tvForgetpassword, tvRegister;
    private SharedPreferences preferences;
    public static FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgetpassword = findViewById(R.id.tvforgetpassword);
        tvRegister = findViewById(R.id.tvRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        if (getIntent().getIntExtra("status", 1) == 0){
            firebaseAuth.signOut();
        }else if (firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userid", preferences.getString("userid", ""));
            startActivity(intent);
            finish();
        }


    }

    public void login(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Lütfen eposta adresinizi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Lütfen şifrenizi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                    String userid = firebaseAuth.getCurrentUser().getUid();
                    preferences.edit().putString("userid", userid).apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userid", userid);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Tekar deneyin!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void goregister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}
