package classes;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jordan Perez on 16/12/2021
 */


public class DAOUser {

    private static final String TAG ="DAOUser";
    private DatabaseReference databaseReference;

    public DAOUser(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Users");

    }

    public Task<Void> addUserToFirebase(User user) {
        //TODO: validate security rules in the firebase console
//        if (user == null ){
//            Log.d(TAG, "addUserToFirebase: user == null");
//            //throw exception
//        }

        return databaseReference.push().setValue(user);

    }
}

// 2em option:
//import { getDatabase, ref, set } from "firebase/database";
//
//        function writeUserData(userId, name, email, imageUrl) {
//        const db = getDatabase();
//        set(ref(db, 'users/' + userId), {
//        username: name,
//        email: email,
//        profile_picture : imageUrl
//        });
