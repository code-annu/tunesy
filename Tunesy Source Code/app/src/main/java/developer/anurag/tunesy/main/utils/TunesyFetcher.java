package developer.anurag.tunesy.main.utils;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.main.data_model.MixPlaylistDataModel;
import developer.anurag.tunesy.main.data_model.TrackDataModel;
import developer.anurag.tunesy.main.listeners.TunesyFetcherListener;
import developer.anurag.tunesy.tunesy_api.TunesyService;
import developer.anurag.tunesy.tunesy_api.listeners.TunesyServiceListener;
import developer.anurag.tunesy.tunesy_api.utils.MixPlaylist;
import developer.anurag.tunesy.tunesy_api.utils.Track;


public class TunesyFetcher implements TunesyServiceListener{
    private final TrackDataModel trackDataModel;
    private final MixPlaylistDataModel mixPlaylistDataModel;
    private final int FETCH_COUNT=100;
    private final Context context;
    private final TunesyService tunesyService;
    private final Handler handler=new Handler();
    private final TunesyFetcherListener myListener;

    private final Random random=new Random();

    public TunesyFetcher(Context context,TrackDataModel trackDataModel, MixPlaylistDataModel mixPlaylistDataModel,TunesyFetcherListener listener) {
        this.trackDataModel = trackDataModel;
        this.mixPlaylistDataModel = mixPlaylistDataModel;
        this.context=context;

        this.myListener=listener;
        this.tunesyService=new TunesyService(this);
    }

    public void fetchTracksAndMixings(){
        this.tunesyService.fetchRandomTracks(this.FETCH_COUNT);
    }

    private List<Track> getRandomTrackList(List<Track> fromTrackList){
        int sizeLimit=25;
        Set<Track> randomTrackSet=new HashSet<>();
        for(int i=0;i<sizeLimit;i++){
            randomTrackSet.add(fromTrackList.get(this.random.nextInt(fromTrackList.size())));
        }
        List<Track> randomTrackList=new ArrayList<>(randomTrackSet);
        randomTrackSet.clear();
        return randomTrackList;
    }

    private List<MixPlaylist> getRandomMixPlaylistList(List<MixPlaylist> fromMixPlaylistList){
        int sizeLimit=10;
        Set<MixPlaylist> randomMixPlaylistSet=new HashSet<>();
        for(int i=0;i<sizeLimit;i++){
            randomMixPlaylistSet.add(fromMixPlaylistList.get(this.random.nextInt(fromMixPlaylistList.size())));
        }
        List<MixPlaylist> randomMixPlaylistList=new ArrayList<>(randomMixPlaylistSet);
        randomMixPlaylistSet.clear();
        return randomMixPlaylistList;
    }


    @Override
    public void onFetchRandomTracks(List<Track> trackList) {
        Thread thread=new Thread(()->{
            String[] trackContainerTitleArray=this.context.getResources().getStringArray(R.array.track_container_titles);
            Map<String, List<Track>> trackMap=new HashMap<>();
            Stack<String> containerTitleStack=new Stack<>();
            for(String containerTitle:trackContainerTitleArray){
                trackMap.put(containerTitle,this.getRandomTrackList(trackList));
                containerTitleStack.push(containerTitle);
            }
            this.handler.post(()->{
                this.trackDataModel.setTrackMapKeyStack(containerTitleStack);
                this.trackDataModel.setTrackMap(trackMap);
                this.tunesyService.fetchRandomMixings(FETCH_COUNT);
            });
        });
        thread.start();
    }

    @Override
    public void onFetchRandomMixings(List<MixPlaylist> mixPlaylistList) {
        Thread thread=new Thread(()->{
            String[] containerTitleArray=this.context.getResources().getStringArray(R.array.mix_playlist_container_titles);
            Map<String,List<MixPlaylist>> mixPlaylistMap=new HashMap<>();
            Stack<String> containerTitleStack=new Stack<>();
            for(String containerTitle:containerTitleArray){
                mixPlaylistMap.put(containerTitle,this.getRandomMixPlaylistList(mixPlaylistList));
                containerTitleStack.push(containerTitle);
            }
            this.handler.post(()->{
                this.mixPlaylistDataModel.setDailyMixPlaylistList(this.getRandomMixPlaylistList(mixPlaylistList));
                this.mixPlaylistDataModel.setMixPlaylistMapKeyStack(containerTitleStack);
                this.mixPlaylistDataModel.setMixPlaylistMap(mixPlaylistMap);
                this.myListener.onFetchComplete();
            });
        });
        thread.start();
    }

    @Override
    public void onSearchTracksAndArtists(List<Track> trackList) {

    }
}
