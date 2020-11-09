package com.smzdm.core.sectionlayoutmanager.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smzdm.core.sectionlayoutmanager.R;

/**
 * @author Rango on 2020/11/6
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView tv;

    public ItemViewHolder(@NonNull View p) {
        super(LayoutInflater.from(p.getContext()).inflate(R.layout.item_layout, null));
        tv = itemView.findViewById(R.id.tv);
    }
}
