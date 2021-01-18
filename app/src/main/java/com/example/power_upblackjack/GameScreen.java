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

    int clickCount = 1; // click count for the first row of cards
    int newCounter = 0; // click count for the second row of cards

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

                // create the new card programmatically
                ImageView newCard = new ImageView(GameScreen.this);

                // find ID of constraintLayout to reference the xml file to add the card to
                ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintLayoutID);

                // the goal is to programmatically add cards instead of experimentally locating them
                // find the xy coordinates of each card, then place each card a fixed distance away from the previous card
                // for the second row, re-locate the initial x coordinate below the first row, then repeat

                // instantiate cards to reference them later
                ImageView card1 = (ImageView) findViewById(R.id.playerCard1);
                ImageView card2 = (ImageView) findViewById(R.id.playerCard2);

                // x distance
                float distanceCards = card2.getX() - card1.getX();

                // xy coordinates of the 2nd card (dealt automatically)
                float card2XCoor = card2.getX();
                float card2YCoor = card2.getY();

                float newXCoor = card2XCoor; // initially, then add space between cards below
                float constYCoor = card2YCoor; // the same until the second row is added

                // create the x distance between cards
                float xShift = clickCount * distanceCards; // card n X = card n-1 X + xShift
                newXCoor += xShift;
                clickCount += 1;

                // https://stackoverflow.com/questions/6418726/android-setting-x-y-of-image-programmatically
                newCard.setX(newXCoor);
                newCard.setY(constYCoor);

                // check if card goes off the screen
                // if it goes off the screen, wrap the x back to the initial spot and increment again
                // https://stackoverflow.com/questions/14039454/how-can-you-tell-if-a-view-is-visible-on-screen-in-android
                if (newXCoor >= Resources.getSystem().getDisplayMetrics().widthPixels) { // if the x coordinate is past the screen
                    // reset the x coordinate
                    // start incrementing the distance between cards - newCounter is initially 0, then the distance is added to the coordinate
                    newCard.setX(card1.getX() + newCounter * distanceCards);
                    newCard.setY(constYCoor + 100);
                    newCounter++; // only for second row
                }

                TypedArray newImages = getResources().obtainTypedArray(R.array.apptour);
                int choice = (int) (Math.random() * newImages.length());

                newCard.setImageResource(newImages.getResourceId(choice, R.drawable.back_red_basic)); // random png

                //newCard.setImageResource(R.drawable.ace_hearts_white_png);
                cl.addView(newCard);

                // take care of running count - add to it as more cards are drawn
                int currentRunningCount = trackRunningCount(newImages, 0, 0, choice);

                int oldChoice1 = (int) getRandomCard().get(1);
                int oldChoice2 = (int) getRandomCard().get(2);

                //System.out.println(oldChoice1);
                //System.out.println(oldChoice2);

                TypedArray oldImages = (TypedArray) getRandomCard().get(0);
                int oldRunningCount = trackRunningCount(oldImages, oldChoice1, oldChoice2, 0);
                System.out.println("OLD RC" + oldRunningCount);

                int totalRunningCount = oldRunningCount;
                totalRunningCount += currentRunningCount;

                // https://stackoverflow.com/questions/4768969/how-do-i-change-textview-value-inside-java-code
                String runningCountStr = Integer.toString(totalRunningCount);

                TextView textViewToChange = (TextView) findViewById(R.id.runningCountTextView);
                textViewToChange.setText(runningCountStr);

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

    public ArrayList getRandomCard() {
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

        trackRunningCount(images, choice1, choice2, 0);

        TypedArray nonRecycledArray = images;
        //images.recycle(); // https://stackoverflow.com/questions/21354501/typed-array-should-be-recycled-after-use-with-recycle

        // https://stackoverflow.com/questions/19602601/create-an-arraylist-with-multiple-object-types
        ArrayList<Object> randomCardInfo = new ArrayList <>();
        randomCardInfo.add(nonRecycledArray);
        randomCardInfo.add(choice1);
        randomCardInfo.add(choice2);

        return randomCardInfo;
    }

    public int trackRunningCount(TypedArray imagesProvided, int choice1Param, int choice2Param, int choiceNParam) {
        // figure out the cumulative value of each of the player's cards

        String cardValue1 = imagesProvided.getString(choice1Param);
        String cardValue2 = imagesProvided.getString(choice2Param);
        String nthCardValue = imagesProvided.getString(choiceNParam);
        int runningCount = 0;

        //System.out.println(cardValue1);
        //System.out.println(cardValue2);
        //System.out.println(nthCardValue);


        String[] valueNum = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] valueFace = {"jack", "queen", "king"}; // not ace - dealt with separately

        for (String value : valueNum) {
            if (cardValue1.contains(value)) {
                runningCount += Integer.parseInt(value);
            }
            if (cardValue2.contains(value)) {
                runningCount += Integer.parseInt(value);
            }
            if (nthCardValue.contains(value)) {
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
            if (nthCardValue.contains(value)) {
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
        if (nthCardValue.contains("ace")) {
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

        System.out.println("RC " + runningCount);
        return runningCount;

    }

    public void addCard(View view) {
        // https://stackoverflow.com/questions/15097950/adding-imageview-to-the-layout-programmatically

        ImageView newCard = new ImageView(GameScreen.this);
        // ConstraintLayout cl = findViewById(R.id.constraintLayoutID);

        TypedArray newCardImages = (TypedArray) getRandomCard().get(0);
        int newCardChoice = (int) (Math.random() * newCardImages.length());

        newCard.setImageResource(newCardImages.getResourceId(newCardChoice, R.drawable.back_red_basic)); // random png

        //cl.addView(newCard);

    }

}