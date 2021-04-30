package com.timetablecarpenters.pocketcalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * BaseActivity supplies the common functionalities like the menu and the toolbar for the other
 * sub-class activities.
 * @author Deniz Mert Dilaverler
 * @version 17.04.21
 *
 */
public class BaseActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final String TAG = "BaseActivity";
    public static final String SMALL = "Small";
    public static final String MEDIUM = "Medium";
    public static final String LARGE = "Large";
    protected static final String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    protected static final String[] dateNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    protected static float x1, x2, y1, y2;
    protected static final float MIN_DISTANCE = 100;
    protected  GestureDetector gestureDetector;
    protected Toolbar toolbar;

    /**
     * assigns the buttons as variables and assigns a clickListener on to them
     * initializes a GestureDetector to detect swiping functions
     * initializes the toolbar at the top
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.monthly_button) != null) {
            Button monthlyButton = findViewById(R.id.monthly_button);
            Button weeklyButton = findViewById(R.id.weekly_button);
            Button dailyButton = findViewById(R.id.daily_button);
            monthlyButton.setOnClickListener(new BaseActivity.ViewChangeClickListener());
            weeklyButton.setOnClickListener(new BaseActivity.ViewChangeClickListener());
            dailyButton.setOnClickListener(new BaseActivity.ViewChangeClickListener());
        }

        this.gestureDetector = new GestureDetector(BaseActivity.this, this);
        editInTextFont();
    }


    /**
     * listens to the inputs of the menu items
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = null;
        switch (id) {
            case R.id.to_calender:
                intent = new Intent(this, MonthActivity.class);
                break;
            case R.id.upcoming:
                intent = new Intent(this, UpcomingEvents.class);
                break;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.About_Devs:
                //todo: this is for testing purposes, remove in final version
                intent = new Intent(this, MapsActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * creates the menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * ClickListener that checks for "Monthly" , "Daily" , "Weekly" button clicks
     */
    public class ViewChangeClickListener implements View.OnClickListener {
        /**
         * when clicked creates an intent of the desired activity and starts the activity
         * @param v
         */
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.monthly_button:
                    intent = new Intent(BaseActivity.this, MonthActivity.class);
                    break;
                case R.id.weekly_button:
                    intent = new Intent(BaseActivity.this, WeekActivity.class);
                    break;
                case R.id.daily_button:
                    intent = new Intent(BaseActivity.this, DayActivity.class);
                    break;
                default:
                    intent = null;
            }
            if (intent != null) {
                BaseActivity.this.startActivity(intent);
                finish();
            }

        }
    }

    /**
     * called when a click is registered
     * if the click travels enough distance to be registered as a swipe
     * depending on the direction swipe methods are called. These methods don't have a function in this class
     * however they can be overridden for various uses
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                // horizontal swipe
                float valueX = x2 - x1;
                // vertical swipe
                float valueY = y2 - y1;

                if (Math.abs(valueX) > MIN_DISTANCE) {
                    // detect left to right swipe
                    if (valueX > 0) {
                        rightSwipe();
                    }
                    else {
                        leftSwipe();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Edits the font sizes of textViews according to settings
     */
    public void editInTextFont(){
        TextView textView = findViewById(R.id.dateText);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("inTextPref", MODE_PRIVATE);
        String inTextFontSize = sp.getString("inTextFontSize","");
        if (inTextFontSize.equals(SMALL))
        {
            textView.setTextSize(16);
        }
        if (inTextFontSize.equals(MEDIUM))
        {
            textView.setTextSize(20);
        }
        if (inTextFontSize.equals(LARGE))
        {
            textView.setTextSize(40);
        }
        //Toast.makeText(BaseActivity.this,"is selected",Toast.LENGTH_LONG).show();
    }
    /**
     * After the user pressed the back
     * Shows the dialog that asks the user
     * is sure or not
     */
    public void onBackPressed() {
        // Here you want to show the user a dialog box
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle("Exiting the App")
                .setMessage("Are you sure?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // The user wants to leave - so dismiss the dialog and exit
                        finish();
                        dialog.dismiss();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // The user is not sure, so you can exit or just stay
                dialog.dismiss();
            }
        }).show();

    }


    /**
     * Override to add functionality
     */
    public void leftSwipe() {
        Log.d(TAG, "leftSwipe: ");
    }

    /**
     * override to add functionality
     */
    public void rightSwipe() {
        Log.d(TAG, "rightSwipe: ");
    }

    // these methods are a part of the OngestureListener interface and currently has no uses
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
