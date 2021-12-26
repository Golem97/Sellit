package classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.activities.ProfileActivity;

/**
 * Created by Jordan Perez on 26/12/2021
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ImageViewHolder> {


    private Context mContext;
    private List<Message> mMessages;

    private DatabaseReference itemRef;
    private DatabaseReference userRef;


    public MessageAdapter(Context context, List<Message> messages) {
        mContext = context;
        mMessages = messages;
    }

    public MessageAdapter() {
    }

    //Return matching ImageViewHolder
    @NonNull
    @Override
    public MessageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_message, parent, false);
        return new MessageAdapter.ImageViewHolder(v);
    }

    //Displays item title, item price and image
    @Override
    public void onBindViewHolder(MessageAdapter.ImageViewHolder holder, int position) {
        Message currentMessage = mMessages.get(position);



        userRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Users/");

        String userid = currentMessage.getUser_sender_id();

            holder.textViewTime.setText(currentMessage.getDate());

            holder.textViewMessage.setText(currentMessage.getContent());



        Task<DataSnapshot> userData = userRef.child(userid).get();
        userData.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String username = String.valueOf(dataSnapshot.child("name").getValue());

                String picUrl = String.valueOf(dataSnapshot.child("profilePicture").getValue());

                Glide.with(mContext)
                        .load(picUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.profilePic);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public List<Message> getMessages() {
        return mMessages;
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
    }


    //ImageViewHolder Inner Class
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTime;
        public TextView textViewMessage;
        public ImageView profilePic;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewMessage= itemView.findViewById(R.id.text_view_message);
            profilePic = itemView.findViewById(R.id.profileImageChat);
        }
    }
}
