
package com.example.allergyintolerances;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();

    EditText userNameET;
    EditText userEmailET;
    EditText passwordET;
    EditText passwordConfET;
    private  FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userNameET = findViewById(R.id.editTextUserName);
        userEmailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        passwordConfET = findViewById(R.id.editTextPasswordConf);
        mAuth = FirebaseAuth.getInstance();

    }

    public void register(View view) {
        String userNameStr = userNameET.getText().toString();
        String userEmailStr = userEmailET.getText().toString();
        String passrowdStr = passwordET.getText().toString();
        String passwordConfStr = passwordConfET.getText().toString();


        if (!passrowdStr.equals(passwordConfStr)){
            //Log.i(LOG_TAG, "A jelszavak nem egyeznek meg!");
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this,"Passwords do not match!", duration).show();
        }else if(!(passrowdStr.length()>=8)){
            //Log.i(LOG_TAG, "A jelszó túl rövid!");
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this,"The password is too short!", duration).show();
        }else if(userNameStr.length()<1){
            //Log.i(LOG_TAG, "Adjon meg felhasználónevet!");
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this,"Username too short!", duration).show();
        }else if(userEmailStr.length()<1) {
            //Log.i(LOG_TAG, "Adjon meg email címet!");
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this, "Email is too short!", duration).show();
        }else{
            mAuth.createUserWithEmailAndPassword(userEmailStr,passrowdStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.i(LOG_TAG, "User created succesfully!");

                        startAllergyIntolerance();
                    } else{
                        Log.i(LOG_TAG, "User created unsuccesfully!");
                    }
                }
            });

        }
    }

    public void goback(View view) {
        finish();
    }

    public void startAllergyIntolerance(){
        Intent intent = new Intent(this, AllergyIntolerance.class);
        startActivity(intent);
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