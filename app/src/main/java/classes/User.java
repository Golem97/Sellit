package classes;
/**
 * Created by Jordan Perez on 15/12/2021
 */

/*********** IMPORTS ***********/
import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


/*********** CLASS ***********/
public class User {

    /*********** VARIABLES ***********/

    private String userId; //obtained by signing in to Google
    private String name; //obtained by signing in to Google
    private String profilePicture; //obtained by signing in to Google

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }

    private String email; //obtained by signing in to Google
    private double balance;

    //TODO: Chats
    //private Chat<Messages> chat;



    /*********** CONSTRUCTORS ***********/

    //Default Constructor
    public User(){};

    //Constructor
    public User(String userId, String name, String profilePicture, String email, double balance) {
        this.userId = userId;
        this.name = name;
        this.profilePicture = profilePicture;
        this.email = email;
        this.balance = balance;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
