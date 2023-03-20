package com.onechance.socialmedia.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onechance.socialmedia.Model.Comment;
import com.onechance.socialmedia.Model.User;
import com.onechance.socialmedia.R;
import com.onechance.socialmedia.databinding.ActivityCommentBinding;
import com.onechance.socialmedia.databinding.CommentSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    ArrayList<Comment> list=new ArrayList<>();

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.comment_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment=list.get(position);

        String time = TimeAgo.using(comment.getCommentAt());
        holder.binding.chatTime.setText(time);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(comment.getCommentBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);

                        Picasso.get()
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.profile);

                        holder.binding.chat.setText(Html.fromHtml("<b>" + user.getName()  +  "</b>" + " "+ comment.getCommentBody()));

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

    public class ViewHolder extends RecyclerView.ViewHolder{

        CommentSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CommentSampleBinding.bind(itemView);


        }
    }
}
