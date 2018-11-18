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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BLOOD_TYPE = "bloodtype";
    private static final String RH = "rh";

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


    // TODO: Rename and change types of parameters
    private String bloodtype, rh;



    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String bloodtype, String rh) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(BLOOD_TYPE, bloodtype);
        args.putString(RH, rh);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bloodtype = getArguments().getString(BLOOD_TYPE);
            rh = getArguments().getString(RH);
        }
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
        recyclerView = view.findViewById(R.id.rvAnnouncements);
        announcementAdapter = new AnnouncementAdapter(activity, announcements, HomeFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(announcementAdapter);
        Log.d("AAA", bloodtype);
        Log.d("BBB", rh);

        reference.child("news").child(bloodtype).child(rh).addValueEventListener(new ValueEventListener() {
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
            Log.d("NAMEEE", name);
            String message = String.valueOf(ds.child("message").getValue());
            String location = String.valueOf(ds.child("location").getValue());
            String image = String.valueOf(ds.child("image").getValue());
            String phone = String.valueOf(ds.child("phone").getValue());

            Announcement announcement = new Announcement(name, image, message, "00:00", bloodtype, rh, phone, location);
            announcements.add(announcement);
        }
        announcementAdapter.notifyDataSetChanged();

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
