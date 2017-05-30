package com.example.hp.activitydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.hp.activitydemo.MenuFragment.summaryList;

public class SummaryActivity extends AppCompatActivity {

    static View emptyCartView;
    static RelativeLayout relativeLayout;
    static LinearLayout emptyCartLayout;
    static String strTotalItemPrice = "0";
    RecyclerView listSummaryItems;
    //a list to store all the Items from firebase database
    List<Summary> items;
    TextView totalItemPrice;
    Button proceed, backToMenu;
    int iTotalItemPrice = 0;
    LinearLayout linearLayout;
    ImageView add, remove, deleteItem;
    boolean itemChanged;
    int iUpdatedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        relativeLayout = (RelativeLayout) findViewById(R.id.layout_cart_items);
        emptyCartLayout = (LinearLayout) findViewById(R.id.layout_empty_cart);

        backToMenu = (Button) findViewById(R.id.backToMenu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (summaryList.size() != 0) {

            relativeLayout.setVisibility(View.VISIBLE);
            emptyCartLayout.setVisibility(View.GONE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            final MenuFragment menuFragment = new MenuFragment();
            itemChanged = false;
            totalItemPrice = (TextView) findViewById(R.id.totalPrice);
            linearLayout = (LinearLayout) findViewById(R.id.checkout);
            proceed = (Button) findViewById(R.id.proceed);


            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), UserLogin.class));
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            listSummaryItems = (RecyclerView) findViewById(R.id.listSummaryItems);
            listSummaryItems.setLayoutManager(layoutManager);
            listSummaryItems.setItemAnimator(null);
            items = new ArrayList<>(summaryList);

            final SummaryList summaryItemAdapter = new SummaryList(SummaryActivity.this, items);
            listSummaryItems.setAdapter(summaryItemAdapter);

            summaryItemAdapter.SetOnItemClickListener(new SummaryList.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Summary item = items.get(position);

                    deleteItem = (ImageView) view.findViewById(R.id.deleteItem);
                    add = (ImageView) view.findViewById(R.id.summary_addButton);
                    remove = (ImageView) view.findViewById(R.id.summary_removeButton);

                    String strQuantity = item.getItemQuantity();
                    int count = Integer.parseInt(strQuantity);


                    if (view == add) {
                        if (count + 1 <= 10) {
                            count++;
                            itemChanged = true;
                            menuFragment.addItemToCart(SummaryActivity.this, 1);
                        } else
                            itemChanged = false;
                    }

                    if (view == remove) {
                        if (count - 1 >= 1) {
                            count--;
                            itemChanged = true;
                            menuFragment.removeItemFromCart(SummaryActivity.this, 1);
                        } else
                            itemChanged = false;
                    }

                    if (itemChanged) {
                        item.setItemQuantity(String.valueOf(count));
                        String strCurrentPrice = item.getItemPrice().substring(3);
                        int iCurrentPrice = Integer.parseInt(strCurrentPrice);
                        String strActualPrice = item.getItemActualPrice().substring(3);
                        int iActualPrice = Integer.parseInt(strActualPrice);
                        if (view == add) {
                            iUpdatedPrice = iCurrentPrice + iActualPrice;
                            iTotalItemPrice = iTotalItemPrice + iActualPrice;
                        } else {
                            iUpdatedPrice = iCurrentPrice - iActualPrice;
                            iTotalItemPrice = iTotalItemPrice - iActualPrice;
                        }
                        String strUpdatedPrice = String.valueOf(iUpdatedPrice);
                        item.setItemPrice("Rs " + strUpdatedPrice);
                        summaryItemAdapter.notifyItemChanged(position);

                        strTotalItemPrice = String.valueOf(iTotalItemPrice);
                        totalItemPrice.setText("Total: Rs " + strTotalItemPrice);
                    }

                    if (view == deleteItem) {
                        String strCurrentPrice = item.getItemPrice().substring(3);
                        String strCurrentQuantity = item.getItemQuantity();
                        int iCurrentQuantity = Integer.parseInt(strCurrentQuantity);
                        int iCurrentPrice = Integer.parseInt(strCurrentPrice);
                        iTotalItemPrice = iTotalItemPrice - iCurrentPrice;
                        strTotalItemPrice = String.valueOf(iTotalItemPrice);
                        totalItemPrice.setText("Total: Rs " + strTotalItemPrice);
                        menuFragment.removeItemFromCart(SummaryActivity.this, iCurrentQuantity);
                    }

                    if (strTotalItemPrice == "0")
                        linearLayout.setVisibility(View.GONE);
                }
            });

            for (Summary summaryItem : summaryList) {
                String strPrice = summaryItem.getItemPrice();
                strPrice = strPrice.substring(3);
                int iPrice = Integer.valueOf(strPrice);
                iTotalItemPrice = iTotalItemPrice + iPrice;
                strTotalItemPrice = String.valueOf(iTotalItemPrice);
            }
            totalItemPrice.setText("Total: Rs " + strTotalItemPrice);

            if (strTotalItemPrice == "0")
                linearLayout.setVisibility(View.GONE);
        } else {
            emptyCartLayout();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void emptyCartLayout() {
        relativeLayout.setVisibility(View.GONE);
        emptyCartLayout.setVisibility(View.VISIBLE);
    }
}
