package com.example.hp.activitydemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.hp.activitydemo.MenuFragment.summaryList;

public class UserOrderSummary extends AppCompatActivity {

    static String strGrandTotal;
    TextView nickNameAddress, userCompleteAddress, subTotal, taxes, grandTotal;
    String strNickNameAddress, strUserCompleteAddress, strTaxes;
    String userName, userFlat, userStreet, userLocality, userAddress;
    Button proceedToPayment;
    RecyclerView listOrderedItems;
    List<UserOrder> items;
    int iSubTotal, iGrandTotal, iTaxes;
    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        proceedToPayment = (Button) findViewById(R.id.proceedToPayment);
        proceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payment = new Intent(getApplicationContext(), PaymentGateway.class);
                payment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(payment);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listOrderedItems = (RecyclerView) findViewById(R.id.listOrderedItems);
        listOrderedItems.setLayoutManager(layoutManager);
        listOrderedItems.addItemDecoration(new DividerItemDecoration(UserOrderSummary.this, DividerItemDecoration.VERTICAL));

        items = new ArrayList<>();
        for (Summary summaryItem : summaryList) {
            UserOrder orderedItems = new UserOrder();
            orderedItems.setItemName(summaryItem.getItemName());
            orderedItems.setItemTotalPrice(summaryItem.getItemPrice());
            orderedItems.setItemQuantity(summaryItem.getItemQuantity());
            items.add(orderedItems);
        }
        //items = new ArrayList<>(summaryList);


        final UserOrderAdapter orderItemAdapter = new UserOrderAdapter(UserOrderSummary.this, items);
        listOrderedItems.setAdapter(orderItemAdapter);

        nickNameAddress = (TextView) findViewById(R.id.nickNameAddress);
        userCompleteAddress = (TextView) findViewById(R.id.userCompleteAddress);
        subTotal = (TextView) findViewById(R.id.order_summary_subtotal);
        taxes = (TextView) findViewById(R.id.order_summary_taxes);
        grandTotal = (TextView) findViewById(R.id.order_summary_grandtotal);

        strTaxes = taxes.getText().toString().substring(3);

        iSubTotal = Integer.parseInt(SummaryActivity.strTotalItemPrice);
        iTaxes = Integer.parseInt(strTaxes);
        iGrandTotal = iTaxes + iSubTotal;

        strGrandTotal = String.valueOf(iGrandTotal);

        subTotal.setText("Rs " + SummaryActivity.strTotalItemPrice);
        grandTotal.setText("Rs " + strGrandTotal);

        SharedPreferences sharedPreferences = getSharedPreferences("userAddress", Context.MODE_PRIVATE);

        userName = sharedPreferences.getString("name", "");
        userFlat = sharedPreferences.getString("flat", "");
        userStreet = sharedPreferences.getString("street", "");
        userLocality = sharedPreferences.getString("locality", "");

        userAddress = (userFlat + ", " + userStreet + ", " + userLocality);

        databaseUser = FirebaseDatabase.getInstance().getReference("Users");

        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("8883458716")) {
                    databaseUser = FirebaseDatabase.getInstance().getReference("Users").child("8883458716");
                    databaseUser.child("Name").setValue(userName);
                    databaseUser.child("Address").setValue(userAddress);
                    return;
                } else {
                    if (!dataSnapshot.child("8883458716").child("Address").getValue().toString().equals(userAddress)) {
                        databaseUser.child("8883458716").child("Address").setValue(userAddress);
                    }
                    if (!dataSnapshot.child("8883458716").child("Name").getValue().toString().equals(userName)) {
                        databaseUser.child("8883458716").child("Name").setValue(userName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        strNickNameAddress = sharedPreferences.getString("nickname", "");
        strUserCompleteAddress = sharedPreferences.getString("salute", "") + userName + "\n" + userFlat + "\n" + userStreet
                + "\n" + userLocality;

        nickNameAddress.setText(strNickNameAddress);
        userCompleteAddress.setText(strUserCompleteAddress);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Address.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

