package classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import nath.ariel.sellit_v6.activities.AdminActivity;
import nath.ariel.sellit_v6.activities.ChatActivity;
import nath.ariel.sellit_v6.activities.ProfileActivity;

/**
 * Created by Jordan Perez on 19/12/2021
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {


    private Context mContext;
    private List<Item> mItems;

    private DatabaseReference itemRef;
    private DatabaseReference userRef;


    public ImageAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    public ImageAdapter() {
    }

    //Return matching ImageViewHolder
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_image, parent, false);
        return new ImageViewHolder(v);
    }

    //Displays item title, item price and image
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Item currentItem = mItems.get(position);

        holder.textViewName.setText(currentItem.getName());

        String priceText = String.valueOf(currentItem.getPrice());
        holder.textViewPrice.setText((priceText) + " $");

        Glide.with(mContext)
                .load(currentItem.getPhotoUrl())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .fitCenter()
                .into(holder.imageView);

        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    //save currentItem seller's id
                    String sellerId = currentItem.getUser_id();
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("sellerId",sellerId);
                    mContext.startActivity(intent);

            }
        });

        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create Alert
                AlertDialog dlg = new AlertDialog.Builder(mContext)
                        .setTitle("Warning !")
                        .setMessage("Are you sure you want to buy this item ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //dismiss alert box and delete item
                                dialog.dismiss();

                                //check if user balance is enough
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String userId = currentUser.getUid();

                                userRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                                        .getReference("Sellit/Users/");
                                itemRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                                        .getReference("Sellit/Items/");


                                Task<DataSnapshot> snap = userRef.get();
                                snap.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        double currentBalance = Double.parseDouble(dataSnapshot.child(userId).child("balance").getValue().toString());

                                        //If buyer has enough money in account
                                        if(currentItem.getPrice() <= currentBalance ){
                                            //proceed to transaction, update balances accordingly and delete item

                                            //update currentUser balance in database:
                                            double updatedBuyerBalance = currentBalance-currentItem.getPrice();
                                            userRef.child(userId).child("balance").setValue(updatedBuyerBalance);

                                            //update seller's balance in database:
                                            String sellerId = currentItem.getUser_id();
                                            double sellerBalance = Double.parseDouble(dataSnapshot.child(sellerId).child("balance").getValue().toString());
                                            double updatedSellerBalance = sellerBalance+currentItem.getPrice();
                                            userRef.child(sellerId).child("balance").setValue(updatedSellerBalance);

                                            //delete item
                                            itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    //DELETE ITEM FROM STORAGE
                                                    StorageReference storage = FirebaseStorage.getInstance("gs://sell-86b95.appspot.com")
                                                            .getReference("Sellit/Items/").child(sellerId).child(currentItem.getStorageId());
                                                    storage.delete();

                                                    //DELETE ITEMS FROM REALTIME DATABASE
                                                    //if item belongs to this user
                                                    itemRef.child(currentItem.getItem_id()).removeValue();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });

                                            Toast.makeText(mContext, "Transaction Successful", Toast.LENGTH_SHORT).show();

                                            //TODO: notification to seller: item sold

                                            Intent intent = new Intent(mContext, ProfileActivity.class);
                                            mContext.startActivity(intent);

                                        }
                                        else{
                                            Toast.makeText(mContext, "Insufficient Balance", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //just dismiss alert box without deleting item
                                dialog.dismiss();
                            }
                        })
                        .create();
                dlg.show();
            }
        });
    }



    //Returns number of items in list
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        mItems = items;
    }

    //ImageViewHolder Inner Class
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewPrice;
        public ImageView imageView;
        public Button buyBtn;
        public Button chatBtn;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            imageView = itemView.findViewById(R.id.image_view_upload);
            buyBtn = itemView.findViewById(R.id.buyBtn);
            chatBtn = itemView.findViewById(R.id.chatBtn);
        }
    }
}
