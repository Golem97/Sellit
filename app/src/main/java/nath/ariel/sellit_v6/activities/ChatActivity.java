package nath.ariel.sellit_v6.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

import classes.ImageAdapter;
import classes.Item;
import classes.Message;
import classes.MessageAdapter;
import nath.ariel.sellit_v6.R;

import nath.ariel.sellit_v6.databinding.ActivityChatBinding;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    //firebase
    private DatabaseReference chatsReference;
    private DatabaseReference userReference;

    private FirebaseAuth firebaseAuth;

    //RecyclerView layout
    private RecyclerView mRecyclerView;

    //Adapter
    private MessageAdapter mAdapter = new MessageAdapter();
    ;

    //Items List
    private List<Message> mUploads;

    //binding
    private ActivityChatBinding binding;

    //TAG
    private static final String TAG = "CHAT_ACTIVITY_IN_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Log.d(TAG, "Entering Activity");

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //binding to layout
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String id = firebaseUser.getUid();

        //set profile picture
        setProfilePicture(firebaseUser.getPhotoUrl());

        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvChat.setText(name);


        //set messageChat EditText
        binding.messageChat.addTextChangedListener(mTextWatcher);


        //Get sellerId from chat btn
        Intent intent = getIntent();
        String sellerId = intent.getStringExtra("sellerId");

        //Get References
        chatsReference = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Chats/");
        userReference = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Users/");

        binding.sendbtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Task<DataSnapshot> userData = userReference.child(id).get();

                userData.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        String date = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss")
                                .format(Calendar.getInstance().getTime());

                        String userName = String.valueOf(dataSnapshot.child("name").getValue());

                        String content = binding.messageChat.getText().toString().trim();
                        content = userName+": "+content;

                        Message message = new Message(id,sellerId,date,content);

                        String chatId = chatsReference.child(id).child(sellerId).push().getKey();
                        message.setMessage_id(chatId);
                        chatsReference.child(id).child(sellerId).child(chatId).setValue(message);
                        chatsReference.child(sellerId).child(id).child(chatId).setValue(message);

//                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
//                        startActivity(intent);
                        Toast.makeText(ChatActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        //RecyclerView Initialisation + Settings
        mRecyclerView = binding.recyclerViewChat;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();
        //get data
        chatsReference.child(id).child(sellerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot is a List containing our data
                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //get message
                    Message message = postSnapshot.getValue(Message.class);
                        mUploads.add(message);
                }
                //update adapter
                mAdapter.setContext(ChatActivity.this);
                mAdapter.setMessages(mUploads);

                //set RecyclerView with updated adapter
                mRecyclerView.setAdapter(mAdapter);
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
                .into(binding.ChatImageView);
    }

    //check if there are no empty field and if entered price is an int
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String content = binding.messageChat.getText().toString().trim();
            binding.sendbtnChat.setEnabled(!content.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
}
