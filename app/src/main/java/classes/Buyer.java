package classes;

import android.net.Uri;

import java.util.List;

/**
 * Created by Jordan Perez on 15/12/2021
 */
public class Buyer extends User {

    private List<Item> buyHistory;
    private List<Item> wishlist;

    public Buyer(String userId, String name, Uri profilePicture, String email) {
        super(userId, name, profilePicture, email);
    }

//    private boolean addToWishList(Item i){
//        //add items (still available) to buyer's wishlist
//        return true;
//    }
//
//    private boolean requestBuy(Item i){
//        //send a buy request to the seller
//        //set i.request to true
//        //if seller
//        //if seller accepts, the item is removed from sellPending and is added to buyer's buyHistory List
//        return true;
//    }
}
