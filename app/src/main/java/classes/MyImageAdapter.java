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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.activities.MyItemsActivity;
import nath.ariel.sellit_v6.activities.ProfileActivity;


/**
 * Created by Jordan Perez on 21/12/2021
 */
public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ImageViewHolder>{

    private Context mContext; //activity
    private List<Item> mItems; // list of items

    private StorageReference storedImage;
    private DatabaseReference imageRef;

    //constructor
    public MyImageAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    //empty constructor
    public MyImageAdapter() {}

    //Return new matching ImageViewHolder
    @NonNull
    @Override
    public MyImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_myimage, parent, false);
        return new MyImageAdapter.ImageViewHolder(v);
    }

    //Displays item title, item price and image
    @Override
    public void onBindViewHolder(MyImageAdapter.ImageViewHolder holder, int position) {
        Item currentItem = mItems.get(position);

        //name
        holder.textViewName.setText(currentItem.getName());

        //price
        String priceText = String.valueOf(currentItem.getPrice());
        holder.textViewPrice.setText((priceText) + " $");

        //picture
        Glide.with(mContext)
                .load(currentItem.getPhotoUrl())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .fitCenter()
                .into(holder.imageView);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create Alert
                AlertDialog dlg = new AlertDialog.Builder(mContext)
                        .setTitle("Warning !")
                        .setMessage("Are you sure you want to delete this item ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //dismiss alert box and delete item
                                dialog.dismiss();
                                //delete
                                //get realtime database item path
                                imageRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                                        .getReference("Sellit/Items/"+currentItem.getItem_id());

                                //get storage photo url
                                storedImage = FirebaseStorage.getInstance("gs://sell-86b95.appspot.com")
                                        .getReferenceFromUrl(currentItem.getPhotoUrl());

                                //if successfully deleted from storage
                                storedImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //delete from realtime database
                                        imageRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(mContext, "item deleted", Toast.LENGTH_SHORT).show();

                                                //refresh my items
                                                Intent intent = new Intent(mContext, ProfileActivity.class);
                                                mContext.startActivity(intent);
                                            }
                                        });
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
        public Button deleteBtn;

        //its constructor
        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name_my_image);
            textViewPrice = itemView.findViewById(R.id.text_view_price_my_image);
            imageView = itemView.findViewById(R.id.image_view_upload_my_image);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

}
