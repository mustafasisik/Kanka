package com.turkcellgroup38.kanka.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turkcellgroup38.kanka.R;
import com.turkcellgroup38.kanka.activities.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private EditText etName, etPhone, etBloodType, etRh, etAge, etEmail, etLocation;
    private ImageView ivProfile, ivBanner;
    private Button buttonUpdate;
    private AppCompatActivity activity;
    @Override
    public void onAttach(Context context) {
        activity = (AppCompatActivity) context;
        super.onAttach(context);
    }

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setView(view);
        return view;
    }

    private void setView(View view) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etBloodType = view.findViewById(R.id.etBloodType);
        etRh = view.findViewById(R.id.etRh);
        etAge = view.findViewById(R.id.etAge);
        etEmail = view.findViewById(R.id.etEmail);
        etLocation = view.findViewById(R.id.etLocation);

        ivProfile = view.findViewById(R.id.ivProfile);
        ivBanner = view.findViewById(R.id.ivbanner);


        buttonUpdate = view.findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String blood = etBloodType.getText().toString().trim();
                String rh = etRh.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String location = etLocation.getText().toString().trim();
                map.put("name", name);
                map.put("phone", phone);
                map.put("blood", blood);
                map.put("rh", rh);
                map.put("age", age);
                map.put("email", email);
                map.put("location", location);


                FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(activity, "Başarılı Şekilde Güncellendi.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity, "Lütfen yeniden deneyin!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = String.valueOf(dataSnapshot.child("name").getValue());
                String age = String.valueOf(dataSnapshot.child("age").getValue());
                String blood = String.valueOf(dataSnapshot.child("blood").getValue());
                String rh = String.valueOf(dataSnapshot.child("rh").getValue());
                String phone = String.valueOf(dataSnapshot.child("phone").getValue());
                String email = String.valueOf(dataSnapshot.child("email").getValue());
                String location = String.valueOf(dataSnapshot.child("location").getValue());
                String image = String.valueOf(dataSnapshot.child("image").getValue());

                etName.setText(name);
                etAge.setText(age);
                etPhone.setText(phone);
                etRh.setText(rh);
                etBloodType.setText(blood);
                etEmail.setText(email);
                etLocation.setText(location);
                Glide.with(activity).load(image).into(ivProfile);
                MainActivity.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
