package developer.anurag.tunesy.main.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.FragmentPlayerBinding;
import developer.anurag.tunesy.databinding.FragmentPlaylistBinding;
import developer.anurag.tunesy.main.controllers.PlaylistFragmentController;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;


public class PlaylistFragment extends BottomSheetDialogFragment {
    private FragmentPlaylistBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentPlaylistBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        BottomSheetBehavior<FrameLayout> behavior=dialog.getBehavior();
        behavior.setSkipCollapsed(true);
        Window window=dialog.getWindow();
        if(window!=null){
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PlayingTracksDataModel playingTracksDataModel=new ViewModelProvider(this.requireActivity()).get(PlayingTracksDataModel.class);

        PlaylistFragmentController myController=new PlaylistFragmentController(this.getContext(),this.binding);
        myController.setFragmentActivity(this.getActivity());
        myController.setPlayingTracksDataModel(playingTracksDataModel);
        myController.initPlayingPlaylistContainerRV();
        myController.addMediaListener();

    }
}