package developer.anurag.tunesy.main.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.FragmentSearchBinding;
import developer.anurag.tunesy.main.controllers.SearchFragmentController;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;
import developer.anurag.tunesy.main.data_model.TrackDataModel;


public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentSearchBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initSearchView();

        PlayingTracksDataModel playingTracksDataModel=new ViewModelProvider(requireActivity()).get(PlayingTracksDataModel.class);

        SearchFragmentController myController=new SearchFragmentController(this.getContext(),this.binding);
        myController.setFragmentManager(this.getParentFragmentManager());
        myController.setPlayingTracksDataModel(playingTracksDataModel);
        myController.initSearchResultContainerRV();
        myController.setSearchTrackAndArtistSVListener();
    }

    private void initSearchView(){
        View plateView=this.binding.fsSearchSongArtistSV.findViewById(androidx.appcompat.R.id.search_plate);
        plateView.setBackgroundColor(Color.TRANSPARENT);
        EditText searchEditText=this.binding.fsSearchSongArtistSV.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(requireContext().getColor(R.color.secondary_white));
        searchEditText.setHintTextColor(requireContext().getColor(R.color.graphite));
        ImageView icon=this.binding.fsSearchSongArtistSV.findViewById(androidx.appcompat.R.id.search_mag_icon);
        icon.setColorFilter(requireContext().getColor(R.color.graphite));
    }
}