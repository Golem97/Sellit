package nath.ariel.sellit_v6.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nath.ariel.sellit_v6.databinding.ActivityTempBinding;

/**
 * Created by Jordan Perez on 23/12/2021
 */
public class TempActivity extends AppCompatActivity {

    //view  Binding
    private ActivityTempBinding binding ;

    private FirebaseAuth firebaseAuth;
    private static final String TAG ="TEMP_ACTIVITY_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding to layout
        binding = ActivityTempBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "FirebaseAuth.getInstance() successful");

        //checkUser
        checkUser();
        Log.d(TAG, "checkUser() successful");

        //handle click on logout button
        binding.logoutBtnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //handle click on Customer button
        binding.CustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        //handle click on Admin button
        binding.AdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Log.d(TAG+" "+String.valueOf(firebaseUser), "firebaseUser.getCurrentUser successful");

        if (firebaseUser == null){
            //user not logged in -> redirect to MainActivity class to log
            //We should Toast a message like "not logged in, try again"
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else{
            //user already logged in
            //get user info
            String email = firebaseUser.getEmail();
            binding.emailTvTemp.setText(email);

            //set Username
            String name = firebaseUser.getDisplayName();
            binding.nameTvTemp.setText(name);

            //gets profile picture from google and displays it using Glide
            setProfilePicture(firebaseUser.getPhotoUrl());
        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.TempImageView);
    }

}
