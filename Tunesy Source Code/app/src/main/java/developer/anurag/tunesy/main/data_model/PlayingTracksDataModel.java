package developer.anurag.tunesy.main.data_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import developer.anurag.tunesy.tunesy_api.utils.Track;

public class PlayingTracksDataModel extends ViewModel {
    private final MutableLiveData<List<Track>> playingTrackList=new MutableLiveData<>();

    public MutableLiveData<List<Track>> getPlayingTrackList() {
        return playingTrackList;
    }

    public void setPlayingTrackList(List<Track> playingTrackList) {
        this.playingTrackList.setValue(playingTrackList);
    }
}
