package classes;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Jordan Perez on 15/12/2021
 */
public class Item {

    private String item_id;
    private String user_id;
    private String name;
    private String description;
    private double price;
    private boolean available;
    private String photoUrl;
    private String storageId;


    //Empty Constructor needed
    public Item() {
    }

    //Constructor
    public Item(String user_id, String name, String description, double price, boolean available, String photoUrl,String storageId) {
        this.item_id="";
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
        this.photoUrl = photoUrl;
        this.storageId = storageId;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) { this.price = price; }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageName(String storageId) {
        this.storageId = storageId;
    }

}