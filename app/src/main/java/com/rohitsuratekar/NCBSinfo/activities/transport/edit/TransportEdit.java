package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class TransportEdit extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.TRANSPORT_EDIT;
    }
}