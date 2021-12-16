package nath.ariel.sellit_v6.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nath.ariel.sellit_v6.databinding.ActivityProfilBinding;

public class ProfilActivity extends AppCompatActivity {

    //view  Binding
    private ActivityProfilBinding binding ;

    private FirebaseAuth firebaseAuth;
    private static final String TAG ="PROFIL_ACTIVITY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding to layout
        binding = ActivityProfilBinding.inflate(getLayoutInflater());
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
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //handle click on seller button
        binding.Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), SellerActivity.class);
                 startActivity(intent);
            }
        });

        //handle click on buyer button
        binding.Buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuyerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkUser() {
        //get  current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Log.d(TAG+" "+String.valueOf(firebaseUser), "firebaseUser.getCurrentUser successful");

        if (firebaseUser == null){
            //user not logged in -> redirect to MainActivity class to log
            //We should Toast a message like "not logged in, try again"
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else{
            //user already logged in
            //get user info
            String email = firebaseUser.getEmail();
            Log.d(TAG+" "+email, "getEmail successfull");
            //set Email
            binding.emailTv.setText(email);
            Log.d(TAG, "setText(email) successfull");
        }
    }

    public void Buyer(View view) {


    }
}