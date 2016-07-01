package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.utilities.Converters;

import java.util.Calendar;

public class TransportFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    /**
     * @param routes : Current Route
     * @return : fragment
     */
    public static TransportFragment newInstance(Routes routes) {
        TransportFragment fragment = new TransportFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("route", routes.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public TransportFragment() {
    }

    TransportModel transport;
    TextView weekTitle, sundayTitle, footnote1, footnote2;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Bundle args = getArguments();
        String name = args.getString("route", Routes.NCBS_IISC.toString());

        transport = new TransportModel(Routes.valueOf(name), getContext());

        View rootView = inflater.inflate(R.layout.transport_list, container, false);
        weekTitle = (TextView) rootView.findViewById(R.id.weekday_trip_title);
        sundayTitle = (TextView) rootView.findViewById(R.id.sunday_trip_title);
        footnote1 = (TextView) rootView.findViewById(R.id.transport_footnote1);
        footnote2 = (TextView) rootView.findViewById(R.id.transport_footnote2);
        perform(rootView);
        return rootView;
    }

    public void perform(View v) {

        //UI initialization
        ListView weekList = (ListView) v.findViewById(R.id.weekdays_trips);
        ListView sundayList = (ListView) v.findViewById(R.id.sunday_trips);

        String[] rawWeekTrips;
        String[] rawSundayTrips;
        //Get raw trips
        if (transport.isBuggy()) {
            rawWeekTrips = new Converters().stringToarray(pref.getString(Routes.BUGGY_FROM_NCBS.getWeekKey(), TransportHelper.DEFAULT_TRIPS));
            rawSundayTrips = new Converters().stringToarray(pref.getString(Routes.BUGGY_FROM_MANDARA.getWeekKey(), TransportHelper.DEFAULT_TRIPS));
        } else {
            rawWeekTrips = transport.getRawTripsWeekDays();
            rawSundayTrips = transport.getRawTripsSunday();
        }


        //Convert to regular format
        rawWeekTrips = new Converters().convertToSimpleDate(rawWeekTrips);
        rawSundayTrips = new Converters().convertToSimpleDate(rawSundayTrips);

        if (transport.isBuggy()) {

            for (int i = 0; i < rawWeekTrips.length; i++) {
                if (rawWeekTrips[i].equals(new Converters().convertToSimpleDate(
                        new TransportHelper(getContext()).nextTrip(Routes.BUGGY_FROM_NCBS)[1]
                ))) {
                    rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                    break;
                }
            }

            for (int i = 0; i < rawSundayTrips.length; i++) {
                if (rawSundayTrips[i].equals(new Converters().convertToSimpleDate(
                        new TransportHelper(getContext()).nextTrip(Routes.BUGGY_FROM_MANDARA)[1]
                ))) {
                    rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                    break;
                }
            }
        } else {

            String targetString = new Converters().convertToSimpleDate(transport.getNextTrip());
            if (transport.getNextTripDay() == Calendar.SUNDAY) {
                boolean gotDate = false;
                for (int i = 0; i < rawSundayTrips.length; i++) {
                    if (rawSundayTrips[i].equals(targetString)) {
                        rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                        gotDate = true;
                        break;
                    }
                }
                if (!gotDate) {
                    for (int i = 0; i < rawWeekTrips.length; i++) {
                        if (rawWeekTrips[i].equals(targetString)) {
                            rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                            break;
                        }
                    }
                }
            } else {
                boolean gotDate = false;
                for (int i = 0; i < rawWeekTrips.length; i++) {
                    if (rawWeekTrips[i].equals(targetString)) {
                        rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                        gotDate = true;
                        break;
                    }
                }
                if (!gotDate) {
                    for (int i = 0; i < rawSundayTrips.length; i++) {
                        if (rawSundayTrips[i].equals(targetString)) {
                            rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                            break;
                        }
                    }
                }
            } //Else Sunday
        } //Else isBuggy

        TransportAdapter weekAdapter = new TransportAdapter(getActivity(), R.layout.transport_item, rawWeekTrips);
        TransportAdapter sundayAdapter = new TransportAdapter(getActivity(), R.layout.transport_item, rawSundayTrips);
        sundayList.setAdapter(sundayAdapter);
        weekList.setAdapter(weekAdapter);
        weekTitle.setText(transport.getGetWeekTitle());
        sundayTitle.setText(transport.getGetSundayTitle());
        footnote1.setText(transport.getGetFootnote1());
        footnote2.setText(transport.getGetFootnote2());

    }

    private String coloredText(String string) {
        return "<font color=\"red\">" + string + "**</font>";
    }


}
