package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.Models.Users;
import com.example.chattingapp.databinding.ActivityAboutOurAppBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AboutOurAppActivity extends AppCompatActivity {


    ActivityAboutOurAppBinding binding;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutOurAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();

        binding.txtThanksName.setText("Thank you (" + auth.getCurrentUser().getEmail() + ")");

        binding.iconBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutOurAppActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        binding.txtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutOurAppActivity.this, "Our Privacy Policy is available on our website, let's take a look now.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutOurAppActivity.this, "Our Information is available on our website, let's take a look now.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutOurAppActivity.this, "Don't miss our news, please turn on notification setting!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtInviteAFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutOurAppActivity.this, "Introduce our app to your friends now for chatting", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutOurAppActivity.this, "If you need help, visit our website to read more.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}