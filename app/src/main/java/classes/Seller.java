package classes;

import java.util.List;

/**
 * Created by Jordan Perez on 15/12/2021
 */
public class Seller extends User {

    private List<Item> sellingItems;
    private List<Item> sellHistory;


    private boolean acceptRequest(Item i){
        //set i.inRequest = false
        //set i.available = false
        //add item to buyer's buyHistory
        //add item to Seller's sellHistory
        return true;
    }
}
