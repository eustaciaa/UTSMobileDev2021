package umn.ac.id.uts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edtUsername, edtPassword;
    String username_valid = "uasmobile";
    Integer password_valid = -1058311594;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.realLogin);
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(edtUsername.getText().toString().equals(username_valid) == true && edtPassword.getText().toString().hashCode() == password_valid) {
                    Intent intentMusicListing = new Intent(LoginActivity.this, SongListingActivity.class);
                    startActivityForResult(intentMusicListing, 1);
                }
                else{
                    Toast.makeText(getApplicationContext(), "The username and password combination is not valid", Toast.LENGTH_LONG).show();
                    Log.d("LoginFailed","username and password combination is not valid");
                }
            }
        });
    }
}