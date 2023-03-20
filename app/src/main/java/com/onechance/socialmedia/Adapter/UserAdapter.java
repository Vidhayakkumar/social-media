package com.onechance.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onechance.socialmedia.Model.Follow;
import com.onechance.socialmedia.Model.User;
import com.onechance.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    ArrayList<User> list;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = list.get(position);
        Picasso.get()
                .load(user.getProfileImage())
                .placeholder(R.drawable.adduser)
                .into(holder.profile);

        holder.name.setText(user.getName());
        holder.profession.setText(user.getProfession());

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(user.getUserId())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                            holder.btnFollow.setText("Following");
                            holder.btnFollow.setTextColor(context.getResources().getColor(R.color.black));
                            holder.btnFollow.setEnabled(false);

                        }else {
                            holder.btnFollow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Follow follow = new Follow();
                                    follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    follow.setFollowedAt(new Date().getTime());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(user.getUserId())
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(follow)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Users")
                                                            .child(user.getUserId())
                                                            .child("followersCount")
                                                            .setValue(user.getFollowersCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                                                                    holder.btnFollow.setText("Following");
                                                                    holder.btnFollow.setTextColor(context.getResources().getColor(R.color.black));
                                                                    holder.btnFollow.setEnabled(false);
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, profession;
        ImageView profile;
        AppCompatButton btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.postName);
            profession = itemView.findViewById(R.id.postProfession);
            profile = itemView.findViewById(R.id.profile);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
