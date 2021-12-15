package classes;

import java.util.List;

/**
 * Created by Jordan Perez on 15/12/2021
 */
public class Buyer extends User {

    private List<Item> buyHistory;
    private List<Item> wishlist;

    private boolean addToWishList(Item i){
        //add items (not sold yet) to buyer's wishlist
        return true;
    }

    private boolean requestBuy(Item i){
        //send a buy request to the seller
        //set i.request to true
        //it means that it's going to add the item in a List of the seller called sellPending
        //if seller accepts, the item is removed from sellPending and is added to buyer's buyHistory List
        return true;
    }

}
