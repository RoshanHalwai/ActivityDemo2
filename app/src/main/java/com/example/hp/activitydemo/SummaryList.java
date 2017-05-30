package com.example.hp.activitydemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.hp.activitydemo.MenuFragment.summaryList;

/**
 * Created by HP on 5/4/2017.
 */

public class SummaryList extends RecyclerView.Adapter<SummaryList.ViewHolder> {

    List<Summary> items;
    SummaryList.OnItemClickListener mItemClickListener;
    private Context context;

    public SummaryList(Context context, List<Summary> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public SummaryList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_summary_items_list, parent, false);
        SummaryList.ViewHolder viewHolder = new SummaryList.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final SummaryList.ViewHolder holder, int position) {
        Summary item = items.get(position);

        holder.textItemName.setText(item.getItemName());
        holder.textItemPrice.setText(item.getItemPrice());
        holder.quantity.setText(item.getItemQuantity());
        holder.textItemActualPrice.setText(item.getItemActualPrice());
    }

    public void SetOnItemClickListener(final SummaryList.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textItemName;
        public TextView textItemPrice;
        public TextView textItemActualPrice;
        public ImageView add;
        public ImageView remove;
        public ImageView deleteItem;
        public TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            textItemName = (TextView) itemView.findViewById(R.id.summary_item_name);
            textItemPrice = (TextView) itemView.findViewById(R.id.summary_item_price);
            textItemActualPrice = (TextView) itemView.findViewById(R.id.summary_item_actual_price);
            add = (ImageView) itemView.findViewById(R.id.summary_addButton);
            remove = (ImageView) itemView.findViewById(R.id.summary_removeButton);
            quantity = (TextView) itemView.findViewById(R.id.summary_item_quantity);
            deleteItem = (ImageView) itemView.findViewById(R.id.deleteItem);

            add.setOnClickListener(this);
            remove.setOnClickListener(this);
            deleteItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }

            if (v == deleteItem) {
                items.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), items.size());
                for (Summary summaryItem : summaryList) {
                    if (summaryItem.getItemName() == textItemName.getText()) {
                        summaryList.remove(summaryItem);
                        break;
                    }
                }
                if (summaryList.size() == 0) {
                    SummaryActivity summaryActivity = new SummaryActivity();
                    summaryActivity.emptyCartLayout();
                }
            }
        }
    }
}
