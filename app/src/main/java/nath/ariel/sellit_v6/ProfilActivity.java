package nath.ariel.sellit_v6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.transition.MaterialContainerTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nath.ariel.sellit_v6.databinding.ActivityProfilBinding;

public class ProfilActivity extends AppCompatActivity {

    //view  Binding
    private ActivityProfilBinding binding ;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //handle click ,logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();

            }
        });



    }

    private void checkUser() {
        //get  current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            //user not logged in
            startActivity(new Intent(this,MainActivity.class));
            finish();

        }
        else{
            //user logged in
            //get user info
            String email =firebaseUser.getEmail();
            //set Email
            binding.emailTv.setText(email);

        }
    }
}