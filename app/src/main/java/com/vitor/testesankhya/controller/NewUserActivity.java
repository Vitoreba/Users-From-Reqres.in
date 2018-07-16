package com.vitor.testesankhya.controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vitor.testesankhya.R;
import com.vitor.testesankhya.util.Global;
import com.vitor.testesankhya.util.Verify;

import java.util.Base64;

public class NewUserActivity extends AppCompatActivity {

    EditText edtNewUser, edtPassword, edtConfirmPassword;
    Button btnCreate, btnCleanFields;

    private FirebaseAuth mAuth;

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        edtNewUser = findViewById(R.id.edtNewUser);
        edtPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtNewPasswordConfirm);

        btnCreate = findViewById(R.id.btnCreateNewUser);
        btnCleanFields = findViewById(R.id.btnCleanFields);

        btnCleanFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanFields(view);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser(view);
            }
        });
    }

    private void createNewUser(View view) {
        if(validFields()) {
            registerNewUser();
        }
    }

    private boolean validFields() {
        boolean isValid = true;

        String email = edtNewUser.getText().toString();
        if(!Verify.isValidEmail(email)) {
            edtNewUser.setError("O email digitado não é válido.");
            isValid = false;
        }
        // Numa situação real, qualquer senha pega no aplicativo seria codificada antes de
        // trabalharmos
        if(edtPassword.getText().length() < 8){
            edtPassword.setError("A senha precisa ter pelo menos 8 digitos.");
            isValid = false;
        }
        boolean equalPasswords = edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString());
        if(!equalPasswords) {
            edtConfirmPassword.setError("As senhas não são iguais.");
            isValid = false;
        }

        return isValid;
    }

    private void registerNewUser() {
        String email = edtNewUser.getText().toString();
        String password = edtPassword.getText().toString();
        mProgressDialog = ProgressDialog.show(NewUserActivity.this, "Aguarde",
                "Carregando");
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Global.user = user;

                    Intent intent = new Intent(NewUserActivity.this
                            , MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("ERROR", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(NewUserActivity.this, task.getException().getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cleanFields(View view) {
        edtNewUser.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
