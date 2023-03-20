package com.onechance.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onechance.socialmedia.Adapter.CommentAdapter;
import com.onechance.socialmedia.Model.Comment;
import com.onechance.socialmedia.Model.Post;
import com.onechance.socialmedia.Model.User;
import com.onechance.socialmedia.databinding.ActivityCommentBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String postId, postedBy;
    Intent intent;
    ArrayList<Comment> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();

        setSupportActionBar(binding.toolbar2);
        CommentActivity.this.setTitle("Comments");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

        database.getReference().child("posts")
                .child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Post post = snapshot.getValue(Post.class);
                        Picasso.get()
                                .load(post.getPostImage())
                                .placeholder(R.drawable.placeholder)
                                .into(binding.storyImage);

                        binding.like.setText(post.getPostLike() + "");
                        binding.description.setText(post.getDescription());
                        binding.comment.setText(post.getCommentCounts()+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Users")
                .child(postedBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(binding.profile);

                        binding.name.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.commentsPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment comments = new Comment();
                comments.setCommentBody(binding.EdtComment.getText().toString());
                comments.setCommentBy(FirebaseAuth.getInstance().getUid());
                comments.setCommentAt(new Date().getTime());
                database.getReference()
                        .child("posts")
                        .child(postId)
                        .child("Comments")
                        .push()
                        .setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference()
                                        .child("posts")
                                        .child(postId)
                                        .child("commentCounts")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int commentCount=0;
                                                if(snapshot.exists()){
                                                    commentCount=snapshot.getValue(Integer.class);
                                                }
                                                    database.getReference().child("posts")
                                                            .child(postId)
                                                            .child("commentCounts")
                                                            .setValue(commentCount+ 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    binding.EdtComment.setText("");
                                                                    Toast.makeText(CommentActivity.this, "commented", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });
            }
        });

        CommentAdapter adapter=new CommentAdapter(this,list);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        binding.commentsRv.setLayoutManager(manager);
        binding.commentsRv.setAdapter(adapter);

        database.getReference().child("posts")
                .child(postId)
                .child("Comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Comment comment=dataSnapshot.getValue(Comment.class);
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}