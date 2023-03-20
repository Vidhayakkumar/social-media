package com.onechance.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onechance.socialmedia.Model.Story;
import com.onechance.socialmedia.Model.User;
import com.onechance.socialmedia.Model.UserStories;
import com.onechance.socialmedia.R;
import com.onechance.socialmedia.databinding.StoryDesignLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder>{

    ArrayList<Story> list;
    Context context;

    public StoryAdapter(ArrayList<Story> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.story_design_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Story story=list.get(position);

        UserStories userStories=story.getStories().get(story.getStories().size()-1);

        Picasso.get()
                .load(userStories.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.storyImage);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.profile);

                        holder.binding.storyName.setText(user.getName());


                        holder.binding.storyImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                ArrayList<MyStory> myStories = new ArrayList<>();
//
//                                for(Story story: data){
//                                    myStories.add(new MyStory(
//                                            story.getImageUrl(),
//                                            story.getDate()
//                                    ));
//                                }
                            }
                        });
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

        StoryDesignLayoutBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding=StoryDesignLayoutBinding.bind(itemView);
        }
    }
}
