package nath.ariel.sellit_v6.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import classes.Item;
import classes.Message;
import nath.ariel.sellit_v6.R;

import nath.ariel.sellit_v6.databinding.ActivityChatBinding;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class ChatActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    //firebase
    private StorageReference storage;
    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    //binding
    private ActivityChatBinding binding;

    //image url
    private Uri imageUrl;

    private StorageTask mUploadTask;

    //TAG
    private static final String TAG = "SELLER_ACTIVITY_IN_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        Log.d(TAG, "Entering Activity");

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //binding to layout
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //get our storage and database references
        database = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app").getReference("Sellit");
        storage = FirebaseStorage.getInstance("gs://sell-86b95.appspot.com").getReference("Sellit");


        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvCustomer.setText(name);




        //handle click on upload button
        binding.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //avoid uploading when no image and uploading too many times by clicking on upload button too fast
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(ChatActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    //upload to storage
                    uploadMessage();
                }
            }
        });
    }


    private void uploadMessage() {

        String sender = String.valueOf(binding.recieverId.getText());
        String reciever = String.valueOf(binding.senderId.getText());
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String content = String.valueOf(binding.message.getText());


        //create item
        Message Message = new Message(sender, reciever, date, content);

        //push it and save item_id
        String MessageId = database.child("Messages").push().getKey();

        //set item id
        Message.setMessage_id(MessageId);

        //check item id
        Log.d(TAG, "MessageId = "+Message.getMessage_id());

        //set value with item attributes
        database.child("messages").child(MessageId).setValue(Message);

        Toast.makeText(ChatActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

        //After successfully uploading item redirect to profile
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }
}
