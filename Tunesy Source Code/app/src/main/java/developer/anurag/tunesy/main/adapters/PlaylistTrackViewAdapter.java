package developer.anurag.tunesy.main.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.session.MediaController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.main.listeners.PlaylistItemTouchHelperAdapterListener;
import developer.anurag.tunesy.main.listeners.PlaylistTrackViewAdapterListener;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.main.utils.InternetImageLoaderUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class PlaylistTrackViewAdapter extends RecyclerView.Adapter<PlaylistTrackViewAdapter.PlaylistTrackViewHolder> implements PlaylistItemTouchHelperAdapterListener {
    private final Context context;
    private List<Track> trackList;
    private PlaylistTrackViewAdapterListener myListener;
    private int playingIndex=0;
    private MediaController mediaController;

    public PlaylistTrackViewAdapter(Context context, List<Track> trackList) {
        this.context = context;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public PlaylistTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.playing_playlist_track_view,parent,false);
        return new PlaylistTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistTrackViewHolder holder, int position) {
        Track track=trackList.get(position);
        holder.trackTitleTv.setText(track.getTitle());
        holder.trackArtistNameTv.setText(track.getArtists());
        InternetImageLoaderUtil.loadImage(this.context,holder.trackArtworkIV,track.getArtworkUri(), ConverterUtil.convertDpToPixel(this.context,50));

        if(this.myListener!=null){
            holder.mainContainerCL.setOnClickListener(v->{
                this.myListener.onClick(position);
            });
        }

        if(position==this.playingIndex){
            holder.mainContainerCL.setBackgroundResource(R.drawable.playing_track_bg);
        }
        else holder.mainContainerCL.setBackgroundResource(R.drawable.transparent_clickable_bg);


    }

    @Override
    public int getItemCount() {
        return this.trackList.size();
    }

    public void addListener(PlaylistTrackViewAdapterListener myListener){
        this.myListener=myListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        this.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPlayingIndex(int playingIndex) {
        this.playingIndex = playingIndex;
        this.notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.trackList, i, i + 1);
                this.mediaController.moveMediaItem(i,i+1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(this.trackList, i, i - 1);
                this.mediaController.moveMediaItem(i,i-1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        this.mediaController.removeMediaItem(position);
        this.trackList.remove(position);
        notifyItemRemoved(position);
    }

    public void setMediaController(MediaController mediaController) {
        this.mediaController = mediaController;
    }

    public static class PlaylistTrackViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackTitleTv,trackArtistNameTv;
        private final ImageView trackArtworkIV;
        private final ConstraintLayout mainContainerCL;
        public PlaylistTrackViewHolder(@NonNull View view) {
            super(view);

            this.trackTitleTv=view.findViewById(R.id.pptvTrackTitleTV);
            this.trackArtistNameTv=view.findViewById(R.id.pptvTrackArtistNameTV);
            this.trackArtworkIV=view.findViewById(R.id.pptvTrackArtworkIV);
            this.mainContainerCL=view.findViewById(R.id.pptvMainContainerCL);
        }
    }
}
