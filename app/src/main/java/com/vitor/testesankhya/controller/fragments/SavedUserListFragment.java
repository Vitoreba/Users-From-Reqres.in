package com.vitor.testesankhya.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitor.testesankhya.R;
import com.vitor.testesankhya.util.UserAdapter;
import com.vitor.testesankhya.controller.UserDetailActivity;
import com.vitor.testesankhya.model.User;
import com.vitor.testesankhya.model.persistence.dao.UserDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class SavedUserListFragment extends Fragment {

    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;

    private UserDAO dao;
    private ArrayList<User> users;
    private UserAdapter.UserAdapterListener userListener;

    public SavedUserListFragment() {
        // Required empty public constructor
    }

    public static SavedUserListFragment newInstance() {
        return new SavedUserListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_users, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        userRecyclerView = view.findViewById(R.id.recSavedUsers);

        try {
            dao = new UserDAO(getActivity());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(dao != null) {
            users = (ArrayList<User>) dao.findAll();
        } else {
            users = new ArrayList<User>();
        }

        userListener = new UserAdapter.UserAdapterListener() {
            @Override
            public void onItemClickListener(Integer position) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra("user", users.get(position));
                startActivity(intent);
                getActivity().finish();
            }
        };
        userAdapter = new UserAdapter(users, userListener);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userRecyclerView.setAdapter(userAdapter);

    }

}
