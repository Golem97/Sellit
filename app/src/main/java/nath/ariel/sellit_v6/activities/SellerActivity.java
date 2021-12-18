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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import nath.ariel.sellit_v6.R;

import nath.ariel.sellit_v6.databinding.ActivitySellerBinding;


public class SellerActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    //firebase
    private StorageReference storage;
    private DatabaseReference database;

    //binding
    private ActivitySellerBinding binding;
    private FirebaseAuth firebaseAuth;

    //image url
    private Uri imageUrl;

    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        //get user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //binding to layout
        binding = ActivitySellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //get our storage and database references
        database = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app").getReference("Sellit");
        storage = FirebaseStorage.getInstance("gs://sell-86b95.appspot.com").getReference("Sellit");


        //set profile picture
        setProfilePicture(firebaseUser.getPhotoUrl());

        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvSeller.setText(name);

        //TODO: handle safe entry

        //handle click on Imageupload button
        binding.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //handle click on upload button
        binding.itemUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(SellerActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    //upload to storage
                    uploadFile();

                    //TODO: redirect to personal gallery : Create proper intent to switch to gallery layout
                    //here
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); // open gallery
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUrl = data.getData();

            Glide.with(this)
                    .load(imageUrl)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(binding.uploadImage);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl) {
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageSeller);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUrl != null) {
            StorageReference fileReference = storage.child("Items").child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUrl));
            //upload to storage
            mUploadTask = fileReference.putFile(imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    binding.progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(SellerActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            //upload to realtime database
                            //get items infos
                            String id = firebaseAuth.getUid();
                            String name = String.valueOf(binding.productTitle.getText());
                            String descript = String.valueOf(binding.descript.getText());
                            int price = binding.price.getInputType();
                            String downloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();

                            //create item
                            Item item = new Item(id, name, descript, price, true, downloadUrl);


                            String uploadId = database.child("Items").push().getKey();
                            System.out.println("\n\n uploadId ="+uploadId+"\n\n");
                            database.child(uploadId).setValue(item);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SellerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            binding.progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}