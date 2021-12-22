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

import nath.ariel.sellit_v6.databinding.ActivityAdminBinding;

/**
 * Created by Jordan Perez on 22/12/2021
 */
public class AdminActivity extends AppCompatActivity {

    //view  Binding
    private ActivityAdminBinding binding ;

    private FirebaseAuth firebaseAuth;
    private static final String TAG ="ADMIN_ACTIVITY_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding to layout
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
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
        binding.logoutBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //handle click on item button
        binding.itemsBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SellerActivity.class);
//                startActivity(intent);
            }
        });

        //handle click on users button
        binding.usersBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), BuyerActivity.class);
//                startActivity(intent);
            }
        });

        //handle click on manage button
        binding.manageTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MyItemsActivity.class);
//                startActivity(intent);
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
            binding.emailTv.setText(email);

            //set Username
            String name = firebaseUser.getDisplayName();
            binding.nameTvAdmin.setText(name);

            //gets profile picture from google and displays it using Glide
            setProfilePicture(firebaseUser.getPhotoUrl());
        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.AdminImageView);
    }
}

