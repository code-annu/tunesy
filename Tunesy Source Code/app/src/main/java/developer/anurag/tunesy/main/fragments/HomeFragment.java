package developer.anurag.tunesy.main.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.FragmentHomeBinding;
import developer.anurag.tunesy.main.controllers.HomeFragmentController;
import developer.anurag.tunesy.main.data_model.MixPlaylistDataModel;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.data_model.TrackDataModel;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentHomeBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TrackDataModel trackDataModel=new ViewModelProvider(this.requireActivity()).get(TrackDataModel.class);
        MixPlaylistDataModel mixPlaylistDataModel=new ViewModelProvider(this.requireActivity()).get(MixPlaylistDataModel.class);
        PlayingTracksDataModel playingTracksDataModel=new ViewModelProvider(this.requireActivity()).get(PlayingTracksDataModel.class);

        HomeFragmentController myController=new HomeFragmentController(this.requireContext(),this.binding);
        myController.setTrackDataModel(trackDataModel);
        myController.setMixPlaylistDataModel(mixPlaylistDataModel);
        myController.setFragmentManager(getParentFragmentManager());
        myController.setPlayingTracksDataModel(playingTracksDataModel);
        myController.setFragmentActivity(this.requireActivity());
        myController.setObserverForTrackAndMixPlaylistMap();



    }
}