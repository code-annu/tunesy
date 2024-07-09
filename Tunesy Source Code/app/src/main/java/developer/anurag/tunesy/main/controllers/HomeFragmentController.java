package developer.anurag.tunesy.main.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.FragmentHomeBinding;
import developer.anurag.tunesy.main.adapters.ArtistMixViewAdapter;
import developer.anurag.tunesy.main.adapters.TrackColumnViewAdapter;
import developer.anurag.tunesy.main.adapters.TrackRowViewAdapter;
import developer.anurag.tunesy.main.data_model.MixPlaylistDataModel;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.data_model.TrackDataModel;
import developer.anurag.tunesy.main.fragments.MixPlaylistFragment;
import developer.anurag.tunesy.main.fragments.PlayerFragment;
import developer.anurag.tunesy.main.listeners.ArtistMixViewAdapterListener;
import developer.anurag.tunesy.main.listeners.MainActivityListener;
import developer.anurag.tunesy.main.listeners.TrackColumnViewAdapterListener;
import developer.anurag.tunesy.main.listeners.TrackRowViewAdapterListener;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.main.utils.InternetImageLoaderUtil;
import developer.anurag.tunesy.tunesy_api.utils.MixPlaylist;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class HomeFragmentController {
    private final Context context;
    private final FragmentHomeBinding binding;
    private TrackDataModel trackDataModel;
    private MixPlaylistDataModel mixPlaylistDataModel;
    private FragmentActivity fragmentActivity;
    private Map<String, List<MixPlaylist>> mixPlaylistMap;
    private Map<String,List<Track>> trackMap;
    private boolean trackMapHasMoreData=true,mixPlaylistMapHasMoreData=true;
    private final MainActivityListener mainActivityListener;
    private List<MixPlaylist> dailyMixPlaylistList;
    private final Handler handler=new Handler();
    private PlayingTracksDataModel playingTracksDataModel;
    private final MediaController mediaController;
    private final PlayerFragment playerFragment=new PlayerFragment();
    private FragmentManager fragmentManager;

    public HomeFragmentController(Context context, FragmentHomeBinding binding) {
        this.context = context;
        this.binding = binding;

        this.mainActivityListener= (MainActivityListener) this.context;
        this.mediaController=this.mainActivityListener.getPlaybackMediaController();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setTrackDataModel(TrackDataModel trackDataModel) {
        this.trackDataModel = trackDataModel;
    }

    public void setMixPlaylistDataModel(MixPlaylistDataModel mixPlaylistDataModel) {
        this.mixPlaylistDataModel = mixPlaylistDataModel;
    }

    public void setPlayingTracksDataModel(PlayingTracksDataModel playingTracksDataModel) {
        this.playingTracksDataModel = playingTracksDataModel;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public void setObserverForTrackAndMixPlaylistMap(){
        this.trackDataModel.getTrackMap().observe(this.fragmentActivity,trackMap->{
            this.trackMap=trackMap;
        });

        this.mixPlaylistDataModel.getMixPlaylistMap().observe(this.fragmentActivity,mixPlaylistMap->{
            this.mixPlaylistMap=mixPlaylistMap;
            this.hideLoadingLAV();
        });

        this.mixPlaylistDataModel.getDailyMixPlaylistList().observe(this.fragmentActivity,mixPlaylistList->{
            this.dailyMixPlaylistList=mixPlaylistList;
        });
    }

    private void loadData(){
        int timeLimit=10;
        Random random=new Random();
        for(int i=0;i<timeLimit;i++){
            int randNum= random.nextInt(10);
            if(this.trackMapHasMoreData || this.mixPlaylistMapHasMoreData){
                if(randNum%2==0){
                    randNum=random.nextInt(10);
                    if(randNum%2==0){
                        this.loadTracksInRow();
                    }
                    else {
                        this.loadTracksInColumn();
                    }
                }else {
                    this.loadMixPlaylists();
                }
            }
        }
    }


    private void loadTracksInColumn(){
        Stack<String> titleStack=this.trackDataModel.getTrackMapKeyStack();
        if(!titleStack.isEmpty()){
            String title=titleStack.pop();
            List<Track> trackList=this.trackMap.get(title);
            if(trackList!=null){
                View view= LayoutInflater.from(this.context).inflate(R.layout.track_container_view,this.binding.fhDataContainerLL,false);

                TextView containerTitleTV=view.findViewById(R.id.tcvContainerTitleTV);
                containerTitleTV.setText(title);

                TextView playAllTV=view.findViewById(R.id.tcvPlayAllTracksTV);
                playAllTV.setOnClickListener(v->{
                    this.handler.postDelayed(()->{
                        this.playerFragment.show(this.fragmentManager,null);
                        playTracks(trackList,0);
                        playingTracksDataModel.setPlayingTrackList(trackList);
                        },100);
                });

                RecyclerView trackContainerRV=view.findViewById(R.id.tcvTrackContainerRV);
                TrackColumnViewAdapter adapter=new TrackColumnViewAdapter(this.context,trackList);
                GridLayoutManager layoutManager=new GridLayoutManager(this.context,2,RecyclerView.HORIZONTAL,false);
                trackContainerRV.setLayoutManager(layoutManager);
                adapter.addListener(position -> {
                    this.handler.postDelayed(()->{
                        this.playerFragment.show(this.fragmentManager,null);
                        playTracks(trackList,position);
                        playingTracksDataModel.setPlayingTrackList(trackList);
                    },100);
                });
                this.handler.post(()->{
                    trackContainerRV.setAdapter(adapter);
                    this.binding.fhDataContainerLL.addView(view,this.binding.fhDataContainerLL.getChildCount()-1);
                });
            }
        }else this.trackMapHasMoreData=false;
    }

    private void loadTracksInRow(){
        Stack<String> titleStack=this.trackDataModel.getTrackMapKeyStack();
        if(!titleStack.isEmpty()){
            String title=titleStack.pop();
            List<Track> trackList=this.trackMap.get(title);
            if(trackList!=null){
                View view= LayoutInflater.from(this.context).inflate(R.layout.track_container_view,this.binding.fhDataContainerLL,false);
                TextView containerTitleTV=view.findViewById(R.id.tcvContainerTitleTV);
                containerTitleTV.setText(title);

                TextView playAllTV=view.findViewById(R.id.tcvPlayAllTracksTV);
                playAllTV.setOnClickListener(v->{
                    this.handler.postDelayed(()->{
                        this.playerFragment.show(this.fragmentManager,null);
                        playTracks(trackList,0);
                        playingTracksDataModel.setPlayingTrackList(trackList);
                    },100);
                });

                RecyclerView trackContainerRV=view.findViewById(R.id.tcvTrackContainerRV);
                TrackRowViewAdapter adapter=new TrackRowViewAdapter(this.context,trackList,50,380);
                GridLayoutManager layoutManager=new GridLayoutManager(this.context,4,RecyclerView.HORIZONTAL,false);
                PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
                pagerSnapHelper.attachToRecyclerView(trackContainerRV);
                trackContainerRV.setLayoutManager(layoutManager);
                adapter.addListener(new TrackRowViewAdapterListener() {
                    @Override
                    public void onTrackClick(int position) {
                        handler.postDelayed(()->{
                            playerFragment.show(fragmentManager,null);
                            playTracks(trackList,position);
                            playingTracksDataModel.setPlayingTrackList(trackList);
                        },100);

                    }

                    @Override
                    public void onTrackActionMenuIBClick(int position) {

                    }
                });
                this.handler.post(()->{
                    trackContainerRV.setAdapter(adapter);
                    this.binding.fhDataContainerLL.addView(view,this.binding.fhDataContainerLL.getChildCount()-1);
                });

            }
        }else this.trackMapHasMoreData=false;
    }

    private void loadMixPlaylists(){
        Stack<String> titleStack=this.mixPlaylistDataModel.getMixPlaylistMapKeyStack();
        if(!titleStack.isEmpty()) {
            String title = titleStack.pop();
            List<MixPlaylist> mixPlaylistList=this.mixPlaylistMap.get(title);
            if(mixPlaylistList!=null){
                View view= LayoutInflater.from(this.context).inflate(R.layout.mix_playlist_container_view,this.binding.fhDataContainerLL,false);

                TextView containerTitleTV=view.findViewById(R.id.mpcvContainerTitleTV);
                containerTitleTV.setText(title);

                RecyclerView mixPlaylistContainerRV=view.findViewById(R.id.mpcvTrackContainerRV);
                ArtistMixViewAdapter adapter=new ArtistMixViewAdapter(this.context,mixPlaylistList);
                LinearLayoutManager layoutManager=new LinearLayoutManager(this.context,RecyclerView.HORIZONTAL,false);
                mixPlaylistContainerRV.setLayoutManager(layoutManager);
                adapter.addListener(position -> {
                    this.trackDataModel.setPlaylistToShow(mixPlaylistList.get(position));
                    this.mainActivityListener.showFragment(new MixPlaylistFragment());
                });
                this.handler.post(()->{
                    mixPlaylistContainerRV.setAdapter(adapter);
                    this.binding.fhDataContainerLL.addView(view,this.binding.fhDataContainerLL.getChildCount()-1);
                });

            }
        }else this.mixPlaylistMapHasMoreData=false;
    }

    private void loadDailyMixPlaylist(){
        Handler handler=new Handler();
        Thread thread=new Thread(()->{
            for(MixPlaylist mixPlaylist:this.dailyMixPlaylistList){
                View view=LayoutInflater.from(this.context).inflate(R.layout.daily_mix_view,this.binding.fhDataContainerLL,false);

                TextView titleTV=view.findViewById(R.id.dmvMixTitleTV);
                titleTV.setText(mixPlaylist.getTitle());

                ImageView artworkIV=view.findViewById(R.id.dmvMixArtworkIV);
                InternetImageLoaderUtil.loadImage(this.context,artworkIV,mixPlaylist.getArtworkUri(),ConverterUtil.convertDpToPixel(this.context,50));

                GridLayout.LayoutParams layoutParams=new GridLayout.LayoutParams();
                layoutParams.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);
                layoutParams.width=0;
                layoutParams.setMargins(15,10,15,10);
                view.setLayoutParams(layoutParams);

                view.setOnClickListener(v->{
                    this.trackDataModel.setPlaylistToShow(mixPlaylist);
                    this.mainActivityListener.showFragment(new MixPlaylistFragment());
                });
                handler.post(()->{
                    this.binding.fhDailMixContainerGL.addView(view);
                });
            }
            this.loadData();
        });
        thread.start();

    }

    /*----------------------- Helper functions -------------------*/
    private void playTracks(List<Track> trackList,int position){
        mediaController.clearMediaItems();
        for(Track track:trackList){
            mediaController.addMediaItem(MediaItem.fromUri(track.getTrackUri()));
        }
        mediaController.prepare();
        mediaController.play();
        mediaController.seekTo(position,0);
    }

    private void hideLoadingLAV(){
        ObjectAnimator animator=ObjectAnimator.ofFloat(this.binding.fhLoadingLAV,"alpha",1,0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.fhLoadingLAV.setVisibility(View.GONE);
                loadDailyMixPlaylist();
            }
        });
        animator.setDuration(200);
        animator.start();

    }


}
