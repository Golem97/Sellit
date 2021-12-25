package nath.ariel.sellit_v6.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import classes.ManageUsersAdapter;
import classes.User;
import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.databinding.ActivityManageusersBinding;

/**
 * Created by Jordan Perez on 24/12/2021
 */
public class ManageusersActivity extends AppCompatActivity {


    //firebase
    private StorageReference storage;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth firebaseAuth;

    //RecyclerView layout
    private RecyclerView mRecyclerView;

    //Adapter
    private ManageUsersAdapter mAdapter;

    //Users List
    private List<User> mUploads;

    //binding
    private ActivityManageusersBinding binding;

    //TAG
    private static final String TAG = "MANAGE_USERS_ACTIVITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminitems);

        Log.d(TAG, "Entering Activity");

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //binding to layout
        binding = ActivityManageusersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String id = firebaseAuth.getUid();

        //set profile picture
        setProfilePicture(firebaseUser.getPhotoUrl());

        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvManage.setText(name);

        //RecyclerView Initialisation + Settings
        mRecyclerView = binding.recyclerViewManage;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<User>();

        mDatabaseReference = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Users");


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            //display all users
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot is a List containing our data
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    mUploads.add(user);
                }

                mAdapter = new ManageUsersAdapter(ManageusersActivity.this, mUploads);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);

            }

            //When we don't have permission to access the data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ManageusersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setProfilePicture(Uri profilePictureUrl) {
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageManage);
    }

}
