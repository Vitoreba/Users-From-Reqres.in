package com.vitor.testesankhya.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitor.testesankhya.R;
import com.vitor.testesankhya.util.UserAdapter;
import com.vitor.testesankhya.controller.UserDetailActivity;
import com.vitor.testesankhya.model.Page;
import com.vitor.testesankhya.model.User;
import com.vitor.testesankhya.rest.client.UserClient;
import com.vitor.testesankhya.rest.service.RetrofitConfig;
import com.vitor.testesankhya.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class UserListFragment extends Fragment {

    private RecyclerView userRecyclerView;

    private UserAdapter userAdapter;
    private UserAdapter.UserAdapterListener userListener;

    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    private ArrayList<User> users;

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance() {
        return new UserListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list,
                container,
                false);

        init(view);

        populate();

        return view;
    }

    private void init(View view) {
        userRecyclerView = view.findViewById(R.id.recUsers);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        userRecyclerView.setLayoutManager(linearLayoutManager);

        users = new ArrayList<User>();

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
        userRecyclerView.setLayoutManager(linearLayoutManager);
        userRecyclerView.setAdapter(userAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        userRecyclerView.addOnScrollListener(scrollListener);
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int page) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside
        //          the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        UserClient.getInstance().doGetUserList(page,
                new RetrofitConfig.OnRestResponseListener<Page>() {
                    @Override
                    public void onRestSuccess(Page response) {
                        int curSize = userAdapter.getItemCount();
                        users.addAll(response.getData());
                        userAdapter.notifyItemRangeInserted(curSize, users.size() - 1);
                    }

                    @Override
                    public void onRestError(ResponseBody body, Integer code) {
                        Log.e("RestError", "" + code);
                    }
                }
        );
    }

    private void populate() {
        UserClient.getInstance().doGetUserList(1,
            new RetrofitConfig.OnRestResponseListener<Page>() {
                @Override
                public void onRestSuccess(Page response) {
                    users.addAll(response.getData());
                    userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onRestError(ResponseBody body, Integer code) {
                    Log.e("RestError", "" + code);
                }
            }
        );
    }

}
