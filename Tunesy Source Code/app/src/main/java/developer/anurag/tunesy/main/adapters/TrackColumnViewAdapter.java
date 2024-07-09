package developer.anurag.tunesy.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.main.listeners.TrackColumnViewAdapterListener;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.main.utils.InternetImageLoaderUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class TrackColumnViewAdapter extends RecyclerView.Adapter<TrackColumnViewAdapter.TrackColumnViewHolder> {
    private final Context context;
    private final List<Track> trackList;
    private final int artworkWidth;
    private TrackColumnViewAdapterListener myListener;

    public TrackColumnViewAdapter(Context context, List<Track> tracks) {
        this.context = context;
        this.trackList = tracks;
        this.artworkWidth= ConverterUtil.convertDpToPixel(this.context,150);
    }

    public TrackColumnViewAdapter(Context context, List<Track> tracks,float artworkWidth) {
        this.context = context;
        this.trackList = tracks;
        this.artworkWidth= ConverterUtil.convertDpToPixel(this.context,artworkWidth);
    }



    @NonNull
    @Override
    public TrackColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.track_column_view,parent,false);
        return new TrackColumnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackColumnViewHolder holder, int position) {
        Track track=this.trackList.get(position);
        holder.trackTitleTV.setText(track.getTitle());
        InternetImageLoaderUtil.loadImage(this.context,holder.trackArtworkIV,track.getArtworkUri(),this.artworkWidth);

        if(this.myListener!=null){
            holder.mainContainerCL.setOnClickListener(view->{
                this.myListener.onTrackClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.trackList.size();
    }


    public void addListener(TrackColumnViewAdapterListener listener){
        this.myListener=listener;
    }

    public static class TrackColumnViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackTitleTV;
        private final ShapeableImageView trackArtworkIV;
        private final ConstraintLayout mainContainerCL;
        public TrackColumnViewHolder(@NonNull View view) {
            super(view);

            this.trackTitleTV=view.findViewById(R.id.tcvTrackTitleTV);
            this.trackArtworkIV=view.findViewById(R.id.tcvTrackArtworkIV);
            this.mainContainerCL=view.findViewById(R.id.tcvMainContainerCL);
        }
    }


}
