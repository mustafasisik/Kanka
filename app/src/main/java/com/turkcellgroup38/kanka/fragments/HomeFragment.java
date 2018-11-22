package com.turkcellgroup38.kanka.fragments;

import android.content.Context;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turkcellgroup38.kanka.R;
import com.turkcellgroup38.kanka.activities.MainActivity;
import com.turkcellgroup38.kanka.adapters.AnnouncementAdapter;
import com.turkcellgroup38.kanka.models.Announcement;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private RadioGroup radioGroup;
    private RadioButton rbA, rbB, rbAB, rb0;

    private String blood="A", rh="-";

    private AppCompatActivity activity;
    @Override
    public void onAttach(Context context) {
        activity = (AppCompatActivity) context;
        super.onAttach(context);
    }


    private RecyclerView recyclerView;
    private AnnouncementAdapter announcementAdapter;
    private ArrayList<Announcement> announcements = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setView(view);
        return view;
    }

    private void setView(View view) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.rvAnnouncements);
        announcementAdapter = new AnnouncementAdapter(activity, announcements, HomeFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(announcementAdapter);

        radioGroup = view.findViewById(R.id.radioGroupMain);
        rbA = view.findViewById(R.id.rbA);
        rbB = view.findViewById(R.id.rbB);
        rbAB = view.findViewById(R.id.rbAB);
        rb0 = view.findViewById(R.id.rb0);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbA:
                        blood = "A";
                        break;
                    case R.id.rbB:
                        blood = "B";
                        break;
                    case R.id.rbAB:
                        blood = "AB";
                        break;
                    case R.id.rb0:
                        blood = "0";
                        break;
                }
                setReference();
            }
        });

        ToggleButton tbRh = view.findViewById(R.id.tbRh);

        tbRh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    rh = "+";
                }else {
                    rh = "-";
                }
                setReference();
            }
        });

        setReference();
    }

    private void setReference() {
        reference.child("news").child(blood).child(rh).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getAnnouncements(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getAnnouncements(DataSnapshot dataSnapshot) {
        announcements.clear();
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            String userid = ds.getKey();
            String name = String.valueOf(ds.child("name").getValue());
            String message = String.valueOf(ds.child("message").getValue());
            String location = String.valueOf(ds.child("location").getValue());
            String image = String.valueOf(ds.child("image").getValue());
            String phone = String.valueOf(ds.child("phone").getValue());

            Announcement announcement = new Announcement(name, image, message, "00:00", blood, rh, phone, location);
            announcements.add(announcement);
        }
        announcementAdapter.notifyDataSetChanged();
        MainActivity.progressBar.setVisibility(View.INVISIBLE);

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

    @Override
    public void onClick(View view) {

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
