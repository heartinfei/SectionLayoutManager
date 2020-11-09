package com.smzdm.core.sectionlayoutmanager;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
//
//    @Override
//    public void onLayoutCompleted(RecyclerView.State state) {
//        super.onLayoutCompleted(state);
//        //onLayoutChildren 执行完毕的时候会调用到这里，只有一次
//    }

    View sectionView;

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int result = super.scrollVerticallyBy(dy, recycler, state);
//        sectionView = getChildAt(3);
//        removeView(sectionView);
//        addView(recycler.getViewForPosition(0));
        //TODO:LinearLayoutManager 的布局完成
//        removeView(sectionView);
//        addView(sectionView);
//        sectionView.layout(0, 0, sectionView.getMeasuredWidth(), sectionView.getMeasuredHeight());
//        if (sectionView == null) {
//            View firstVisibleView = getChildAt(0);
//            RecyclerView.ViewHolder viewHolder = getViewHolderByView(firstVisibleView);
//            if (viewHolder instanceof Section) {
//                sectionView = firstVisibleView;
//                sectionView.layout(0, 0, sectionView.getMeasuredWidth(), sectionView.getMeasuredHeight());
//            }
//        } else {
//            int index = findFirstCompletelyVisibleItemPosition() - findFirstVisibleItemPosition();
//            View firstCompleteView = getChildAt(index);
//            RecyclerView.ViewHolder viewHolder = getViewHolderByView(firstCompleteView);
//            if (viewHolder instanceof Section) {
//
//            } else {
//                sectionView.layout(0, 0, sectionView.getMeasuredWidth(), sectionView.getMeasuredHeight());
//            }
//        }

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
    public void layoutDecoratedWithMargins(@NonNull View child, int left, int top, int right,
                                           int bottom) {
        RecyclerView.ViewHolder viewHolder = getViewHolderByView(child);
        if (viewHolder instanceof Section) {
            child.layout(left,0,right,child.getMeasuredHeight());
        } else {
            super.layoutDecoratedWithMargins(child, left, top, right, bottom);
        }

    }

    @Override
    public void measureChildWithMargins(@NonNull View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);
    }

    @Override
    public void removeAndRecycleViewAt(int index, @NonNull RecyclerView.Recycler recycler) {
        super.removeAndRecycleViewAt(index, recycler);
    }


}
