package com.example.android.cp_stalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.cp_stalk.MainActivity;
import com.example.android.cp_stalk.R;
import com.example.android.cp_stalk.UserViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText email, pass;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private String uid,user_name,user_handle;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginactivity);
        email = findViewById(R.id.edit_text_email);
        pass = findViewById(R.id.edit_text_password);
        progressBar = findViewById(R.id.progressbar);
        FirebaseApp.initializeApp(this);
        if(FirebaseDatabase.getInstance().getReference() == null)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);
        findViewById(R.id.createaccount).setOnClickListener(this);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                loginUser();
                break;
            case R.id.createaccount:
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
        }
    }

    private void loginUser() {

        progressBar.setVisibility(View.VISIBLE);
        final String email1 = email.getText().toString();
        final String password = pass.getText().toString();

        if (TextUtils.isEmpty(email1)) {
            email.setError("Enter Email address");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pass.setError("Enter Password");
            return;
        }
        auth.signInWithEmailAndPassword(email1,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    // there was an error
                    if (password.length() < 6) {
                        pass.setError("Min length 6");
                    } else {
                        Toast.makeText(LoginActivity.this, "Auth failed", Toast.LENGTH_LONG).show();
                    }
                }
                else {

                    //-----Retrieving data -----------
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    uid = user.getUid();


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user_name = dataSnapshot.child(uid).child("name").getValue(String.class);
                            user_handle = dataSnapshot.child(uid).child("handle").getValue(String.class);

                            progressBar.setVisibility(View.INVISIBLE);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed=sp.edit();
                            ed.putString("handle",user_handle);
                            ed.putString("name",user_name);
                            ed.putString("email",email1);
                            ed.commit();
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                }
            }

        });
    }






}