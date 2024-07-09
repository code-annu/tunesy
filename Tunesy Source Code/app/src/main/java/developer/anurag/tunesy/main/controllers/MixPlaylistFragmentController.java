package developer.anurag.tunesy.main.controllers;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaController;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.FragmentMixPlaylistBinding;
import developer.anurag.tunesy.main.adapter_decorators.TrackRowViewVerticalDecoration;
import developer.anurag.tunesy.main.adapters.TrackRowViewAdapter;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.data_model.TrackDataModel;
import developer.anurag.tunesy.main.fragments.PlayerFragment;
import developer.anurag.tunesy.main.listeners.MainActivityListener;
import developer.anurag.tunesy.main.listeners.TrackRowViewAdapterListener;
import developer.anurag.tunesy.main.utils.ColorUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class MixPlaylistFragmentController {
    private final Context context;
    private final FragmentMixPlaylistBinding binding;
    private FragmentActivity fragmentActivity;
    private TrackDataModel trackDataModel;
    private TrackRowViewAdapter playlistTrackAdapter;
    private final MainActivityListener mainActivityListener;
    private PlayingTracksDataModel playingTracksDataModel;
    private List<Track> trackList;
    private final Handler handler=new Handler();
    private final MediaController mediaController;
    private final PlayerFragment playerFragment=new PlayerFragment();


    public MixPlaylistFragmentController(Context context, FragmentMixPlaylistBinding binding) {
        this.context = context;
        this.binding = binding;

        this.mainActivityListener= (MainActivityListener) this.context;
        this.mediaController=this.mainActivityListener.getPlaybackMediaController();
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public void setTrackDataModel(TrackDataModel trackDataModel) {
        this.trackDataModel = trackDataModel;
    }

    public void setPlayingTracksDataModel(PlayingTracksDataModel playingTracksDataModel) {
        this.playingTracksDataModel = playingTracksDataModel;
    }


    public void setNavigationClickListener(){
        this.binding.fmpToolBar.setNavigationOnClickListener(v -> {
            this.mainActivityListener.backPress();
        });
    }


    public void initTrackContainerRV() {
        this.trackDataModel.getPlaylistToShow().observe(fragmentActivity,mixPlaylist -> {
            trackList=mixPlaylist.getTrackList();
            this.setToolbarTitle(mixPlaylist.getTitle());
            this.setArtwork(mixPlaylist.getArtworkUri());
            this.playlistTrackAdapter=new TrackRowViewAdapter(this.context,mixPlaylist.getTrackList());
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false);
            this.binding.fmpTrackContainerRV.setLayoutManager(linearLayoutManager);
            this.playlistTrackAdapter.addListener(new TrackRowViewAdapterListener() {
                @Override
                public void onTrackClick(int position) {
                    handler.postDelayed(()->{
                        playerFragment.show(fragmentActivity.getSupportFragmentManager(),null);
                        playTracks(trackList,position);
                        playingTracksDataModel.setPlayingTrackList(trackList);
                    },200);
                }

                @Override
                public void onTrackActionMenuIBClick(int position) {

                }
            });
            TrackRowViewVerticalDecoration itemDecoration=new TrackRowViewVerticalDecoration(0,100,450);
            this.binding.fmpTrackContainerRV.addItemDecoration(itemDecoration);
            this.binding.fmpTrackContainerRV.setAdapter(this.playlistTrackAdapter);
            this.showPlayTrackFAB();
        });
    }

    private void setToolbarTitle(String title){
        this.binding.fmpCollapsingToolbarLayout.setTitle(title);
    }

    private void setArtwork(String artworkUri){
        Glide.with(this.context).load(artworkUri).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                binding.fmpMixPlaylistArtworkIV.setImageDrawable(drawable);
                Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
                setBackgroundForToolbar(bitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void setBackgroundForToolbar(Bitmap bitmap){
        Palette.from(bitmap).generate(palette -> {
            int startColor=this.context.getColor(R.color.primary_black);
            int endColor=startColor;
            if(palette!=null){
                startColor=palette.getVibrantColor(this.context.getColor(R.color.primary_black));
            }

            if(startColor==endColor && palette!=null){
                startColor=palette.getDarkMutedColor(this.context.getColor(R.color.primary_black));
            }
            startColor=ColorUtil.getDarkenColor(startColor,0.5f);
            int darkerStartColor=ColorUtil.getDarkenColor(startColor,0.7f);
            GradientDrawable backgroundDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{endColor,startColor});
            this.binding.fmpCollapsingToolbarLayout.setBackground(backgroundDrawable);
            this.binding.fmpCollapsingToolbarLayout.setContentScrimColor(startColor);
            this.binding.fmpPlayTracksFAB.setBackgroundTintList(ColorStateList.valueOf(darkerStartColor));
            this.fragmentActivity.getWindow().setStatusBarColor(darkerStartColor);
        });
    }


    public void setPlayFABClickListener(){
        this.binding.fmpPlayTracksFAB.setOnClickListener(view->{
            this.playerFragment.show(this.fragmentActivity.getSupportFragmentManager(),null);
            this.handler.postDelayed(()-> {
                this.playTracks(this.trackList,0);
                this.playingTracksDataModel.setPlayingTrackList(this.trackList);
            },100);
        });
    }


    private void playTracks(List<Track> trackList,int position){
        mediaController.clearMediaItems();
        for(Track track:trackList){
            mediaController.addMediaItem(MediaItem.fromUri(track.getTrackUri()));
        }
        mediaController.seekTo(position,0);
        mediaController.prepare();
        mediaController.play();
    }

    private void showPlayTrackFAB(){
        if(this.binding.fmpPlayTracksFAB.getAlpha()<1){
            ObjectAnimator animator=ObjectAnimator.ofFloat(this.binding.fmpPlayTracksFAB,"alpha",0,1);
            animator.setDuration(300);
            animator.start();
        }
    }


}
