package com.vitor.testesankhya.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vitor.testesankhya.R;
import com.vitor.testesankhya.model.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> users;
    private UserAdapterListener listener;

    public UserAdapter(ArrayList<User> users, UserAdapterListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public UserAdapter(Context context, ArrayList<User> users, UserAdapterListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_recycler_view_cell, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = users.get(i);

        viewHolder.populate(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView userPhoto;
        private TextView userName, userLastName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.imgViewUserPhoto);
            userName = itemView.findViewById(R.id.txtViewUserName);
            userLastName = itemView.findViewById(R.id.txtViewUserLastName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickListener(getAdapterPosition());
                }
            });
        }

        public void populate(User user) {
            userName.setText(user.getFirst_name());
            userLastName.setText(user.getLast_name());

            Uri uri = Uri.parse(user.getAvatar());
            userPhoto.setImageURI(uri);
        }
    }

    public interface UserAdapterListener {
        void onItemClickListener(Integer position);
    }
}
