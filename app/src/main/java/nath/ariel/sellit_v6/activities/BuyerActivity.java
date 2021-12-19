package nath.ariel.sellit_v6.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.databinding.ActivityBuyerBinding;
import nath.ariel.sellit_v6.databinding.ActivitySellerBinding;

public class BuyerActivity extends AppCompatActivity {

    //firebase
    private StorageReference storage;
    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    //binding
    private ActivityBuyerBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //binding to layout
        binding = ActivityBuyerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //set profile picture
        setProfilePicture(firebaseUser.getPhotoUrl());

        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvBuyer.setText(name);
    }

    private void setProfilePicture(Uri profilePictureUrl) {
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageBuyer);
    }
}