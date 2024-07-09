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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.main.listeners.PlayingTrackViewAdapterListener;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.main.utils.InternetImageLoaderUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class PlayingTrackViewAdapter extends RecyclerView.Adapter<PlayingTrackViewAdapter.PlayTrackViewHolder> {
    private final Context context;
    private List<Track> trackList;
    private PlayingTrackViewAdapterListener myListener;


    public PlayingTrackViewAdapter(Context context, List<Track> trackList) {
        this.context = context;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public PlayTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.playing_track_view,parent,false);
        return new PlayTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayTrackViewHolder holder, int position) {
        Track track=trackList.get(position);
        holder.trackTitleTV.setText(track.getTitle());
        holder.trackArtistNameTV.setText(track.getArtists());
        InternetImageLoaderUtil.loadImage(this.context,holder.trackArtworkIV,track.getArtworkUri(), ConverterUtil.convertDpToPixel(this.context,50));


        if(this.myListener!=null){
            holder.mainContainerCL.setOnClickListener(v->{
                this.myListener.onClick(position);
            });
        }

    }

    @Override
    public int getItemCount() {
        return this.trackList.size();
    }

    public void addListener(PlayingTrackViewAdapterListener myListener){
        this.myListener=myListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        this.notifyDataSetChanged();
    }


    public static class PlayTrackViewHolder extends RecyclerView.ViewHolder{
        private final TextView trackTitleTV,trackArtistNameTV;
        private final ImageView trackArtworkIV;
        private final ConstraintLayout mainContainerCL;
        public PlayTrackViewHolder(@NonNull View view) {
            super(view);
            this.trackTitleTV=view.findViewById(R.id.ptvTrackTitleTV);
            this.trackArtistNameTV=view.findViewById(R.id.ptvTrackArtistsTV);
            this.trackArtworkIV=view.findViewById(R.id.ptvTrackArtworkIV);
            this.mainContainerCL=view.findViewById(R.id.ptvMainContainerCL);
        }
    }
}
