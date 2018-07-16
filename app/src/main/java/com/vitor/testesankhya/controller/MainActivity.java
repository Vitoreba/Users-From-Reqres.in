package com.vitor.testesankhya.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.vitor.testesankhya.R;
import com.vitor.testesankhya.controller.fragments.SavedUserListFragment;
import com.vitor.testesankhya.controller.fragments.UserListFragment;
import com.vitor.testesankhya.util.Global;
import com.vitor.testesankhya.util.MainFragmentTabsManager;

public class MainActivity
        extends AppCompatActivity
        implements  BottomNavigationView.OnNavigationItemSelectedListener {

    private FrameLayout mFragment;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        if(!Global.FRESCO_INIT) {
            Fresco.initialize(this);
            Global.FRESCO_INIT = true;
        }

        mFragment = findViewById(R.id.main_frame_layout);
        mBottomNavigationView = findViewById(R.id.main_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        setCurrentFragment(MainFragmentTabsManager.MainFragmentType.MAIN_FRAGMENT_HOME);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                setCurrentFragment(MainFragmentTabsManager.MainFragmentType.MAIN_FRAGMENT_HOME);
                return true;
            case R.id.navigation_downloaded:
                setCurrentFragment(MainFragmentTabsManager.MainFragmentType.MAIN_FRAGMENT_SAVED);
                return true;
            default:
                break;
        }
        return false;
    }

    private void setCurrentFragment(MainFragmentTabsManager.MainFragmentType fragmentType) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_frame_layout);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment newFragment = MainFragmentTabsManager.getInstance()
                .getMainFragmentByType(fragmentType);

        if (currentFragment == null) {
            fragmentTransaction.add(R.id.main_frame_layout, newFragment);
            fragmentTransaction.commit();
            MainFragmentTabsManager.getInstance().setCurrentSelectedFragment(newFragment);
        } else {
            if (!newFragment.isAdded()) {
                fragmentTransaction.add(R.id.main_frame_layout, newFragment);
            }

            int currentFragmentsCount = (fragmentManager.getFragments() != null)
                    ? fragmentManager.getFragments().size()
                    : 0;
            for (int i = 0; i < currentFragmentsCount; i++) {
                Fragment existentFragment = fragmentManager.getFragments().get(i);
                if (existentFragment != null) {
                    if (existentFragment instanceof UserListFragment ||
                            existentFragment instanceof SavedUserListFragment) {
                        fragmentTransaction.hide(existentFragment);
                    }
                }
            }
            fragmentTransaction.show(newFragment);
            MainFragmentTabsManager.getInstance().setCurrentSelectedFragment(newFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        // Criar alerta qdo for sair da Main
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set other dialog properties
        builder.setTitle("Sair da Aplicação");
        builder.setMessage("Deseja mesmo fazer logout?");

        // Add the buttons
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent intent = new Intent(MainActivity.this
                        , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                // DO NOTHING
                dialog.cancel();
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Show the Dialog
        dialog.show();
    }
}
