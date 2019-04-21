package com.example.android.cp_stalk;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.cp_stalk.Database.DBHelperUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextName, editTextEmail, editTextPassword, editTextHandle;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeractivity);

        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextHandle = findViewById(R.id.edit_handle);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null) {


        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String handle = editTextHandle.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }

        if (handle.isEmpty()) {
            editTextHandle.setError(getString(R.string.input_error_phone));
            editTextHandle.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    name,
                                    email,
                                    handle
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        Intent i=new Intent(RegisterActivity.this,MainActivity.class);
                                        DBHelperUsers dbHelperUsers=new DBHelperUsers(RegisterActivity.this);
                                        dbHelperUsers.insert(handle);

                                        SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor ed=sp.edit();
                                        ed.putString("handle",handle);
                                        ed.putString("name",name);
                                        ed.putString("email",email);
                                        ed.commit();

                                        startActivity(i);
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


}
