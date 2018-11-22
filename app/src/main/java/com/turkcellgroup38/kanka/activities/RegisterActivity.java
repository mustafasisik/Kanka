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
import com.google.firebase.database.FirebaseDatabase;
import com.turkcellgroup38.kanka.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etAdSoyad, etEmail, etPassword1 , etPassword2;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("user", MODE_PRIVATE);


        etEmail = findViewById(R.id.etEmail);
        etAdSoyad = findViewById(R.id.etName);
        etPassword1 = findViewById(R.id.etPassword1);
        etPassword2 = findViewById(R.id.etPassword2);
        btnRegister= findViewById(R.id.btnRegister);
        tvLogin= findViewById(R.id.tvLogin);
    }

    public void register(View view) {
        final String name = etAdSoyad.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        String password1 = etPassword1.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Lütfen eposta adresinizi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        } if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Lütfen Ad ve Soyadı giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password1)){
            Toast.makeText(this, "Lütfen şifrenizi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }if (!password1.equals(password2)){
            Toast.makeText(this, "Şifreler Eşleşmiyor.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                    String userid = firebaseAuth.getCurrentUser().getUid();
                    preferences.edit().putString("userid", userid).apply();
                    preferences.edit().putString("email", email).apply();
                    preferences.edit().putString("name", name).apply();
                    setUser(userid, email, name);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("userid", userid);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUser(String userid, String email, String name) {
        Map<String, Object> userinfo = new HashMap<>();
        userinfo.put("name", name);
        userinfo.put("email", email);
        userinfo.put("phone", "0555 555 55 55");
        userinfo.put("age", "18");
        userinfo.put("blood", "0");
        userinfo.put("rh", "-");
        userinfo.put("location", "istanbul");
        userinfo.put("image", "https://i.hizliresim.com/JOPJvj.png");
        FirebaseDatabase.getInstance().getReference().child("users").child(userid).updateChildren(userinfo);

    }

    public void gologin(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
