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

    public void dealCards(View view) {
        ImageView img = ((ImageView)findViewById(R.id.playerCard1));

        // take the values from arrays.xml and create a TypedArray
        // then select a random array element and set the image resource accordingly
        // recycle the TypedArray - doing so is good practice

        // https://stackoverflow.com/questions/3497074/how-to-select-from-resources-randomly-r-drawable-xxxx
        TypedArray images = getResources().obtainTypedArray(R.array.apptour);
        int choice = (int) (Math.random() * images.length());
        img.setImageResource(images.getResourceId(choice, R.drawable.ace_clubs_white_png));
        images.recycle(); // https://stackoverflow.com/questions/21354501/typed-array-should-be-recycled-after-use-with-recycle
    }
}