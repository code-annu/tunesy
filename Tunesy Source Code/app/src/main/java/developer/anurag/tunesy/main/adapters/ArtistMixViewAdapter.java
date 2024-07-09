package developer.anurag.tunesy.main.adapters;

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
import developer.anurag.tunesy.main.listeners.ArtistMixViewAdapterListener;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.main.utils.InternetImageLoaderUtil;
import developer.anurag.tunesy.tunesy_api.utils.MixPlaylist;

public class ArtistMixViewAdapter extends RecyclerView.Adapter<ArtistMixViewAdapter.ArtistMixViewHolder> {
    private final Context context;
    private final List<MixPlaylist> mixPlaylistList;
    private final int artworkWidth;
    private ArtistMixViewAdapterListener myListener;

    public ArtistMixViewAdapter(Context context, List<MixPlaylist> mixPlaylistList) {
        this.context = context;
        this.mixPlaylistList = mixPlaylistList;
        this.artworkWidth=ConverterUtil.convertDpToPixel(this.context,150);
    }


    @NonNull
    @Override
    public ArtistMixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.artist_mix_view,parent,false);
        return new ArtistMixViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistMixViewHolder holder, int position) {
        MixPlaylist mixPlaylist =this.mixPlaylistList.get(position);
        holder.mixTitleTV.setText(mixPlaylist.getTitle());
        holder.mixOfArtistsNameTV.setText(ConverterUtil.converListToString(mixPlaylist.getTracksOfArtistsList()));
        InternetImageLoaderUtil.loadImage(this.context,holder.mixArtworkIV, mixPlaylist.getArtworkUri(),ConverterUtil.convertDpToPixel(this.context,this.artworkWidth));

        this.setBarColors(holder.mixArtworkSideBarV,holder.mixArtworkBottomBarV,position);

        if(this.myListener!=null){
            holder.mainContainerCL.setOnClickListener(view->{
                this.myListener.onClick(position);
            });
        }

    }

    @Override
    public int getItemCount() {
        return this.mixPlaylistList.size();
    }


    public void addListener(ArtistMixViewAdapterListener myListener) {
        this.myListener = myListener;
    }

    private void setBarColors(View sideBar,View bottomBar,int position){
        if(position==0 || position==4 || position==10){
            sideBar.setBackgroundColor(this.context.getColor(R.color.yellow));
            bottomBar.setBackgroundColor(this.context.getColor(R.color.yellow));
        } else if (position==3 || position==9) {
            sideBar.setBackgroundColor(this.context.getColor(R.color.violet));
            bottomBar.setBackgroundColor(this.context.getColor(R.color.violet));
        }
        else if(position==1 || position==5 || position==11){
            sideBar.setBackgroundColor(this.context.getColor(R.color.green));
            bottomBar.setBackgroundColor(this.context.getColor(R.color.green));
        }
        else if(position==2 || position==6 || position==12){
            sideBar.setBackgroundColor(this.context.getColor(R.color.pink));
            bottomBar.setBackgroundColor(this.context.getColor(R.color.pink));
        }else {
            sideBar.setBackgroundColor(this.context.getColor(R.color.primary_blue));
            bottomBar.setBackgroundColor(this.context.getColor(R.color.primary_blue));
        }

    }

    public final static class ArtistMixViewHolder extends RecyclerView.ViewHolder {
        private final TextView mixTitleTV,mixOfArtistsNameTV;
        private final ImageView mixArtworkIV;
        private final View mixArtworkBottomBarV,mixArtworkSideBarV;
        private final ConstraintLayout mainContainerCL;

        public ArtistMixViewHolder(@NonNull View view) {
            super(view);

            this.mainContainerCL=view.findViewById(R.id.amvMainContainerCL);
            this.mixTitleTV=view.findViewById(R.id.amvMixTitleTV);
            this.mixOfArtistsNameTV=view.findViewById(R.id.amvMixOfArtistsNameTV);
            this.mixArtworkIV=view.findViewById(R.id.amvMixArtworkIV);
            this.mixArtworkBottomBarV=view.findViewById(R.id.amvMixArtworkBottomBarV);
            this.mixArtworkSideBarV=view.findViewById(R.id.amvMixArtworkSideBarV);
        }
    }
}
