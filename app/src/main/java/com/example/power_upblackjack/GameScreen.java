package com.example.power_upblackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // https://stackoverflow.com/questions/2868047/fullscreen-activity-in-android
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_screen);

        Intent playGameIntent = getIntent();
    }

    // https://stackoverflow.com/questions/18703841/call-method-on-activity-load-android
    @Override
    protected void onStart()
    {
        super.onStart();
        getRandomCard();
    }


    /*
    // https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    // https://stackoverflow.com/questions/5259872/building-an-array-of-files-in-a-directory
    public static Drawable[] getAllCardsOld(String dirPath) {

        // create a new object of type File with the provided directory string
        File dir = new File(dirPath);

        // create an arrayList to contain the png card files
        ArrayList<String> pngFiles = new ArrayList<String>();

        // make sure the file really is a directory, then add the files
        if (dir.isDirectory()) {
            File[] listOfFiles = dir.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile() || getFileExtension(file).equals(".png")) {
                    pngFiles.add(file.getName());
                }
            }
        }
        Drawable[] pngFilesArray = pngFiles.toArray(new Drawable[]{});
        return pngFilesArray;
    }

    // https://stackoverflow.com/questions/31921927/how-to-get-all-drawable-resources
    public ArrayList<Drawable> getAllCards() {
        // field of drawables gets all values in drawable folder
        Field[] drawables = R.drawable.class.getFields();

        // convert to arrayList
        ArrayList<Drawable> drawableResources = new ArrayList<>();

        for(Field field : drawables)
        {
            // drawable's id added to arraylist
            try {
                //drawableResources.add(field.get);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return drawableResources;
    }
    */

    public void getRandomCard() {
        ImageView img1 = ((ImageView)findViewById(R.id.playerCard1));
        ImageView img2 = ((ImageView)findViewById(R.id.playerCard2));

        // take the values from arrays.xml and create a TypedArray
        // then select a random array element and set the image resource accordingly
        // recycle the TypedArray - doing so is good practice

        // https://stackoverflow.com/questions/3497074/how-to-select-from-resources-randomly-r-drawable-xxxx
        TypedArray images = getResources().obtainTypedArray(R.array.apptour);
        int choice1 = (int) (Math.random() * images.length());
        int choice2 = (int) (Math.random() * images.length());

        img1.setImageResource(images.getResourceId(choice1, R.drawable.back_red_basic)); // random png
        img2.setImageResource(images.getResourceId(choice2, R.drawable.back_red_basic)); // random png

        img1.setTag(images.getResourceId(choice1, R.drawable.back_red_basic));
        img2.setTag(images.getResourceId(choice2, R.drawable.back_red_basic));

        // figure out the cumulative value of each of the player's cards

        String cardValue1 = images.getString(choice1);
        String cardValue2 = images.getString(choice2);
        int runningCount = 0;

        String[] valueNum = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] valueFace = {"jack", "queen", "king"};

        for (String value : valueNum) {
            if (cardValue1.contains(value)) {
                runningCount += Integer.parseInt(value);
            }
            if (cardValue2.contains(value)) {
                runningCount += Integer.parseInt(value);
            }
        }

        for (String value : valueFace) {
            if (cardValue1.contains(value)) {
                runningCount += 10; // jack, queen, and king are all 10
            }
            if (cardValue2.contains(value)) {
                runningCount += 10;
            }
        }

        // for ace, it counts as 11 unless it would bust ( > 21), in which case it counts as 1
        if (cardValue1.contains("ace")) {
            if ((runningCount + 11) <= 21) {
                runningCount += 11;
            }
            else { // > 21
                runningCount += 1;
            }
        }
        if (cardValue2.contains("ace")) {
            if ((runningCount + 11) <= 21) {
                runningCount += 11;
            }
            else { // > 21
                runningCount += 1;
            }
        }

        System.out.println(runningCount);

        images.recycle(); // https://stackoverflow.com/questions/21354501/typed-array-should-be-recycled-after-use-with-recycle
    }

}