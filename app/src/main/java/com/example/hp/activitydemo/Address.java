package com.example.hp.activitydemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Address extends AppCompatActivity implements View.OnClickListener {

    Button home, office, other, selectedButton;
    EditText editTextName, editTextFlat, editTextStreet, editTextLocality;
    RadioButton radioMr, radioMrs, radioMiss, selectedRadioButton;
    String name, flat, street, locality, strSalute;
    RadioGroup salute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences sharedPref = getSharedPreferences("userAddress", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("name", "");

        salute = (RadioGroup) findViewById(R.id.salute);

        home = (Button) findViewById(R.id.nickHome);
        office = (Button) findViewById(R.id.nickOffice);
        other = (Button) findViewById(R.id.nickOther);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextFlat = (EditText) findViewById(R.id.editTextFlat);
        editTextStreet = (EditText) findViewById(R.id.editTextStreet);
        editTextLocality = (EditText) findViewById(R.id.editTextLocality);

        radioMr = (RadioButton) findViewById(R.id.radioMr);
        radioMrs = (RadioButton) findViewById(R.id.radioMrs);
        radioMiss = (RadioButton) findViewById(R.id.radioMiss);

        selectedRadioButton = radioMr;

        selectedButton = home;

        if (!userName.equals("")) {
            editTextName.setText(sharedPref.getString("name", ""));
            editTextFlat.setText(sharedPref.getString("flat", ""));
            editTextStreet.setText(sharedPref.getString("street", ""));
            editTextLocality.setText(sharedPref.getString("locality", ""));
            if (sharedPref.getString("salute", "").equals("Mr."))
                checkRadioButton(radioMr);
            else if (sharedPref.getString("salute", "").equals("Mrs."))
                checkRadioButton(radioMrs);
            else
                checkRadioButton(radioMiss);

            if (sharedPref.getString("nickname", "").equals("Home"))
                checkNickNameButton(home);
            else if (sharedPref.getString("nickname", "").equals("Office"))
                checkNickNameButton(office);
            else
                checkNickNameButton(other);

            editTextName.setEnabled(false);
            editTextFlat.setEnabled(false);
            editTextStreet.setEnabled(false);
            editTextLocality.setEnabled(false);

            home.setEnabled(false);
            office.setEnabled(false);
            other.setEnabled(false);

            for (int i = 0; i < salute.getChildCount(); i++)
                salute.getChildAt(i).setEnabled(false);
        }

        home.setOnClickListener(this);
        office.setOnClickListener(this);
        other.setOnClickListener(this);

        radioMr.setOnClickListener(this);
        radioMrs.setOnClickListener(this);
        radioMiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == home) {
            selectedButton = home;
            checkNickNameButton(home);
        } else if (v == office) {
            selectedButton = office;
            checkNickNameButton(office);
        } else if (v == other) {
            selectedButton = other;
            checkNickNameButton(other);
        } else if (v == radioMr) {
            selectedRadioButton = radioMr;
            checkRadioButton(radioMr);
        } else if (v == radioMrs) {
            selectedRadioButton = radioMrs;
            checkRadioButton(radioMrs);
        } else if (v == radioMiss) {
            selectedRadioButton = radioMiss;
            checkRadioButton(radioMiss);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add) {
            name = editTextName.getText().toString();
            flat = editTextFlat.getText().toString();
            street = editTextStreet.getText().toString();
            locality = editTextLocality.getText().toString();
            if (name.length() == 0) {
                editTextName.setError("User Name is invalid!");
            } else if (flat.length() == 0) {
                editTextFlat.setError("Address is invalid!");
            } else if (street.length() == 0) {
                editTextStreet.setError("Address is invalid!");
            } else if (locality.length() == 0) {
                editTextLocality.setError("Address is invalid!");
            } else {
                storeUserAddressToSharedPreference(name, flat, street, locality,
                        selectedRadioButton.getText().toString(), selectedButton.getText().toString());
                startActivity(new Intent(getApplicationContext(), UserOrderSummary.class));
            }
        } else if (id == R.id.action_edit) {
            editTextName.setEnabled(true);
            editTextFlat.setEnabled(true);
            editTextStreet.setEnabled(true);
            editTextLocality.setEnabled(true);

            home.setEnabled(true);
            office.setEnabled(true);
            other.setEnabled(true);

            salute.setEnabled(true);
            for (int i = 0; i < salute.getChildCount(); i++)
                salute.getChildAt(i).setEnabled(true);
        }

        return super.onOptionsItemSelected(item);
    }

    private void storeUserAddressToSharedPreference(String name, String flat, String street,
                                                    String locality, String strSalute, String nickname) {
        SharedPreferences sharedPref = getSharedPreferences("userAddress", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("name", name);
        editor.putString("flat", flat);
        editor.putString("street", street);
        editor.putString("locality", locality);
        editor.putString("salute", strSalute);
        editor.putString("nickname", nickname);
        editor.apply();
    }

    private void checkRadioButton(RadioButton v) {
        if (v == radioMr) {
            radioMr.setChecked(true);
            radioMrs.setChecked(false);
            radioMiss.setChecked(false);
        } else if (v == radioMrs) {
            radioMr.setChecked(false);
            radioMrs.setChecked(true);
            radioMiss.setChecked(false);
        } else {
            radioMr.setChecked(false);
            radioMrs.setChecked(false);
            radioMiss.setChecked(true);
        }
    }

    private void checkNickNameButton(Button v) {
        if (v == home) {
            home.setBackground(getDrawable(R.drawable.my_button_selected_bg));
            office.setBackground(getDrawable(R.drawable.my_button_bg));
            other.setBackground(getDrawable(R.drawable.my_button_bg));
        } else if (v == office) {
            home.setBackground(getDrawable(R.drawable.my_button_bg));
            office.setBackground(getDrawable(R.drawable.my_button_selected_bg));
            other.setBackground(getDrawable(R.drawable.my_button_bg));
        } else {
            home.setBackground(getDrawable(R.drawable.my_button_bg));
            office.setBackground(getDrawable(R.drawable.my_button_bg));
            other.setBackground(getDrawable(R.drawable.my_button_selected_bg));
        }
    }
}
