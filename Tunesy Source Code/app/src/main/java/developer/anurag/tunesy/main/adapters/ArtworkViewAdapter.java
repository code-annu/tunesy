package developer.anurag.tunesy.main.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.main.utils.InternetImageLoaderUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class ArtworkViewAdapter extends RecyclerView.Adapter<ArtworkViewAdapter.ArtworkViewHolder> {
    private final Context context;
    private List<Track> trackList;

    public ArtworkViewAdapter(Context context,List<Track> trackList){
        this.context=context;
        this.trackList=trackList;
    }



    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.artwork_view,parent,false);
        return new ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, int position) {
        Track track=trackList.get(position);
        InternetImageLoaderUtil.loadImage(this.context,holder.artworkIV,track.getArtworkUri(), ConverterUtil.convertDpToPixel(this.context,500));

    }

    @Override
    public int getItemCount() {
        return this.trackList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        this.notifyDataSetChanged();
    }

    public static class ArtworkViewHolder extends RecyclerView.ViewHolder {
        private final ImageView artworkIV;
        public ArtworkViewHolder(@NonNull View view) {
            super(view);

            this.artworkIV=view.findViewById(R.id.avTrackArtworkIV);
        }
    }
}
