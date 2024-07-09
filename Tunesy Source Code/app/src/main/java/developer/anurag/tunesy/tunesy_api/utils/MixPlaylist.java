package developer.anurag.tunesy.tunesy_api.utils;

import java.util.List;

public class MixPlaylist {
    private String title,artworkUri;
    private List<String> tracksOfArtistsList;
    private List<Track> trackList;

    public MixPlaylist(String title, String artworkUri, List<String> tracksOfArtistsList, List<Track> trackList) {
        this.title = title;
        this.artworkUri = artworkUri;
        this.tracksOfArtistsList = tracksOfArtistsList;
        this.trackList = trackList;
    }

    public MixPlaylist() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtworkUri() {
        return artworkUri;
    }

    public void setArtworkUri(String artworkUri) {
        this.artworkUri = artworkUri;
    }

    public List<String> getTracksOfArtistsList() {
        return tracksOfArtistsList;
    }

    public void setTracksOfArtistsList(List<String> tracksOfArtistsList) {
        this.tracksOfArtistsList = tracksOfArtistsList;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }


}
