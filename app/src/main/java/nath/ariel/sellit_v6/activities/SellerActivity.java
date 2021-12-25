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
import nath.ariel.sellit_v6.R;

import nath.ariel.sellit_v6.databinding.ActivitySellerBinding;


public class SellerActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    //firebase
    private StorageReference storage;
    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    //binding
    private ActivitySellerBinding binding;

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
        binding = ActivitySellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //get our storage and database references
        database = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app").getReference("Sellit");
        storage = FirebaseStorage.getInstance("gs://sell-86b95.appspot.com").getReference("Sellit");

        //set profile picture
        setProfilePicture(firebaseUser.getPhotoUrl());

        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvSeller.setText(name);

        //check if there are no empty field + if price is an int
        binding.productTitle.addTextChangedListener(mTextWatcher);
        binding.descript.addTextChangedListener(mTextWatcher);
        binding.price.addTextChangedListener(mTextWatcher);


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
                //avoid uploading when no image and uploading too many times by clicking on upload button too fast
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(SellerActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    //upload to storage
                    uploadFile();
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

    //check if there are no empty field and if entered price is an int
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String mTitle = binding.productTitle.getText().toString().trim();
            String mDescript = binding.descript.getText().toString().trim();
            String mPrice = binding.price.getText().toString().trim();

            binding.itemUpload.setEnabled(!mTitle.isEmpty() && !mDescript.isEmpty() && !mPrice.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void uploadFile() {

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (imageUrl != null) {
            long picId = System.currentTimeMillis();
            StorageReference fileReference = storage.child("Items").child(user_id).child(picId
                    + "." + getFileExtension(imageUrl));

            //upload to storage
            mUploadTask = fileReference.putFile(imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //delay progressBar
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    binding.progressBar.setProgress(0);
                                }
                            }, 500);

                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //upload to realtime database
                                    //get items infos

                                    String downloadUrl = uri.toString();
                                    String id = firebaseAuth.getUid();

                                    String name = String.valueOf(binding.productTitle.getText());
                                    String descript = String.valueOf(binding.descript.getText());
                                    double price = Integer.parseInt(binding.price.getText().toString());

                                    //set storageId
                                    String storageId = String.format("%d.%s", picId, getFileExtension(imageUrl));

                                    //create item
                                    Item item = new Item(id, name, descript, price, true, downloadUrl, storageId);

                                    //push it and save item_id
                                    String itemId = database.child("Items").push().getKey();

                                    //set item id
                                    item.setItem_id(itemId);

                                    //check item id
                                    Log.d(TAG, "uploadId = "+item.getItem_id());

                                    //set value with item attributes
                                    database.child("Items").child(itemId).setValue(item);

                                    Toast.makeText(SellerActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                                    //After successfully uploading item redirect to profile
                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
                                }
                            });
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