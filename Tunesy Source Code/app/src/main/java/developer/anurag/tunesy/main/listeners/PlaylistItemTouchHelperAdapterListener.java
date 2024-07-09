package developer.anurag.tunesy.main.listeners;

public interface PlaylistItemTouchHelperAdapterListener {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
