package com.example.power_upblackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintLayoutIDMain);

        // set light green hex code in colors.xml
        cl.setBackgroundColor(getResources().getColor(R.color.poker_green));
    }

    public void startGame(View view) {
        Intent playGameIntent = new Intent(this, GameScreen.class);
        startActivity(playGameIntent);
    }
}