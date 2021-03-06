package com.example.excelpanel.excelpanel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ViewGroup;

import java.util.List;



public class TopRecyclerViewAdapter<T> extends RecyclerViewAdapter<T> {

    private OnExcelPanelListener excelPanelListener;

    public TopRecyclerViewAdapter(Context context, List<T> list, OnExcelPanelListener excelPanelListener) {
        super(context, list);
        this.excelPanelListener = excelPanelListener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = super.getItemViewType(position);
        if(viewType == TYPE_NORMAL){
            viewType = excelPanelListener.getTopItemViewType(position);
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        if (excelPanelListener != null) {
            return excelPanelListener.onCreateTopViewHolder(parent, viewType);
        } else {
            return null;
        }
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (excelPanelListener != null) {
            excelPanelListener.onBindTopViewHolder(holder, position);
            //use to adjust width
            holder.itemView.setTag(ExcelPanel.TAG_KEY, new Pair<>(0, position));
            excelPanelListener.onAfterBind(holder, position, false, true);
        }
    }
}
