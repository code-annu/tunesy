package developer.anurag.tunesy.main.data_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import developer.anurag.tunesy.tunesy_api.utils.MixPlaylist;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class TrackDataModel extends ViewModel {
    private final MutableLiveData<Map<String, List<Track>>> trackMap=new MutableLiveData<>();
    private Stack<String> trackMapKeyStack=new Stack<>();
    private final MutableLiveData<MixPlaylist> playlistToShow=new MutableLiveData<>();
    private List<Track> searchResultTrackList=new ArrayList<>();


    public MutableLiveData<Map<String, List<Track>>> getTrackMap() {
        return trackMap;
    }

    public void setTrackMap(Map<String, List<Track>> trackMap) {
        this.trackMap.setValue(trackMap);
    }

    public Stack<String> getTrackMapKeyStack() {
        return trackMapKeyStack;
    }

    public void setTrackMapKeyStack(Stack<String> trackMapKeyStack) {
        this.trackMapKeyStack = trackMapKeyStack;
    }

    public MutableLiveData<MixPlaylist> getPlaylistToShow() {
        return playlistToShow;
    }

    public void setPlaylistToShow(MixPlaylist playlistToShow) {
        this.playlistToShow.setValue(playlistToShow);
    }

    public List<Track> getSearchResultTrackList() {
        return searchResultTrackList;
    }

    public void setSearchResultTrackList(List<Track> searchResultTrackList) {
        this.searchResultTrackList = searchResultTrackList;
    }
}
