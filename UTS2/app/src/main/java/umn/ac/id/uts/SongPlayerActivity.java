package umn.ac.id.uts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static umn.ac.id.uts.SongListingActivity.songList;

public class SongPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    TextView tvTitle, tvArtist, tvAlbum, tvDurationPlayed, tvDurationTotal;
    ImageView ivCover, ivNextBtn, ivPrevBtn, ivBackBtn, ivShufBtn, ivReptBtn;
    FloatingActionButton playPauseBtn;
    SeekBar skbrDuration;
    int position = -1;
    static ArrayList<Song> songs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);

        tvTitle = findViewById(R.id.playerTitle);
        tvArtist = findViewById(R.id.playerArtist);
        tvAlbum = findViewById(R.id.playerAlbum);
        tvDurationPlayed = findViewById(R.id.durationPlayed);
        tvDurationTotal = findViewById(R.id.durationTotal);

        ivCover = findViewById(R.id.playerCover);
        ivNextBtn = findViewById(R.id.next);
        ivPrevBtn = findViewById(R.id.prev);
        ivBackBtn = findViewById(R.id.backBtn);
        ivShufBtn = findViewById(R.id.shuffle);
        ivReptBtn = findViewById(R.id.repeat);

        playPauseBtn = findViewById(R.id.play);
        skbrDuration = findViewById(R.id.playerSeekBar);

        getIntentMethod();

        setTitleAndArtist(position);
        mediaPlayer.setOnCompletionListener(this);

        skbrDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SongPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    skbrDuration.setProgress(currentPosition);
                    tvDurationPlayed.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });

        ivBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mediaPlayer.isPlaying()){
                    playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                    skbrDuration.setMax(mediaPlayer.getDuration() / 1000);

                    SongPlayerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer != null){
                                int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                skbrDuration.setProgress(currentPosition);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                }
                Intent intentSongListing = new Intent(SongPlayerActivity.this, SongListingActivity.class);
                intentSongListing.putExtra("isLoginActivity","0");
                startActivityForResult(intentSongListing, 1);
            }
        });
    }

    private void setTitleAndArtist(int position) {
        tvTitle.setText(songs.get(position).getTitle());
        tvArtist.setText(songs.get(position).getSinger());
        tvAlbum.setText(songs.get(position).getAlbum());
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                ivPrevBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();

            position = ((position - 1) % songs.size());
            uri = Uri.parse(songs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);

            setTitleAndArtist(position);
            skbrDuration.setMax(mediaPlayer.getDuration() / 1000);
            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        skbrDuration.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);

            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();

            position = ((position - 1) % songs.size());
            uri = Uri.parse(songs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);

            setTitleAndArtist(position);
            skbrDuration.setMax(mediaPlayer.getDuration() / 1000);
            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        skbrDuration.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);

            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//            mediaPlayer.start();
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                ivNextBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();

            position = ((position + 1) % songs.size());
            uri = Uri.parse(songs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);

            setTitleAndArtist(position);
            skbrDuration.setMax(mediaPlayer.getDuration() / 1000);
            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        skbrDuration.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);

            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();

            position = ((position + 1) % songs.size());
            uri = Uri.parse(songs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);

            setTitleAndArtist(position);
            skbrDuration.setMax(mediaPlayer.getDuration() / 1000);
            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        skbrDuration.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);

            playPauseBtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//            mediaPlayer.start();
        }
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            mediaPlayer.pause();
            skbrDuration.setMax(mediaPlayer.getDuration() / 1000);

            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        skbrDuration.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
        else{
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
            skbrDuration.setMax(mediaPlayer.getDuration() / 1000);

            SongPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        skbrDuration.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private String formattedTime(int currentPosition) {
        String totalOut = "", totalNew = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;

        if(seconds.length() == 1){
            return totalNew;
        }
        else{
            return totalOut;
        }

    }

    private void getIntentMethod(){
        position = getIntent().getIntExtra("position", -1);
        songs = songList;

        if(songs != null){
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_24);
            uri = Uri.parse(songs.get(position).getPath());
        }

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }

        skbrDuration.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(uri.toString());

        int durationTotal = Integer.parseInt(songs.get(position).getDuration()) / 1000;
        tvDurationTotal.setText(formattedTime(durationTotal));

        byte[] cover = mmr.getEmbeddedPicture();
        if(cover != null){
            Glide.with(this).asBitmap().load(cover).into(ivCover);
        }
        else{
            Glide.with(this).asBitmap().load(R.drawable.grey).into(ivCover);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
        if(mediaPlayer != null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}