package com.vitor.testesankhya.model.persistence.dao;

import android.content.Context;
import android.util.Log;

import com.vitor.testesankhya.model.User;

import java.sql.SQLException;

public class UserDAO extends GenericDAO {
    private static final String TAG = "UserDAO";
    private static UserDAO mInstance;

    public UserDAO(Context context) throws SQLException {
        super(context, User.class);
    }

    public static UserDAO getInstance(Context context) {
        if (mInstance == null) {
            try {
                mInstance = new UserDAO(context);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return mInstance;
    }

    public User findById(Integer id) {
        User user = null;
        try {
            return (User) this.dao.queryBuilder().where().eq("id", id).queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return user;
        }
    }



}