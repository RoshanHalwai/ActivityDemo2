package com.example.hp.activitydemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HP on 5/7/2017.
 */

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.ViewHolder> {

    List<UserOrder> items;
    private Context context;

    public UserOrderAdapter(Context context, List<UserOrder> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public UserOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_ordered_items, parent, false);
        UserOrderAdapter.ViewHolder viewHolder = new UserOrderAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final UserOrderAdapter.ViewHolder holder, int position) {
        UserOrder item = items.get(position);

        holder.textItemName.setText(item.getItemName());
        holder.quantity.setText(item.getItemQuantity());
        holder.textItemTotalPrice.setText(item.getItemTotalPrice());
        if (Integer.parseInt(item.getItemQuantity()) == 1)
            holder.textItem.setText("Item");
        else
            holder.textItem.setText("Items");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textItemName;
        public TextView textItemTotalPrice;
        public TextView quantity;
        public TextView textItem;

        public ViewHolder(View itemView) {
            super(itemView);
            textItemName = (TextView) itemView.findViewById(R.id.order_item_name);
            textItemTotalPrice = (TextView) itemView.findViewById(R.id.order_item_price);
            quantity = (TextView) itemView.findViewById(R.id.order_item_quantity);
            textItem = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}