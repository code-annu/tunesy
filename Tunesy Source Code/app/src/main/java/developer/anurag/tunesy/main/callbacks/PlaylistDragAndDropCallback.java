package developer.anurag.tunesy.main.callbacks;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import developer.anurag.tunesy.main.listeners.PlaylistItemTouchHelperAdapterListener;

public class PlaylistDragAndDropCallback extends ItemTouchHelper.Callback {
    private PlaylistItemTouchHelperAdapterListener myListener;
    public PlaylistDragAndDropCallback(PlaylistItemTouchHelperAdapterListener myListener) {
        this.myListener = myListener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        final int swipeFlags=ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        this.myListener.onItemMove(viewHolder.getAbsoluteAdapterPosition(),target.getAbsoluteAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        this.myListener.onItemDismiss(viewHolder.getAbsoluteAdapterPosition());

    }
}
