package com.example.hp.activitydemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean cartIconPressed = false;
    RecyclerView listViewItems;
    //a list to store all the Items from firebase database
    List<Item> items;
    //our database reference object
    DatabaseReference databaseItemsAppetizer;
    ImageView add, remove;
    ProgressDialog progressDialog;
    Fragment fragment = null;
    private Menu menu;
    private Typeface typeface;

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

    private void addItemToCart() {
        Context context = this.getApplicationContext();
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
        BadgeDrawable badgeDrawable = new BadgeDrawable(context);

        String count = badgeDrawable.getCount();
        int iCount = Integer.parseInt(count);
        iCount = iCount + 1;
        count = String.valueOf(iCount);
        setBadgeCount(context, layerDrawable, count);
    }

    private void removeItemFromCart() {
        Context context = this.getApplicationContext();
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
        BadgeDrawable badgeDrawable = new BadgeDrawable(context);

        String count = badgeDrawable.getCount();
        int iCount = Integer.parseInt(count);
        if (iCount > 0) {
            iCount = iCount - 1;
            count = String.valueOf(iCount);
            setBadgeCount(context, layerDrawable, count);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_new_order);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuFragment.menu = menu;

        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
        if (BadgeDrawable.mCount != "")
            setBadgeCount(this, layerDrawable, BadgeDrawable.mCount);
        else
            setBadgeCount(this, layerDrawable, "0");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up addItembutton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            cartIconPressed = true;
            startActivity(new Intent(getApplicationContext(), SummaryActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        if (itemId == R.id.nav_new_order) {
            fragment = new MenuFragment();
        } else if (itemId == R.id.nav_account) {
            fragment = new accountFragment();
        } else if (itemId == R.id.nav_refer_and_earn) {
            Intent i = new Intent(MainActivity.this, ReferAndEarn.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_new_order) {
            if (MenuFragment.summaryList.size() == 0) {
                fragment = new MenuFragment();
            } else {
                String title = "New Order";
                String message = "Are you sure you want to create a " + "\n" +
                        "new order?" + " Your current order will be lost";
                displayMessageBox(title, message);
            }
        } else if (id == R.id.nav_account) {
            fragment = new accountFragment();
        } else if (id == R  .id.nav_refer_and_earn) {
            Intent i = new Intent(MainActivity.this, ReferAndEarn.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        replaceFragment(fragment);
        return true;
    }

    private void displayMessageBox(final String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MenuFragment.summaryList.clear();
                MenuItem itemCart = MenuFragment.menu.findItem(R.id.action_cart);
                LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
                BadgeDrawable badgeDrawable = new BadgeDrawable(getApplicationContext());
                MenuFragment.setBadgeCount(getApplicationContext(), layerDrawable, "0");
                fragment = new MenuFragment();
                replaceFragment(fragment);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                fragment = new MenuFragment();
                replaceFragment(fragment);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
