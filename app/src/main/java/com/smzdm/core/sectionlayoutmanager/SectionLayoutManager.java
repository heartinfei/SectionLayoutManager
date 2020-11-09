package com.smzdm.core.sectionlayoutmanager;

import android.content.Context;
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


    /**
     * 整体思路，在
     *
     * @param dy       >0 是手指向上滑动
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //在LinearLayoutManager布局看把要钉住的ViewHolder回收掉，在LinearLayoutManager布局完成后再重新按照自己的
        // 需求排列。
        // 这样做的原因是利用LinearLayoutManager计算当前View的尺寸和滚动距离等参数，我们要做的只是把LineaLayoutManger
        //完成的布局修改一下排列
        if (sectionView != null) {
            removeView(sectionView);
            recycler.recycleView(sectionView);
        }
        if (dy > 0) {
            //手指向上滑动
            RecyclerView.ViewHolder viewHolder = findFirstSectionViewHolder();
            if (viewHolder instanceof Section && sectionView == null) {
                //检查第一个可见的ViewHolder是不是吸顶的ViewHolder，如果是则提取出来
                sectionView = viewHolder.itemView;
            }
        }

        if (sectionView != null) {
            removeView(sectionView);
            recycler.recycleView(sectionView);
        }

        int result = super.scrollVerticallyBy(dy, recycler, state);

        if (sectionView != null) {
            RecyclerView.ViewHolder vh = getViewHolderByView(getChildAt(1));
            View firstSectionView = vh instanceof Section ? vh.itemView : null;

            if (firstSectionView != null
                    && dy < 0
                    && firstSectionView.getTop() > 0) {
//                removeView(sectionView);
//                recycler.recycleView(sectionView);
                sectionView = null;
            } else {
                removeView(sectionView);
                addView(sectionView);
                sectionView.layout(0, 0, sectionView.getMeasuredWidth(), sectionView.getMeasuredHeight());
            }
        }

        return result;
    }


    private RecyclerView.ViewHolder findFirstSectionViewHolder() {
        for (int i = 0; i < getChildCount(); i++) {
            try {
                RecyclerView.ViewHolder viewHolder = getViewHolderByView(getChildAt(i));
                if (viewHolder instanceof Section && viewHolder.itemView != sectionView) {
                    return viewHolder;
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
