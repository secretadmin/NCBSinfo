package com.rohitsuratekar.NCBSinfo.background;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.EventDetails;
import com.rohitsuratekar.NCBSinfo.activity.EventUpdates;
import com.rohitsuratekar.NCBSinfo.constants.ExternalConstants;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;
import com.rohitsuratekar.NCBSinfo.models.CommonEventModel;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.tempActivitites.CAMP;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Notifications extends BroadcastReceiver {
    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String currentSwitch = intent.getExtras().getString(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
        switch (currentSwitch) {
            case General.GEN_SENDNOTIFICATION:
                String title = intent.getExtras().getString(General.GEN_NOTIFY_TITLE, "Alert!");
                String message = intent.getExtras().getString(General.GEN_NOTIFY_MESSAGE, "You have an unread notification");
                String datacode = intent.getExtras().getString(General.GEN_NOTIFICATION_DATACODE,"null");
                int dataID = intent.getExtras().getInt(General.GEN_NOTIFICATION_DATA_ID,1);
                sendNotification(title, message,datacode,dataID);
                break;
            case General.GEN_DAILYNOTIFICATION:
                DailyNotifications();
                break;
            default:
                new LogEntry(context, StatusCodes.STATUS_FAILED_NOTIFICATIONS);
                break;
        }

    }

    private void sendNotification(String title, String notificationMessage, String datacode, int dataID) {

        NotificationManager mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        int requestID = (int) System.currentTimeMillis();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent;

        //Change datacode whenever temporary events are done
        switch (datacode) {
            case "null":
                notificationIntent = new Intent(mContext, EventUpdates.class);
                break;
            case ExternalConstants.CONFERENCE_CAMP2016:
                notificationIntent = new Intent(mContext, CAMP.class);
                break;
            default:
                notificationIntent = new Intent(mContext, EventDetails.class);
                notificationIntent.putExtra(General.GEN_EVENTDETAILS_DATACODE, datacode);
                notificationIntent.putExtra(General.GEN_EVENTDETAILS_DATA_ID, dataID);
                break;
        }

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(mContext, requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int color = mContext.getResources().getColor(R.color.colorPrimary);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                .setColor(color)
                .setContentText(notificationMessage).setAutoCancel(true);
        mBuilder.setSound(alarmSound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
        new LogEntry(mContext,StatusCodes.STATUS_EVENTNOTIFIED,title);
    }
    private void DailyNotifications(){
        Database db = new Database(mContext);
        List<DataModel> jcdata = db.getFullDatabase();
        List<TalkModel> talkdata = db.getTalkDatabase();
        List<CommonEventModel> alldata = new ArrayList<>();
        alldata.addAll(new GeneralHelp().makeCommonList(jcdata,talkdata));
        int TimedNote = 0;
        for (CommonEventModel entry: alldata){
            if(entry.getActioncode()!= SQL.ACTION_NOTIFIED){
                Date tempdate = new GeneralHelp().convertToDate(entry.getDate(),entry.getTime());
                if ((tempdate.getTime()- Calendar.getInstance().getTime().getTime())<0){

                    if (entry.getDatacode().equals(General.GEN_DATACODE_TALK)){
                        entry.setActioncode(SQL.ACTION_NOTIFIED);
                        db.updateTalkEntry(new GeneralHelp().CommonEventToTalk(entry));
                        db.close();
                    }
                    else{
                    entry.setActioncode(SQL.ACTION_NOTIFIED);
                    db.updateDataEntry(new GeneralHelp().CommonEventToData(entry));
                    db.close();
                    }
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE,1);
                    if((tempdate.getTime()-cal.getTime().getTime())<0){
                        Calendar cal3 = Calendar.getInstance();
                        long timeleft = tempdate.getTime()-Calendar.getInstance().getTime().getTime(); //Get time difference from now
                        timeleft = timeleft - (PreferenceManager.getDefaultSharedPreferences(mContext).getInt(Preferences.PREF_NOTIFICATION_ONSET,mContext.getResources().getInteger(R.integer.notification_onset_default)))*1000;
                        if(timeleft<0){
                            timeleft = 0;  //In case someone had put very large time onset
                        }
                        cal3.add(Calendar.MILLISECOND, (int) timeleft);
                        Intent intent = new Intent(mContext, Notifications.class);
                        intent.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_SENDNOTIFICATION );
                        intent.putExtra(General.GEN_NOTIFY_TITLE,entry.getNotificationTitle());
                        if(entry.getDatacode().equals(General.GEN_DATACODE_TALK)){
                            intent.putExtra(General.GEN_NOTIFY_MESSAGE,entry.getCommonItem2()); }
                        else {
                            intent.putExtra(General.GEN_NOTIFY_MESSAGE,entry.getCommonItem1());}

                        intent.putExtra(General.GEN_NOTIFICATION_DATACODE,entry.getDatacode());
                        intent.putExtra(General.GEN_NOTIFICATION_DATA_ID,entry.getDataID());
                        int requestID = new GeneralHelp().getMiliseconds(entry.getTimestamp());
                        PendingIntent sender = PendingIntent.getBroadcast(mContext, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

                        //Compatibility
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal3.getTimeInMillis(), sender);
                        }
                        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal3.getTimeInMillis(),sender);
                        }
                        else
                        {
                            alarmMgr.set(AlarmManager.RTC_WAKEUP, cal3.getTimeInMillis(), sender);
                        }

                        entry.setActioncode(SQL.ACTION_SEND);
                        if(entry.getDatacode().equals(General.GEN_DATACODE_TALK)){
                        db.updateTalkEntry(new GeneralHelp().CommonEventToTalk(entry));}
                        else {db.updateDataEntry(new GeneralHelp().CommonEventToData(entry));}
                        db.close();
                        TimedNote++;
                        new LogEntry(mContext,StatusCodes.STATUS_SET_NOTIFICATION,entry.getNotificationTitle());
                    }
                }
            }
        }
        String detailNote = TimedNote+" notifications added for today";
        new LogEntry(mContext,StatusCodes.STATUS_DAILYNOTIFICATIONS,detailNote);
    }
}
