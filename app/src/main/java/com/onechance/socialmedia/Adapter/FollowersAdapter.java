package com.onechance.socialmedia.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onechance.socialmedia.Model.Follow;
import com.onechance.socialmedia.Model.User;
import com.onechance.socialmedia.R;
import com.onechance.socialmedia.databinding.ProfileSmBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {



    ArrayList<Follow> list;
    Context context;

    public FollowersAdapter(ArrayList<Follow> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.profile_sm,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Follow follow=list.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(follow.getFollowedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);

                        Picasso.get()
                                .load(Objects.requireNonNull(user).getProfileImage())
                                .placeholder(R.drawable.adduser)
                                .into(binding.profile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    ProfileSmBinding binding;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding=ProfileSmBinding.bind(itemView);


        }
    }
}
