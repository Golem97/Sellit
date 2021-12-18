package nath.ariel.sellit_v6.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import classes.Item;
import nath.ariel.sellit_v6.R;

import nath.ariel.sellit_v6.databinding.ActivitySellerBinding;


public class SellerActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private DatabaseReference database;
    private ActivitySellerBinding binding ;
    private FirebaseAuth firebaseAuth ;
    private Uri imageUrl;



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

        database = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        //set profile picture
        setProfilePicture(firebaseUser.getPhotoUrl());

        //set Username
        String name = firebaseUser.getDisplayName();
        binding.nameTvSeller.setText(name);

        //TODO: handle safe entry

        //handle click on upload button
        binding.itemUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get items infos
                String name = String.valueOf(binding.productTitle.getText());
                String descript = String.valueOf(binding.descript.getText());
                int price = binding.price.getInputType();

                //create item
                Item item = new Item(firebaseAuth.getUid(), name,descript, price, true, imageUrl);

                //upload to fire base items
                System.out.println(binding.productTitle.getText()+" "+binding.descript.getText()+" "+binding.price.getText()+" "
                        +imageUrl + firebaseAuth.getUid());

                //upload to database
                database.child("Sellit").child("Items").push().setValue(item);

                //TODO: redirect to personal gallery : Create proper gallery layout
                //here
            }
        });


        //handle click on Imageupload button
        binding.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); // open gallery
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ){
                imageUrl = data.getData();

            //Picasso.get().load(imageUrl).into(binding.uploadImage);
            //binding.uploadImage.setImageURI(imageUrl);
            Glide.with(this)
                    .load(imageUrl)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(binding.uploadImage);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageSeller);
    }

}