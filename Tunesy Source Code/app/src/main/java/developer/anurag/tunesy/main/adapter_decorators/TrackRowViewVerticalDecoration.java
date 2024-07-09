package developer.anurag.tunesy.main.adapter_decorators;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrackRowViewVerticalDecoration extends RecyclerView.ItemDecoration {
    int mTop,mBottom,mLeft,mRight,mTopOfFirstItem,mBottomOfLastItem;

    public TrackRowViewVerticalDecoration(int margin, int marginTopOfFirstItem, int marginBottomOfLastItem){
        this.mTop=margin;
        this.mRight=margin;
        this.mBottom=margin;
        this.mLeft=margin;
        this.mTopOfFirstItem=marginTopOfFirstItem;
        this.mBottomOfLastItem=marginBottomOfLastItem;
    }

    public TrackRowViewVerticalDecoration(int mTop,int mRight,int mBottom,int mLeft, int marginTopOfFirstItem, int marginBottomOfLastItem){
        this.mTop=mTop;
        this.mRight=mRight;
        this.mBottom=mBottom;
        this.mLeft=mLeft;
        this.mTopOfFirstItem=marginTopOfFirstItem;
        this.mBottomOfLastItem=marginBottomOfLastItem;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position=parent.getChildAdapterPosition(view);
        int itemCount=0;

        if(parent.getAdapter()!=null){
            itemCount=parent.getAdapter().getItemCount();
        }

        if(position==0){
            outRect.top=this.mTopOfFirstItem;
        }
        else if(position==itemCount-1){
            outRect.bottom=this.mBottomOfLastItem;
        }else {
            outRect.top=this.mTop;
            outRect.bottom=this.mBottom;
        }
        outRect.left=this.mLeft;
        outRect.right=this.mRight;
    }
}
