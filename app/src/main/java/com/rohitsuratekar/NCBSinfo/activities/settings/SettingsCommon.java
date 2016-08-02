package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.settings.snake.SnakeActivity;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;

import java.util.regex.Pattern;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 12-07-16.
 */
public class SettingsCommon extends AppCompatActivity {

    public static final String INTENT = SettingsCommon.class.getName();
    public static final String TERMS = "termsAndConditions";
    public static final String ABOUT_US = "aboutUs";
    public static final String FAQ = "faq";
    public static final String PRIVACY = "privacy";
    TextView commonText;
    BaseParameters baseParameters;
    int magicTap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_base);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        commonText = (TextView) findViewById(R.id.settings_common_text);
        baseParameters = new BaseParameters(getBaseContext());

        final String trigger = getIntent().getStringExtra(INTENT);
        magicTap = 0;





        if (trigger != null) {
            switch (trigger) {
                case TERMS:
                    commonText.setText(Html.fromHtml(getResources().getString(R.string.terms)));
                    setTitle(R.string.settings_terms);
                    break;
                case ABOUT_US:
                    commonText.setText(Html.fromHtml(getResources().getString(R.string.about_us)));
                    setTitle(R.string.settings_about_ncbinfo);
                    break;
                case FAQ:
                    commonText.setText(Html.fromHtml(getResources().getString(R.string.faq)));
                    setTitle(R.string.settings_faq);
                    break;
                case PRIVACY:
                    commonText.setText(Html.fromHtml(getResources().getString(R.string.privacy_statement)));
                    setTitle(R.string.settings_privacy);
                    break;


            }
        }

        commonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trigger != null && trigger.equals(TERMS)) {
                    Log.i(getClass().getSimpleName(), "Tap :"+ magicTap);
                    magicTap++;
                    if (magicTap > 3) {
                        startActivity(new Intent(SettingsCommon.this, SnakeActivity.class));
                        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                    }
                }
            }
        });

        //To make text links clickable
        commonText.setMovementMethod(LinkMovementMethod.getInstance());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            this.overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
    }


}