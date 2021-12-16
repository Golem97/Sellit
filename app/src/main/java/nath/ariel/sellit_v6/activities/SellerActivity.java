package nath.ariel.sellit_v6.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.databinding.ActivityMainBinding;
import nath.ariel.sellit_v6.databinding.ActivityProfilBinding;
import nath.ariel.sellit_v6.databinding.ActivitySellerBinding;


public class SellerActivity extends AppCompatActivity {

    private ActivitySellerBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        //binding to layout
        binding = ActivitySellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
}