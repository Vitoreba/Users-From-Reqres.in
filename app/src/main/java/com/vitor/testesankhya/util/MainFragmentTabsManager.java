package com.vitor.testesankhya.util;

import android.support.v4.app.Fragment;

import com.vitor.testesankhya.controller.fragments.SavedUserListFragment;
import com.vitor.testesankhya.controller.fragments.UserListFragment;

public class MainFragmentTabsManager {

    public enum MainFragmentType {
        MAIN_FRAGMENT_HOME,
        MAIN_FRAGMENT_SAVED,
    }

    private UserListFragment mHomeFragment;
    private SavedUserListFragment mSavedFragment;

    private Fragment mCurrentSelectedFragment = null;

    //Singleton
    private static MainFragmentTabsManager mInstance = null;

    private MainFragmentTabsManager(){ }

    public static MainFragmentTabsManager getInstance(){
        if(mInstance == null) {
            mInstance = new MainFragmentTabsManager();
        }
        return mInstance;
    }


    public void setCurrentSelectedFragment(Fragment fragment){
        mCurrentSelectedFragment = fragment;
    }

    public Fragment getCurrentSelectedFragment(){
        return mCurrentSelectedFragment;
    }

    private UserListFragment getHomeFragment() {
        if(mHomeFragment == null){
            mHomeFragment = mHomeFragment.newInstance();
            mHomeFragment.setRetainInstance(true);
        }
        return mHomeFragment;
    }

    private SavedUserListFragment getSavedFragment() {
        if(mSavedFragment == null){
            mSavedFragment = mSavedFragment.newInstance();
            mSavedFragment.setRetainInstance(true);
        }
        return mSavedFragment;
    }

    public Boolean verifyFragmentInstantiatedByType(MainFragmentType fragmentType) {
        switch (fragmentType) {
            case MAIN_FRAGMENT_HOME:
                return (mHomeFragment != null);
            case MAIN_FRAGMENT_SAVED:
                return (mSavedFragment != null);
            default:
                return false;
        }
    }

    public Fragment getMainFragmentByType(MainFragmentType fragmentType) {
        Fragment fragment = null;
        switch (fragmentType) {
            case MAIN_FRAGMENT_HOME:
                fragment = getHomeFragment();
                break;
            case MAIN_FRAGMENT_SAVED:
                fragment = getSavedFragment();
                break;
            default:
                break;
        }

        return fragment;
    }

    public void clearAllFragments(){
        mHomeFragment = null;
        mSavedFragment = null;
    }

}