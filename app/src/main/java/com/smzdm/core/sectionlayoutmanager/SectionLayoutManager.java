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

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (sectionView == null) {
            sectionView = findFirstSectionView();
        }
//        removeView(sectionView);
//        recycler.recycleView(sectionView);
        Log.i(tag, "------------------");
    }

    int distance = 0;
    int lastTop = -1;

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        removeView(sectionView);
        recycler.recycleView(sectionView);
        int result = super.scrollVerticallyBy(dy, recycler, state);
        removeView(findFirstSectionView());
        addView(sectionView);
        sectionView.layout(0, 0, sectionView.getMeasuredWidth(), sectionView.getMeasuredHeight());
        return result;
    }


    private View findFirstSectionView() {
        for (int i = 0; i < getChildCount(); i++) {
            try {
                RecyclerView.LayoutParams lp = ((RecyclerView.LayoutParams) getChildAt(i).getLayoutParams());
                Field viewHolderField = lp.getClass().getDeclaredField("mViewHolder");
                viewHolderField.setAccessible(true);
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) viewHolderField.get(lp);
                if (viewHolder instanceof Section) {
                    return viewHolder.itemView;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
