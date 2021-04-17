package com.timetablecarpenters.pocketcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * BaseActivity supplies the common functionalities like the menu and the toolbar for the other
 * sub-class activities.
 * @author Deniz Mert Dilaverler
 * @version 17.04.21
 *
 */
// hi bitches
public class BaseActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final String TAG = "BaseActivity";
    protected static final String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    protected static final String[] dateNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    protected static float x1, x2, y1, y2;
    protected static final float MIN_DISTANCE = 100;
    protected  GestureDetector gestureDetector;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button monthlyButton = findViewById(R.id.monthly_button);
        Button weeklyButton = findViewById(R.id.weekly_button);
        Button dailyButton = findViewById(R.id.daily_button);
        monthlyButton.setOnClickListener(new BaseActivity.ViewChangeClickListener());
        weeklyButton.setOnClickListener(new BaseActivity.ViewChangeClickListener());
        dailyButton.setOnClickListener(new BaseActivity.ViewChangeClickListener());

        this.gestureDetector = new GestureDetector(BaseActivity.this, this);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
 
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class ViewChangeClickListener implements View.OnClickListener {
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
            if (intent != null)
                BaseActivity.this.startActivity(intent);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
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
                    if (x2 > x1) {
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

    public void leftSwipe() {
        Log.d(TAG, "leftSwipe: ");
    }
    public void rightSwipe() {
        Log.d(TAG, "rightSwipe: ");
    }

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
