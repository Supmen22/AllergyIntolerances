package com.example.allergyintolerances;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private static final String LOG_TAG = Login.class.getName();
    EditText userEmailET;
    EditText passwordET;
    private FirebaseAuth mAuth;

    private NotificationHandler mNoftificationHandler;



    private SharedPreferences preferences;
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        mNoftificationHandler = new NotificationHandler(this);
    }

    public void login(View view){
        userEmailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);

        String userEmailStr = userEmailET.getText().toString();
        String passwordStr = passwordET.getText().toString();

        //Log.i("LoginActivity", "Bejelentkezett: " + userEmailStr + " a következő jelszóval: " + passwordStr );
        mAuth.signInWithEmailAndPassword(userEmailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(LoginActivity.this, "Login succesful!", Toast.LENGTH_SHORT).show();
                    mNoftificationHandler.send("Üdvözlünk"+userEmailStr);
                    startAllergyIntolerance();
                }else{
                    Toast.makeText(Login.this, "Login unsuccesful!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void startAllergyIntolerance(){
        Intent intent = new Intent(this, AllergyIntolerance.class);
        startActivity(intent);
    }

    public void loginAsGuest(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(LoginActivity.this, "Login succesful!", Toast.LENGTH_SHORT).show();
                    startAllergyIntolerance();
                } else {
                    Toast.makeText(Login.this, "Login unsuccesful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }
}