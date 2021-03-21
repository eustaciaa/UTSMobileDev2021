package umn.ac.id.uts;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Glide.with(this).asBitmap().load(R.drawable.profile_photo).override(1080, 600).into((ImageView) findViewById(R.id.profileImage));
    }
}