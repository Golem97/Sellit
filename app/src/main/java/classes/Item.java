package classes;

import android.net.Uri;

/**
 * Created by Jordan Perez on 15/12/2021
 */
public class Item {

    private String user_id;
    private String name;
    private String description;
    private double price;
    private boolean available;
    private String photoUrl;

    public Item(String user_id, String name, String description, double price, boolean available, String photoUrl) {
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
        this.photoUrl = photoUrl;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) { this.available = available; }

    public String getPhotoUrl() { return photoUrl; }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}