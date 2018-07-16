package com.vitor.testesankhya.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vitor.testesankhya.R;
import com.vitor.testesankhya.util.Global;
import com.vitor.testesankhya.util.Verify;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUser, edtPassword;
    private Button btnCreateUser, btnLogin;

    private FirebaseAuth mAuth;

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            FirebaseAuth.getInstance().signOut();
        }

        edtUser = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPassword);

        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnLogin = findViewById(R.id.btnLogin);

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser(view);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin(view);
            }
        });

    }

    private void userLogin(View view) {
        if(validFields()) {
            doLogin();
        }
    }

    private boolean validFields() {
        boolean isValid = true;

        if (edtUser.getText().toString().isEmpty()) {
            edtUser.setError("Campo não pode ser vazio.");
            isValid = false;
        }
        if (!Verify.isValidEmail(edtUser.getText().toString())) {
            edtUser.setError("Email não é válido.");
            isValid = false;
        }
        if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError("Campo não pode ser vazio.");
            isValid = false;
        }

        return isValid;
    }

    private void doLogin() {
        String email = edtUser.getText().toString();
        String password = edtPassword.getText().toString();
        mProgressDialog = ProgressDialog.show(LoginActivity.this, "Aguarde",
                "Carregando");
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.hide();
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Global.user = user;

                    Intent intent = new Intent(LoginActivity.this
                            , MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("ERROR", "loginUserWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                }
                }
            });
    }

    private void registerNewUser(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
        finish();
    }
}
