package nath.ariel.sellit_v6.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import classes.AllChatsAdapter;
import classes.User;
import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.databinding.ActivityAllchatsBinding;

/**
 * Created by Jordan Perez on 26/12/2021
 */
public class AllChatsActivity extends AppCompatActivity {

    //firebase
    private DatabaseReference chatsReference;
    private DatabaseReference userReference;

    private FirebaseAuth firebaseAuth;

    //RecyclerView layout
    private RecyclerView mRecyclerView;

    //Adapter
    private AllChatsAdapter mAdapter = new AllChatsAdapter();


    //Items List
    private List<User> mUsers;

    //binding
    private ActivityAllchatsBinding binding;

    //TAG
    private static final String TAG = "ALL_CHATS_ACTIVITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allchats);

        Log.d(TAG, "Entering Activity");

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //binding to layout
        binding = ActivityAllchatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //set profile picture
        setProfilePicture(firebaseUser.getPhotoUrl());

        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvAllChats.setText(name);

        //handle click on chat Image button
        binding.chatIconAllChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllChatsActivity.class);
                startActivity(intent);
            }
        });


//        //save currentUser seller's id
//        String sellerId = firebaseUser.getUid();
//        Intent intent = new Intent(AllChatsActivity.this, ChatActivity.class);
//        intent.putExtra("sellerId",sellerId);




        //Get References
        chatsReference = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Chats/");
        userReference = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Users/");


        //RecyclerView Initialisation + Settings
        mRecyclerView = binding.recyclerViewAllChats;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();
        //get all users with open chat
        chatsReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot is a List containing our data
                Log.d(TAG,"entering onDataChange");

                mUsers.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //get Users with open conversation
                    String userId = String.valueOf(postSnapshot.getKey());
                    Log.d(TAG, "userId = " +userId);


                    Task<DataSnapshot> user = userReference.child(userId).get();
                    user.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot2) {
                            User user = dataSnapshot2.getValue(User.class);
                            Log.d(TAG, "user = " +user.toString());
                            mUsers.add(user);
                            Log.d(TAG,"OnSuccess: mUsers = "+ Arrays.toString(mUsers.toArray()));

                            //update adapter
                            mAdapter.setContext(AllChatsActivity.this);
                            Log.d(TAG,"mAdapter.setUsers(mUsers)");
                            Log.d(TAG,"mUsers = "+ Arrays.toString(mUsers.toArray()));

                            mAdapter.setUsers(mUsers);

                            //set RecyclerView with updated adapter
                            mRecyclerView.setAdapter(mAdapter);
                        }

                    });
                }
            }

            //When we don't have permission to access the data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setProfilePicture(Uri profilePictureUrl) {
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageAllChats);
    }

}