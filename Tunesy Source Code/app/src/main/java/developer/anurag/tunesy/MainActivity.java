package developer.anurag.tunesy;

import android.content.ComponentName;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;

import developer.anurag.tunesy.databinding.ActivityMainBinding;
import developer.anurag.tunesy.main.controllers.MainActivityController;
import developer.anurag.tunesy.main.controllers.MiniPlayerController;
import developer.anurag.tunesy.main.data_model.FragmentDataModel;
import developer.anurag.tunesy.main.data_model.MixPlaylistDataModel;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.data_model.TrackDataModel;
import developer.anurag.tunesy.main.fragments.HomeFragment;
import developer.anurag.tunesy.main.fragments.LibraryFragment;
import developer.anurag.tunesy.main.fragments.PlayerFragment;
import developer.anurag.tunesy.main.fragments.SearchFragment;
import developer.anurag.tunesy.main.listeners.MainActivityListener;
import developer.anurag.tunesy.main.utils.TunesyFetcher;
import developer.anurag.tunesy.playback.PlaybackService;

public class MainActivity extends AppCompatActivity implements MainActivityListener {
    public static final HomeFragment HOME_FRAGMENT=new HomeFragment();
    public static final SearchFragment SEARCH_FRAGMENT=new SearchFragment();
    public static final LibraryFragment LIBRARY_FRAGMENT=new LibraryFragment();
    private MainActivityController myController;
    private MiniPlayerController miniPlayerController;
    private MediaController mediaController;
    private ActivityMainBinding binding;
    private TrackDataModel trackDataModel;
    private MixPlaylistDataModel mixPlaylistDataModel;
    private FragmentDataModel fragmentDataModel;
    private PlayingTracksDataModel playingTracksDataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        this.trackDataModel = new ViewModelProvider(this).get(TrackDataModel.class);
        this.mixPlaylistDataModel = new ViewModelProvider(this).get(MixPlaylistDataModel.class);
        this.fragmentDataModel = new ViewModelProvider(this).get(FragmentDataModel.class);
        this.playingTracksDataModel = new ViewModelProvider(this).get(PlayingTracksDataModel.class);

        this.setMediaController();


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                myController.onBackPress();
            }
        });
    }


    private void setMediaController() {
        SessionToken sessionToken =
                new SessionToken(this, new ComponentName(this, PlaybackService.class));
        ListenableFuture<MediaController> controllerFuture =
                new MediaController.Builder(this, sessionToken).buildAsync();
        controllerFuture.addListener(() -> {
            try {
                this.mediaController=controllerFuture.get();
                this.initControllers();

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, MoreExecutors.directExecutor());
    }

    private void initControllers(){
        // Set up the controller
        this.myController=new MainActivityController(this, binding);
        this.myController.setTrackDataModel(trackDataModel);
        this.myController.setMixPlaylistDataModel(mixPlaylistDataModel);
        this.myController.setFragmentDataModel(fragmentDataModel);
        this.myController.setPlayingTracksDataModel(playingTracksDataModel);
        this.myController.setFragmentManager(getSupportFragmentManager());
        this.myController.setAppCompatActivity(this);
        this.myController.fetchData();
        this.myController.setBottomNavigationListener();
        this.myController.showFragment(HOME_FRAGMENT);

        // setup the mini player
        this.miniPlayerController=new MiniPlayerController(this,binding);
        this.miniPlayerController.setFragmentManager(getSupportFragmentManager());
        this.miniPlayerController.setPlayingTracksDataModel(playingTracksDataModel);
        this.miniPlayerController.setAppCompatActivity(this);
        this.miniPlayerController.initMiniPlayer();
        this.miniPlayerController.setPlayPauseIBClickListener();
        this.miniPlayerController.setMediaController(this.mediaController);
    }

    @Override
    public void showFragment(Fragment fragmentToShow) {
        this.myController.showFragment(fragmentToShow);
    }

    @Override
    public void backPress() {
        this.myController.onBackPress();
    }

    @Override
    public MediaController getPlaybackMediaController() {
        return this.mediaController;
    }
}