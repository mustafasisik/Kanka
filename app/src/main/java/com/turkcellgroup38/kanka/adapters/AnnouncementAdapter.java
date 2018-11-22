package com.turkcellgroup38.kanka.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.turkcellgroup38.kanka.R;
import com.turkcellgroup38.kanka.models.Announcement;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Announcement> announcements;
    private View.OnClickListener onClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName, tvMessage, tvTime;
        public Button buttonCall;
        public ImageView ivProfile;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            ivProfile = v.findViewById(R.id.ivProfile);
            tvMessage = v.findViewById(R.id.tvMessage);
            tvTime = v.findViewById(R.id.tvTime);

            buttonCall = v.findViewById(R.id.buttonCall);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AnnouncementAdapter(Context context, ArrayList<Announcement> announcements, View.OnClickListener onClickListener) {
        this.context = context;
        this.announcements = announcements;
        this.onClickListener = onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        assert inflater != null;
        view = inflater.inflate(R.layout.simple_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Announcement announcement = announcements.get(position);
        holder.tvName.setText(announcement.getName());
        holder.tvMessage.setText(announcement.getMessage());
        holder.tvTime.setText(announcement.getTime());
        Glide.with(context).load(announcement.getImage()).into(holder.ivProfile);
        holder.buttonCall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + announcement.getPhone()));
                context.startActivity(intent);
            }
        });

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return announcements.size();
    }

}