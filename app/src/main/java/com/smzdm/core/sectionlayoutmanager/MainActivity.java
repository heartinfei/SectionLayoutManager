package com.smzdm.core.sectionlayoutmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.smzdm.core.sectionlayoutmanager.holders.ItemViewHolder;
import com.smzdm.core.sectionlayoutmanager.holders.SectionViewHolder;

public class MainActivity extends AppCompatActivity {
    static final String tag = "MainActivity";
    private RecyclerView rlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rlv = findViewById(R.id.rlv);
        rlv.setAdapter(new MyAdapter());
        rlv.setLayoutManager(new SectionLayoutManager(this));
        findViewById(R.id.btn).setOnClickListener(v ->
//                rlv.getAdapter().notifyDataSetChanged()
                        rlv.scrollToPosition(10)
        );
    }

    static class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        SectionViewHolder sectionViewHolder;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                if (sectionViewHolder == null) {
                    sectionViewHolder = new SectionViewHolder(parent);
                }
                boolean b = sectionViewHolder.itemView.isAttachedToWindow();
                return new SectionViewHolder(parent);
            }
            return new ItemViewHolder(parent);
        }

        @Override
        public int getItemViewType(int position) {
            return position % 20 == 0 ? 0 : 1;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Log.i(tag, "onBindViewHolder");
            if (getItemViewType(position) == 1) {
                ((ItemViewHolder) holder).tv.setText("ViewHolder item " + position);
            } else {
                ((SectionViewHolder) holder).tv.setText("Section " + position);
            }
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }
}