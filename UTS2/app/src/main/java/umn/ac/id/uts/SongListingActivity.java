package umn.ac.id.uts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class SongListingActivity extends AppCompatActivity {

    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_listing);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        btnOk = popupView.findViewById(R.id.ok);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        new Handler().postDelayed(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(findViewById(R.id.musiclist), Gravity.CENTER, 0, 0);
            }
        }, 100);

        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });
    }
}