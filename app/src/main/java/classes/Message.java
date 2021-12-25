package classes;



import android.net.Uri;

import com.google.firebase.database.Exclude;

/**

 */
public class Message {

    private String message_id;
    private String user_sender_id;
    private String user_reciever_id;
    private String date;
    private String content;

    //Empty Constructor needed
    public Message() {
    }

    //Constructor
    public Message(String user_sender_id, String user_reciever_id, String date, String content) {
        this.message_id="";
        this.user_sender_id = user_sender_id;
        this.user_reciever_id = user_reciever_id;
        this.date = date;
        this.content = content;
    }
    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSender_id() {
        return user_sender_id;
    }

    public void setSender_id(String user_sender_id) {
        this.user_sender_id = user_sender_id;
    }

    public String getReciever() {
        return user_reciever_id;
    }

    public void setReciever(String user_reciever_id) { this.user_reciever_id = user_reciever_id; }

    public String date() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String content() {
        return content;
    }

    public void setContent(String content) { this.content = content; }

}
