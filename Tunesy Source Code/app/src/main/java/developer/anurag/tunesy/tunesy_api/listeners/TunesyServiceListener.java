package developer.anurag.tunesy.tunesy_api.listeners;

import java.util.List;

import developer.anurag.tunesy.tunesy_api.utils.MixPlaylist;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public interface TunesyServiceListener {
    void onFetchRandomTracks(List<Track> trackList);
    void onFetchRandomMixings(List<MixPlaylist> mixPlaylistList);
    void onSearchTracksAndArtists(List<Track> trackList);
}
