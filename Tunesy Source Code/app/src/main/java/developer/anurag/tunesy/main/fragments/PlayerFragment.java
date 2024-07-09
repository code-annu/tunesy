package developer.anurag.tunesy.main.fragments;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import developer.anurag.tunesy.R;
import developer.anurag.tunesy.databinding.FragmentPlayerBinding;
import developer.anurag.tunesy.main.controllers.PlayerFragmentController;
import developer.anurag.tunesy.main.data_model.PlayingTracksDataModel;


public class PlayerFragment extends BottomSheetDialogFragment {
    private FragmentPlayerBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        BottomSheetBehavior<FrameLayout> behavior=dialog.getBehavior();
        behavior.setSkipCollapsed(true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        Window window=dialog.getWindow();
        if(window!=null){
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentPlayerBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.requestLayout();



        PlayingTracksDataModel playingTracksDataModel=new ViewModelProvider(this.requireActivity()).get(PlayingTracksDataModel.class);

        PlayerFragmentController myController=new PlayerFragmentController(this.getContext(),this.binding);
        myController.setFragmentActivity(this.getActivity());
        myController.setPlayingTracksDataModel(playingTracksDataModel);
        myController.initUi();
        myController.setUpNextBtnClickListener();
        myController.setPlayPauseIBClickListener();
        myController.setNextAndPreviousIBClickListener();
        myController.addMediaControllerListener();
        myController.setPlayingTrackProgressSB();
        myController.setClosePlayerFragmentIBClickListener(this);


    }




}