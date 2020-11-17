package com.smzdm.core.sectionlayoutmanager;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smzdm.core.sectionlayoutmanager.holders.Section;

import java.lang.reflect.Field;
import java.util.SortedSet;
import java.util.TreeSet;

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

    /**
     * 最多吸顶个数
     */
    private int maxSectionCount = 1;

    /**
     * 存储所有 section position
     * 在滚动的过程中进行更新这个已经实现，记录所有的Section的Position，但是有以下情况需要考虑
     * 1. adapter.notifyDataSetChanged()一系列方法调用的时候 更新问题
     * 2. scrollToPosition -- 更新问题
     * 3. 快速滚动的时候有些ViewHolder的绘制过程是省略的
     */
    private SortedSet<Integer> sectionPositions = new TreeSet<>();

    /**
     * position < firstVisibleItemPosition的SectionViewHolder
     * 存储已经吸顶的Section（或已经滚动过去的SectionViewHolder）
     */
    private SectionCache sectionCache = new SectionCache();

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
//        for (int i = 0; i < getChildCount(); i++) {
//            RecyclerView.ViewHolder vh = getViewHolderByView(getChildAt(i));
//            int position = vh.getLayoutPosition();
//            if (vh instanceof Section) {
//                sectionPositions.add(position);
//            } else {
//                sectionPositions.remove(position);
//            }
//        }
    }

    private void removeAllSections() {
        for (RecyclerView.ViewHolder viewHolder : sectionCache) {
            removeView(viewHolder.itemView);
        }
    }


    /**
     * 整体思路，
     * 在手指上滑（向上滚动 dy > 0）的时候,SectionViewHolder不能进入Holder缓存，需要独立缓存
     * 在手指下滑（向下滚动 dy < 0）的时候，SectionViewHolder在离开吸顶位置的时候需要将该ViewHolder重新放入缓存池，
     * 正常显示在列表中
     *
     * @param dy       >0 是手指向上滑动
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        for (int i = 0; i < getChildCount(); i++) {
            View itemView = getChildAt(i);
            RecyclerView.ViewHolder vh = getViewHolderByView(itemView);
            if (!(vh instanceof Section) || sectionCache.peek() == vh) {
                continue;
            }
            if (dy > 0 && vh.itemView.getTop() < dy) {
                sectionCache.push(vh);
                Log.i(tag, "搜集：dy=" + dy);
            } else {
                break;
            }
        }
        //拒绝进入系统的回收复用策略
        removeAllSections();

        int result = super.scrollVerticallyBy(dy, recycler, state);

        RecyclerView.ViewHolder vh = getViewHolderByView(getChildAt(0));
        RecyclerView.ViewHolder attachedSection = sectionCache.peek();
        if ((vh instanceof Section)
                && attachedSection != null
                && attachedSection.getLayoutPosition() == vh.getLayoutPosition()) {
            removeViewAt(0);
        }

        //检查栈顶 -- 同步状态
        for (RecyclerView.ViewHolder removedViewHolder : sectionCache.clearTop(findFirstVisibleItemPosition())) {
            Log.i(tag, "移除ViewHolder:" + removedViewHolder.toString());
            for (int i = 0; i < getChildCount(); i++) {
                RecyclerView.ViewHolder attachedViewHolder = getViewHolderByView(getChildAt(i));
                if (removedViewHolder.getLayoutPosition() == attachedViewHolder.getLayoutPosition()) {
                    View attachedItemView = attachedViewHolder.itemView;
                    int left = attachedItemView.getLeft();
                    int top = attachedItemView.getTop();
                    int bottom = attachedItemView.getBottom();
                    int right = attachedItemView.getRight();

                    removeView(attachedItemView);
                    addView(removedViewHolder.itemView, i);
                    removedViewHolder.itemView.layout(left, top, right, bottom);
                    break;
                }
            }
        }

        RecyclerView.ViewHolder section = sectionCache.peek();
        if (section != null) {
            View itemView = section.itemView;
            if (!itemView.isAttachedToWindow()) {
                addView(itemView);
            }
            View subItem = getChildAt(1);
            if (getViewHolderByView(subItem) instanceof Section) {
                int h = itemView.getMeasuredHeight();
                int top = Math.min(0, -(h - subItem.getTop()));
                int bottom = Math.min(h, subItem.getTop());
                itemView.layout(0, top, itemView.getMeasuredWidth(), bottom);
            } else {
                itemView.layout(0, 0, itemView.getMeasuredWidth(), itemView.getMeasuredHeight());
            }
        }
        return result;
    }


    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        //TODO: 搜集ViewHolderSection
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        super.smoothScrollToPosition(recyclerView, state, position);
    }


    private RecyclerView.ViewHolder getViewHolderByView(View view) {
        if (view == null) {
            return null;
        }
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


}
