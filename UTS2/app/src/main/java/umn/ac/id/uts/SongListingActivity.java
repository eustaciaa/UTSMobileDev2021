package umn.ac.id.uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class SongListingActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    static ArrayList<Song> songList = new ArrayList<>();

    RecyclerView rvSongList;
    SongListAdapter mAdapter;

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


        new Handler().postDelayed(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(findViewById(R.id.musiclist), Gravity.CENTER, 0, 0);
                back_dim_layout.setAlpha((float) 0.5);
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
//        metaRetriver = new MediaMetadataRetriever();
//        metaRetriver.setDataSource(getResources().openRawResourceFd(R.raw.bibi_kazino).getFileDescriptor());
//        try {
//            byteCover = metaRetriver.getEmbeddedPicture();
//            songList.add(new Song(
//                            metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
//                            metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
//                            metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION),
//                            metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
//                            BitmapFactory.decodeByteArray(byteCover, 0, byteCover.length)
//            ));
////            Bitmap songImage = BitmapFactory.decodeByteArray(byteCover, 0, byteCover.length);
//        } catch (Exception e) {
//            songList.add(new Song("Unknown Title", "Unknown Artist", "Unknown Duration", "Unknown Album", BitmapFactory.decodeResource(getResources(), R.drawable.grey)));
//        }
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
}