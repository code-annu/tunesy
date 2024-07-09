package developer.anurag.tunesy.main.controllers;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaController;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import developer.anurag.tunesy.MainActivity;
import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.ActivityMainBinding;
import developer.anurag.tunesy.main.adapters.PlayingTrackViewAdapter;
import developer.anurag.tunesy.main.data_model.FragmentDataModel;
import developer.anurag.tunesy.main.data_model.MixPlaylistDataModel;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.data_model.TrackDataModel;
import developer.anurag.tunesy.main.fragments.MixPlaylistFragment;
import developer.anurag.tunesy.main.fragments.PlayerFragment;
import developer.anurag.tunesy.main.listeners.PlayingTrackViewAdapterListener;
import developer.anurag.tunesy.main.listeners.TunesyFetcherListener;
import developer.anurag.tunesy.main.utils.TunesyFetcher;
import developer.anurag.tunesy.tunesy_api.utils.Track;

public class MainActivityController implements TunesyFetcherListener {
    private final Context context;
    private final ActivityMainBinding binding;
    private TrackDataModel trackDataModel;
    private MixPlaylistDataModel mixPlaylistDataModel;
    private FragmentDataModel fragmentDataModel;
    private AppCompatActivity appCompatActivity;
    private PlayingTracksDataModel playingTracksDataModel;
    private FragmentManager fragmentManager;
    private PlayingTrackViewAdapter miniPlayerPlayingTrackAdapter;

    public MainActivityController(Context context, ActivityMainBinding binding) {
        this.context = context;
        this.binding = binding;
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

    public void setFragmentDataModel(FragmentDataModel fragmentDataModel) {
        this.fragmentDataModel = fragmentDataModel;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public void fetchData(){
        TunesyFetcher tunesyFetcher = new TunesyFetcher(this.context,this.trackDataModel, this.mixPlaylistDataModel, this);
        tunesyFetcher.fetchTracksAndMixings();
    }

    public void showFragment(Fragment fragmentToShow){
        FragmentTransaction transaction=this.fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        if(!fragmentToShow.isAdded()){
            transaction.add(R.id.amMainFragmentContainerView,fragmentToShow);
        }
        for(Fragment fragment:this.fragmentManager.getFragments()){
            if(fragment.isVisible() && fragmentToShow!=fragment){
                transaction.hide(fragment);
            }
        }
        transaction.show(fragmentToShow);
        this.fragmentDataModel.setShowingFragment(fragmentToShow);
        transaction.commit();

        if(!(fragmentToShow instanceof MixPlaylistFragment)){
            this.appCompatActivity.getWindow().setStatusBarColor(this.context.getColor(android.R.color.transparent));
        }
    }

    public void onBackPress(){
        Fragment showingFragment=this.fragmentDataModel.getShowingFragment();
        if(showingFragment!=null && !(showingFragment instanceof MixPlaylistFragment)){
            appCompatActivity.moveTaskToBack(true);
        }else {
            this.showFragment(MainActivity.HOME_FRAGMENT);
            this.fragmentDataModel.setShowingFragment(MainActivity.HOME_FRAGMENT);
            this.appCompatActivity.getWindow().setStatusBarColor(this.context.getColor(R.color.primary_black));
        }
    }


    public void setBottomNavigationListener(){
        this.binding.amBottomNavigation.setOnItemSelectedListener(item -> {
            int id=item.getItemId();
            if(id==R.id.bnmHomeMI){
                this.showFragment(MainActivity.HOME_FRAGMENT);
            }else if (id==R.id.bnmSearchMI){
                this.showFragment(MainActivity.SEARCH_FRAGMENT);
            }else if (id==R.id.bnmLibraryMI){
                this.showFragment(MainActivity.LIBRARY_FRAGMENT);
            }
            return true;
        });

        this.binding.amBottomNavigation.setOnItemReselectedListener(item -> {
            int id=item.getItemId();
            if(id==R.id.bnmHomeMI){
                this.showFragment(MainActivity.HOME_FRAGMENT);
            }

        });
    }



    @Override
    public void onFetchComplete() {

    }
}
