package com.turkcellgroup38.kanka.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.turkcellgroup38.kanka.R;
import com.turkcellgroup38.kanka.fragments.HomeFragment;
import com.turkcellgroup38.kanka.fragments.InformationsFragment;
import com.turkcellgroup38.kanka.fragments.ProfileFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private AHBottomNavigation bottomNavigation;
    private FragmentTransaction fragmentTransaction;
    public static String userid;
    public static ProgressBar progressBar;
    private String blood, rh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);
        userid = getIntent().getStringExtra("userid");

        setNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setNavigation(){
        bottomNavigation = findViewById(R.id.xbottom_navigation);
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorAccent));
        bottomNavigation.setInactiveColor(Color.WHITE);

        // Create items
        AHBottomNavigationItem item0 = new AHBottomNavigationItem(R.string.information, R.drawable.info, R.color.colorPrimary);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.home_page, R.drawable.home, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.profile, R.drawable.user, R.color.colorPrimary);

        // Add items
        bottomNavigation.addItem(item0);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.setCurrentItem(1);


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, HomeFragment.newInstance()).commit();
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (position) {
                    case 0:
                        fragmentTransaction.replace(R.id.fragment, InformationsFragment.newInstance("","")).commit();
                        break;
                    case 1:
                        fragmentTransaction.replace(R.id.fragment, HomeFragment.newInstance()).commit();
                        break;
                    case 2:
                        fragmentTransaction.replace(R.id.fragment, ProfileFragment.newInstance()).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_blood_demand) {
            openDemandDialog();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            getSharedPreferences("user", MODE_PRIVATE).edit().clear().apply();
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            login.putExtra("status", 0);
            startActivity(login);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDemandDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.announce_dialog);
        dialog.show();

        blood = "A";
        rh = "-";

        final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        RadioButton rbA = dialog.findViewById(R.id.rbA);
        RadioButton rbB = dialog.findViewById(R.id.rbB);
        RadioButton rbAB = dialog.findViewById(R.id.rbAB);
        RadioButton rb0 = dialog.findViewById(R.id.rb0);

        final ToggleButton tbRH = dialog.findViewById(R.id.tbRh);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.rb0:
                        blood = "0";
                        break;
                    case R.id.rbA:
                        blood = "A";
                        break;
                    case R.id.rbB:
                        blood = "B";
                        break;
                    case R.id.rbAB:
                        blood = "AB";
                        break;
                }
            }
        });

        final EditText etMessage = dialog.findViewById(R.id.etMessage);
        final EditText etLocation = dialog.findViewById(R.id.etLocation);

        Button buttonAnnounce = dialog.findViewById(R.id.buttonAnnounce);
        buttonAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString().trim();
                String location =etLocation.getText().toString().trim();
                if (TextUtils.isEmpty(message) ){
                    Toast.makeText(MainActivity.this, "lütfen Mesajınızı yazınız", Toast.LENGTH_SHORT).show();

                }  else if (TextUtils.isEmpty(location)){
                    Toast.makeText(MainActivity.this, "Lütfen Konum belirtiniz", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object>  map = new HashMap<>();
                    map.put("message",message);
                    map.put("location",location);
                    map.put("phone", "05555555555");
                    map.put("image", "https://ceotudent.com/wp-content/images/post/user-799/6cgynk.jpg");
                    map.put("name", "Mert Kaya");


                    if (tbRH.isChecked()){
                        rh = "+";
                    }else {
                        rh = "-";
                    }

                    FirebaseDatabase.getInstance().getReference().child("news").child(blood).child(rh).child(MainActivity.userid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Başarılı şekilde ilan paylaşıldı.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(MainActivity.this, "Lütfen yeniden deneyin.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });
    }
}
