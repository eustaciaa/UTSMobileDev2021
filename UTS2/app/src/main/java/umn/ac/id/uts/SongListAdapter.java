package umn.ac.id.uts;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ItemSongViewHolder>{

    private ArrayList<Song> mSongs;
    private LayoutInflater mInflater;
    private Context mContext;

    public SongListAdapter(Context context,
                              ArrayList<Song> songList){
        this.mContext = context;
        this.mSongs = songList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SongListAdapter.ItemSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.song_item_layout, parent , false);
        return new ItemSongViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListAdapter.ItemSongViewHolder holder, int position) {
        Song mSongList = mSongs.get(position);
        holder.tvTitle.setText(mSongList.getTitle());
        holder.tvArtist.setText(mSongList.getSinger());
        holder.tvAlbum.setText(mSongList.getAlbum());
//        holder.imageBox.setImageBitmap(mSumberVideo.getCover());
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    class ItemSongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageBox;
        private TextView tvTitle, tvArtist, tvAlbum;
        private SongListAdapter mAdapter;
        private int mPosition;
        private Song mSong;

        public ItemSongViewHolder(@NonNull View itemView, SongListAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            imageBox = (ImageView) itemView.findViewById(R.id.songImageBox);
            tvTitle = (TextView) itemView.findViewById(R.id.songTitle);
            tvArtist = (TextView) itemView.findViewById(R.id.songSinger);
            tvAlbum = (TextView) itemView.findViewById(R.id.songAlbum);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            mPosition = getLayoutPosition();
//            mSong = mDaftarVideo.get(mPosisi);
//            Intent detilInten = new Intent(mContext, DetilVideoActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("DetilVideo", mSumberVideo);
//            detilInten.putExtras(bundle);
//            mContext.startActivity(detilInten);
        }
    }
}
