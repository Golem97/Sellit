package nath.ariel.sellit_v6.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import nath.ariel.sellit_v6.R;
import nath.ariel.sellit_v6.databinding.ActivityBuyerBinding;
import nath.ariel.sellit_v6.databinding.ActivityMainBinding;
import nath.ariel.sellit_v6.databinding.ActivityProfilBinding;

public class BuyerActivity extends AppCompatActivity {

    private ActivityBuyerBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        //binding to layout
        binding = ActivityBuyerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Disable Landscape Mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
}