package developer.anurag.tunesy.main.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentActivity;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.FragmentPlayerBinding;
import developer.anurag.tunesy.main.adapters.ArtworkViewAdapter;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.fragments.PlaylistFragment;
import developer.anurag.tunesy.main.listeners.MainActivityListener;
import developer.anurag.tunesy.main.utils.ColorUtil;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class PlayerFragmentController {
    private final Context context;

    private  PlayingTracksDataModel playingTracksDataModel;
    private ArtworkViewAdapter playingTrackArtworkAdapter;
    private FragmentActivity fragmentActivity;
    private final FragmentPlayerBinding binding;
    private final MainActivityListener mainActivityListener;
    private final MediaController mediaController;
    private List<Track> playingTrackList=new ArrayList<>();
    private boolean isDragging=false;

    public PlayerFragmentController(Context context, FragmentPlayerBinding binding) {
        this.context = context;
        this.binding=binding;

        this.mainActivityListener= (MainActivityListener) context;
        this.mediaController=this.mainActivityListener.getPlaybackMediaController();


    }

    public void setPlayingTracksDataModel(PlayingTracksDataModel playingTracksDataModel) {
        this.playingTracksDataModel = playingTracksDataModel;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public void initUi(){
        this.initPlayingPlaylistContainerRV();
        this.setPlayingProgress();
        if(this.mediaController.isPlaying()){
            this.binding.fpPlayingTrackPlayPauseIB.setImageResource(R.drawable.pause_in_circle);
        }else {
            this.binding.fpPlayingTrackPlayPauseIB.setImageResource(R.drawable.play_in_circle_transparent);
        }

        this.binding.fpPlayingTrackPlayedProgressTV.setText(ConverterUtil.convertSecDurationToStr((int) (this.mediaController.getCurrentPosition()/1000)));


    }

    public void setPlayPauseIBClickListener(){
        this.binding.fpPlayingTrackPlayPauseIB.setOnClickListener(view-> {
            if(this.mediaController.isPlaying())this.mediaController.pause();
            else this.mediaController.play();
        });
    }


    public void setUpNextBtnClickListener(){
        this.binding.fpUpNextBtn.setOnClickListener(view->{
            new Handler().postDelayed(()->{
                PlaylistFragment playlistFragment=new PlaylistFragment();
                playlistFragment.show(this.fragmentActivity.getSupportFragmentManager(),null);
            },200);
        });
    }


    private void initPlayingPlaylistContainerRV(){
        this.playingTrackArtworkAdapter=new ArtworkViewAdapter(this.context,new ArrayList<>());
        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(this.binding.fpPlayingPlaylistArtworkContainerRV);
        this.binding.fpPlayingPlaylistArtworkContainerRV.setAdapter(this.playingTrackArtworkAdapter);

        // setup scroll listener
        this.binding.fpPlayingPlaylistArtworkContainerRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager layoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
                    int currentItemPosition;
                    if (layoutManager != null) {
                        currentItemPosition = layoutManager.findFirstVisibleItemPosition();
                        if(currentItemPosition!=mediaController.getCurrentMediaItemIndex()){
                            mediaController.seekTo(currentItemPosition,0);
                        }
                    }
                }
            }
        });

        this.playingTracksDataModel.getPlayingTrackList().observe(this.fragmentActivity,trackList-> {
            this.playingTrackList=trackList;
            this.playingTrackArtworkAdapter.setTrackList(trackList);
            int currentItemPosition=this.mediaController.getCurrentMediaItemIndex();
            this.binding.fpPlayingPlaylistArtworkContainerRV.smoothScrollToPosition(currentItemPosition);
            Track track=trackList.get(currentItemPosition);
            this.setTitleAndArtistsTV(track);
            this.binding.fpPlayingTrackDurationTV.setText(ConverterUtil.convertSecDurationToStr((int) (this.mediaController.getDuration()/1000)));
            this.binding.fpPlayingTrackProgressSB.setMax((int) (this.mediaController.getDuration()/1000));
            new Handler().postDelayed(()->{
                this.binding.fpPlayingPlaylistArtworkContainerRV.setAlpha(1f);
            },800);
            this.loadImageSetBackground(track.getArtworkUri());

        });
    }

    public void addMediaControllerListener(){
        this.mediaController.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                if(!playingTrackList.isEmpty()){
                    int currentItemPosition=mediaController.getCurrentMediaItemIndex();
                    Track track=playingTrackList.get(currentItemPosition);
                    setTitleAndArtistsTV(track);
                    binding.fpPlayingPlaylistArtworkContainerRV.smoothScrollToPosition(currentItemPosition);
                    loadImageSetBackground(track.getArtworkUri());
                }

            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                binding.fpPlayingTrackDurationTV.setText(ConverterUtil.convertSecDurationToStr((int) (mediaController.getDuration()/1000)));
                binding.fpPlayingTrackProgressSB.setMax((int) (mediaController.getDuration()/1000));


            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if(isPlaying)binding.fpPlayingTrackPlayPauseIB.setImageResource(R.drawable.pause_in_circle);
                else binding.fpPlayingTrackPlayPauseIB.setImageResource(R.drawable.play_in_circle_transparent);
            }
        });
    }

    public void setPlayingTrackProgressSB(){
        this.binding.fpPlayingTrackProgressSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isDragging=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaController.seekTo(seekBar.getProgress()* 1000L);
                isDragging=false;
            }
        });
    }

    public void setClosePlayerFragmentIBClickListener(BottomSheetDialogFragment bottomSheetDialogFragment){
        this.binding.fpClosePlayerFragmentIB.setOnClickListener(view->{
            bottomSheetDialogFragment.dismiss();
        });
    }

    private void setTitleAndArtistsTV(Track track){
        this.binding.fpPlayingTrackTitleTV.setText(track.getTitle());
        this.binding.fpPlayingTrackArtistsTV.setText(track.getArtists());
    }

    private void loadImageSetBackground(String artworkUri){
        Glide.with(this.context).load(artworkUri).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
                setBackgroundColor(bitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

    }

    private void setBackgroundColor(Bitmap bitmap){
        Palette.from(bitmap).generate(palette -> {
            int topColor=0;
            if(palette!=null){
                topColor=palette.getVibrantColor(this.context.getColor(R.color.primary_black));

                if (topColor == this.context.getColor(R.color.primary_black)) {
                    topColor=palette.getMutedColor(this.context.getColor(R.color.primary_black));
                }

            }

            topColor= ColorUtils.blendARGB(topColor,this.context.getColor(R.color.primary_black),0.5f);

            int bottomColor=this.context.getColor(R.color.primary_black);

            GradientDrawable drawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{bottomColor,topColor});
            this.binding.getRoot().setBackground(drawable);
        });
    }

    private void setPlayingProgress(){
        Handler handler=new Handler();
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                if(mediaController!=null && mediaController.isPlaying() && !isDragging){
                    int duration=(int) (mediaController.getCurrentPosition()/1000);
                    binding.fpPlayingTrackProgressSB.setProgress(duration);
                    binding.fpPlayingTrackPlayedProgressTV.setText(ConverterUtil.convertSecDurationToStr(duration));
                }
                handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);
    }

    public void setNextAndPreviousIBClickListener(){
        this.binding.fpSkipNextIB.setOnClickListener(view->{
            if(this.mediaController.hasNextMediaItem())this.mediaController.seekToNext();
        });

        this.binding.fpSkipPreviousIB.setOnClickListener(view->{
            if(this.mediaController.hasPreviousMediaItem())this.mediaController.seekToPrevious();
        });
    }



}
