package nath.ariel.sellit_v6.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nath.ariel.sellit_v6.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    //view  Binding
    private ActivityProfileBinding binding ;

    private FirebaseAuth firebaseAuth;
    private static final String TAG ="PROFIL_ACTIVITY_TAG";

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding to layout
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
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
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //handle click on seller button
        binding.Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), SellerActivity.class);
                 startActivity(intent);
            }
        });

        //handle click on buyer button
        binding.Buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuyerActivity.class);
                startActivity(intent);
            }
        });

        //handle click on myItems button
        binding.myItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyItemsActivity.class);
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
            binding.emailTv.setText(email);

            //set Username
            String name = firebaseUser.getDisplayName();
            binding.nameTv.setText(name);

            //gets profile picture from google and displays it using Glide
            setProfilePicture(firebaseUser.getPhotoUrl());
        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageView);
    }
}