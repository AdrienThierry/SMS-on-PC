/**
 * SMS related functions
 */

package com.example.adrien.sms_on_pc;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

import java.util.ArrayList;

public class SMS {

    private static Context mContext;

    // --------------------------------------------------
    // !! MUST BE CALLED BEFORE ANY OTHER FUNCTION OF
    // THIS CLASS !!
    // --------------------------------------------------
    public static void initialize(Context context) {
        mContext = context;
    }

    // --------------------------------------------------
    // Get all SMS
    // --------------------------------------------------
    public static ArrayList<ArrayList<String>> getAllSms() {
        ArrayList<String> lstSms = new ArrayList<>();
        ArrayList<String> lstPeople = new ArrayList<>();
        ContentResolver cr = mContext.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[]{Telephony.Sms.Inbox.PERSON, Telephony.Sms.Inbox.BODY},
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                lstPeople.add(c.getString(0));
                lstSms.add(c.getString(1));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        ArrayList<ArrayList<String>> result = new ArrayList<>();

        result.add(lstPeople);
        result.add(lstSms);

        return result;
    }

    // --------------------------------------------------
    // Get all SMS from contact ID
    // --------------------------------------------------
    public static ArrayList<ArrayList<String>> getAllSmsFromContact(int contactID) {
        ArrayList<String> lstSms = new ArrayList<>();

        ContentResolver cr = mContext.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[]{Telephony.Sms.Inbox.BODY},
                Telephony.Sms.Inbox.PERSON + "='" + contactID + "'",
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                lstSms.add(c.getString(0));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        ArrayList<ArrayList<String>> result = new ArrayList<>();

        result.add(lstSms);

        return result;
    }
}
