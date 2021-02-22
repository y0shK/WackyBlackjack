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

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends AppCompatActivity {

    int clickCount = 1; // click count for the first row of cards
    int newCounter = 0; // click count for the second row of cards

    int runningCountGlobal = 0;

    boolean clairvoyance = false;
    boolean sabotage = false;
    boolean incineration = false;
    boolean transmutation = false;
    boolean jokerPowerup = false;

    boolean clairvoyanceUsed = false;

    boolean sabotageUsed = false;

    boolean incinerationUsed = false;
    boolean incinerationBust = false;


    public void generateDealerCards() {
        TextView textViewToStand = (TextView) findViewById(R.id.runningCountTextView); // access TextView
        int finalStandValue = getTextViewIntegerContents(textViewToStand);

        // instantiate necessary ImageViews
        ImageView newDealerCard1 = new ImageView(GameScreen.this);
        ImageView newDealerCard2 = new ImageView(GameScreen.this);
        ImageView cardStack = (ImageView) findViewById(R.id.cardStack);

        // instantiate old imageViews to find x distance
        ImageView card1 = (ImageView) findViewById(R.id.playerCard1);
        ImageView card2 = (ImageView) findViewById(R.id.playerCard2);

        // x distance
        float distanceCards = card2.getX() - card1.getX();

        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintLayoutID);

        float dealerXCoor = cardStack.getX();
        float dealerYCoor = cardStack.getY();

        int dealerCount;
        int runningCount = 0;
        int dealerCardAddMultiplier = 1;

        TextView dealerTextView = (TextView) findViewById(R.id.dealerTextView);

        while (runningCount < 17) {
            ImageView newDealerCard = new ImageView(GameScreen.this);
            TypedArray newImages = getResources().obtainTypedArray(R.array.apptour);
            int choice = (int) (Math.random() * newImages.length());

            newDealerCard.setImageResource(newImages.getResourceId(choice, R.drawable.back_red_basic)); // random png

            cl.addView(newDealerCard);

            newDealerCard.setX(dealerXCoor + dealerCardAddMultiplier * distanceCards);
            newDealerCard.setY(dealerYCoor);

            dealerCount = trackRunningCount(newImages, choice, -1, dealerTextView);
            runningCount += dealerCount;

            String dealerCountStr = Integer.toString(runningCount);
            dealerTextView.setText(dealerCountStr);

            dealerCardAddMultiplier++;
        }
    }

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

                // generate a new typedArray to get new random cards ("hit" in blackjack)
                // same procedure as before, obtain typedArray from XML and then get a random xml element
                // https://stackoverflow.com/questions/15097950/adding-imageview-to-the-layout-programmatically
                TypedArray newImages = getResources().obtainTypedArray(R.array.apptour);
                int choice = (int) (Math.random() * newImages.length());

                newCard.setImageResource(newImages.getResourceId(choice, R.drawable.back_red_basic)); // random png

                cl.addView(newCard);

                // https://stackoverflow.com/questions/4768969/how-do-i-change-textview-value-inside-java-code
                // before this block of code, the TextView shows the combined value of the initial two cards given
                // now, access the TextView, convert its content to int, add any new cards' value, and release the new value as TextView output

                TextView textViewToChange = (TextView) findViewById(R.id.runningCountTextView); // access TextView

                // // use pre-defined method to take textView contents of CharSequence, convert to str, then int
                int runningCount = getTextViewIntegerContents(textViewToChange);

                //CharSequence previousRunningCountChars = textViewToChange.getText();
                //String previousRunningCountStr = previousRunningCountChars.toString();
                //int runningCount = Integer.parseInt(previousRunningCountStr);

                // each time "hit" command is activated, a new card is added, and the count increases
                // trackRunningCount has 2 params for the initial 2 cards dealt out
                // however, additional cards are dealt one at a time, so the -1 parameter is a dummy param
                int temp = trackRunningCount(newImages, choice, -1, textViewToChange);
                runningCount += temp;

                String runningCountStr = Integer.toString(runningCount);
                textViewToChange.setText(runningCountStr);

                // putting the check to see if the player is > 21 here ensures that any bust is caught immediately,
                // not the next time a card is hit

                // set the appropriate variables so that if a bust occurs,
                // an automatic game over will also occur

                ImageView blueChip = (ImageView) findViewById(R.id.bluePokerChipNoBg);
                ImageView purpleChip = (ImageView) findViewById(R.id.purpleChipNoBg);
                TextView gameCondition = new TextView(GameScreen.this);

                if (getTextViewIntegerContents(textViewToChange) > 21) {

                    // if incineration occurs, then show the card that led to the bust
                    // remove the amount of the card from the running count
                    // and auto-hit the stand button

                    if (incineration) {
                        // newCard.setVisibility(View.VISIBLE); // invisible and doesn't take up layout space
                        runningCountStr = getString(R.string.incineration_player_count, runningCount, temp);
                        runningCount -= temp;
                        textViewToChange.setText(runningCountStr);
                        incinerationBust = true;
                        runningCountGlobal = runningCount;
                        //generateDealerCards();
                        //blueChip.performClick();
                    }
                    else {
                        // if bust occurs,
                        // game over and the player loses
                        // experimentally defined
                        gameCondition.setX((float) (purpleChip.getX() * 1.35));
                        gameCondition.setY((float) (purpleChip.getY() * 1.45));
                        gameCondition.setTextColor(getResources().getColor(R.color.black));

                        gameCondition.setText(R.string.lose_string);
                        cl.addView(gameCondition);

                        // once stand is chosen,
                        // the game is over one way or another
                        // https://stackoverflow.com/questions/9144215/how-to-make-a-button-press-once-and-then-not-pressable-anymore
                        blueChip.setEnabled(false);

                    }
                    redChip.setEnabled(false);
                }


                //gameCondition.add

            }

        });

        ImageView blueChip = (ImageView) findViewById(R.id.bluePokerChipNoBg);
        blueChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // there are two possibilities for stand
                // no powerup OR non-clairvoyance powerup (~80% of the time)
                // clairvoyance powerup (~20% of the time)
                // if clairvoyance is used, then use the same cards that are displayed via purple buttonClick
                // else, create the cards on blue buttonClick

                boolean shouldShowDealerCards;
                int sabotageAddVal = 0;

                // clairvoyance logic
                if (clairvoyanceUsed) {
                    shouldShowDealerCards = false;
                }
                else { // the clairvoyance powerup is not used - generate new cards
                    shouldShowDealerCards = true;
                }

                // if sabotage is used, create cards here, not on button click
                // the user shouldn't sabotage and know cards ahead of time
                if (sabotageUsed) {
                    shouldShowDealerCards = true;
                }

                // generate dealer cards if clairvoyance has not been used
                if (shouldShowDealerCards) {
                    generateDealerCards();
                }

                TextView playerCountView = (TextView) findViewById(R.id.runningCountTextView);
                TextView dealerCountView = (TextView) findViewById(R.id.dealerTextView);

                int finalPlayerCount;
                if (! incinerationBust) {
                    finalPlayerCount = getTextViewIntegerContents(playerCountView);
                }
                else {
                    finalPlayerCount = runningCountGlobal;
                }

                int finalDealerCount = getTextViewIntegerContents(dealerCountView);

                if (sabotageUsed) {
                    Random rand = new Random();
                    sabotageAddVal = rand.nextInt(4);

                    String sabotageDealerCount = getString(R.string.sabotage_dealer_count, finalDealerCount, sabotageAddVal);
                    dealerCountView.setText(sabotageDealerCount);

                    finalDealerCount += sabotageAddVal;
                }

                TextView gameCondition = new TextView(GameScreen.this);
                ImageView purpleChip = (ImageView) findViewById(R.id.purpleChipNoBg);

                if (finalPlayerCount > 21) {
                    // if the player busts, regardless of the dealer's hand, the player loses
                    gameCondition.setText(R.string.lose_string);
                }
                else if (finalPlayerCount <= 21 && finalDealerCount > 21) {
                    gameCondition.setText(R.string.win_string);
                }
                else { // nobody busted
                    if (finalPlayerCount > finalDealerCount) {
                        gameCondition.setText(R.string.win_string);
                    }
                    else if (finalPlayerCount < finalDealerCount) {
                        gameCondition.setText(R.string.lose_string);
                    }
                    else {
                        gameCondition.setText(R.string.tie_string);
                    }
                }

                // experimentally defined
                gameCondition.setX((float) (purpleChip.getX() * 1.35));
                gameCondition.setY((float) (purpleChip.getY() * 1.45));
                gameCondition.setTextColor(getResources().getColor(R.color.black));

                ConstraintLayout cl = findViewById(R.id.constraintLayoutID);
                cl.addView(gameCondition);

                // if the clairvoyance powerup is used, the user can continue to hit or stand
                // otherwise, the game must end

                if (shouldShowDealerCards) { // not used
                    blueChip.setEnabled(false);
                    redChip.setEnabled(false);
                    purpleChip.setEnabled(false); // can't use the powerup after the game
                }
                else {
                    blueChip.setEnabled(true);
                    redChip.setEnabled(true);
                }

            }
        });

        // start implementing powerups
        // every time, the player gets a new powerup
        // clairvoyance: reveal the dealer's hand - 0
        // sabotage: add a random amount (1-4) to try to make them bust - 1
        // incineration: get rid of one of your cards if you bust - 2
        // transmutation: click one of your cards and replace it with a random card - 3
        // joker: randomly replace your entire hand - 4


        // generate random number for power-up
        Random rand = new Random();
        int powerupType = rand.nextInt(5); // 0-4, bound is exclusive

        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintLayoutID);
        ImageView purpleChip = findViewById(R.id.purpleChipNoBg);
        ImageView powerupImage = (ImageView) findViewById(R.id.replacePowerup);

        if (powerupType == 0) {
            clairvoyance = true;
            powerupImage.setImageResource(R.drawable.clairvoyance);
        }
        else if (powerupType == 1) {
            sabotage = true;
            powerupImage.setImageResource(R.drawable.sabotage);
        }
        else if (powerupType == 2) {
            incineration = true;
            powerupImage.setImageResource(R.drawable.flames);
        }
        else if (powerupType == 3) {
            transmutation = true;
            powerupImage.setImageResource(R.drawable.transmutation);
        }
        else if (powerupType == 4) {
            jokerPowerup = true;
            powerupImage.setImageResource(R.drawable.joker_skull);
        }

        // for debugging purposes
        clairvoyance = false;
        sabotage = false;
        incineration = true;

        purpleChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start with clairyovance
                // if clairvoyance is the powerup type,
                // when the clairyovance button is clicked, reveal the dealer's hand

                TextView playerCountView = (TextView) findViewById(R.id.runningCountTextView);
                TextView dealerCountView = (TextView) findViewById(R.id.dealerTextView);

                // once the clairvoyance button is clicked, go through the dealer's actions
                // and leave the player the option to either hit or stand as desired
                // when the player stands, the game ends as usual

                // https://stackoverflow.com/questions/16732398/android-call-onclick-method-without-clicking
                //blueChip.callOnClick();

                // two booleans - one for using clairvoyance and one for generating the dealer cards
                // the first boolean, clairvoyanceUsed, is initially false and becomes true onClick
                // the second boolean, clairvoyanceDone, is initially false, and becomes true onClick
                // to permit the Stand button to end the game

                if (clairvoyance) {
                    generateDealerCards();
                    clairvoyanceUsed = true;
                }
                else if (sabotage) {
                    sabotageUsed = true;
                }
                else if (incineration) {
                    incinerationUsed = true;
                }

                purpleChip.setEnabled(false); // can't call powerup again


                // if the clairvoyance button is clicked, then keep the dealer's total
                // don't re-shuffle them

                //redChip.setEnabled(true);
                //blueChip.setEnabled(true);

                //redChip.setEnabled(true);
                //blueChip.setEnabled(true);



            }
        });
    }

    // https://stackoverflow.com/questions/18703841/call-method-on-activity-load-android
    @Override
    protected void onStart()
    {
        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintLayoutID);

        // set poker green hex code in colors.xml
        // #35654d
        cl.setBackgroundColor(getResources().getColor(R.color.poker_green));

        // https://stackoverflow.com/questions/36236181/how-to-remove-title-bar-from-the-android-activity
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e) {

        }

        super.onStart();
        getRandomCard();
        ImageView purpleChip = (ImageView) findViewById(R.id.purpleChipNoBg);
        ImageView redChip = (ImageView) findViewById(R.id.redPokerChipNoBg);
        purpleChip.setY(redChip.getY());
    }

    public int getTextViewIntegerContents(TextView tv) {
        // TextView contents are type CharSequence
        // https://stackoverflow.com/questions/5858307/charsequence-to-int
        CharSequence cs = tv.getText();

        String str = cs.toString();
        int intVal = Integer.parseInt(str);
        return intVal;
    }

    public TypedArray getRandomCard() {
        ImageView img1 = ((ImageView)findViewById(R.id.playerCard1));
        ImageView img2 = ((ImageView)findViewById(R.id.playerCard2));

        // take the values from arrays.xml and create a TypedArray
        // then select a random array element and set the image resource accordingly
        // recycle the TypedArray - doing so is good practice


        // https://stackoverflow.com/questions/15097950/adding-imageview-to-the-layout-programmatically
        // https://stackoverflow.com/questions/3497074/how-to-select-from-resources-randomly-r-drawable-xxxx
        TypedArray images = getResources().obtainTypedArray(R.array.apptour);
        int choice1 = (int) (Math.random() * images.length());
        int choice2 = (int) (Math.random() * images.length());
        TextView textViewToChange = (TextView) findViewById(R.id.runningCountTextView);

        img1.setImageResource(images.getResourceId(choice1, R.drawable.back_red_basic)); // random png
        img2.setImageResource(images.getResourceId(choice2, R.drawable.back_red_basic)); // random png

        //img1.setTag(images.getResourceId(choice1, R.drawable.back_red_basic));
        //img2.setTag(images.getResourceId(choice2, R.drawable.back_red_basic));

        trackRunningCount(images, choice1, choice2, textViewToChange);

        TypedArray nonRecycledArray = images;
        images.recycle(); // https://stackoverflow.com/questions/21354501/typed-array-should-be-recycled-after-use-with-recycle

        // https://stackoverflow.com/questions/19602601/create-an-arraylist-with-multiple-object-types
        // ArrayList<Object> randomCardInfo = new ArrayList <Object>();

        return nonRecycledArray;
    }

    public int trackRunningCount(TypedArray imagesProvided, int choice1Param, int choice2Param, TextView toChange) {
        // figure out the cumulative value of each of the player's cards

        // instantiate the variable that will hold the contents of each card
        // but don't define them until error checking is conducted
        String cardValue1;
        String cardValue2;

        if (choice1Param > -1) { // the 0th element all the way to the nth element can be called
            cardValue1 = imagesProvided.getString(choice1Param);
        }
        else {
            cardValue1 = "-1"; // dummy string that won't increment the count
        }

        if (choice2Param > -1) {
            cardValue2 = imagesProvided.getString(choice2Param);
        }
        else {
            cardValue2 = "-1";
        }

        //System.out.println(cardValue1);
        //System.out.println(cardValue2);

        int runningCount = 0;

        // get the value of the textView and extract the integer contents
        // use the TextViewToChange for the player's running count
        // dealer textView is the dealer's running count
        TextView textViewToChange = (TextView) findViewById(R.id.runningCountTextView);
        int textViewIntVal = getTextViewIntegerContents(textViewToChange);

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
        // to make sure that ace works properly, check the textView for the current contents and then append the ace value

        if (cardValue1.contains("ace")) {
            // bugfix: textViewIntVal + 11 must be < 21 - two aces should be 12, not 22
            if ((textViewIntVal + 11) < 21) { // if adding an ace would not result in a bust
                runningCount += 11; // then add the maximum value to the running count
            } else { // else, just add the minimum value and bust
                runningCount += 1;
            }
        }
        if (cardValue2.contains("ace")) {
            if ((textViewIntVal + 11) < 21) {
                runningCount += 11;
            } else { // > 21
                runningCount += 1;
            }
        }

        // https://stackoverflow.com/questions/4768969/how-do-i-change-textview-value-inside-java-code
        String runningCountStr = Integer.toString(runningCount);

        //TextView textViewToChange = (TextView) findViewById(R.id.runningCountTextView);
        toChange.setText(runningCountStr); // this setText is for the initial two cards' value

        return runningCount;

    }
}