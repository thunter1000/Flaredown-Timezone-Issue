package com.flaredown.timezoneissue;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final String MILLSTORE = "millisecondStore";

    public Calendar c1;
    public SharedPreferences sp;

    /**
     * To use the app for testing set the phones timezone in the settings in which you want to save to sharedprefs
     * Open the app click "Click to store c1" then click "Close the Application" change the timezone to something else.
     * Start the app and notice how c2 remains to the same time.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        LinearLayout rootView = (LinearLayout) findViewById(R.id.main_layout);
        List<TextView> textViews = new ArrayList<>();

        c1 = Calendar.getInstance();
        c1.set(2015, Calendar.OCTOBER, 21, 7, 00); // Sets the time to 2015-10-21 7:00AM current timezone;

        TextView tv1 = new TextView(this);
        tv1.setText("c1: " + stateTime(c1));
        rootView.addView(tv1);

        Button button = new Button(this);
        button.setText("Click to store c1");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Stores the time in sharepreferences.
                 * It does this by adding the timezone offset, meaning it stores 7:00AM GMT-0 no matter the timezone it is set in,
                 * this removes the timezone from the getTimeInMillis().
                 */
                sp.edit().putLong(MILLSTORE, c1.getTimeInMillis() + getCurrentTimezoneOffset(c1)).commit();
            }
        });
        rootView.addView(button);


        Long millFromSP = sp.getLong(MILLSTORE, -1);

        if(millFromSP != -1) {
            Calendar calendar = Calendar.getInstance();
            /**
             * Retrives the value from sharedpreferences and subtracts the timezone difference. In effect adding the timezone difference.
             * Turning the 7:00AM GMT-0 to 7:00AM (what ever timezone the phone is using)
             */
            calendar.setTimeInMillis(millFromSP - getCurrentTimezoneOffset(Calendar.getInstance()));

            TextView tv2 = new TextView(this);
            tv2.setText("c2: " + stateTime(calendar));
            rootView.addView(tv2);
        }


        Button closeApp = new Button(this);
        closeApp.setText("Close the application");
        rootView.addView(closeApp);
        closeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public int getCurrentTimezoneOffset(Calendar c) {
        return c.getTimeZone().getOffset(c.getTimeInMillis());
    }

    public String stateTime(Calendar c) {
        int timezoneDifff = c.getTimeZone().getOffset(c.getTimeInMillis());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + "   " + c.getTimeInMillis() + " tzo: " + timezoneDifff;
    }
}
