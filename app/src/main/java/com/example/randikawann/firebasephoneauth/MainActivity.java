package com.example.randikawann.firebasephoneauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private Button btLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLogout = (Button) findViewById(R.id.btLogout);

        mAuth = FirebaseAuth.getInstance();

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendToAuth();
            }
        });
    }

    private void sendToAuth(){
        Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(authIntent);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        if(currentUser == null){
            Intent authIntent = new Intent(MainActivity.this,AuthActivity.class);
            startActivity(authIntent);
        }
    }
}
