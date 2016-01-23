package com.rohitsuratekar.NCBSinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dexter on 1/8/2016.
 */
public class BasicShuttle extends Fragment {

    public static BasicShuttle newInstance(int index) {
        BasicShuttle fragment = new BasicShuttle();
       // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }
    public BasicShuttle() {
    }
    String GlobalShuttleFrom;
    String GlobalShuttleto;
    int isBuggy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        int index = args.getInt("index", 0);
        switch (index) {
            case 0:
                GlobalShuttleFrom = "ncbs";
                GlobalShuttleto = "iisc";
                isBuggy = 0;

                break;
            case 1:
                GlobalShuttleFrom = "iisc";
                GlobalShuttleto = "ncbs";
                isBuggy = 0;
                break;
            case 2:
                GlobalShuttleFrom = "ncbs";
                GlobalShuttleto = "mandara";
                isBuggy = 0;
                break;
            case 3:
                GlobalShuttleFrom = "mandara";
                GlobalShuttleto = "ncbs";
                isBuggy = 0;
                break;
            case 4:
                GlobalShuttleFrom = "ncbs";
                GlobalShuttleto = "mandara";
                isBuggy = 1;
                break;
            case 5:
                GlobalShuttleFrom = "mandara";
                GlobalShuttleto = "ncbs";
                isBuggy = 1;
                break;

        }
        View rootView = inflater.inflate(R.layout.fragment_ncbs_to_iisc, container, false);
        perform(rootView);
        return rootView;
    }

    public void perform(View v) {
        String WeekDate = "12/31/2015 00:00:00";
        String SundayDate = "1/31/2016 00:00:00";
        String weeksmall = "12/31/2015 ";
        String sundaysmall = "1/31/2016 ";
        String[] AllArrays = new ShuttleTimings().OnlyTrips(GlobalShuttleFrom, GlobalShuttleto, WeekDate, isBuggy);
        String[] after = new String[AllArrays.length];
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat modformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat onlytime = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());

        String current_weekday = weeksmall+onlytime.format(Calendar.getInstance().getTime());
        String current_sunday = sundaysmall+onlytime.format(Calendar.getInstance().getTime());

        Calendar nextcal = new ShuttleTimings().newNextShuttle(GlobalShuttleFrom, GlobalShuttleto, current_weekday, isBuggy);
        String nextShuttleWeek = modformat.format(nextcal.getTime());

        Calendar sundaynext = new ShuttleTimings().newNextShuttle(GlobalShuttleFrom, GlobalShuttleto, current_sunday, isBuggy);
        String nextShuttleSunday = modformat.format(sundaynext.getTime());
        int selectionItem1 = 0;
        int selectionItem2 = 0;

        for (int i =0; i<AllArrays.length; i++){

            try {
                Date tempdate = format.parse(AllArrays[i]);
                after[i] = modformat.format(tempdate);
                if (after[i].equals(nextShuttleWeek)){
                    after[i]="<font color=\"blue\">"+modformat.format(tempdate)+"**</font>";
                    selectionItem1 = i;

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        String[] AllArrays_sunday = new ShuttleTimings().OnlyTrips(GlobalShuttleFrom, GlobalShuttleto, SundayDate, isBuggy);
        String[] sunday = new String[AllArrays_sunday.length];
        for (int i =0; i<AllArrays_sunday.length; i++){
            try {
                Date tempdate = format.parse(AllArrays_sunday[i]);
                sunday[i] = modformat.format(tempdate);

                if (sunday[i].equals(nextShuttleSunday)){
                    sunday[i]="<font color=\"blue\">"+modformat.format(tempdate)+"**</font>";
                    selectionItem2 = i;

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        ListView afterList = (ListView) v.findViewById(R.id.TripList_weekday);
        ListView sundaylist = (ListView) v.findViewById(R.id.TripList_sunday);
        RListAdaptor adapter5 = new RListAdaptor(getActivity(), R.layout.list_view_item, after);
        RListAdaptor adapter2 = new RListAdaptor(getActivity(), R.layout.list_view_item, sunday);
        sundaylist.setAdapter(adapter2);
        afterList.setAdapter(adapter5);
        afterList.setSelection(selectionItem1);
        afterList.requestFocus();
        sundaylist.setSelection(selectionItem2);
        sundaylist.requestFocus();

        TextView foot = (TextView) v.findViewById(R.id.footNoteText);
        String tempText;
        if (isBuggy==1){tempText=getString(R.string.next_buggy);}
        else {tempText=getString(R.string.next_shuttle);}
        foot.setText(getString(R.string.shuttle_footnote2, tempText, modformat.format(Calendar.getInstance().getTime())));
    }



 }



