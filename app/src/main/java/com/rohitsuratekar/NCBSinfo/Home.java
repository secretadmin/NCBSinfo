package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitsuratekar.NCBSinfo.activities.OfflineHome;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.background.NetworkOperations;
import com.rohitsuratekar.NCBSinfo.background.ServiceCentre;
import com.rohitsuratekar.NCBSinfo.background.firebase.DataBuilder;
import com.rohitsuratekar.NCBSinfo.background.firebase.DataStructure;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity implements AppConstants {

    public final String TAG = getClass().getSimpleName();

    //Set UI
    @BindView(R.id.home_icon) ImageView homeIcon;
    @BindView(R.id.home_fragment1) ImageView fragment1;
    @BindView(R.id.home_fragment2) ImageView fragment2;
    @BindView(R.id.home_fragment3) ImageView fragment3;

    DisplayMetrics metrics;
    int f1_x, f1_y, f2_x, f2_y, f3_x, f3_y;
    Button online, offline;
    Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        //Initialization
        pref = new Preferences(getBaseContext());

        //Initialize app with latest app version
        try {
            pref.app().setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);


        online = (Button) findViewById(R.id.home_onlineBtn);
        offline = (Button) findViewById(R.id.home_offlineBtn);


      Intent i = new Intent(this, ServiceCentre.class);
        i.putExtra(ServiceCentre.INTENT, ServiceCentre.RESET_APP_DATA);
        startService(i);

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.app().setMode(modes.OFFLINE);
                startActivity(new Intent(Home.this, OfflineHome.class));
            }
        });


        f1_x = 0;
        f1_y = 0;
        f2_x = 0;
        f2_y = 0;
        f3_x = 0;
        f3_y = 0;

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragments();
            }
        });

        setFragments();
        runnable.run();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();



        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Login.class));
            }
        });

//        UpdateUser ee = new UpdateUser("email","ttt","kio");
//        Map<String, Object> postValues = ee.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("debug",postValues);
//        mDatabase.updateChildren(childUpdates);


    }

    private void setFragments() {
        int temp1 = getX();
        int temp2 = getY();
        int temp3 = getX();
        int temp4 = getY();
        int temp5 = getX();
        int temp6 = getY();
        Translate(fragment1, f1_x, f1_y, temp1, temp2);
        f1_x = temp1;
        f1_y = temp2;
        Translate(fragment2, f2_x, f2_y, temp3, temp4);
        f2_x = temp3;
        f2_y = temp4;
        Translate(fragment3, f3_x, f3_y, temp5, temp6);
        f3_x = temp5;
        f3_y = temp6;


    }

    private int getX() {
        ViewGroup.LayoutParams params = fragment1.getLayoutParams();
        return new General().randInt(params.width, metrics.widthPixels - params.width);
    }

    private int getY() {
        ViewGroup.LayoutParams params = fragment1.getLayoutParams();
        return new General().randInt(params.height, metrics.heightPixels / 2 - params.height);
    }

    private void Translate(ImageView view, int x, int y, int newX, int newY) {

        TranslateAnimation anim = new TranslateAnimation(x, newX, y, newY);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        public void run() {

            setFragments();

            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Home timer stopped");
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "Home timer stopped");
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        runnable.run();
        super.onRestart();
    }


}
