package developer.anurag.tunesy.tunesy_api.utils;

import java.util.List;

public class RemoteMixPlaylist {
    private String mixingTitle,artworkUri;
    private List<String> artistList;
    private List<Long> trackIDsList;

    public RemoteMixPlaylist(String mixingTitle, String artworkUri, List<String> artistList, List<Long> trackIDsList) {
        this.mixingTitle = mixingTitle;
        this.artworkUri = artworkUri;
        this.artistList = artistList;
        this.trackIDsList = trackIDsList;
    }

    public RemoteMixPlaylist() {
    }

    public String getMixingTitle() {
        return mixingTitle;
    }

    public void setMixingTitle(String mixingTitle) {
        this.mixingTitle = mixingTitle;
    }

    public String getArtworkUri() {
        return artworkUri;
    }

    public void setArtworkUri(String artworkUri) {
        this.artworkUri = artworkUri;
    }

    public List<String> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<String> artistList) {
        this.artistList = artistList;
    }

    public List<Long> getTrackIDsList() {
        return trackIDsList;
    }

    public void setTrackIDsList(List<Long> trackIDsList) {
        this.trackIDsList = trackIDsList;
    }
}
