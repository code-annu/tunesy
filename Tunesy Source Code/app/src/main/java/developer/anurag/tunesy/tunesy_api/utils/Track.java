package developer.anurag.tunesy.tunesy_api.utils;

public class Track {
    private long id;
    private String title,artists,artworkUri,trackUri;

    public Track(long id, String title, String artists, String artworkUri, String trackUri) {
        this.id = id;
        this.title = title;
        this.artists = artists;
        this.artworkUri = artworkUri;
        this.trackUri = trackUri;
    }

    public Track() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getArtworkUri() {
        return artworkUri;
    }

    public void setArtworkUri(String artworkUri) {
        this.artworkUri = artworkUri;
    }

    public String getTrackUri() {
        return trackUri;
    }

    public void setTrackUri(String trackUri) {
        this.trackUri = trackUri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
