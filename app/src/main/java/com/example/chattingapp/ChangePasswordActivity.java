package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.Models.Users;
import com.example.chattingapp.databinding.ActivityChangePasswordBinding;
import com.example.chattingapp.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getSupportActionBar().hide();

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        binding.iconBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldPass = binding.edtOldPassword.getText().toString();
                String newPass = binding.edtNewPassword.getText().toString();
                String confirmPass = binding.edtConfirmPassword.getText().toString();


                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users users = snapshot.getValue(Users.class);
                                Picasso.get()
                                        .load(users.getProfilePic())
                                        .placeholder(R.drawable.user)
                                        .into(binding.imgAvatar);
                                if (!confirmPass.equals(newPass)) {
                                    binding.edtConfirmPassword.setError("Your confirm password is wrong!");
                                    return;
                                }
                                if (!oldPass.equals(users.getPassword())) {
                                    binding.edtOldPassword.setError("Your old password is wrong!");
                                    binding.edtOldPassword.setText("");
                                    binding.edtNewPassword.setText("");
                                    binding.edtConfirmPassword.setText("");
                                    return;
                                }

                                HashMap<String, Object> obj = new HashMap<>();
                                obj.put("password", confirmPass);



                                auth.getCurrentUser().updatePassword(confirmPass);

                                database.getReference().child("Users")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .updateChildren(obj);

                                Toast.makeText(ChangePasswordActivity.this, "Your password changed", Toast.LENGTH_SHORT).show();

                                binding.edtOldPassword.setText("");
                                binding.edtNewPassword.setText("");
                                binding.edtConfirmPassword.setText("");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });
    }
}