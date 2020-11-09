package com.smzdm.core.sectionlayoutmanager.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smzdm.core.sectionlayoutmanager.R;

/**
 * @author Rango on 2020/11/6
 */
public class SectionViewHolder extends RecyclerView.ViewHolder implements Section {
    public TextView tv;

    public SectionViewHolder(@NonNull View p) {
        super(LayoutInflater.from(p.getContext()).inflate(R.layout.section_item_layout, ((ViewGroup) p), false));
        tv = itemView.findViewById(R.id.tv);
//        setIsRecyclable(false);
    }


}
