package com.example.power_upblackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;

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

        // hide action bar to provide more prominence for "Wacky Blackjack" title
        getSupportActionBar().hide();

        //textColor();
        //setStatusBarColor();
        //getActionBar()/* or getSupportActionBar() */.setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));
    }

    public void startGame(View view) {
        Intent playGameIntent = new Intent(this, GameScreen.class);
        startActivity(playGameIntent);
    }

    public void goToOptions(View view) {
        Intent optionsIntent = new Intent(this, Options.class);
        startActivity(optionsIntent);
    }

    public void howToPlay(View view) {
        Intent htpIntent = new Intent(this, HowToPlay.class);
        startActivity(htpIntent);
    }

    public void appInfo(View view) {
        Intent appInfoIntent = new Intent(this, AppInfo.class);
        startActivity(appInfoIntent);
    }


    // https://stackoverflow.com/questions/22192291/how-to-change-the-status-bar-color-in-android?rq=1
    public void setStatusBarColor() {
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.black));
    }

}