package com.onechance.socialmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onechance.socialmedia.CommentActivity;
import com.onechance.socialmedia.Model.Post;
import com.onechance.socialmedia.Model.User;
import com.onechance.socialmedia.databinding.DasbordRvSampleBinding;
import com.onechance.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.util.ArrayList;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {

    Context context;
    ArrayList<Post> list = new ArrayList<Post>();

    public postAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dasbord_rv_sample, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = list.get(position);
        Picasso.get()
                .load(post.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.storyImage);

        String postDescription = post.getDescription();
        if (postDescription.equals("")) {
            holder.binding.postDescription.setVisibility(View.GONE);
        } else {
            holder.binding.postDescription.setText(postDescription);
            holder.binding.postDescription.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(post.getPostedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.profile);

                        holder.binding.like.setText(post.getPostLike() + "");
                        holder.binding.userName.setText(user.getName());
                        holder.binding.about.setText(user.getProfession());
                        holder.binding.comment.setText(post.getCommentCounts() + "");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(post.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart, 0, 0, 0);
                        }else {
                            holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(post.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference().child("posts")
                                                            .child(post.getPostId())
                                                            .child("postLike")
                                                            .setValue(post.getPostLike() + 1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart, 0, 0, 0);
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

        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("postedBy",post.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DasbordRvSampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DasbordRvSampleBinding.bind(itemView);
        }
    }
}
