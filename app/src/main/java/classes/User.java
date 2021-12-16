package classes;
/**
 * Created by Jordan Perez on 15/12/2021
 */

/*********** IMPORTS ***********/
import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/*********** CLASS ***********/
public class User {

    /*********** VARIABLES ***********/

    private String userId; //obtained by signing in to Google
    private String name; //obtained by signing in to Google
    private Uri profilePicture; //obtained by signing in to Google
    private String email; //obtained by signing in to Google

    private boolean isSeller;
    private boolean isBuyer;
    private String address;


    /*********** CONSTRUCTORS ***********/

    //Default Constructor
    public User(){};

    //Constructor
    public User(String userId, String name, Uri profilePicture, String email) {
        this.userId = userId;
        this.name = name;
        this.profilePicture = profilePicture;
        this.email = email;
        this.isSeller = false;
        this.isBuyer = false;
    }

    /*********** METHODS ***********/



    /*********** GETTERS AND SETTERS ***********/
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Uri profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public boolean isBuyer() {
        return isBuyer;
    }

    public void setBuyer(boolean buyer) {
        isBuyer = buyer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
