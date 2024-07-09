package developer.anurag.tunesy.main.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.FragmentPlaylistBinding;
import developer.anurag.tunesy.main.adapter_decorators.TrackRowViewVerticalDecoration;
import developer.anurag.tunesy.main.adapters.PlayingTrackViewAdapter;
import developer.anurag.tunesy.main.adapters.PlaylistTrackViewAdapter;
import developer.anurag.tunesy.main.callbacks.PlaylistDragAndDropCallback;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.listeners.MainActivityListener;
import developer.anurag.tunesy.main.listeners.PlayingTrackViewAdapterListener;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class PlaylistFragmentController {
    private final Context context;
    private final FragmentPlaylistBinding binding;
    private PlaylistTrackViewAdapter playingPlaylistTrackAdapter;
    private FragmentActivity fragmentActivity;
    private PlayingTracksDataModel playingTracksDataModel;
    private List<Track> playingTrackList=new ArrayList<>();
    private MediaController mediaController;

    public PlaylistFragmentController(Context context, FragmentPlaylistBinding binding) {
        this.context = context;
        this.binding = binding;

        this.mediaController= ((MainActivityListener)this.context).getPlaybackMediaController();
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public void setPlayingTracksDataModel(PlayingTracksDataModel playingTracksDataModel) {
        this.playingTracksDataModel = playingTracksDataModel;
    }

    public void initPlayingPlaylistContainerRV(){
        this.playingPlaylistTrackAdapter=new PlaylistTrackViewAdapter(this.context,playingTrackList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false);
        this.binding.fpPlayingPlaylistContainerRV.setLayoutManager(linearLayoutManager);
        int marginTB= ConverterUtil.convertDpToPixel(this.context,20);
        int margin=ConverterUtil.convertDpToPixel(this.context,5);
        TrackRowViewVerticalDecoration decoration=new TrackRowViewVerticalDecoration(margin,marginTB,marginTB);
        this.binding.fpPlayingPlaylistContainerRV.addItemDecoration(decoration);
        this.playingPlaylistTrackAdapter.addListener(position -> {
            this.mediaController.seekTo(position,0);
        });

        PlaylistDragAndDropCallback dragAndDropCallback=new PlaylistDragAndDropCallback(this.playingPlaylistTrackAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(dragAndDropCallback);
        itemTouchHelper.attachToRecyclerView(this.binding.fpPlayingPlaylistContainerRV);
        this.playingPlaylistTrackAdapter.setMediaController(this.mediaController);

        this.binding.fpPlayingPlaylistContainerRV.setAdapter(this.playingPlaylistTrackAdapter);

        this.playingTracksDataModel.getPlayingTrackList().observe(this.fragmentActivity,trackList-> {
            this.playingTrackList=trackList;
            this.playingPlaylistTrackAdapter.setTrackList(trackList);
            this.playingPlaylistTrackAdapter.setPlayingIndex(this.mediaController.getCurrentMediaItemIndex());
            Track track=trackList.get(this.mediaController.getCurrentMediaItemIndex());
            this.loadImageSetBackground(track.getArtworkUri());
        });
    }

    public void addMediaListener(){
        this.mediaController.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                Track track=playingTrackList.get(mediaController.getCurrentMediaItemIndex());
                loadImageSetBackground(track.getArtworkUri());
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                playingPlaylistTrackAdapter.setPlayingIndex(mediaController.getCurrentMediaItemIndex());
            }
        });
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
            topColor= ColorUtils.blendARGB(topColor,this.context.getColor(R.color.primary_black),0.8f);

            int bottomColor=this.context.getColor(R.color.primary_black);
            GradientDrawable drawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{bottomColor,topColor});
            this.binding.getRoot().setBackground(drawable);
        });
    }




}
