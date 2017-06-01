package com.example.hp.activitydemo;


import android.content.DialogInterface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;

/**
 * A simple {@link Fragment} subclass.
 */
public class accountFragment extends Fragment implements View.OnClickListener {


    Button logOut;
    MainActivity main;

    public accountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logOut = (Button) view.findViewById(R.id.log_out);
        logOut.setOnClickListener(this);
        getActivity().setTitle("Account");
    }

    @Override
    public void onClick(View view) {
        if (view == logOut) {
            String title = "Log Out";
            String message = "Are you sure you want to Log Out?";
            confirmMessageBox(title, message);
        }
    }

    private void confirmMessageBox(final String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*MenuFragment.summaryList.clear();
                MenuItem itemCart = MenuFragment.menu.findItem(R.id.action_cart);
                LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
                BadgeDrawable badgeDrawable = new BadgeDrawable(getApplicationContext());
                MenuFragment.setBadgeCount(getApplicationContext(), layerDrawable, "0");
                fragment = new MenuFragment();
                replaceFragment(fragment);*/
                logOutUser();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                /*fragment = new MenuFragment();
                replaceFragment(fragment);*/
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logOutUser() {
        AuthUI.getInstance().signOut(getActivity());
    }

}
