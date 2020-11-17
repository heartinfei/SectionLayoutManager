package com.smzdm.core.sectionlayoutmanager;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 吸顶ViewHolder的缓存
 *
 * @author Rango on 2020/11/17
 */
public class SectionCache extends Stack<RecyclerView.ViewHolder> {

    private Map<Integer, RecyclerView.ViewHolder> filterMap = new HashMap<>(16, 64);

    @Override
    public RecyclerView.ViewHolder push(RecyclerView.ViewHolder item) {
        if (item == null) {
            return null;
        }
        int position = item.getLayoutPosition();
        //避免存在重复的Value
        if (filterMap.containsKey(position)) {
            //返回null说明没有添加成功
            return null;
        }
        filterMap.put(position, item);
        return super.push(item);
    }

    @Override
    public synchronized RecyclerView.ViewHolder peek() {
        if (size() == 0) {
            return null;
        }
        return super.peek();
    }

    /**
     * 栈顶清理
     * 根据LayoutPosition清理
     *
     * @param layoutPosition 大于position的内容会被清理
     */
    public List<RecyclerView.ViewHolder> clearTop(int layoutPosition) {
        List<RecyclerView.ViewHolder> removedViewHolders = new LinkedList<>();
        Iterator<RecyclerView.ViewHolder> it = iterator();
        while (it.hasNext()) {
            RecyclerView.ViewHolder top = it.next();
            if (top.getLayoutPosition() > layoutPosition) {
                it.remove();
                filterMap.remove(top.getLayoutPosition());
                removedViewHolders.add(top);
            }
        }
        return removedViewHolders;
    }
}
