package classes;

import android.net.Uri;

import java.util.List;

/**
 * Created by Jordan Perez on 15/12/2021
 */
public class Seller extends User {

    private List<Item> sellingItems;
    private List<Item> sellHistory;

    public Seller(String userId, String name, Uri profilePicture, String email) {
        super(userId, name, profilePicture, email);
    }


//    private boolean acceptRequest(Item i){
//        //set i.inRequest = false
//        //set i.available = false
//        //add item to buyer's buyHistory
//        //add item to Seller's sellHistory
//        return true;
//    }

}
