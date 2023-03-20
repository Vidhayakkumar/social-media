package com.onechance.socialmedia.Fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.onechance.socialmedia.Adapter.StoryAdapter;
import com.onechance.socialmedia.Adapter.postAdapter;
import com.onechance.socialmedia.Model.Post;
import com.onechance.socialmedia.Model.Story;
import com.onechance.socialmedia.Model.UserStories;
import com.onechance.socialmedia.R;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {

    ArrayList<Story> storyList = new ArrayList<>();
    ArrayList<Post> postList = new ArrayList<>();
    RecyclerView storyRv, dashbordRv;
    postAdapter postAdapter;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    RoundedImageView addStoryImage;

    ActivityResultLauncher<String> galleryLauncher;

    public HomeFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        storyRv = view.findViewById(R.id.storyRv);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        StoryAdapter adapter = new StoryAdapter(storyList, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);

        database.getReference()
                .child("stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            storyList.clear();
                            for(DataSnapshot storySnapshot : snapshot.getChildren()){
                                Story story=new Story();
                                story.setStoryBy(storySnapshot.getKey());
                                story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UserStories> stories=new ArrayList<>();
                                for(DataSnapshot snapshot1: storySnapshot.child("userStories").getChildren()){
                                    UserStories userStories=snapshot1.getValue(UserStories.class);
                                    stories.add(userStories);
                                }
                                story.setStories(stories);
                                storyList.add(story);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            post.setPostId(dataSnapshot.getKey());
                            postList.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                        ;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        dashbordRv = view.findViewById(R.id.dashBordRv);

        postAdapter = new postAdapter(getContext(), postList);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        dashbordRv.setLayoutManager(linearLayoutManager1);
        dashbordRv.setAdapter(postAdapter);


        addStoryImage = view.findViewById(R.id.storyImage);
        addStoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        addStoryImage.setImageURI(result);
                        final StorageReference reference = storage.getReference()
                                .child("stories")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(new Date().getTime() + "");
                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Story story = new Story();
                                        story.setStoryAt(new Date().getTime());

                                        database.getReference()
                                                .child("stories")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .child("postedBy")
                                                .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        UserStories stories = new UserStories(uri.toString(), story.getStoryAt());

                                                        database.getReference()
                                                                .child("stories")
                                                                .child(FirebaseAuth.getInstance().getUid())
                                                                .child("userStories")
                                                                .push()
                                                                .setValue(stories);
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    }
                });


        return view;


    }
}