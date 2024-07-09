package developer.anurag.tunesy.main.adapter_decorators;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrackRowViewHorizontalDecoration extends RecyclerView.ItemDecoration {
    int mTop,mBottom,mLeft,mRight;
    public TrackRowViewHorizontalDecoration(int margin){
        this.mTop=margin;
        this.mRight=margin;
        this.mBottom=margin;
        this.mLeft=margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top=this.mTop;
        outRect.bottom=this.mBottom;
        outRect.left=this.mLeft;
        outRect.right=this.mRight;
    }
}
