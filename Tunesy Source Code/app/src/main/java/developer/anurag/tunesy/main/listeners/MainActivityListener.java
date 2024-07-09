package developer.anurag.tunesy.main.listeners;

import androidx.fragment.app.Fragment;
import androidx.media3.session.MediaController;

public interface MainActivityListener {
    void showFragment(Fragment fragmentToShow);
    void backPress();
    MediaController getPlaybackMediaController();

}
