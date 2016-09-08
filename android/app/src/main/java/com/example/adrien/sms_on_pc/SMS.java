/**
 * SMS related functions
 */

package com.example.adrien.sms_on_pc;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.HashMap;

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
    // Get all addresses
    // --------------------------------------------------
    public static ArrayList<String> getAddresses() {
        ArrayList<String> result = new ArrayList<>();

        ContentResolver cr = mContext.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[]{Telephony.Sms.Inbox.ADDRESS},
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                if (!result.contains(c.getString(0))) {
                    result.add(c.getString(0));
                }
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        return result;
    }

    // --------------------------------------------------
    // Get all addresses with corresponding message
    // count in the form HashMap<Address, msgCount>
    // --------------------------------------------------
    public static HashMap<String, Integer> getMessageCounts() {
        HashMap<String, Integer> result = new HashMap<>();

        ContentResolver cr = mContext.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[]{Telephony.Sms.Inbox.ADDRESS},
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                if (result.get(c.getString(0)) == null) {
                    result.put(c.getString(0), 1);
                }
                else {
                    result.put(c.getString(0), result.get(c.getString(0)) + 1);
                }
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        return result;
    }

    // --------------------------------------------------
    // Get all SMS from address
    // --------------------------------------------------
    public static ArrayList<ArrayList<String>> getAllSmsFromAddress(String address) {
        ArrayList<String> lstSms = new ArrayList<>();

        ContentResolver cr = mContext.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[]{Telephony.Sms.Inbox.BODY},
                Telephony.Sms.Inbox.ADDRESS + "='" + address + "'",
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
