package com.example.appwithfragment.RecyclerViewFragment;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Евгения on 22.08.2016.
 */
public class RecyclerViewDecorator extends RecyclerView.ItemDecoration {
    private int dividerWidth = 0;
    public RecyclerViewDecorator(int px){
        dividerWidth = px;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int span = ((GridLayoutManager)parent.getLayoutManager()).getSpanCount();
        outRect.bottom +=dividerWidth;
        if(span == 3){
            if(parent.getChildLayoutPosition(view)%3 != 0){
                outRect.left += dividerWidth;
            }else outRect.left = 0;

        }else {
            if(parent.getChildLayoutPosition(view)% 2 != 0){
                outRect.right = 0;
            }else
                outRect.right += dividerWidth;
            if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
                outRect.top += dividerWidth;
            }
        }

    }
}
