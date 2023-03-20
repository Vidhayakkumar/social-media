package com.onechance.socialmedia.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onechance.socialmedia.Adapter.NotificationAdapter;
import com.onechance.socialmedia.Model.Notification;
import com.onechance.socialmedia.R;

import java.util.ArrayList;

public class Notification2Fragment extends Fragment {

     ArrayList<Notification> list=new ArrayList<>();
     RecyclerView recyclerView;
    public Notification2Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification2, container, false);

        recyclerView=view.findViewById(R.id.Notification2Rv);

//        list.add(new Notification(R.drawable.rohit," <b>vihdayak kumar</b> Like your your photo and comments nic pic","9:20"));
//        list.add(new Notification(R.drawable.rohit,"<b>vihdaysk </b> Like your your photo and comments nic pic","9:20"));
//        list.add(new Notification(R.drawable.rohit,"<b>vihdaysk </b> Like your your photo and comments nic pic","9:20"));
//        list.add(new Notification(R.drawable.rohit,"vihdaysk","9:20"));
//        list.add(new Notification(R.drawable.rohit,"vihdaysk","9:20"));
//        list.add(new Notification(R.drawable.rohit,"vihdaysk","9:20"));
//        list.add(new Notification(R.drawable.rohit,"vihdaysk","9:20"));
//        list.add(new Notification(R.drawable.virat,"vihdaysk","9:20"));
//        list.add(new Notification(R.drawable.virat,"vihdaysk","9:20"));
//        list.add(new Notification(R.drawable.virat,"vihdaysk","9:20"));
//        list.add(new Notification(R.drawable.hardik,"vihdaysk","9:20"));
//        list.add(new Notification(R.drawable.rohit,"vihdaysk","9:20"));

        NotificationAdapter adapter=new NotificationAdapter(list,getContext());

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        return view;
    }
}