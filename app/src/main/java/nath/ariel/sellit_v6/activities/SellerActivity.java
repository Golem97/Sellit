package nath.ariel.sellit_v6.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import classes.Item;
import nath.ariel.sellit_v6.R;

import nath.ariel.sellit_v6.databinding.ActivitySellerBinding;


public class SellerActivity extends AppCompatActivity {

    private DatabaseReference database;


    private ActivitySellerBinding binding ;
    private FirebaseAuth firebaseAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        //binding to layout
        binding = ActivitySellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        database = FirebaseDatabase.getInstance().getReference();

        //handle click on logout button
        binding.itemUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Item(String user_id, String name, String description, int price, boolean available, String photoUrl)
                String name = String.valueOf(binding.productTitle.getText());
                String descript = String.valueOf(binding.descript.getText());
                int price = binding.price.getInputType();
                Item item = new Item(firebaseAuth.getUid(), name,descript, price, true,
                        "www.stam.co.il");

                //upload to fire base items
                System.out.println(binding.productTitle.getText()+" "+binding.descript.getText()+" "+binding.price.getText()+" "
                        +"www.stam.co.il" + firebaseAuth.getUid());
                System.out.println("database reference: "+database);
                database.child("Sellit").child("Items").setValue(item);
            }
        });
    }

}