package umn.ac.id.uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.login);
        btnProfile = (Button) findViewById(R.id.profile);

        btnProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivityForResult(intentProfile, 1);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // JANGAN LUPA GANTI SONGLISTINGACTIVITY JADI LOGINACTIVITY
//                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                Intent intentLogin = new Intent(MainActivity.this, SongListingActivity.class);
                startActivityForResult(intentLogin, 1);
            }
        });
    }


}