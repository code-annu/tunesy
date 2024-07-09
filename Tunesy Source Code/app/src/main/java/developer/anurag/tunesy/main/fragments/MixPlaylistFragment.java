package developer.anurag.tunesy.main.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import developer.anurag.tunesy.databinding.FragmentMixPlaylistBinding;
import developer.anurag.tunesy.main.controllers.MixPlaylistFragmentController;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.data_model.TrackDataModel;


public class MixPlaylistFragment extends Fragment {
    private FragmentMixPlaylistBinding binding;
    private TrackDataModel trackDataModel;
    private PlayingTracksDataModel playingTracksDataModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding= FragmentMixPlaylistBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.trackDataModel=new ViewModelProvider(this.requireActivity()).get(TrackDataModel.class);
        this.playingTracksDataModel=new ViewModelProvider(this.requireActivity()).get(PlayingTracksDataModel.class);

        Handler handler=new Handler();
        handler.postDelayed(()->{
            MixPlaylistFragmentController myController=new MixPlaylistFragmentController(this.requireContext(),this.binding);
            myController.setFragmentActivity(this.requireActivity());
            myController.setTrackDataModel(this.trackDataModel);
            myController.setPlayingTracksDataModel(this.playingTracksDataModel);
            myController.setPlayFABClickListener();
            myController.initTrackContainerRV();
            myController.setNavigationClickListener();
        },300);


    }



}