package com.example.power_upblackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

public class HowToPlay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        Intent htpIntent = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // hide action bar to provide more prominence for "Wacky Blackjack" title
        Objects.requireNonNull(getSupportActionBar()).hide();

        ConstraintLayout cl = findViewById(R.id.howToPlayID);

        // set light green hex code in colors.xml
        cl.setBackgroundColor(getResources().getColor(R.color.poker_green));
    }

}