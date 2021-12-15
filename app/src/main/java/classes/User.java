package classes;

import android.net.Uri;

/**
 * Created by Jordan Perez on 15/12/2021
 */
public class User {


    private String userId;
    private String name;
    private Uri profilePicture;
    private String email;
    private boolean isSeller;
    private boolean isBuyer;

    private String address;


    public User(String userId, String name, Uri profilePicture, String email) {
        this.userId = userId;
        this.name = name;
        this.profilePicture = profilePicture;
        this.email = email;
        this.isSeller = false;
        this.isBuyer = false;


        //ADD USER TO FIREBASE DATABASE:

    }
}
