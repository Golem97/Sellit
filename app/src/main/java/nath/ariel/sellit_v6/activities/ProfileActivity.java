package nath.ariel.sellit_v6.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nath.ariel.sellit_v6.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    //view  Binding
    private ActivityProfileBinding binding ;

    private FirebaseAuth firebaseAuth;
    private static final String TAG ="PROFILE_ACTIVITY_TAG";
    private DatabaseReference balanceUserReference;

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
        binding.logoutBtnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //handle click on seller button
        binding.SellBtnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), SellerActivity.class);
                 startActivity(intent);
            }
        });

        //handle click on buyer button
        binding.buyBtnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuyerActivity.class);
                startActivity(intent);
            }
        });

        //handle click on chat Image button
        binding.chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllChatsActivity.class);
                startActivity(intent);
            }
        });

        //handle click on chat Image button
        binding.contactAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("sellerId","lk9i02Hn0vcKmQSf2dRQEtd9UKr2");
                startActivity(intent);
            }
        });

        //handle click on myItems button
        binding.myItemsBtnCustomer.setOnClickListener(new View.OnClickListener() {
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

            balanceUserReference = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("Sellit/Users/"+firebaseUser.getUid()+"/balance");

            balanceUserReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //String balance = String.valueOf(snapshot.getValue(User.class).getBalance());
                    String bal = String.valueOf(snapshot.getValue());
                    binding.userBalance.setText(bal);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            //user already logged in
            //get user info
            String email = firebaseUser.getEmail();
            binding.emailTv.setText(email);

            //set Username
            String name = firebaseUser.getDisplayName();
            binding.nameTvCustomer.setText(name);

            //gets profile picture from google and displays it using Glide
            setProfilePicture(firebaseUser.getPhotoUrl());
        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.customerImageView);
    }
}