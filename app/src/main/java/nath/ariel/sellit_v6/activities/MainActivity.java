package nath.ariel.sellit_v6.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import classes.User;
import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //TODO: notifications
    //TODO: Delete
    //TODO: diroug
    //TODO:transactions
    //TODO: Chat
    //TODO: User admin

    //view binding
    private ActivityMainBinding binding ;
    private static final int RC_SIGN_IN =100;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth ;
    private static final String TAG ="GOOGLE_SIGN_IN_TAG";

    private boolean isAdmin=false;
    private DatabaseReference usersPathDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding to layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //configure the Google Sign in
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Checks if User exists
        // if he does, it determines if its an admin or a customer and set global variable isAdmin accordingly
        // plus, if he exists, it redirects him to the proper activity
        checkUser();

        //Google Sign In btn
        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //begin Google sign in
                Log.d(TAG, "onClick: begin Google SignIn");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

    }

    private void checkUser() {
        //if user already logged in go to user activity
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            Log.d(TAG, "checkUser: Already logged in ");

                startActivity(new Intent(this, TempActivity.class));
                finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result returned from lunching the Intent GoogleSignInApi.getSignInIntent (...);
        if(requestCode==RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google Sign in Intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Identify with success , now auth with firebase
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);

            }
            catch (Exception e){
                Log.d(TAG, "onActivityResult: "+e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //loginSuccess
                        Log.d(TAG, "onSuccess: Logged  In");

                        //Get User
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        //Get User's mail
                        String email = firebaseUser.getEmail();

                        //check if user is new or existing
                        if (authResult.getAdditionalUserInfo().isNewUser()){

                            //get User Informations
                            String uid = firebaseUser.getUid();
                            String profilePic = firebaseUser.getPhotoUrl().toString();
                            String name = firebaseUser.getDisplayName();

                            //Create User
                            User user = new User(uid,name,profilePic,email);

                            usersPathDatabase = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                                    .getReference("Sellit").child("Users");

                            usersPathDatabase.push().setValue(user);


                            Log.d(TAG, "onSuccess: Account Created");
                            Toast.makeText(MainActivity.this, "Account created ..\n"+email, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //existing User
                            Log.d(TAG, "onSuccess: Existing User ..."+email);
                            Toast.makeText(MainActivity.this, "Existing User ..\n"+email, Toast.LENGTH_SHORT).show();
                        }

                        startActivity(new Intent(MainActivity.this , TempActivity.class));
                        finish();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //logg failed
                        Log.d(TAG, "onFailure: Failed Signin "+e.getMessage());
                    }
                });
    }

}