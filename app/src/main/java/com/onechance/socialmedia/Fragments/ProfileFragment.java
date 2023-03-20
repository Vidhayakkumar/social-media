package com.onechance.socialmedia.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onechance.socialmedia.Adapter.FollowersAdapter;
import com.onechance.socialmedia.Model.User;
import com.onechance.socialmedia.Model.Follow;
import com.onechance.socialmedia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    RecyclerView profileRv;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ImageView profile;
    TextView profileName, profileAbout, followers;
    ArrayList<Follow> list = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        profileRv = view.findViewById(R.id.profileRv);
        profile = view.findViewById(R.id.profile);
        profileName = view.findViewById(R.id.profileName);
        profileAbout = view.findViewById(R.id.profileAbout);
        followers=view.findViewById(R.id.followers);


        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            Picasso.get()
                                    .load(user.getProfileImage())
                                    .placeholder(R.drawable.adduser)
                                    .into(profile);

                            profileName.setText(user.getName());
                            profileAbout.setText(user.getProfession());
                            followers.setText(user.getFollowersCount()+"");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Profile not uploaded", Toast.LENGTH_SHORT).show();
                    }
                });


//        list.add(new Follow(R.drawable.hardik));
//        list.add(new Follow(R.drawable.rohit));
//        list.add(new Follow(R.drawable.hardik));
//        list.add(new Follow(R.drawable.virat));
//        list.add(new Follow(R.drawable.rohit));
//        list.add(new Follow(R.drawable.virat));
//        list.add(new Follow(R.drawable.virat));

        FollowersAdapter adapter = new FollowersAdapter(list, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        profileRv.setLayoutManager(manager);
        profileRv.setAdapter(adapter);


        database.getReference().child("Users")
                .child(auth.getUid())
                .child("followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for( DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Follow follow=dataSnapshot.getValue(Follow.class);
                            list.add(follow);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 18);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri uri = data.getData();
            profile.setImageURI(uri);
            final StorageReference reference = storage.getReference().child("profile").child(FirebaseAuth.getInstance().getUid());
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "profile update", Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(auth.getUid()).child("profileImage").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}