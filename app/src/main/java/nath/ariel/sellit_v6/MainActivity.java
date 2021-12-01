package nath.ariel.sellit_v6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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

import nath.ariel.sellit_v6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //view binding
    private ActivityMainBinding binding ;
    private static final int RC_SIGN_IN =100;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth ;
    private static final String TAG ="GOOGLE_SIGN_IN_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //configure the Google Sign in
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
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
        //if user already loggded in go to user activity
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            Log.d(TAG, "checkUser: Already logged in ");
            startActivity(new Intent(this,ProfilActivity.class));
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
                //Indentify with success , now auth  with firebase
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
                
            }
            catch (Exception e){
                Log.d(TAG, "onActivityResult: "+e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: bgin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //loginSuccess
                        Log.d(TAG, "onSuccess: Logged  In");
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        //get  User Info
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        Log.d(TAG, "onSuccess: Email "+email);
                        Log.d(TAG, "onSuccess: Uid "+uid);
                        //check if user is new or existing

                        if (authResult.getAdditionalUserInfo().isNewUser()){
                            //User is new : creat new account
                            Log.d(TAG, "onSuccess: Account Created");
                            Toast.makeText(MainActivity.this, "Account created ..\n"+email, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //existing User _Logged In
                            Log.d(TAG, "onSuccess: Existing User ..."+email);
                            Toast.makeText(MainActivity.this, "Existing User ..\n"+email, Toast.LENGTH_SHORT).show();

                        }

                        //Start profil Activity
                        startActivity(new Intent(MainActivity.this ,ProfilActivity.class));
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