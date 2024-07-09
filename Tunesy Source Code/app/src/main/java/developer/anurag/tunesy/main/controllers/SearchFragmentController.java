package developer.anurag.tunesy.main.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaController;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.tunesy.databinding.FragmentSearchBinding;
import developer.anurag.tunesy.main.adapter_decorators.TrackRowViewVerticalDecoration;
import developer.anurag.tunesy.main.adapters.TrackRowViewAdapter;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.fragments.PlayerFragment;
import developer.anurag.tunesy.main.listeners.MainActivityListener;
import developer.anurag.tunesy.main.listeners.TrackRowViewAdapterListener;
import developer.anurag.tunesy.main.utils.ConverterUtil;
import developer.anurag.tunesy.tunesy_api.TunesyService;
import developer.anurag.tunesy.tunesy_api.listeners.TunesyServiceListener;
import developer.anurag.tunesy.tunesy_api.utils.MixPlaylist;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class SearchFragmentController implements TunesyServiceListener {
    private final Context context;
    private final FragmentSearchBinding binding;
    private TrackRowViewAdapter searchResultAdapter;
    private final TunesyService tunesyService;
    private List<Track> searchResultTrackList=new ArrayList<>();
    private final MediaController mediaController;
    private Track trackToAdd;
    private final PlayerFragment playerFragment=new PlayerFragment();
    private PlayingTracksDataModel playingTracksDataModel;
    private FragmentManager fragmentManager;

    public SearchFragmentController(Context context, FragmentSearchBinding binding) {
        this.context = context;
        this.binding = binding;

        tunesyService=new TunesyService(this);

        MainActivityListener mainActivityListener = (MainActivityListener) this.context;
        this.mediaController= mainActivityListener.getPlaybackMediaController();
    }

    public void setPlayingTracksDataModel(PlayingTracksDataModel playingTracksDataModel) {
        this.playingTracksDataModel = playingTracksDataModel;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void initSearchResultContainerRV(){
        this.searchResultAdapter=new TrackRowViewAdapter(this.context,this.searchResultTrackList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false);
        this.binding.fsSearchResultsContainerRv.setLayoutManager(layoutManager);
        TrackRowViewVerticalDecoration decoration=new TrackRowViewVerticalDecoration(0,30, ConverterUtil.convertDpToPixel(this.context,100));
        this.binding.fsSearchResultsContainerRv.addItemDecoration(decoration);
        this.searchResultAdapter.addListener(new TrackRowViewAdapterListener() {
            @Override
            public void onTrackClick(int position) {
                trackToAdd=searchResultTrackList.get(position);
                tunesyService.fetchRandomTracks(30);
            }

            @Override
            public void onTrackActionMenuIBClick(int position) {

            }
        });
        this.binding.fsSearchResultsContainerRv.setAdapter(this.searchResultAdapter);
    }

    public void setSearchTrackAndArtistSVListener(){
        Handler handler=new Handler();
        this.binding.fsSearchSongArtistSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handler.postDelayed(()->{
                    tunesyService.searchTracksAndArtists(query.trim().toLowerCase());
                },300);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                handler.postDelayed(()->{
                    tunesyService.searchTracksAndArtists(query.trim().toLowerCase());
                },300);
                return true;
            }
        });
    }


    @Override
    public void onFetchRandomTracks(List<Track> trackList) {
        this.mediaController.clearMediaItems();
        trackList.add(0,trackToAdd);
        this.playerFragment.show(this.fragmentManager,null);
        for(Track track:trackList){
            this.mediaController.addMediaItem(MediaItem.fromUri(track.getTrackUri()));
        }
        this.mediaController.prepare();
        this.mediaController.play();
        this.playingTracksDataModel.setPlayingTrackList(trackList);
    }

    @Override
    public void onFetchRandomMixings(List<MixPlaylist> mixPlaylistList) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSearchTracksAndArtists(List<Track> trackList) {
        this.searchResultTrackList=trackList;
        this.searchResultAdapter.setTrackList(trackList);
    }
}
