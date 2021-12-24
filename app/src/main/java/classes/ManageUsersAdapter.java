package classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.activities.AdminActivity;

/**
 * Created by Jordan Perez on 24/12/2021
 */
public class ManageUsersAdapter extends RecyclerView.Adapter<ManageUsersAdapter.ImageViewHolder>{

    private Context mContext; //activity
    private List<User> mUsers; // list of items

    private StorageReference storedImage;
    private DatabaseReference userRef;

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

        holder.textEnterBalance.addTextChangedListener(mTextWatcher);

        holder.changeBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                        .getReference("Sellit/Users/"+curentUser.getUserId());
                //TODO: update user balance

                // Attach a listener to read the data at our posts reference
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String currentBalance = String.valueOf(dataSnapshot.getValue());


                        String addedBalance = holder.textEnterBalance.getText().toString().trim();
                        double updatedBalance = Double.parseDouble(currentBalance);

                        double bal = Double.parseDouble(currentBalance);
                        //double updatedBalance = currentBalance - bal;

//                        if (updatedBalance < 0 ){
//                            Toast.makeText(mContext, "User has insufficient balance", Toast.LENGTH_SHORT).show();
//                        }
//                        else{

                        User user = dataSnapshot.getValue(User.class);
                        user.setBalance(updatedBalance);
                        userRef.updateChildren(curentUser.getUserId().toString(), user);
                        //userRef.updateChildren("balance",updatedBalance);
                            //dataSnapshot(updatedBalance);
                        Toast.makeText(mContext, "datasnapshot value = "+dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                       // Toast.makeText(mContext, "userRef = "+userRef.toString(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(mContext, "Balance Updated", Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

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
                                //delete
                                //get realtime database item path
                                userRef = FirebaseDatabase.getInstance("https://sell-86b95-default-rtdb.europe-west1.firebasedatabase.app")
                                        .getReference("Sellit/Users/"+curentUser.getUserId());

                                //TODO: delete all user's items??

                                //delete user from realtime database
                                userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(mContext, "user deleted", Toast.LENGTH_SHORT).show();

                                        //refresh my items
                                        Intent intent = new Intent(mContext, AdminActivity.class);
                                        mContext.startActivity(intent);
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
