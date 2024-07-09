package developer.anurag.tunesy.main.data_model;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

public class FragmentDataModel extends ViewModel {

    private Fragment showingFragment=null;

    public Fragment getShowingFragment() {
        return showingFragment;
    }

    public void setShowingFragment(Fragment showingFragment) {
        this.showingFragment = showingFragment;
    }
}
