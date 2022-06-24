package com.example.chattingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.ChatDetailActivity;
import com.example.chattingapp.Models.Users;
import com.example.chattingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UsersFullAdapter extends RecyclerView.Adapter<UsersFullAdapter.ViewHolder>{

    ArrayList<Users> list;
    Context context;

    public UsersFullAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public UsersFullAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_full_action, parent, false);

        return new UsersFullAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersFullAdapter.ViewHolder holder, int position) {

        Users users = list.get(position);

        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.user).into(holder.imageView);
        holder.txtUserName.setText(users.getUserName());
        holder.txtStatus.setText(users.getStatus());
        holder.btnCall.setImageResource(R.drawable.call);
        holder.btnVideoCall.setImageResource(R.drawable.videocall);

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Sorry, call is not available now", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Sorry, video call is not available now", Toast.LENGTH_SHORT).show();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("profilePic", users.getProfilePic());
                intent.putExtra("userName", users.getUserName());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, btnCall, btnVideoCall;
        TextView txtUserName, txtStatus;


        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgProfileAvatar);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtStatus = itemView.findViewById(R.id.txtLastMessage);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnVideoCall = itemView.findViewById(R.id.btnVideoCall);

        }
    }
}
