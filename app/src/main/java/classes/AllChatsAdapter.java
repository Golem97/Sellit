package classes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.activities.AllChatsActivity;
import nath.ariel.sellit_v6.activities.ChatActivity;

/**
 * Created by Jordan Perez on 26/12/2021
 */
public class AllChatsAdapter extends RecyclerView.Adapter<AllChatsAdapter.ImageViewHolder> {
    private static final String TAG = "ALL_CHATS_ADAPTER";


    private Context mContext;
    private List<User> mUsers;

    private DatabaseReference userRef;


    public AllChatsAdapter(Context context, List<User> users) {
        mContext = context;
        mUsers = users;
    }

    public AllChatsAdapter() {
    }

    //Return matching ImageViewHolder
    @NonNull
    @Override
    public AllChatsAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_allchatscardview, parent, false);
        return new AllChatsAdapter.ImageViewHolder(v);
    }

    //Displays users
    @Override
    public void onBindViewHolder(AllChatsAdapter.ImageViewHolder holder, int position) {
        User currentUser = mUsers.get(position);

        userRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Users/");
        Log.d(TAG, "currentUser = "+currentUser.toString());
        Log.d(TAG, "currentUser.getName = "+currentUser.getName());

        holder.textViewName.setText(currentUser.getName());


        String picUrl = String.valueOf(currentUser.getProfilePicture());
        Glide.with(mContext)
                .load(picUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profilePic);

        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save currentUser id
                String sellerId = currentUser.getUserId();
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("sellerId",sellerId);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public void setUsers(List<User> users) {
        mUsers = users;
    }

    //ImageViewHolder Inner Class
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public Button chatBtn;
        public ImageView profilePic;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name_AllChatsCardView);
            chatBtn= itemView.findViewById(R.id.startChatBtnAllChatsCardView);
            profilePic = itemView.findViewById(R.id.profileImageAllChatsCardView);
        }
    }
}
