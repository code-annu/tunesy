package developer.anurag.tunesy.tunesy_api;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import developer.anurag.tunesy.tunesy_api.listeners.TunesyServiceListener;
import developer.anurag.tunesy.tunesy_api.utils.MixPlaylist;
import developer.anurag.tunesy.tunesy_api.utils.RemoteMixPlaylist;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class TunesyService {
    private final TunesyServiceListener myListener;
    private final CollectionReference trackDataCollection,mixingDataCollection;
    private final Random random=new Random();

    public TunesyService(@NonNull TunesyServiceListener myListener) {
        this.myListener = myListener;

        this.trackDataCollection= FirebaseFirestore.getInstance().collection("track_data");
        this.mixingDataCollection= FirebaseFirestore.getInstance().collection("mixing_data");

    }


    public void fetchRandomTracks(int fetchCount){
        this.trackDataCollection.get().addOnSuccessListener(collection -> {
            List<Track> allTrackList=collection.toObjects(Track.class);
            Set<Track> randomTrackSet=new HashSet<>();
            for(int i=0;i<fetchCount;i++){
                int randomIndex=this.random.nextInt(allTrackList.size());
                randomTrackSet.add(allTrackList.get(randomIndex));
            }

            allTrackList.clear();
            this.myListener.onFetchRandomTracks(new ArrayList<>(randomTrackSet));
            randomTrackSet.clear();
        });

    }

    public void fetchRandomMixings(int fetchCount){
        this.mixingDataCollection.get().addOnSuccessListener(collection -> {
            List<RemoteMixPlaylist> allMixingList = collection.toObjects(RemoteMixPlaylist.class);
            Set<RemoteMixPlaylist> randomMixingSet = new HashSet<>();
            for (int i = 0; i < fetchCount; i++) {
                int randomIndex = this.random.nextInt(allMixingList.size());
                randomMixingSet.add(allMixingList.get(randomIndex));
            }

            this.trackDataCollection.get().addOnSuccessListener(trackCollection->{
                List<Track> allTrackList=trackCollection.toObjects(Track.class);
                List<MixPlaylist> mixPlaylistList =new ArrayList<>();

                for(RemoteMixPlaylist randomMixing:randomMixingSet){
                    MixPlaylist mixPlaylist=new MixPlaylist();
                    mixPlaylist.setTitle(randomMixing.getMixingTitle());
                    mixPlaylist.setArtworkUri(randomMixing.getArtworkUri());
                    mixPlaylist.setTracksOfArtistsList(randomMixing.getArtistList());
                    List<Track> trackList=new ArrayList<>();

                    for(Track track:allTrackList){
                        if(randomMixing.getTrackIDsList().contains(track.getId())){
                            trackList.add(track);
                        }
                    }
                    mixPlaylist.setTrackList(trackList);
                    mixPlaylistList.add(mixPlaylist);
                }
                this.myListener.onFetchRandomMixings(mixPlaylistList);
                randomMixingSet.clear();
                allTrackList.clear();
                allMixingList.clear();
            });
        });
    }


    public void searchTracksAndArtists(String query){
        this.trackDataCollection.get().addOnSuccessListener(collection->{
            if(query.trim().isEmpty()){
                this.myListener.onSearchTracksAndArtists(new ArrayList<>());
                return;
            }

            List<Track> allTrackList=collection.toObjects(Track.class);
            List<Track> searchResultList=new ArrayList<>();
            for(Track track:allTrackList){
                if (track.getTitle().toLowerCase().trim().contains(query)|| track.getArtists().toLowerCase().trim().contains(query)){
                    searchResultList.add(track);
                }
            }
            this.myListener.onSearchTracksAndArtists(searchResultList);

        });
    }
}
