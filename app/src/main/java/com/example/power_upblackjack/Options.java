package com.example.power_upblackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Intent playGameIntent = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // hide action bar to provide more prominence for "Wacky Blackjack" title
        getSupportActionBar().hide();

        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintLayoutIDOptions);

        // set light green hex code in colors.xml
        cl.setBackgroundColor(getResources().getColor(R.color.poker_green));
    }
}