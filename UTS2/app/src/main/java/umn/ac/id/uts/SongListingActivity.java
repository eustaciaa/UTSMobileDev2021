package umn.ac.id.uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class SongListingActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static ArrayList<Song> songList = new ArrayList<>();

    RecyclerView rvSongList;
    SongListAdapter mAdapter;
    PopupMenu popup;

    AssetFileDescriptor afd;
    MediaMetadataRetriever metaRetriver;
    byte[] byteCover;

    LinearLayout back_dim_layout;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_listing);
        getPermission();

        back_dim_layout = (LinearLayout) findViewById(R.id.musiclist);

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

        Intent intent = getIntent();
        String isLoginActivity = intent.getStringExtra("isLoginActivity");

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(isLoginActivity.equals("1")) {
                    popupWindow.showAtLocation(findViewById(R.id.musiclist), Gravity.CENTER, 0, 0);
                    back_dim_layout.setAlpha((float) 0.5);
                }
            }
        }, 1000);

        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
                back_dim_layout.setAlpha((float) 1);
            }
        });
    }

    public static ArrayList<Song> fillSongList(Context context){
        ArrayList<Song> tempSongList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                Song songFiles = new Song(title, artist, album, path, duration);
                Log.e("Path: " + path, "Album: " + album);
                tempSongList.add(songFiles);
            }
            cursor.close();
        }
        return tempSongList;
    }

    private void getPermission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SongListingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
        else{
//            Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
            songList = fillSongList(this);

            rvSongList = (RecyclerView) findViewById(R.id.recyclerView);
            rvSongList.setHasFixedSize(true);
            if(!(songList.size() < 1)){
                Log.w("songList size is not fewer than 1", "true");
                mAdapter = new SongListAdapter(this, songList);
                rvSongList.setAdapter(mAdapter);
                rvSongList.setLayoutManager(new LinearLayoutManager(this));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                songList = fillSongList(this);

                rvSongList = (RecyclerView) findViewById(R.id.recyclerView);
                rvSongList.setHasFixedSize(true);
                if(!(songList.size() < 1)){
                    Log.w("songList size is not fewer than 1", "true");
                    mAdapter = new SongListAdapter(this, songList);
                    rvSongList.setAdapter(mAdapter);
                    rvSongList.setLayoutManager(new LinearLayoutManager(this));
                }
            }
            else{
                ActivityCompat.requestPermissions(SongListingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            }
        }
    }

    public void showPopup(View v) {
        popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more_menu, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_menu:
                        Intent intentProfile = new Intent(SongListingActivity.this, ProfileActivity.class);
                        startActivityForResult(intentProfile, 1);
                        return true;
                    case R.id.logout_menu:
                        Intent intentMain = new Intent(SongListingActivity.this, MainActivity.class);
                        startActivityForResult(intentMain, 1);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}