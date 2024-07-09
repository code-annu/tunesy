package developer.anurag.tunesy.main.controllers;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.ActivityMainBinding;
import developer.anurag.tunesy.main.adapters.PlayingTrackViewAdapter;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.fragments.PlayerFragment;
import developer.anurag.tunesy.main.utils.ColorUtil;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class MiniPlayerController {
    private final Context context;
    private final ActivityMainBinding binding;
    private PlayingTracksDataModel playingTracksDataModel;
    private PlayingTrackViewAdapter miniPlayerPlayingTrackAdapter;
    private FragmentManager fragmentManager;
    private AppCompatActivity appCompatActivity;
    private MediaController mediaController;
    private List<Track> playingTrackList=new ArrayList<>();

    public MiniPlayerController(Context context, ActivityMainBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void setPlayingTracksDataModel(PlayingTracksDataModel playingTracksDataModel) {
        this.playingTracksDataModel = playingTracksDataModel;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setMediaController(MediaController mediaController) {
        this.mediaController = mediaController;
        this.setMediaControllerListener();
        this.setPlayingProgress();
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public void initMiniPlayer(){
        this.miniPlayerPlayingTrackAdapter=new PlayingTrackViewAdapter(this.context,new ArrayList<>());
        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(this.binding.amMiniPlayerPlayingTracksContainerRV);
        this.miniPlayerPlayingTrackAdapter.addListener(position -> {
            PlayerFragment fragment=new PlayerFragment();
            fragment.show(this.fragmentManager,null);
        });
        this.binding.amMiniPlayerPlayingTracksContainerRV.setAdapter(this.miniPlayerPlayingTrackAdapter);


        this.binding.amMiniPlayerContainerGL.setOnClickListener(view->{
            PlayerFragment fragment=new PlayerFragment();
            fragment.show(this.fragmentManager,null);
        });

        this.playingTracksDataModel.getPlayingTrackList().observe(this.appCompatActivity,trackList-> {
            this.playingTrackList=trackList;
            this.miniPlayerPlayingTrackAdapter.setTrackList(trackList);
            this.showMiniPlayer();
        });

        this.binding.amMiniPlayerPlayingTracksContainerRV.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager linearLayoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
                    if(linearLayoutManager!=null){
                        int firstVisibleItemPosition=linearLayoutManager.findFirstVisibleItemPosition();
                        try {
                            if(firstVisibleItemPosition!=mediaController.getCurrentMediaItemIndex())mediaController.seekTo(firstVisibleItemPosition,0);
                        }catch (NullPointerException ignored){}
                    }
                }
            }

        });
    }

    private void setMediaControllerListener(){
        this.mediaController.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if(isPlaying)binding.amMiniPlayerPlayPauseIB.setImageResource(R.drawable.pause_icon);
                else binding.amMiniPlayerPlayPauseIB.setImageResource(R.drawable.play_icon);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if(playbackState==Player.STATE_READY){
                    int currentMediaItemIndex=mediaController.getCurrentMediaItemIndex();
                    binding.amMiniPlayerPlayedPB.setProgress(0);
                    if(!playingTrackList.isEmpty()){
                        Track track=playingTrackList.get(currentMediaItemIndex);
                        loadImageAndSetBackground(track.getArtworkUri());
                    }
                    binding.amMiniPlayerPlayedPB.setMax((int) (mediaController.getDuration()/1000));
                    binding.amMiniPlayerPlayingTracksContainerRV.smoothScrollToPosition(currentMediaItemIndex);
                }
            }
        });
    }


    private void showMiniPlayer(){
        if(this.binding.amMiniPlayerContainerGL.getVisibility()==View.GONE){
            this.binding.amMiniPlayerContainerGL.setVisibility(View.VISIBLE);
            ObjectAnimator animator=ObjectAnimator.ofFloat(this.binding.amMiniPlayerContainerGL,"alpha",0,1);
            animator.setDuration(300);
            animator.start();
        }
    }

    private void loadImageAndSetBackground(String artworkUri){
        Glide.with(this.context).load(artworkUri).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
                setMiniPlayerBackGround(bitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void setMiniPlayerBackGround(Bitmap bitmap){
        Palette.from(bitmap).generate(palette -> {
            int backgroundColor=this.context.getColor(R.color.secondary_black);
            if(palette!=null){
                backgroundColor=palette.getVibrantColor(this.context.getColor(R.color.secondary_black));

                if (backgroundColor==this.context.getColor(R.color.secondary_black)){
                    backgroundColor=palette.getMutedColor(this.context.getColor(R.color.secondary_black));
                }
            }
            GradientDrawable drawable=new GradientDrawable();
            drawable.setColor(ColorUtil.getDarkenColor(backgroundColor,.5f));
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(ConverterUtil.convertDpToPixel(this.context,8));
            this.binding.amMiniPlayerContainerGL.setBackground(drawable);
        });
    }

    private void setPlayingProgress(){
        Handler handler=new Handler();
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                if(mediaController!=null && mediaController.isPlaying()){
                    binding.amMiniPlayerPlayedPB.setProgress((int) (mediaController.getCurrentPosition()/1000));
                }
                handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);
    }


    public void setPlayPauseIBClickListener(){
        this.binding.amMiniPlayerPlayPauseIB.setOnClickListener(view->{
            if(this.mediaController!=null){
                if(mediaController.isPlaying())this.mediaController.pause();
                else mediaController.play();
            }
        });
    }






}
