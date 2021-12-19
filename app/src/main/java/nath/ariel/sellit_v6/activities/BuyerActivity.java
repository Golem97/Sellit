package nath.ariel.sellit_v6.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import java.util.EventListener;
import java.util.List;

import classes.ImageAdapter;
import classes.Item;
import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.databinding.ActivityBuyerBinding;

public class BuyerActivity extends AppCompatActivity {

    //firebase
    private StorageReference storage;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth firebaseAuth;

    //RecyclerView layout
    private RecyclerView mRecyclerView;

    //Adapter
    private ImageAdapter mAdapter = new ImageAdapter();
    ;

    //Items List
    private List<Item> mUploads;

    //binding
    private ActivityBuyerBinding binding;

    //TAG
    private static final String TAG = "BUYER_ACTIVITY_IN_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        Log.d(TAG, "Entering Activity");

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //binding to layout
        binding = ActivityBuyerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String id = firebaseAuth.getUid();

        //set profile picture
        setProfilePicture(firebaseUser.getPhotoUrl());

        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvBuyer.setText(name);

        //RecyclerView Initialisation + Settings
        mRecyclerView = binding.recylcerView;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        //Get References
        mDatabaseReference = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Items");

        //get data
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot is a List containing our data
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //get item
                    Item item = postSnapshot.getValue(Item.class);

                    //if item doesn't belong to current user
                    if (!item.getUser_id().equals(id)) {
                        mUploads.add(item); //then it's buyable by him
                    }
                }
                //update adapter
                mAdapter.setContext(BuyerActivity.this);
                mAdapter.setItems(mUploads);

                //set RecyclerView with updated adapter
                mRecyclerView.setAdapter(mAdapter);
            }

            //When we don't have permission to access the data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BuyerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfilePicture(Uri profilePictureUrl) {
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageBuyer);
    }
}