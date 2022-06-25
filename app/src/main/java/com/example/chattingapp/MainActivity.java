package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chattingapp.Adapters.FragmentsAdapter;
import com.example.chattingapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Welcome to our chatting app");

        auth = FirebaseAuth.getInstance();

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.groupchat:

                Intent intent = new Intent(MainActivity.this, GroupChatActivity.class);
                startActivity(intent);

                break;

            case R.id.updateProfile:

                Intent intent1 = new Intent(MainActivity.this, UpdateProfileActivity.class);
                startActivity(intent1);
                break;

            case R.id.changePassword:

                Intent intent2 = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent2);
                break;

            case R.id.about:

                Intent intent3  = new Intent(MainActivity.this, AboutOurAppActivity.class);
                startActivity(intent3);
                break;

            case R.id.logout:
                auth.signOut();
                Intent intent4 = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent4);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}