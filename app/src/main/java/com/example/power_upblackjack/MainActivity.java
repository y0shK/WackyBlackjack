package com.example.power_upblackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ConstraintLayout cl = findViewById(R.id.constraintLayoutIDMain);

        // set light green hex code in colors.xml
        cl.setBackgroundColor(getResources().getColor(R.color.poker_green));

        // hide action bar to provide more prominence for "Wacky Blackjack" title
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void startGame(View view) {
        Intent playGameIntent = new Intent(this, GameScreen.class);
        startActivity(playGameIntent);
    }

    public void howToPlay(View view) {
        Intent htpIntent = new Intent(this, HowToPlay.class);
        startActivity(htpIntent);
    }

    public void appInfo(View view) {
        Intent appInfoIntent = new Intent(this, AppInfo.class);
        startActivity(appInfoIntent);
    }


}