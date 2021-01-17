package com.example.power_upblackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends AppCompatActivity {

    int clickCount = 1;
    int newCounter = 1;

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

        // https://stackoverflow.com/questions/23805711/trying-to-create-imageview-when-click-the-button
        ImageView redChip = (ImageView) findViewById(R.id.redPokerChipNoBg);
        redChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // https://stackoverflow.com/questions/17417571/how-many-times-a-button-is-clicked-in-android
                //clickCount = clickCount + 1;

                ImageView newCard = new ImageView(GameScreen.this);
                ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintLayoutID);

                //int card3XCoor = 325;
                //int card3YCoor = 712;

                ImageView card1 = (ImageView) findViewById(R.id.playerCard1);
                ImageView card2 = (ImageView) findViewById(R.id.playerCard2);

                float distanceCards = card2.getX() - card1.getX();

                float card2XCoor = card2.getX();
                float card2YCoor = card2.getY();

                float newXCoor = card2XCoor; // initially, then add space between cards below
                float constYCoor = card2YCoor; // always the same

                //FIXME keep adding space between cards until >= 21

                // first card drawn - 3rd overall

                //if (clickCount == 0) {
                  //  xShift = 0; // put in a specific xy location
                //}
                //else { // hit more than once
                  //  xShift = clickCount * distanceCards; // increment the shift so each card is evenly spaced
                    // y coordinate stays the same
                //}

                float xShift = clickCount * distanceCards;
                newXCoor += xShift;
                clickCount += 1;

                Rect actualPosition = new Rect();
                newCard.getGlobalVisibleRect(actualPosition);
                final Rect screen = new Rect(0, 0,
                        Resources.getSystem().getDisplayMetrics().widthPixels,
                        Resources.getSystem().getDisplayMetrics().heightPixels);

                //int newRowCounter = 1;
                boolean secondRowStarted;

                if (newXCoor >= Resources.getSystem().getDisplayMetrics().widthPixels) {
                    //float newXShift = newRowCounter * distanceCards;
                    newXCoor = card1.getX();
                    constYCoor = card1.getY() + 100;
                    secondRowStarted = true;
                }
                else {
                    secondRowStarted = false;
                }

                //int newCounter = 1;
                if (constYCoor > card2YCoor) { // is the y coordinate lower? i.e. is it 2nd row?
                    newXCoor += newCounter * distanceCards;
                }
                newCounter += 1;

                // https://stackoverflow.com/questions/6418726/android-setting-x-y-of-image-programmatically
                newCard.setX(newXCoor);
                newCard.setY(constYCoor);

                TypedArray images = getResources().obtainTypedArray(R.array.apptour);
                int choice = (int) (Math.random() * images.length());

                newCard.setImageResource(images.getResourceId(choice, R.drawable.back_red_basic)); // random png

                //newCard.setImageResource(R.drawable.ace_hearts_white_png);
                cl.addView(newCard);
            }

        });
    }

    // https://stackoverflow.com/questions/18703841/call-method-on-activity-load-android
    @Override
    protected void onStart()
    {
        super.onStart();
        getRandomCard();
        //trackRunningCount();
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

    public TypedArray getRandomCard() {
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

        trackRunningCount(images, choice1, choice2);

        TypedArray nonRecycledArray = images;
        images.recycle(); // https://stackoverflow.com/questions/21354501/typed-array-should-be-recycled-after-use-with-recycle

        return nonRecycledArray;
    }

    public void trackRunningCount(TypedArray imagesProvided, int choice1Param, int choice2Param) {
        // figure out the cumulative value of each of the player's cards

        String cardValue1 = imagesProvided.getString(choice1Param);
        String cardValue2 = imagesProvided.getString(choice2Param);
        int runningCount = 0;

        String[] valueNum = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] valueFace = {"jack", "queen", "king"}; // not ace - dealt with separately

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

        // https://stackoverflow.com/questions/4768969/how-do-i-change-textview-value-inside-java-code
        String runningCountStr = Integer.toString(runningCount);

        TextView textViewToChange = (TextView) findViewById(R.id.runningCountTextView);
        textViewToChange.setText(runningCountStr);

    }

    public void addCard(View view) {
        // https://stackoverflow.com/questions/15097950/adding-imageview-to-the-layout-programmatically

        ImageView newCard = new ImageView(GameScreen.this);
        // ConstraintLayout cl = findViewById(R.id.constraintLayoutID);

        TypedArray newCardImages = getRandomCard();
        int newCardChoice = (int) (Math.random() * newCardImages.length());

        newCard.setImageResource(newCardImages.getResourceId(newCardChoice, R.drawable.back_red_basic)); // random png

        //cl.addView(newCard);

    }

}