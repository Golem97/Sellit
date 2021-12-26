package classes;



import android.net.Uri;

import com.google.firebase.database.Exclude;

/**

 */
public class Message {

    private String message_id;
    private String user_sender_id;
    private String user_receiver_id;
    private String date;
    private String content;

    //Empty Constructor needed
    public Message() {
    }

    //Constructor
    public Message(String user_sender_id, String user_receiver_id, String date, String content) {
        this.message_id="";
        this.user_sender_id = user_sender_id;
        this.user_receiver_id = user_receiver_id;
        this.date = date;
        this.content = content;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getUser_sender_id() {
        return user_sender_id;
    }

    public void setUser_sender_id(String user_sender_id) {
        this.user_sender_id = user_sender_id;
    }

    public String getUser_receiver_id() {
        return user_receiver_id;
    }

    public void setUser_receiver_id(String user_receiver_id) {
        this.user_receiver_id = user_receiver_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
