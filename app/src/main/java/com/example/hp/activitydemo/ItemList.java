package com.example.hp.activitydemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemList extends RecyclerView.Adapter<ItemList.ViewHolder> {

    List<Item> items;
    OnItemClickListener mItemClickListener;
    private Context context;

    public ItemList(Context context, List<Item> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Item item = items.get(position);

        Glide.with(context)
                .load(item.getUrl())
                .override(800, 600)
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
                .into(holder.imageView);

        holder.textItemName.setText(item.getItem());
        holder.textItemDescription.setText(item.getDescription());
        holder.textItemPrice.setText(item.getPrice());
        holder.quantity.setText(item.getQuantity());
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textItemName;
        public TextView textItemDescription;
        public TextView textItemPrice;
        public ImageView add;
        public ImageView remove;
        public TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.itemImage);
            textItemName = (TextView) itemView.findViewById(R.id.item_name);
            textItemDescription = (TextView) itemView.findViewById(R.id.item_description);
            textItemPrice = (TextView) itemView.findViewById(R.id.price);
            add = (ImageView) itemView.findViewById(R.id.addButton);
            remove = (ImageView) itemView.findViewById(R.id.removeButton);
            quantity = (TextView) itemView.findViewById(R.id.quantity);

            add.setOnClickListener(this);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}