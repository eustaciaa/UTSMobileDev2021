package umn.ac.id.uts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

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
                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intentLogin, 1);
            }
        });
    }
}