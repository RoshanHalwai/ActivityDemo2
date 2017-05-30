package com.example.hp.activitydemo;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    public static Set<Summary> summaryList = new HashSet<>();
    public static Menu menu;
    RecyclerView listViewItems;
    //a list to store all the Items from firebase database
    List<Item> items;
    //our database reference object
    DatabaseReference databaseItemsAppetizer;
    ImageView add, remove;
    ProgressDialog progressDialog;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {
        BadgeDrawable badgeDrawable;

        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badgeDrawable = (BadgeDrawable) reuse;
        } else {
            badgeDrawable = new BadgeDrawable(context);
        }

        badgeDrawable.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badgeDrawable);
    }

    public void addItemToCart(Context context, int increasedCount) {
        //Context context = getActivity();
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
        BadgeDrawable badgeDrawable = new BadgeDrawable(context);

        String count = badgeDrawable.getCount();
        int iCount = Integer.parseInt(count);
        iCount = iCount + increasedCount;
        count = String.valueOf(iCount);
        setBadgeCount(context, layerDrawable, count);
    }

    public void removeItemFromCart(Context context, int decreasedCount) {
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
        BadgeDrawable badgeDrawable = new BadgeDrawable(context);

        String count = badgeDrawable.getCount();
        int iCount = Integer.parseInt(count);
        if (iCount > 0) {
            iCount = iCount - decreasedCount;
            count = String.valueOf(iCount);
            setBadgeCount(context, layerDrawable, count);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Menu");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Menu for the Day");
        progressDialog.setCancelable(false);
        progressDialog.setInverseBackgroundForced(false);
        progressDialog.show();

        databaseItemsAppetizer = FirebaseDatabase.getInstance().getReference("Appetizer");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listViewItems = (RecyclerView) getView().findViewById(R.id.listViewItems);
        listViewItems.setLayoutManager(layoutManager);
        items = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        //attaching value event listener
        databaseItemsAppetizer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous items list
                items.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting items
                    Item item = postSnapshot.getValue(Item.class);
                    //adding items to the list
                    items.add(item);
                }

                //creating adapter
                final ItemList itemAdapter = new ItemList(getActivity(), items);
                itemAdapter.SetOnItemClickListener(new ItemList.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Item item = items.get(position);

                        boolean itemChanged = false;

                        add = (ImageView) view.findViewById(R.id.addButton);
                        remove = (ImageView) view.findViewById(R.id.removeButton);

                        String strQuantity = item.getQuantity();
                        int count = Integer.parseInt(strQuantity);

                        if (view == add) {
                            if (count + 1 <= 10) {
                                count++;
                                itemChanged = true;
                                addItemsToSummaryList(item.getItem(), item.getPrice(), count);
                                addItemToCart(getActivity(), 1);
                            }
                        }

                        if (view == remove) {
                            if (count - 1 >= 0) {
                                count--;
                                itemChanged = true;
                                removeItemsFromSummaryList(item.getItem(), item.getPrice(), count);
                                removeItemFromCart(getActivity(), 1);
                            }
                        }

                        if (itemChanged) {
                            item.setQuantity(String.valueOf(count));
                            itemAdapter.notifyItemChanged(position);
                        }
                    }

                    private void addItemsToSummaryList(String item, String price, int count) {
                        Summary summary = new Summary();
                        int summaryPrice, summaryCount;
                        boolean foundItem = false;
                        String strPrice = price.substring(3);


                        if (summaryList == null || summaryList.isEmpty()) {
                            summary.setItemName(item);
                            summary.setItemQuantity(String.valueOf(count));
                            summary.setItemPrice(price);
                            summary.setItemActualPrice(price);
                            summaryList.add(summary);
                        } else {
                            for (Summary summaryItem : summaryList) {
                                if (summaryItem.getItemName() == item) {
                                    summaryList.remove(summaryItem);
                                    summary.setItemName(item);
                                    summary.setItemActualPrice(price);
                                    summaryCount = 1 + Integer.parseInt(summaryItem.getItemQuantity());
                                    summary.setItemQuantity(String.valueOf(summaryCount));
                                    summaryPrice = Integer.parseInt(strPrice);
                                    summaryPrice = summaryPrice * summaryCount;
                                    summary.setItemPrice("Rs " + String.valueOf(summaryPrice));
                                    foundItem = true;
                                    summaryList.add(summary);
                                    break;
                                }
                            }
                            if (!foundItem) {
                                summary.setItemName(item);
                                summary.setItemQuantity(String.valueOf(count));
                                summary.setItemPrice(price);
                                summary.setItemActualPrice(price);
                                summaryList.add(summary);
                            }
                        }
                    }

                    private void removeItemsFromSummaryList(String item, String price, int count) {
                        Summary summary = new Summary();
                        int summaryPrice, summaryCount;
                        String strPrice = price.substring(3);


                        for (Summary summaryItem : summaryList) {
                            if (summaryItem.getItemName() == item) {
                                summaryList.remove(summaryItem);
                                summary.setItemName(item);
                                summary.setItemActualPrice(price);
                                summaryCount = Integer.parseInt(summaryItem.getItemQuantity()) - 1;
                                summary.setItemQuantity(String.valueOf(summaryCount));
                                summaryPrice = Integer.parseInt(strPrice);
                                summaryPrice = summaryPrice * summaryCount;
                                summary.setItemPrice("Rs " + String.valueOf(summaryPrice));
                                if (summaryCount > 0)
                                    summaryList.add(summary);
                                break;
                            }
                        }
                    }
                });
                //attaching adapter to the listview
                listViewItems.setAdapter(itemAdapter);

                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
