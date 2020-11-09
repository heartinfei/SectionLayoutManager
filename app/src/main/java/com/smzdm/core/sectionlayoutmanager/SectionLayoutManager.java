package com.smzdm.core.sectionlayoutmanager;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smzdm.core.sectionlayoutmanager.holders.Section;

import java.lang.reflect.Field;

/**
 * @author Rango on 2020/11/5
 */
public class SectionLayoutManager extends LinearLayoutManager {
    String tag = "SectionLayoutManager";

    public SectionLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void detachAndScrapAttachedViews(@NonNull RecyclerView.Recycler recycler) {
//        View sectionView = recycler.getViewForPosition(0);
        super.detachAndScrapAttachedViews(recycler);
    }

    View sectionView;

    /*@Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (sectionView == null) {
            sectionView = findFirstSectionView();
        }
//        removeView(sectionView);
//        recycler.recycleView(sectionView);
        Log.i(tag, "------------------");
    }*/

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        RecyclerView.ViewHolder viewHolder = getViewHolderByView(getChildAt(0));
        int postion = viewHolder.getAdapterPosition();
        if (viewHolder instanceof Section) {
            int top = viewHolder.itemView.getTop();
            Log.i(tag, "top:" + top);
            sectionView = viewHolder.itemView;
        }
        if (sectionView != null) {
            removeView(sectionView);
            recycler.recycleView(sectionView);
        }

        int result = super.scrollVerticallyBy(dy, recycler, state);
        View v = getChildAt(0);
        RecyclerView.ViewHolder viewHolder1 = getViewHolderByView(v);
        int p2 = viewHolder1.getAdapterPosition();

        if (sectionView != null) {
            RecyclerView.ViewHolder vh = getViewHolderByView(getChildAt(0));
            View firstSectionView = vh instanceof Section ? vh.itemView : null;
            if (firstSectionView != null) {
                Log.i(tag, "top2:" + firstSectionView.getTop());
                removeView(firstSectionView);
            } else {

            }
            if (firstSectionView != null && firstSectionView.getTop() < 0) {

            }
            removeView(sectionView);
            addView(sectionView);
            sectionView.layout(0, 0, sectionView.getMeasuredWidth(), sectionView.getMeasuredHeight());

        }

        return result;
    }


    private View findFirstSectionView() {
        for (int i = 0; i < getChildCount(); i++) {
            try {
                RecyclerView.ViewHolder viewHolder = getViewHolderByView(getChildAt(i));
                if (viewHolder instanceof Section) {
                    return viewHolder.itemView;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private RecyclerView.ViewHolder getViewHolderByView(View view) {
        try {
            RecyclerView.LayoutParams lp = ((RecyclerView.LayoutParams) view.getLayoutParams());
            Field viewHolderField = lp.getClass().getDeclaredField("mViewHolder");
            viewHolderField.setAccessible(true);
            return (RecyclerView.ViewHolder) viewHolderField.get(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void offsetChildrenVertical(int dy) {
        super.offsetChildrenVertical(dy);
    }

    @Override
    public void layoutDecorated(@NonNull View child, int left, int top, int right, int bottom) {
        super.layoutDecorated(child, left, top, right, bottom);
    }

    @Override
    public void layoutDecoratedWithMargins(@NonNull View child, int left, int top, int right, int bottom) {
        super.layoutDecoratedWithMargins(child, left, top, right, bottom);
    }

    @Override
    public void measureChildWithMargins(@NonNull View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);
    }

    @Override
    public void removeAndRecycleViewAt(int index, @NonNull RecyclerView.Recycler recycler) {
        super.removeAndRecycleViewAt(index, recycler);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }


}
