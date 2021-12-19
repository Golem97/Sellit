package classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import nath.ariel.sellit_v6.R;


/**
 * Created by Jordan Perez on 19/12/2021
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {


    private Context mContext;
    private List<Item> mItems;

    public ImageAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    public ImageAdapter() { }

    //Return matching ImageViewHolder
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_image,parent,false);
        return new ImageViewHolder(v);
    }

    //Get data out of uploaded item
   /* @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Item itemCurrent = mItems.get(position);
        holder.textViewName.setText(itemCurrent.getName());

        Glide.with(mContext)
                .load(itemCurrent.getPhotoUrl())
                .fitCenter()
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);
    }*/
     @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Item uploadCurrent = mItems.get(position);
        holder.textViewName.setText(uploadCurrent.getName());

        String priceText = String.valueOf(uploadCurrent.getPrice());
        holder.textViewPrice.setText((priceText));


        Glide.with(mContext)
                .load(uploadCurrent.getPhotoUrl())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .fitCenter()
                .into(holder.imageView);
    }


    //Returns number of items in list
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    //ImageViewHolder Inner Class
    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewPrice;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
