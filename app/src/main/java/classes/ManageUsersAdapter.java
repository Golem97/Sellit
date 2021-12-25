package classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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
import nath.ariel.sellit_v6.activities.MainActivity;
import nath.ariel.sellit_v6.activities.ManageusersActivity;

/**
 * Created by Jordan Perez on 24/12/2021
 */
public class ManageUsersAdapter extends RecyclerView.Adapter<ManageUsersAdapter.ImageViewHolder>{

    //TAG
    private static final String TAG = "MANAGE_ADAPTER_IN_TAG";

    private Context mContext; //activity
    private List<User> mUsers; // list of items

    private StorageReference storedImage;
    private DatabaseReference userRef;
    private DatabaseReference itemRef;

    //constructor
    public ManageUsersAdapter(Context context, List<User> users) {
        mContext = context;
        mUsers = users;
    }

    //empty constructor
    public ManageUsersAdapter() {}

    //Return new matching ImageViewHolder
    @NonNull
    @Override
    public ManageUsersAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_managecardview, parent, false);
        return new ManageUsersAdapter.ImageViewHolder(v);
    }

    //Displays item title, item price and image
    @Override
    public void onBindViewHolder(ManageUsersAdapter.ImageViewHolder holder, int position) {
        User curentUser = mUsers.get(position);

        //name
        holder.textViewName.setText(curentUser.getName());

        //balance
        double balance = curentUser.getBalance();
        holder.textViewBalance.setText("Balance: "+(balance) + " $");


        //get user reference
        userRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Users/"+curentUser.getUserId());

        itemRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Sellit/Items");


        //check if there are no empty field and if entered price is an int
        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String mBalance = holder.textEnterBalance.getText().toString().trim();
                holder.changeBalanceBtn.setEnabled(!mBalance.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        //link editText button to mWatcher so button "change balance" can detect if it's not empty and then
        // recognize if it has to be activated
        holder.textEnterBalance.addTextChangedListener(mTextWatcher);

        //update user balance
        holder.changeBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String updatedBalanceString = holder.textEnterBalance.getText().toString().trim();
                double bal = Double.parseDouble(updatedBalanceString);

                //update user's balance in database
                userRef.child("balance").setValue(bal);
                Toast.makeText(mContext, "Balance Updated", Toast.LENGTH_SHORT).show();
            }
        });


        holder.deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create Alert
                AlertDialog dlg = new AlertDialog.Builder(mContext)
                        .setTitle("Warning !")
                        .setMessage("Are you sure you want to delete this user ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //dismiss alert box and delete item
                                dialog.dismiss();


                                itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        StorageReference storage = FirebaseStorage.getInstance("gs://sell-86b95.appspot.com")
                                                .getReference("Sellit/Items/"+curentUser.getUserId());

                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            //DELETE ITEMS FROM REALTIME DATABASE
                                            if (postSnapshot.child("user_id").getValue().equals(curentUser.getUserId())) {
                                                //if item belongs to this user
                                                itemRef.child(postSnapshot.getKey()).removeValue();

                                                //DELETE ITEM FROM STORAGE
                                                //get image name
                                                String storageName = String.valueOf(postSnapshot.child("storageId").getValue());
                                                //get item storageRef image
                                                storage.child(storageName).delete();
                                            }

                                        }


                                        //USER DELETION
                                        userRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                                                .getReference("Sellit/Users/"+curentUser.getUserId());

                                        //DELETE USER
                                        userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(mContext, "user deleted", Toast.LENGTH_SHORT).show();

                                                //refresh my items
                                                Intent intent = new Intent(mContext, AdminActivity.class);
                                                mContext.startActivity(intent);

                                                //TODO: delete user from firebase authentication
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

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
        public TextView textViewBalance;
        public EditText textEnterBalance;
        public Button deleteUserBtn;
        public Button changeBalanceBtn;

        //its constructor
        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name_manageusers);
            textViewBalance = itemView.findViewById(R.id.text_view_balance);
            textEnterBalance = itemView.findViewById(R.id.setBalanceText);
            changeBalanceBtn = itemView.findViewById(R.id.changeBalanceBtn);
            deleteUserBtn = itemView.findViewById(R.id.deleteUserBtn);
        }
    }
}
