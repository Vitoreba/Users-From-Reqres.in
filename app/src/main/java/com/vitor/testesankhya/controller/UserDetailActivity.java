package com.vitor.testesankhya.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vitor.testesankhya.R;
import com.vitor.testesankhya.model.User;
import com.vitor.testesankhya.model.persistence.dao.UserDAO;

import java.sql.SQLException;

public class UserDetailActivity extends AppCompatActivity {

    private Bundle extras;
    private User user;

    private SimpleDraweeView userPhoto;
    private TextView userName, userLastName;
    private Switch switchSalvarOffline;

    private UserDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        init();

        fillIn();
    }

    private void init() {
        extras = getIntent().getExtras();
        user = (User) extras.get("user");

        userPhoto = findViewById(R.id.imgViewUserPhoto2);
        userName = findViewById(R.id.txtViewUserName2);
        userLastName = findViewById(R.id.txtViewUserLastName2);

        try {
            dao = new UserDAO(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        switchSalvarOffline = findViewById(R.id.switchSalvarOffline);
        switchSalvarOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOfflineData(view);
            }
        });
    }

    private void saveOfflineData(View view) {
        if(switchSalvarOffline.isChecked()) {
            dao.createOrUpdate(user);
        } else {
            dao.delete(user);
        }
    }

    private void fillIn() {
        userName.setText(user.getFirst_name());
        userLastName.setText(user.getLast_name());

        Uri uri = Uri.parse(user.getAvatar());
        userPhoto.setImageURI(uri);

        if(dao.findById(user.getId()) != null) {
            switchSalvarOffline.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
