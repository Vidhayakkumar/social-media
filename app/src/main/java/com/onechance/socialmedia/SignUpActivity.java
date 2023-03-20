package com.onechance.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.onechance.socialmedia.Model.User;
import com.onechance.socialmedia.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        binding.SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email, Password,name,profession;
                Email=binding.Email.getText().toString();
                Password=binding.Password.getText().toString();
                name=binding.Name.getText().toString();
                profession=binding.Profession.getText().toString();

                if (Email.isEmpty()){
                    binding.Email.setError("Please enter email");
                    return;
                }
                if(Password.isEmpty()){
                    binding.Password.setError("Please enter password");
                    return;
                }
                if(profession.isEmpty()){
                    binding.Profession.setError("Please enter profession");
                    return;
                }
                if(name.isEmpty()){
                    binding.Name.setError("Please enter password");
                    return;
                }


                auth.createUserWithEmailAndPassword(Email,Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(name,profession,Email,Password);
                            String id=task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Intent intent=new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignUpActivity.this, "You're account created sucesessflly", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}