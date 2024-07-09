package developer.anurag.tunesy.main.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.main.listeners.TrackRowViewAdapterListener;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.main.utils.InternetImageLoaderUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class TrackRowViewAdapter extends RecyclerView.Adapter<TrackRowViewAdapter.TrackRowViewHolder> {

    private final Context context;
    private List<Track> trackList;
    private final int artworkWidth;
    private TrackRowViewAdapterListener myListener;
    private final int containerWidth;

    public TrackRowViewAdapter(Context context, List<Track> tracks) {
        this.context = context;
        this.trackList = tracks;
        this.artworkWidth= ConverterUtil.convertDpToPixel(this.context,50);
        this.containerWidth= ConstraintLayout.LayoutParams.MATCH_PARENT;

    }

    public TrackRowViewAdapter(Context context, List<Track> tracks,float artWorkWidth) {
        this.context = context;
        this.trackList = tracks;
        this.artworkWidth= ConverterUtil.convertDpToPixel(this.context,artWorkWidth);
        this.containerWidth= ConstraintLayout.LayoutParams.MATCH_PARENT;
    }

    public TrackRowViewAdapter(Context context, List<Track> tracks,float artWorkWidth,float containerWidth) {
        this.context = context;
        this.trackList = tracks;
        this.artworkWidth= ConverterUtil.convertDpToPixel(this.context,artWorkWidth);
        this.containerWidth= ConverterUtil.convertDpToPixel(this.context,containerWidth);
    }

    @NonNull
    @Override
    public TrackRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.track_row_view,parent,false);
        return new TrackRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackRowViewHolder holder, int position) {
        Track track=this.trackList.get(position);
        holder.trackTitleTV.setText(track.getTitle());
        holder.trackArtistNameTV.setText(track.getArtists());
        InternetImageLoaderUtil.loadImage(this.context,holder.trackArtWorkIV,track.getArtworkUri(),this.artworkWidth);

        ConstraintLayout.LayoutParams layoutParams=new ConstraintLayout.LayoutParams(this.containerWidth,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        holder.mainContainerGL.setLayoutParams(layoutParams);

        if(this.myListener!=null){
            holder.mainContainerGL.setOnClickListener(view-> this.myListener.onTrackClick(position));

            holder.trackActionMenuIB.setOnClickListener(view-> this.myListener.onTrackActionMenuIBClick(position));
        }

    }

    @Override
    public int getItemCount() {
        return this.trackList.size();
    }


    public void addListener(TrackRowViewAdapterListener listener){
        this.myListener=listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        this.notifyDataSetChanged();
    }

    public static class TrackRowViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackTitleTV,trackArtistNameTV;
        private final ShapeableImageView trackArtWorkIV;
        private final ImageButton trackActionMenuIB;
        private final GridLayout mainContainerGL;
        public TrackRowViewHolder(@NonNull View view) {
            super(view);
            this.trackTitleTV=view.findViewById(R.id.trvTrackTitleTV);
            this.trackArtistNameTV=view.findViewById(R.id.trvTrackArtistNameTV);
            this.trackArtWorkIV=view.findViewById(R.id.trvTrackArtworkIV);
            this.trackActionMenuIB=view.findViewById(R.id.trvActionMenuIB);
            this.mainContainerGL=view.findViewById(R.id.trvMainContainerGL);
        }
    }
}
